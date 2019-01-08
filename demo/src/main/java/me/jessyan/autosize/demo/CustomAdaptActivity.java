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
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * ================================================
 * 本框架核心原理来自于 <a href="https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA">今日头条官方适配方案</a>
 * <p>
 * {@link CustomAdaptActivity} 展示项目内部的 {@link Activity} 自定义适配参数的用法, 需要实现 {@link CustomAdapt}
 * 现在 AndroidAutoSize 是全局以屏幕宽度为基准进行适配的, 并且全局的设计图尺寸为 360 * 640
 * 这里就展示怎么让 {@link CustomAdaptActivity} 单个页面, 有别于全局设置, 以屏幕高度为基准进行适配, 并且更改设计图尺寸为 iPhone 的设计图尺寸
 * 如果这个页面的设计图尺寸有别于其他页面, AndroidAutoSize 允许您改变单个页面的设计图尺寸, {@link #getSizeInDp()}
 * <p>
 * Created by JessYan on 2018/8/11 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class CustomAdaptActivity extends AppCompatActivity implements CustomAdapt {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_adapt);
    }

    /**
     * 跳转到 {@link FragmentHost}, 展示项目内部的 {@link Fragment} 自定义适配参数的用法
     *
     * @param view {@link View}
     */
    public void goCustomAdaptFragment(View view) {
        startActivity(new Intent(getApplicationContext(), FragmentHost.class));
    }

    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选择一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度进行适配, {@code false} 为按照高度进行适配
     */
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    /**
     * 这里使用 iPhone 的设计图, iPhone 的设计图尺寸为 750px * 1334px, 高换算成 dp 为 667 (1334px / 2 = 667dp)
     * <p>
     * 返回设计图上的设计尺寸, 单位 dp
     * {@link #getSizeInDp} 须配合 {@link #isBaseOnWidth()} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth()} 返回 {@code true}, {@link #getSizeInDp} 则应该返回设计图的总宽度
     * 如果 {@link #isBaseOnWidth()} 返回 {@code false}, {@link #getSizeInDp} 则应该返回设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #getSizeInDp} 则返回 {@code 0}
     *
     * @return 设计图上的设计尺寸, 单位 dp
     */
    @Override
    public float getSizeInDp() {
        return 667;
    }
}
