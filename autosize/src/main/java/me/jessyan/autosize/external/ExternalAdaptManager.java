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
import android.support.v4.app.Fragment;

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
     * @param targetClass {@link Activity} class, {@link Fragment} class
     */
    public synchronized ExternalAdaptManager addCancelAdaptOfActivity(Class<?> targetClass) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (!isRun) {
            isRun = true;
        }
        if (mCancelAdaptList == null) {
            mCancelAdaptList = new ArrayList<>();
        }
        mCancelAdaptList.add(targetClass.getCanonicalName());
        return this;
    }

    /**
     * 将需要提供自定义适配参数的三方库 {@link Activity} 添加进来 (但不局限于三方库), 即可让该 {@link Activity} 根据自己提供的适配参数进行适配
     * 默认的全局适配参数不能满足您时可以使用此方法
     * <p>
     * 一般用于三方库的 Activity, 因为三方库的设计图尺寸可能和项目自身的设计图尺寸不一致, 所以要想完美适配三方库的页面
     * 就需要提供三方库的设计图尺寸, 以及适配的方向 (以宽为基准还是高为基准?)
     * 三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试
     * 由于 AndroidAutoSize 可以让布局在所有设备上都等比例缩放, 所以只要您在一个设备上测试出了一个最完美的设计图尺寸
     * 那这个三方库页面在其他设备上也会呈现出同样的适配效果, 等比例缩放, 所以也就完成了三方库页面的屏幕适配
     * 即使在不改三方库源码的情况下也可以完美适配三方库的页面, 这就是 AndroidAutoSize 的优势
     * 但前提是三方库页面的布局使用的是 dp 和 sp, 如果布局全部使用的 px, 那 AndroidAutoSize 也将无能为力
     * <p>
     * 支持链式调用, 如:
     * {@link ExternalAdaptManager#addExternalAdaptInfoOfActivity(Class, ExternalAdaptInfo)#addExternalAdaptInfoOfActivity(Class, ExternalAdaptInfo)}
     *
     * @param targetClass {@link Activity} class, {@link Fragment} class
     * @param info        {@link ExternalAdaptInfo} 适配参数
     */
    public synchronized ExternalAdaptManager addExternalAdaptInfoOfActivity(Class<?> targetClass, ExternalAdaptInfo info) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (!isRun) {
            isRun = true;
        }
        if (mExternalAdaptInfos == null) {
            mExternalAdaptInfos = new HashMap<>(16);
        }
        mExternalAdaptInfos.put(targetClass.getCanonicalName(), info);
        return this;
    }

    /**
     * 这个 {@link Activity} 是否存在在取消适配的列表中, 如果在, 则该 {@link Activity} 适配失效
     *
     * @param targetClass {@link Activity} class, {@link Fragment} class
     * @return {@code true} 为存在, {@code false} 为不存在
     */
    public synchronized boolean isCancelAdapt(Class<?> targetClass) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (mCancelAdaptList == null) {
            return false;
        }
        return mCancelAdaptList.contains(targetClass.getCanonicalName());
    }

    /**
     * 这个 {@link Activity} 是否提供有自定义的适配参数, 如果有则使用此适配参数进行适配
     *
     * @param targetClass {@link Activity} class, {@link Fragment} class
     * @return 如果返回 {@code null} 则说明该 {@link Activity} 没有提供自定义的适配参数
     */
    public synchronized ExternalAdaptInfo getExternalAdaptInfoOfActivity(Class<?> targetClass) {
        Preconditions.checkNotNull(targetClass, "targetClass == null");
        if (mExternalAdaptInfos == null) {
            return null;
        }
        return mExternalAdaptInfos.get(targetClass.getCanonicalName());
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
    public ExternalAdaptManager setRun(boolean run) {
        isRun = run;
        return this;
    }
}
