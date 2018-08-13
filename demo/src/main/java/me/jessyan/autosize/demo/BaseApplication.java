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
package me.jessyan.autosize.demo;

import android.app.Application;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.external.ExternalAdaptInfo;
import me.jessyan.autosize.external.ExternalAdaptManager;

/**
 * ================================================
 * 记得在 App 启动时初始化 AndroidAutoSize
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

        /**
         * {@link AutoSizeConfig} 的每个方法的注释都写的很详细, 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()

                //是否打印 AutoSize 的内部日志, setLog 方法一定要在 init 方法前面调用, 因为执行 init 方法时就会打印日志
                .setLog(true)

                //init 方法只能调用一次, 一般调用一个参数的 init 方法就可以了, 如果有其他扩展需求可以尝试下面的两个参数和三个参数的 init 重载方法
                .init(this)

                //这个 init 方法, 可以快捷设置 setBaseOnWidth(false)
//                .init(this, false)

                //这个 init 方法, 可以快捷设置 setBaseOnWidth(false) 和 setAutoAdaptStrategy(new AutoAdaptStrategy())
//                .init(this, false, new AutoAdaptStrategy())

                //是否使用设备的实际尺寸做适配, 设置为 false, AutoSize 会将屏幕总高度减去状态栏高度来做适配
//                .setUseDeviceSize(false)

                //是否全局按照宽度进行等比例适配, 设置为 false, AutoSize 会全局按照高度进行适配
//                .setBaseOnWidth(false)

                //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())

        ;

        /**
         * {@link ExternalAdaptManager} 是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 {@link ExternalAdaptManager} 的类注释
         */
        AutoSizeConfig.getInstance().getExternalAdaptManager()
                .addCancelAdaptOfActivity(MainActivity.class)
                .addExternalAdaptInfoOfActivity(TestActivity.class, new ExternalAdaptInfo(true, 480));

    }
}
