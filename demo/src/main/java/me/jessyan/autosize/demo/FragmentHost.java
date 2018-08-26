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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * ================================================
 * Created by JessYan on 2018/8/25 14:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class FragmentHost extends AppCompatActivity implements CustomAdapt {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        if (getSupportFragmentManager().findFragmentById(R.id.container1) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container1, new CustomFragment1()).commit();
        }
        if (getSupportFragmentManager().findFragmentById(R.id.container2) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container2, new CustomFragment2()).commit();
        }
        if (getSupportFragmentManager().findFragmentById(R.id.container3) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container3, new CustomFragment3()).commit();
        }
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 720;
    }
}
