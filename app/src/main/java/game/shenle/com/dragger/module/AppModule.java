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

import android.arch.persistence.room.Room;

import com.example.android.observability.persistence.AppDatabase;
import com.example.android.observability.persistence.BookContentBeanDao;
import com.example.android.observability.persistence.BookInfoBeanDao;
import com.example.android.observability.persistence.BookShelfBeanDao;
import com.example.android.observability.persistence.ChapterListBeanDao;
import com.example.android.observability.persistence.GameUserDao;
import com.example.android.observability.persistence.JbContentDao;
import com.example.android.observability.persistence.JbDao;
import com.example.android.observability.persistence.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import game.shenle.com.MyApplication;

@Module(includes = ViewModelModule.class)
public class AppModule{
    private MyApplication app;
    public AppModule(MyApplication app){
        this.app = app;
    }
    @Provides@Singleton
    public MyApplication provideMyApplication(){
        return app;
    }
    @Provides@Singleton
    public UserDao provideUserDao(AppDatabase db){
        return db.userDao();
    }
    @Provides@Singleton
    public JbDao provideJbDao(AppDatabase db){
        return db.jbDao();
    }
    @Provides@Singleton
    public JbContentDao provideJbContentDao(AppDatabase db){
        return db.jbContentDao();
    }
    @Provides@Singleton
    public GameUserDao provideGameUserDao(AppDatabase db){
        return db.gameUserDao();
    }
    @Provides@Singleton
    public BookContentBeanDao provideBookContentBeanDao(AppDatabase db){
        return db.bookContentBeanDao();
    }
    @Provides@Singleton
    public BookInfoBeanDao provideBookInfoBeanDao(AppDatabase db){
        return db.bookInfoBeanDao();
    }
    @Provides@Singleton
    public BookShelfBeanDao provideBookShelfBeanDao(AppDatabase db){
        return db.bookShelfBeanDao();
    }
    @Provides@Singleton
    public ChapterListBeanDao provideChapterListBeanDao(AppDatabase db){
        return db.chapterListBeanDao();
    }

    @Singleton@Provides
    public AppDatabase provideDb(MyApplication app) {
        return Room.databaseBuilder(app, AppDatabase.class,"bkjh.db").build();
    }
}
