![Logo](art/autosize_banner.jpg)

<p align="center">
   <a href="https://bintray.com/jessyancoding/maven/autosize/_latestVersion">
    <img src="https://img.shields.io/badge/Jcenter-v0.6.0-brightgreen.svg?style=flat-square" alt="Latest Stable Version" />
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
  <a href="https://shang.qq.com/wpa/qunwpa?idkey=1a5dc5e9b2e40a780522f46877ba243eeb64405d42398643d544d3eec6624917">
    <img src="https://img.shields.io/badge/QQ群-301733278-orange.svg?style=flat-square" alt="QQ Group" />
  </a>
</p>


## A low-cost Android screen adaptation solution (今日头条屏幕适配方案终极版，一个极低成本的 Android 屏幕适配方案).


## Download
``` gradle
 implementation 'me.jessyan:autosize:0.6.0'
```

## Usage
### Step 1 
#### In AndroidManifest:
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

### Step 2
#### In Application:
```java
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AutoSizeConfig.getInstance()
                .setLog(true)
                .init(this)
                .setUseDeviceSize(false);
    }
}    
```

### Advanced
#### In Activity:

**Customize the adaptation parameters of the Activity**
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

**Cancel the adaptation of the Activity**
```java
public class CancelAdaptActivity extends AppCompatActivity implements CancelAdapt {

}

```

## ProGuard
```
 -keep class me.jessyan.autosize.** { *; }
 -keep interface me.jessyan.autosize.** { *; }
```


## Donate
![alipay](https://raw.githubusercontent.com/JessYanCoding/MVPArms/master/image/pay_alipay.jpg) ![](https://raw.githubusercontent.com/JessYanCoding/MVPArms/master/image/pay_wxpay.jpg)

## About Me
* **Email**: <jess.yan.effort@gmail.com>
* **Home**: <http://jessyan.me>
* **掘金**: <https://gold.xitu.io/user/57a9dbd9165abd0061714613>
* **简书**: <http://www.jianshu.com/u/1d0c0bc634db>

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