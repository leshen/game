/*
 * Copyright (C) 2017 The Android Open Source Project
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

package game.shenle.com

import com.raizlabs.android.dbflow.config.FlowManager
import game.shenle.com.dragger.DaggerAppComponent
import game.shenle.com.dragger.module.AppModule
import lib.shenle.com.base.SLBaseApplication
import skin.support.SkinCompatManager
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater


class MyApplication : SLBaseApplication() {
    override fun initDraggerApp() {
        DaggerAppComponent.builder().appModule(AppModule(this))
                .build().inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        SkinChangeInit()
        FlowManager.init(this)
    }

    /**
     * 换肤功能(https://github.com/ximsfei/Android-skin-support/blob/master/docs/cn/README.md#%E5%BA%94%E7%94%A8%E5%86%85%E6%8D%A2%E8%82%A4)
     */
    private fun SkinChangeInit() {
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin()
    }

    fun changeSkin(isNight :Boolean) {
        SkinCompatManager.getInstance().loadSkin(if(isNight) "night" else "", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN) // 后缀加载
        //        SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_PREFIX_BUILD_IN); // 前缀加载
    }
}
