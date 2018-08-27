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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;

/**
 * ================================================
 * {@link DisplayMetrics} 封装类
 * <p>
 * Created by JessYan on 2018/8/11 16:42
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class DisplayMetricsInfo implements Parcelable {
    private float density;
    private int densityDpi;
    private float scaledDensity;
    private float xdpi;

    public DisplayMetricsInfo(float density, int densityDpi, float scaledDensity, float xdpi) {
        this.density = density;
        this.densityDpi = densityDpi;
        this.scaledDensity = scaledDensity;
        this.xdpi = xdpi;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    public float getScaledDensity() {
        return scaledDensity;
    }

    public void setScaledDensity(float scaledDensity) {
        this.scaledDensity = scaledDensity;
    }

    public float getXdpi() {
        return xdpi;
    }

    public void setXdpi(float xdpi) {
        this.xdpi = xdpi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.density);
        dest.writeInt(this.densityDpi);
        dest.writeFloat(this.scaledDensity);
        dest.writeFloat(this.xdpi);
    }

    protected DisplayMetricsInfo(Parcel in) {
        this.density = in.readFloat();
        this.densityDpi = in.readInt();
        this.scaledDensity = in.readFloat();
        this.xdpi = in.readFloat();
    }

    public static final Creator<DisplayMetricsInfo> CREATOR = new Creator<DisplayMetricsInfo>() {
        @Override
        public DisplayMetricsInfo createFromParcel(Parcel source) {
            return new DisplayMetricsInfo(source);
        }

        @Override
        public DisplayMetricsInfo[] newArray(int size) {
            return new DisplayMetricsInfo[size];
        }
    };

    @Override
    public String toString() {
        return "DisplayMetricsInfo{" +
                "density=" + density +
                ", densityDpi=" + densityDpi +
                ", scaledDensity=" + scaledDensity +
                ", xdpi=" + xdpi +
                '}';
    }
}
