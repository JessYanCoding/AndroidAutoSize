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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * ================================================
 * {@link CustomAdaptActivity} 展示项目内部的 {@link Activity} 自定义适配参数的用法, 需要实现 {@link CustomAdapt}
 * 经过 {@link BaseApplication} 的初始化设置后, 现在 AndroidAutoSize 是全局以屏幕宽度为基准进行适配的
 * 这里就展示怎么让 {@link CustomAdaptActivity} 这一个页面, 有别于全局设置, 以屏幕高度为基准进行适配
 * 这里为了方便展示就不改变设计图尺寸, 直接使用在 AndroidManifest 中填写的全局设计图尺寸, 如果您有需求可以改变试试
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


    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 0;
    }
}
