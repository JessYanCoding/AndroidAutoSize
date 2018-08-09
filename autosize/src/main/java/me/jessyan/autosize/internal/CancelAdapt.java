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
package me.jessyan.autosize.internal;

import android.app.Activity;

/**
 * ================================================
 * AndroidAutoSize 默认项目中的所有模块都使用适配功能, 三方库的 {@link Activity} 也不例外
 * 如果某个页面不想使用适配功能, 请让该页面 {@link Activity} 实现此接口
 * 实现此接口表示放弃适配, 所有的适配效果都将失效
 * <p>
 * Created by JessYan on 2018/8/9 09:54
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface CancelAdapt {
}
