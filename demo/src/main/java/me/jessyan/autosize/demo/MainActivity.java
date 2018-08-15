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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * ================================================
 * 本框架核心原理来自于 <a href="https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA">今日头条官方适配方案</a>
 * <p>
 * {@link MainActivity} 是以屏幕宽度为基准进行适配的, 不懂什么叫基准的话, 请看 {@link AutoSizeConfig#isBaseOnWidth}) 的注释
 * AndroidAutoSize 默认全局以屏幕宽度为基准进行适配
 * 如果想更改为全局以屏幕高度为基准进行适配, 请在 {@link BaseApplication} 中按注释中更改, 为什么强调全局？
 * 因为 AndroidAutoSize 允许每个 {@link Activity} 可以自定义适配参数, 自定义适配参数通过实现 {@link CustomAdapt}
 * 如果不自定义适配参数就会使用全局的适配参数, 全局适配参数在 {@link BaseApplication} 中按注释设置
 * <p>
 * Created by JessYan on 2018/8/9 17:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 需要注意的是暂停 AndroidAutoSize 后, AndroidAutoSize 只是停止了对后续还没有启动的 {@link Activity} 进行适配的工作
     * 但对已经启动且已经适配的 {@link Activity} 不会有任何影响
     *
     * @param view {@link View}
     */
    public void stop(View view) {
        Toast.makeText(getApplicationContext(), "AndroidAutoSize stops working!", Toast.LENGTH_SHORT).show();
        AutoSizeConfig.getInstance().stop(this);
    }

    /**
     * 需要注意的是重新启动 AndroidAutoSize 后, AndroidAutoSize 只是重新开始了对后续还没有启动的 {@link Activity} 进行适配的工作
     * 但对已经启动且在 stop 期间未适配的 {@link Activity} 不会有任何影响
     *
     * @param view {@link View}
     */
    public void restart(View view) {
        Toast.makeText(getApplicationContext(), "AndroidAutoSize continues to work", Toast.LENGTH_SHORT).show();
        AutoSizeConfig.getInstance().restart();
    }

    /**
     * 跳转到 {@link CustomAdaptActivity}, 展示项目内部的 {@link Activity} 自定义适配参数的用法
     *
     * @param view {@link View}
     */
    public void goCustomAdaptActivity(View view) {
        startActivity(new Intent(getApplicationContext(), CustomAdaptActivity.class));
    }

    /**
     * 跳转到三方库的 {@link Activity}, 展示项目外部某些三方库的 {@link Activity} 自定义适配参数的用法
     *
     * @param view {@link View}
     */
    public void goThirdLibraryActivity(View view) {

    }
}
