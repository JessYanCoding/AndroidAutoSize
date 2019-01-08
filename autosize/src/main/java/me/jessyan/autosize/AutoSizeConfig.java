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
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

import me.jessyan.autosize.external.ExternalAdaptManager;
import me.jessyan.autosize.unit.UnitsManager;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.autosize.utils.Preconditions;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * ================================================
 * AndroidAutoSize 参数配置类, 给 AndroidAutoSize 配置一些必要的自定义参数
 * <p>
 * Created by JessYan on 2018/8/8 09:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class AutoSizeConfig {
    private static volatile AutoSizeConfig sInstance;
    private static final String KEY_DESIGN_WIDTH_IN_DP = "design_width_in_dp";
    private static final String KEY_DESIGN_HEIGHT_IN_DP = "design_height_in_dp";
    private Application mApplication;
    /**
     * 用来管理外部三方库 {@link Activity} 的适配
     */
    private ExternalAdaptManager mExternalAdaptManager = new ExternalAdaptManager();
    /**
     * 用来管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
     */
    private UnitsManager mUnitsManager = new UnitsManager();
    /**
     * 最初的 {@link DisplayMetrics#density}
     */
    private float mInitDensity = -1;
    /**
     * 最初的 {@link DisplayMetrics#densityDpi}
     */
    private int mInitDensityDpi;
    /**
     * 最初的 {@link DisplayMetrics#scaledDensity}
     */
    private float mInitScaledDensity;
    /**
     * 最初的 {@link DisplayMetrics#xdpi}
     */
    private float mInitXdpi;
    /**
     * 设计图上的总宽度, 单位 dp
     */
    private int mDesignWidthInDp;
    /**
     * 设计图上的总高度, 单位 dp
     */
    private int mDesignHeightInDp;
    /**
     * 设备的屏幕总宽度, 单位 px
     */
    private int mScreenWidth;
    /**
     * 设备的屏幕总高度, 单位 px, 如果 {@link #isUseDeviceSize} 为 {@code false}, 屏幕总高度会减去状态栏的高度
     */
    private int mScreenHeight;
    /**
     * 状态栏高度, 当 {@link #isUseDeviceSize} 为 {@code false} 时, AndroidAutoSize 会将 {@link #mScreenHeight} 减去状态栏高度
     * AndroidAutoSize 默认使用 {@link ScreenUtils#getStatusBarHeight()} 方法获取状态栏高度
     * AndroidAutoSize 使用者可使用 {@link #setStatusBarHeight(int)} 自行设置状态栏高度
     */
    private int mStatusBarHeight;
    /**
     * 为了保证在不同高宽比的屏幕上显示效果也能完全一致, 所以本方案适配时是以设计图宽度与设备实际宽度的比例或设计图高度与设备实际高度的比例应用到
     * 每个 View 上 (只能在宽度和高度之中选一个作为基准), 从而使每个 View 的高和宽用同样的比例缩放, 避免在与设计图高宽比不一致的设备上出现适配的 View 高或宽变形的问题
     * {@link #isBaseOnWidth} 为 {@code true} 时代表以宽度等比例缩放, {@code false} 代表以高度等比例缩放
     * {@link #isBaseOnWidth} 为全局配置, 默认为 {@code true}, 每个 {@link Activity} 也可以单独选择使用高或者宽做等比例缩放
     */
    private boolean isBaseOnWidth = true;
    /**
     * 此字段表示是否使用设备的实际尺寸做适配
     * {@link #isUseDeviceSize} 为 {@code true} 表示屏幕高度 {@link #mScreenHeight} 包含状态栏的高度
     * {@link #isUseDeviceSize} 为 {@code false} 表示 {@link #mScreenHeight} 会减去状态栏的高度, 默认为 {@code true}
     */
    private boolean isUseDeviceSize = true;
    /**
     * {@link #mActivityLifecycleCallbacks} 可用来代替在 BaseActivity 中加入适配代码的传统方式
     * {@link #mActivityLifecycleCallbacks} 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 {@link Activity}
     */
    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks;
    /**
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     *
     * @see #stop(Activity)
     * @see #restart()
     */
    private boolean isStop;
    /**
     * 是否让框架支持自定义 {@link Fragment} 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
     */
    private boolean isCustomFragment;
    /**
     * 屏幕方向, {@code true} 为纵向, {@code false} 为横向
     */
    private boolean isVertical;
    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 {@code true}, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 {@code false}, 则会跟随系统设置中字体大小的改变, 默认为 {@code false}
     */
    private boolean isExcludeFontScale;
    /**
     * 是否是 Miui 系统
     */
    private boolean isMiui;
    /**
     * Miui 系统中的 mTmpMetrics 字段
     */
    private Field mTmpMetricsField;
    /**
     * 屏幕适配监听器，用于监听屏幕适配时的一些事件
     */
    private onAdaptListener mOnAdaptListener;

    public static AutoSizeConfig getInstance() {
        if (sInstance == null) {
            synchronized (AutoSizeConfig.class) {
                if (sInstance == null) {
                    sInstance = new AutoSizeConfig();
                }
            }
        }
        return sInstance;
    }

    private AutoSizeConfig() {
    }

    public Application getApplication() {
        Preconditions.checkNotNull(mApplication, "Please call the AutoSizeConfig#init() first");
        return mApplication;
    }

    /**
     * v0.7.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     * 此方法默认使用以宽度进行等比例适配, 如想使用以高度进行等比例适配, 请调用 {@link #init(Application, boolean)}
     *
     * @param application {@link Application}
     */
    AutoSizeConfig init(Application application) {
        return init(application, true, null);
    }

    /**
     * v0.7.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     * 此方法使用默认的 {@link AutoAdaptStrategy} 策略, 如想使用自定义的 {@link AutoAdaptStrategy} 策略
     * 请调用 {@link #init(Application, boolean, AutoAdaptStrategy)}
     *
     * @param application   {@link Application}
     * @param isBaseOnWidth 详情请查看 {@link #isBaseOnWidth} 的注释
     */
    AutoSizeConfig init(Application application, boolean isBaseOnWidth) {
        return init(application, isBaseOnWidth, null);
    }

    /**
     * v0.7.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     *
     * @param application   {@link Application}
     * @param isBaseOnWidth 详情请查看 {@link #isBaseOnWidth} 的注释
     * @param strategy      {@link AutoAdaptStrategy}, 传 {@code null} 则使用 {@link DefaultAutoAdaptStrategy}
     */
    AutoSizeConfig init(final Application application, boolean isBaseOnWidth, AutoAdaptStrategy strategy) {
        Preconditions.checkArgument(mInitDensity == -1, "AutoSizeConfig#init() can only be called once");
        Preconditions.checkNotNull(application, "application == null");
        this.mApplication = application;
        this.isBaseOnWidth = isBaseOnWidth;
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        getMetaData(application);
        isVertical = application.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int[] screenSize = ScreenUtils.getScreenSize(application);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
        mStatusBarHeight = ScreenUtils.getStatusBarHeight();
        LogUtils.d("designWidthInDp = " + mDesignWidthInDp + ", designHeightInDp = " + mDesignHeightInDp + ", screenWidth = " + mScreenWidth + ", screenHeight = " + mScreenHeight);

        mInitDensity = displayMetrics.density;
        mInitDensityDpi = displayMetrics.densityDpi;
        mInitScaledDensity = displayMetrics.scaledDensity;
        mInitXdpi = displayMetrics.xdpi;
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if (newConfig != null) {
                    if (newConfig.fontScale > 0) {
                        mInitScaledDensity =
                                Resources.getSystem().getDisplayMetrics().scaledDensity;
                        LogUtils.d("initScaledDensity = " + mInitScaledDensity + " on ConfigurationChanged");
                    }
                    isVertical = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
                    int[] screenSize = ScreenUtils.getScreenSize(application);
                    mScreenWidth = screenSize[0];
                    mScreenHeight = screenSize[1];
                }
            }

            @Override
            public void onLowMemory() {

            }
        });
        LogUtils.d("initDensity = " + mInitDensity + ", initScaledDensity = " + mInitScaledDensity);
        mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl(strategy == null ? new WrapperAutoAdaptStrategy(new DefaultAutoAdaptStrategy()) : strategy);
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        if ("MiuiResources".equals(application.getResources().getClass().getSimpleName()) || "XResources".equals(application.getResources().getClass().getSimpleName())) {
            isMiui = true;
            try {
                mTmpMetricsField = Resources.class.getDeclaredField("mTmpMetrics");
                mTmpMetricsField.setAccessible(true);
            } catch (Exception e) {
                mTmpMetricsField = null;
            }
        }
        return this;
    }

    /**
     * 重新开始框架的运行
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     */
    public void restart() {
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        synchronized (AutoSizeConfig.class) {
            if (isStop) {
                mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                isStop = false;
            }
        }
    }

    /**
     * 停止框架的运行
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     */
    public void stop(Activity activity) {
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        synchronized (AutoSizeConfig.class) {
            if (!isStop) {
                mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                AutoSize.cancelAdapt(activity);
                isStop = true;
            }
        }
    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy {@link AutoAdaptStrategy}
     */
    public AutoSizeConfig setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        Preconditions.checkNotNull(autoAdaptStrategy, "autoAdaptStrategy == null");
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        mActivityLifecycleCallbacks.setAutoAdaptStrategy(new WrapperAutoAdaptStrategy(autoAdaptStrategy));
        return this;
    }

    /**
     * 设置屏幕适配监听器
     *
     * @param onAdaptListener {@link onAdaptListener}
     */
    public AutoSizeConfig setOnAdaptListener(onAdaptListener onAdaptListener) {
        Preconditions.checkNotNull(onAdaptListener, "onAdaptListener == null");
        mOnAdaptListener = onAdaptListener;
        return this;
    }

    /**
     * 是否全局按照宽度进行等比例适配
     *
     * @param baseOnWidth {@code true} 为按照宽度, {@code false} 为按照高度
     * @see #isBaseOnWidth 详情请查看这个字段的注释
     */
    public AutoSizeConfig setBaseOnWidth(boolean baseOnWidth) {
        isBaseOnWidth = baseOnWidth;
        return this;
    }

    /**
     * 是否使用设备的实际尺寸做适配
     *
     * @param useDeviceSize {@code true} 为使用设备的实际尺寸 (包含状态栏), {@code false} 为不使用设备的实际尺寸 (不包含状态栏)
     * @see #isUseDeviceSize 详情请查看这个字段的注释
     */
    public AutoSizeConfig setUseDeviceSize(boolean useDeviceSize) {
        isUseDeviceSize = useDeviceSize;
        return this;
    }

    /**
     * 是否打印 Log
     *
     * @param log {@code true} 为打印
     */
    public AutoSizeConfig setLog(boolean log) {
        LogUtils.setDebug(log);
        return this;
    }

    /**
     * 是否让框架支持自定义 {@link Fragment} 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
     *
     * @param customFragment {@code true} 为支持
     */
    public AutoSizeConfig setCustomFragment(boolean customFragment) {
        isCustomFragment = customFragment;
        return this;
    }

    /**
     * 框架是否已经开启支持自定义 {@link Fragment} 的适配参数
     *
     * @return {@code true} 为支持
     */
    public boolean isCustomFragment() {
        return isCustomFragment;
    }

    /**
     * 框架是否已经停止运行
     *
     * @return {@code false} 框架正在运行, {@code true} 框架已经停止运行
     */
    public boolean isStop() {
        return isStop;
    }

    /**
     * {@link ExternalAdaptManager} 用来管理外部三方库 {@link Activity} 的适配
     *
     * @return {@link #mExternalAdaptManager}
     */
    public ExternalAdaptManager getExternalAdaptManager() {
        return mExternalAdaptManager;
    }

    /**
     * {@link UnitsManager} 用来管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
     *
     * @return {@link #mUnitsManager}
     */
    public UnitsManager getUnitsManager() {
        return mUnitsManager;
    }

    /**
     * 返回 {@link #mOnAdaptListener}
     *
     * @return {@link #mOnAdaptListener}
     */
    public onAdaptListener getOnAdaptListener() {
        return mOnAdaptListener;
    }

    /**
     * 返回 {@link #isBaseOnWidth}
     *
     * @return {@link #isBaseOnWidth}
     */
    public boolean isBaseOnWidth() {
        return isBaseOnWidth;
    }

    /**
     * 返回 {@link #isUseDeviceSize}
     *
     * @return {@link #isUseDeviceSize}
     */
    public boolean isUseDeviceSize() {
        return isUseDeviceSize;
    }

    /**
     * 返回 {@link #mScreenWidth}
     *
     * @return {@link #mScreenWidth}
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * 返回 {@link #mScreenHeight}
     *
     * @return {@link #mScreenHeight}
     */
    public int getScreenHeight() {
        return isUseDeviceSize() ? mScreenHeight : mScreenHeight - mStatusBarHeight;
    }

    /**
     * 获取 {@link #mDesignWidthInDp}
     *
     * @return {@link #mDesignWidthInDp}
     */
    public int getDesignWidthInDp() {
        Preconditions.checkArgument(mDesignWidthInDp > 0, "you must set " + KEY_DESIGN_WIDTH_IN_DP + "  in your AndroidManifest file");
        return mDesignWidthInDp;
    }

    /**
     * 获取 {@link #mDesignHeightInDp}
     *
     * @return {@link #mDesignHeightInDp}
     */
    public int getDesignHeightInDp() {
        Preconditions.checkArgument(mDesignHeightInDp > 0, "you must set " + KEY_DESIGN_HEIGHT_IN_DP + "  in your AndroidManifest file");
        return mDesignHeightInDp;
    }

    /**
     * 获取 {@link #mInitDensity}
     *
     * @return {@link #mInitDensity}
     */
    public float getInitDensity() {
        return mInitDensity;
    }

    /**
     * 获取 {@link #mInitDensityDpi}
     *
     * @return {@link #mInitDensityDpi}
     */
    public int getInitDensityDpi() {
        return mInitDensityDpi;
    }

    /**
     * 获取 {@link #mInitScaledDensity}
     *
     * @return {@link #mInitScaledDensity}
     */
    public float getInitScaledDensity() {
        return mInitScaledDensity;
    }

    /**
     * 获取 {@link #mInitXdpi}
     *
     * @return {@link #mInitXdpi}
     */
    public float getInitXdpi() {
        return mInitXdpi;
    }

    /**
     * 获取屏幕方向
     *
     * @return {@code true} 为纵向, {@code false} 为横向
     */
    public boolean isVertical() {
        return isVertical;
    }

    /**
     * 返回 {@link #isMiui}
     *
     * @return {@link #isMiui}
     */
    public boolean isMiui() {
        return isMiui;
    }

    /**
     * 返回 {@link #mTmpMetricsField}
     *
     * @return {@link #mTmpMetricsField}
     */
    public Field getTmpMetricsField() {
        return mTmpMetricsField;
    }

    /**
     * 设置屏幕方向
     *
     * @param vertical {@code true} 为纵向, {@code false} 为横向
     */
    public AutoSizeConfig setVertical(boolean vertical) {
        isVertical = vertical;
        return this;
    }

    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 {@code true}, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 {@code false}, 则会跟随系统设置中字体大小的改变, 默认为 {@code false}
     *
     * @return {@link #isExcludeFontScale}
     */
    public boolean isExcludeFontScale() {
        return isExcludeFontScale;
    }

    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 {@code true}, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 {@code false}, 则会跟随系统设置中字体大小的改变, 默认为 {@code false}
     *
     * @param excludeFontScale 是否屏蔽
     */
    public AutoSizeConfig setExcludeFontScale(boolean excludeFontScale) {
        isExcludeFontScale = excludeFontScale;
        return this;
    }

    /**
     * 设置屏幕宽度
     *
     * @param screenWidth 屏幕宽度
     */
    public AutoSizeConfig setScreenWidth(int screenWidth) {
        Preconditions.checkArgument(screenWidth > 0, "screenWidth must be > 0");
        mScreenWidth = screenWidth;
        return this;
    }

    /**
     * 设置屏幕高度
     *
     * @param screenHeight 屏幕高度 (需要包含状态栏)
     */
    public AutoSizeConfig setScreenHeight(int screenHeight) {
        Preconditions.checkArgument(screenHeight > 0, "screenHeight must be > 0");
        mScreenHeight = screenHeight;
        return this;
    }

    /**
     * 设置全局设计图宽度
     *
     * @param designWidthInDp 设计图宽度
     */
    public AutoSizeConfig setDesignWidthInDp(int designWidthInDp) {
        Preconditions.checkArgument(designWidthInDp > 0, "designWidthInDp must be > 0");
        mDesignWidthInDp = designWidthInDp;
        return this;
    }

    /**
     * 设置全局设计图高度
     *
     * @param designHeightInDp 设计图高度
     */
    public AutoSizeConfig setDesignHeightInDp(int designHeightInDp) {
        Preconditions.checkArgument(designHeightInDp > 0, "designHeightInDp must be > 0");
        mDesignHeightInDp = designHeightInDp;
        return this;
    }

    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight 状态栏高度
     */
    public AutoSizeConfig setStatusBarHeight(int statusBarHeight) {
        Preconditions.checkArgument(statusBarHeight > 0, "statusBarHeight must be > 0");
        mStatusBarHeight = statusBarHeight;
        return this;
    }

    /**
     * 获取使用者在 AndroidManifest 中填写的 Meta 信息
     * <p>
     * Example usage:
     * <pre>
     * <meta-data android:name="design_width_in_dp"
     *            android:value="360"/>
     * <meta-data android:name="design_height_in_dp"
     *            android:value="640"/>
     * </pre>
     *
     * @param context {@link Context}
     */
    private void getMetaData(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo;
                try {
                    applicationInfo = packageManager.getApplicationInfo(context
                            .getPackageName(), PackageManager.GET_META_DATA);
                    if (applicationInfo != null && applicationInfo.metaData != null) {
                        if (applicationInfo.metaData.containsKey(KEY_DESIGN_WIDTH_IN_DP)) {
                            mDesignWidthInDp = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH_IN_DP);
                        }
                        if (applicationInfo.metaData.containsKey(KEY_DESIGN_HEIGHT_IN_DP)) {
                            mDesignHeightInDp = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT_IN_DP);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
