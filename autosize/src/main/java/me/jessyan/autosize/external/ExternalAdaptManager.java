/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.autosize.external;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.Preconditions;

/**
 * ================================================
 * 管理三方库的适配信息和状态, 通过 {@link AutoSizeConfig#getExternalAdaptManager()} 获取, 切勿自己 new
 * AndroidAutoSize 通过实现接口的方式来让每个 {@link Activity} 都具有自定义适配参数的功能, 从而让每个 {@link Activity} 都可以自定义适配效果
 * 但通过远程依赖的三方库并不能修改源码, 所以也不能让三方库的 {@link Activity} 实现接口, 实现接口的方式就显得无能为力
 * {@link ExternalAdaptManager} 就是专门用来处理这个问题, 项目初始化时把对应的三方库 {@link Activity} 传入 {@link ExternalAdaptManager} 即可
 * <p>
 * Created by JessYan on 2018/8/10 14:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ExternalAdaptManager {
    private List<String> mCancelAdaptList;
    private Map<String, ExternalAdaptInfo> mExternalAdaptInfos;
    private boolean isRun;

    /**
     * 将不需要适配的第三方库 {@link Activity} 添加进来 (但不局限于三方库), 即可让该 {@link Activity} 的适配效果失效
     * <p>
     * 支持链式调用, 如:
     * {@link ExternalAdaptManager#addCancelAdaptOfActivity(Class)#addCancelAdaptOfActivity(Class)}
     *
     * @param activityClass {@link Activity} class
     */
    public synchronized ExternalAdaptManager addCancelAdaptOfActivity(Class<?> activityClass) {
        Preconditions.checkNotNull(activityClass, "activityClass == null");
        if (!isRun) {
            isRun = true;
        }
        if (mCancelAdaptList == null) {
            mCancelAdaptList = new ArrayList<>();
        }
        mCancelAdaptList.add(activityClass.getCanonicalName());
        return this;
    }

    /**
     * 将需要提供自定义适配参数的三方库 {@link Activity} 添加进来 (但不局限于三方库), 即可让该 {@link Activity} 根据自己提供的适配参数进行适配
     * 默认的全局适配参数不能满足您时可以使用此方法
     * <p>
     * 支持链式调用, 如:
     * {@link ExternalAdaptManager#addExternalAdaptInfoOfActivity(Class, ExternalAdaptInfo)#addExternalAdaptInfoOfActivity(Class, ExternalAdaptInfo)}
     *
     * @param activityClass {@link Activity} class
     * @param info          {@link ExternalAdaptInfo} 适配参数
     */
    public synchronized ExternalAdaptManager addExternalAdaptInfoOfActivity(Class<?> activityClass, ExternalAdaptInfo info) {
        Preconditions.checkNotNull(activityClass, "activityClass == null");
        if (!isRun) {
            isRun = true;
        }
        if (mExternalAdaptInfos == null) {
            mExternalAdaptInfos = new HashMap<>(30);
        }
        mExternalAdaptInfos.put(activityClass.getCanonicalName(), info);
        return this;
    }

    /**
     * 这个 {@link Activity} 是否存在在取消适配的列表中, 如果在, 则该 {@link Activity} 适配失效
     *
     * @param activityClass {@link Activity} class
     * @return {@code true} 为存在, {@code false} 为不存在
     */
    public synchronized boolean isCancelAdapt(Class<?> activityClass) {
        Preconditions.checkNotNull(activityClass, "activityClass == null");
        if (mCancelAdaptList == null) {
            return false;
        }
        return mCancelAdaptList.contains(activityClass.getCanonicalName());
    }

    /**
     * 这个 {@link Activity} 是否提供有自定义的适配参数, 如果有则使用此适配参数进行适配
     *
     * @param activityClass {@link Activity} class
     * @return 如果返回 {@code null} 则说明该 {@link Activity} 没有提供自定义的适配参数
     */
    public synchronized ExternalAdaptInfo getExternalAdaptInfoOfActivity(Class<?> activityClass) {
        Preconditions.checkNotNull(activityClass, "activityClass == null");
        if (mExternalAdaptInfos == null) {
            return null;
        }
        return mExternalAdaptInfos.get(activityClass.getCanonicalName());
    }

    /**
     * 此管理器是否已经启动
     *
     * @return {@code true} 为已经启动, {@code false} 为没有启动
     */
    public boolean isRun() {
        return isRun;
    }

    /**
     * 设置管理器的运行状态
     *
     * @param run {@code true} 为让管理器启动运行, {@code false} 为让管理器停止运行
     */
    public void setRun(boolean run) {
        isRun = run;
    }
}
