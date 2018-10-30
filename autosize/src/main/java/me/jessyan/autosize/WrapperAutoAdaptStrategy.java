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
package me.jessyan.autosize;

import android.app.Activity;

/**
 * ================================================
 * {@link AutoAdaptStrategy} 的包装者, 用于给 {@link AutoAdaptStrategy} 的实现类增加一些额外的职责
 * <p>
 * Created by JessYan on 2018/10/30 15:07
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class WrapperAutoAdaptStrategy implements AutoAdaptStrategy {
    private final AutoAdaptStrategy mAutoAdaptStrategy;

    public WrapperAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void applyAdapt(Object target, Activity activity) {
        onAdaptListener onAdaptListener = AutoSizeConfig.getInstance().getOnAdaptListener();
        if (onAdaptListener != null){
            onAdaptListener.onAdaptBefore(target, activity);
        }
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(target, activity);
        }
        if (onAdaptListener != null){
            onAdaptListener.onAdaptAfter(target, activity);
        }
    }
}
