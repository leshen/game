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

package game.shenle.com.dragger.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import game.shenle.com.CreateJbActivity;
import game.shenle.com.EditJbActivity;
import game.shenle.com.GameActivity;
import game.shenle.com.MainActivity;
import game.shenle.com.MyJbListActivity;
import game.shenle.com.MyMusicActivity;
import game.shenle.com.NewGameBeginActivity;
import game.shenle.com.NewUserCreateActivity;
import game.shenle.com.OtherActivity;
import game.shenle.com.ReadBookActivity;

@Module
public abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract GameActivity contributeGameActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract NewGameBeginActivity contributeNewGameBeginActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract NewUserCreateActivity contributeNewUserCreateActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract CreateJbActivity contributeCreateJbActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract EditJbActivity contributeEditJbActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ReadBookActivity contributeReadBookActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MyJbListActivity contributeMyJbListActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract OtherActivity contributeOtherActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MyMusicActivity contributeMyMusicActivity();
}
