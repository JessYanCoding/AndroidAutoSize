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
import android.app.Dialog;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

import me.jessyan.autosize.external.ExternalAdaptInfo;
import me.jessyan.autosize.external.ExternalAdaptManager;
import me.jessyan.autosize.internal.CancelAdapt;
import me.jessyan.autosize.internal.CustomAdapt;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.autosize.utils.Preconditions;

/**
 * ================================================
 * AndroidAutoSize 用于屏幕适配的核心方法都在这里, 核心原理来自于 <a href="https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA">今日头条官方适配方案</a>
 * 此方案只要应用到 {@link Activity} 上, 这个 {@link Activity} 下的所有 {@link Fragment}、{@link Dialog}、
 * 自定义 {@link View} 都会达到适配的效果, 如果某个页面不想使用适配请让该 {@link Activity} 实现 {@link CancelAdapt}
 * <p>
 * 任何方案都不可能完美, 在成本和收益中做出取舍, 选择出最适合自己的方案即可, 在没有更好的方案出来之前, 只有继续忍耐它的不完美, 或者自己作出改变
 * 既然选择, 就不要抱怨, 感谢 今日头条技术团队 和 张鸿洋 等人对 Android 屏幕适配领域的的贡献
 * <p>
 * Created by JessYan on 2018/8/8 19:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class AutoSize {

    private AutoSize() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 使用 AndroidAutoSize 初始化时设置的默认适配参数进行适配 (AndroidManifest 的 Meta 属性)
     *
     * @param activity {@link Activity}
     */
    public static void autoConvertDensityOfGlobal(Activity activity) {
        if (AutoSizeConfig.getInstance().isBaseOnWidth()) {
            autoConvertDensityBaseOnWidth(activity, AutoSizeConfig.getInstance().getDesignWidthInDp());
        } else {
            autoConvertDensityBaseOnHeight(activity, AutoSizeConfig.getInstance().getDesignHeightInDp());
        }
    }

    /**
     * 使用 {@link Activity} 的自定义参数进行适配
     *
     * @param activity    {@link Activity}
     * @param customAdapt {@link Activity} 需实现 {@link CustomAdapt}
     */
    public static void autoConvertDensityOfCustomAdapt(Activity activity, CustomAdapt customAdapt) {
        Preconditions.checkNotNull(customAdapt, "customAdapt == null");
        float sizeInDp = customAdapt.getSizeInDp();

        //如果 CustomAdapt#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
        if (sizeInDp <= 0) {
            if (customAdapt.isBaseOnWidth()) {
                sizeInDp = AutoSizeConfig.getInstance().getDesignWidthInDp();
            } else {
                sizeInDp = AutoSizeConfig.getInstance().getDesignHeightInDp();
            }
        }
        autoConvertDensity(activity, sizeInDp, customAdapt.isBaseOnWidth());
    }

    /**
     * 使用外部三方库的 {@link Activity} 的自定义适配参数进行适配
     *
     * @param activity          {@link Activity}
     * @param externalAdaptInfo 三方库的 {@link Activity} 提供的适配参数, 需要配合 {@link ExternalAdaptManager#addExternalAdaptInfoOfActivity(Class, ExternalAdaptInfo)}
     */
    public static void autoConvertDensityOfExternalAdaptInfo(Activity activity, ExternalAdaptInfo externalAdaptInfo) {
        Preconditions.checkNotNull(externalAdaptInfo, "externalAdaptInfo == null");
        float sizeInDp = externalAdaptInfo.getSizeInDp();

        //如果 ExternalAdaptInfo#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
        if (sizeInDp <= 0) {
            if (externalAdaptInfo.isBaseOnWidth()) {
                sizeInDp = AutoSizeConfig.getInstance().getDesignWidthInDp();
            } else {
                sizeInDp = AutoSizeConfig.getInstance().getDesignHeightInDp();
            }
        }
        autoConvertDensity(activity, sizeInDp, externalAdaptInfo.isBaseOnWidth());
    }

    /**
     * 以宽度为基准进行适配
     *
     * @param activity        {@link Activity}
     * @param designWidthInDp 设计图的总宽度
     */
    public static void autoConvertDensityBaseOnWidth(Activity activity, float designWidthInDp) {
        autoConvertDensity(activity, designWidthInDp, true);
    }

    /**
     * 以高度为基准进行适配
     *
     * @param activity         {@link Activity}
     * @param designHeightInDp 设计图的总高度
     */
    public static void autoConvertDensityBaseOnHeight(Activity activity, float designHeightInDp) {
        autoConvertDensity(activity, designHeightInDp, false);
    }

    /**
     * 这里是今日头条适配方案的核心代码, 核心在于根据当前设备的实际情况做自动计算并转换 {@link DisplayMetrics#density}、
     * {@link DisplayMetrics#scaledDensity}、{@link DisplayMetrics#densityDpi} 这三个值, 有兴趣请看下面的链接
     *
     * @param activity      {@link Activity}
     * @param sizeInDp      设计图上的设计尺寸, 单位 dp, 如果 {@param isBaseOnWidth} 设置为 {@code true},
     *                      {@param sizeInDp} 则应该填写设计图的总宽度, 如果 {@param isBaseOnWidth} 设置为 {@code false},
     *                      {@param sizeInDp} 则应该填写设计图的总高度
     * @param isBaseOnWidth 是否按照宽度进行等比例适配, {@code true} 为以宽度进行等比例适配, {@code false} 为以高度进行等比例适配
     * @see <a href="https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA">今日头条官方适配方案</a>
     */
    public static void autoConvertDensity(Activity activity, float sizeInDp, boolean isBaseOnWidth) {
        Preconditions.checkNotNull(activity, "activity == null");
        float targetDensity;
        if (isBaseOnWidth) {
            targetDensity = AutoSizeConfig.getInstance().getScreenWidth() * 1.0f / sizeInDp;
        } else {
            targetDensity = AutoSizeConfig.getInstance().getScreenHeight() * 1.0f / sizeInDp;
        }
        final float targetScaledDensity = targetDensity * (AutoSizeConfig.getInstance().
                getInitScaledDensity() * 1.0f / AutoSizeConfig.getInstance().getInitDensity());
        final int targetDensityDpi = (int) (targetDensity * 160);

        setDensity(activity, targetDensity, targetDensityDpi, targetScaledDensity);

        LogUtils.d(String.format(Locale.ENGLISH, "The %s has been adapted! \nInfo: isBaseOnWidth = %s, %s = %f, targetDensity = %f, targetScaledDensity = %f, targetDensityDpi = %d"
                , activity.getClass().getName(), isBaseOnWidth, isBaseOnWidth ? "designWidthInDp"
                        : "designHeightInDp", sizeInDp, targetDensity, targetScaledDensity, targetDensityDpi));
    }

    /**
     * 取消适配
     *
     * @param activity {@link Activity}
     */
    public static void cancelAdapt(Activity activity) {
        setDensity(activity, AutoSizeConfig.getInstance().getInitDensity()
                , AutoSizeConfig.getInstance().getInitDensityDpi()
                , AutoSizeConfig.getInstance().getInitScaledDensity());
    }

    /**
     * 赋值
     *
     * @param activity      {@link Activity}
     * @param density       {@link DisplayMetrics#density}
     * @param densityDpi    {@link DisplayMetrics#densityDpi}
     * @param scaledDensity {@link DisplayMetrics#scaledDensity}
     */
    private static void setDensity(Activity activity, float density, int densityDpi, float scaledDensity) {
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        final DisplayMetrics appDisplayMetrics = AutoSizeConfig.getInstance().getApplication().getResources().getDisplayMetrics();

        activityDisplayMetrics.density = density;
        activityDisplayMetrics.densityDpi = densityDpi;
        activityDisplayMetrics.scaledDensity = scaledDensity;

        appDisplayMetrics.density = density;
        appDisplayMetrics.densityDpi = densityDpi;
        appDisplayMetrics.scaledDensity = scaledDensity;
    }
}
