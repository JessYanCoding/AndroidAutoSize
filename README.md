![Logo](art/autosize_banner.jpg)
![Official](https://raw.githubusercontent.com/JessYanCoding/MVPArms/master/image/official.jpeg)

<p align="center">
   <a href="https://bintray.com/jessyancoding/maven/autosize/_latestVersion">
    <img src="https://img.shields.io/badge/Jcenter-v1.1.2-brightgreen.svg?style=flat-square" alt="Latest Stable Version" />
  </a>
  <a href="https://travis-ci.org/JessYanCoding/AndroidAutoSize">
    <img src="https://travis-ci.org/JessYanCoding/AndroidAutoSize.svg?branch=master" alt="Build Status" />
  </a>
  <a href="https://developer.android.com/about/versions/android-4.0.html">
    <img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
  <a href="http://www.apache.org/licenses/LICENSE-2.0">
    <img src="http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License" />
  </a>
  <a href="https://www.jianshu.com/u/1d0c0bc634db">
    <img src="https://img.shields.io/badge/Author-JessYan-orange.svg?style=flat-square" alt="Author" />
  </a>
  <a href="https://shang.qq.com/wpa/qunwpa?idkey=7e59e59145e6c7c68932ace10f52790636451f01d1ecadb6a652b1df234df753">
    <img src="https://img.shields.io/badge/QQ%E7%BE%A4-455850365%20%7C%20301733278-orange.svg?style=flat-square" alt="QQ Group" />
  </a>
</p>

<p align="center">
  <a href="README-zh.md">
    <b>中文说明</b>
  </a>
</p>

## A low-cost Android screen adaptation solution (今日头条屏幕适配方案终极版，一个极低成本的 Android 屏幕适配方案).

## Overview
### Pixel 2 XL | 1440 x 2880 | 560dpi:
<p>
   <img src="art/1440x2880_width.png" width="30%" height="30%">
   <img src="art/1440x2880_height.png" width="30%" height="30%">
   <img src="art/1440x2880_external.png" width="30%" height="30%">
</p>

### Pixel XL | 1440 x 2560 | 560dpi:
<p>
   <img src="art/1440x2560_width.png" width="30%" height="30%">
   <img src="art/1440x2560_height.png" width="30%" height="30%">
   <img src="art/1440x2560_external.png" width="30%" height="30%">
</p>

### Nexus 5X | 1080 x 1920 | 420dpi:
<p>
   <img src="art/1080x1920_width.png" width="30%" height="30%">
   <img src="art/1080x1920_height.png" width="30%" height="30%">
   <img src="art/1080x1920_external.png" width="30%" height="30%">
</p>

### Nexus 4 | 768 x 1280 | 320dpi:
<p>
   <img src="art/768x1280_width.png" width="30%" height="30%">
   <img src="art/768x1280_height.png" width="30%" height="30%">
   <img src="art/768x1280_external.png" width="30%" height="30%">
</p>

### Nexus S | 480 x 800 | 240dpi:
<p>
   <img src="art/480x800_width.png" width="30%" height="30%">
   <img src="art/480x800_height.png" width="30%" height="30%">
   <img src="art/480x800_external.png" width="30%" height="30%">
</p>

## Notice
* [Devices Info](https://material.io/tools/devices/)

* [Introduction Of Function](https://juejin.im/post/5bce688e6fb9a05cf715d1c2)

* [Framework Analysis](https://juejin.im/post/5b7a29736fb9a019d53e7ee2)

* [Common Issues](https://github.com/JessYanCoding/AndroidAutoSize/issues/13)

* [AndroidAutoLayout Migration Guide](https://github.com/JessYanCoding/AndroidAutoSize/issues/90)

* [Android Advanced Framework](https://github.com/JessYanCoding/MVPArms)

## Download
``` gradle
 implementation 'me.jessyan:autosize:1.1.2'
```

## Usage
### Step 1 (just one steps) 
* **Initialize in AndroidManifest, if you use a subunits, you can write the pixel size, no need to convert the pixel to dp, please see [demo-subunits](https://github.com/JessYanCoding/AndroidAutoSize/tree/master/demo-subunits)**
```xml
<manifest>
    <application>            
        <meta-data
            android:name="design_width_in_dp"
            android:value="360"/>
        <meta-data
            android:name="design_height_in_dp"
            android:value="640"/>           
     </application>           
</manifest>
```

<a name="preview"></a>
## Preview
* Real-time preview during layout is an important part of the development phase, in many cases, the default preview device provided by **Android Studio** does not fully display our design, so we need to create the virtual device ourselves, under the **dp, pt, in, mm** four units of virtual device creation method

* If you don't want the status bar and navigation bar to appear in **Preview** during preview, you can select the **panel** theme according to the following image, after using this theme, the vertical resolution just fills the entire preview page
![theme](art/theme_panel.png)

* Virtual device creation method
![create step](art/create_step.png)

### DP
* If you use **dp** as a unit in the **layout** file for layout (**AndroidAutoSize** supports **dp, sp** for layout by default), you can find the screen size according to the formula **(sqrt(vertical resolution^2 + horizontal resolution^2))/dpi** and create an virtual device (**write screen size and resolution only**)
![dp](art/unit_dp.png)

### PT
* If you use **pt** as a unit in the **layout** file for layout (requires **AutoSizeConfig.getInstance().getUnitsManager().setSupportSubunits(Subunits.PT);** to open **pt** support), you can find the screen size according to the formula **(sqrt(vertical resolution^2 + horizontal resolution^2))/72** and create an virtual device (**write screen size and resolution only**)
![pt](art/unit_pt.png)

### IN
* If you use **in** as a unit in the **layout** file for layout (requires **AutoSizeConfig.getInstance().getUnitsManager().setSupportSubunits(Subunits.IN);** to open **in** support), you can find the screen size according to the formula **sqrt(vertical resolution^2 + horizontal resolution^2)** and create an virtual device (**write screen size and resolution only**)
![in](art/unit_in.png)

### MM
* If you use **mm** as a unit in the **layout** file for layout (requires **AutoSizeConfig.getInstance().getUnitsManager().setSupportSubunits(Subunits.MM);** to open **mm** support), you can find the screen size according to the formula **(sqrt(vertical resolution^2 + horizontal resolution^2))/25.4** and create an virtual device (**write screen size and resolution only**)
![mm](art/unit_mm.png)

## Advance (see demo)

### Activity
* **Customize the adaptation parameters of the Activity:**
```java
public class CustomAdaptActivity extends AppCompatActivity implements CustomAdapt {

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
```

* **Cancel the adaptation of the Activity:**
```java
public class CancelAdaptActivity extends AppCompatActivity implements CancelAdapt {

}
```

### Fragment
* **First enable the ability to support Fragment custom parameters**
```java
AutoSizeConfig.getInstance().setCustomFragment(true);
```

* **Customize the adaptation parameters of the Fragment:**
```java
public class CustomAdaptFragment extends Fragment implements CustomAdapt {

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
```

* **Cancel the adaptation of the Fragment:**
```java
public class CancelAdaptFragment extends Fragment implements CancelAdapt {

}
```

### Subunits (see demo-subunits)
* You can choose one of the three unpopular units of **pt, in, mm** as the subunits, the subunits is used to avoid the adverse effects caused by modifying **DisplayMetrics#density**, after using the subunits, you can write the pixel size on the design, you don't need to convert it to **dp**


```java
AutoSizeConfig.getInstance().getUnitsManager()
        .setSupportDP(false)
        .setSupportSP(false)
        .setSupportSubunits(Subunits.MM);
```

## About Me
* **Email**: <jess.yan.effort@gmail.com>
* **Home**: <http://jessyan.me>
* **掘金**: <https://juejin.im/user/57a9dbd9165abd0061714613>
* **简书**: <https://www.jianshu.com/u/1d0c0bc634db>

## License
```
 Copyright 2018, jessyan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
