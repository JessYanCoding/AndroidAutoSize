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
package me.jessyan.autosize.unit;

import android.util.DisplayMetrics;

/**
 * ================================================
 * AndroidAutoSize 支持一些在 Android 系统上比较少见的单位作为副单位, 用于规避修改 {@link DisplayMetrics#density}
 * 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 * <p>
 * Created by JessYan on 2018/8/28 10:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public enum Subunits {
    /**
     * 不使用副单位
     */
    NONE,
    /**
     * 单位 pt
     *
     * @see android.util.TypedValue#COMPLEX_UNIT_PT
     */
    PT,
    /**
     * 单位 in
     *
     * @see android.util.TypedValue#COMPLEX_UNIT_IN
     */
    IN,
    /**
     * 单位 mm
     *
     * @see android.util.TypedValue#COMPLEX_UNIT_MM
     */
    MM
}
