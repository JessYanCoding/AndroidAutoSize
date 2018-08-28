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
package me.jessyan.autosize.demo.subunits;

import android.app.Application;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * ================================================
 * 本框架核心原理来自于 <a href="https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA">今日头条官方适配方案</a>
 * <p>
 * 本框架源码的注释都很详细, 欢迎阅读学习
 * <p>
 * AndroidAutoSize 会在 APP 启动时自动完成初始化, 如果您想设置自定义参数可以在 {@link Application#onCreate()} 中设置
 * <p>
 * Created by JessYan on 2018/8/9 17:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AutoSizeConfig.getInstance().getUnitsManager().setSupportDP(false);
        AutoSizeConfig.getInstance().getUnitsManager().setSupportSP(false);
        AutoSizeConfig.getInstance().getUnitsManager().setSupportSubunits(Subunits.MM);
    }
}
