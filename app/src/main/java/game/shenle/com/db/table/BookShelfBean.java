//Copyright (c) 2017. 章钦豪. All rights reserved.
package game.shenle.com.db.table;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import game.shenle.com.view.contentswitchview.BookContentView;

import android.support.annotation.NonNull;
import kotlin.jvm.Transient;


/**
 * 书架item Bean
 */

@Entity(tableName = "book_shelf_info")
public class BookShelfBean implements Parcelable,Cloneable {
    public static final long REFRESH_TIME = 5*60*1000;   //更新时间间隔 至少
    public static final String LOCAL_TAG = "loc_book";
    @PrimaryKey(autoGenerate = false)@NonNull
    public String url=""; //对应BookInfoBean noteUrl;

    public int durChapter;   //当前章节 （包括番外）

    public int durChapterPage = BookContentView.DURPAGEINDEXBEGIN;  // 当前章节位置   用页码

    public long finalDate;  //最后阅读时间

    public String from;
    @Embedded
    public BookInfoBean bookInfoBean = new BookInfoBean();

    public BookShelfBean(){

    }

    protected BookShelfBean(Parcel in) {
        url = in.readString();
        durChapter = in.readInt();
        durChapterPage = in.readInt();
        finalDate = in.readLong();
        from = in.readString();
        bookInfoBean = in.readParcelable(BookInfoBean.class.getClassLoader());
    }

    public BookShelfBean(String url, int durChapter, int durChapterPage, long finalDate,
                         String from) {
        this.url = url;
        this.durChapter = durChapter;
        this.durChapterPage = durChapterPage;
        this.finalDate = finalDate;
        this.from = from;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(durChapter);
        dest.writeInt(durChapterPage);
        dest.writeLong(finalDate);
        dest.writeString(from);
        dest.writeParcelable(bookInfoBean, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Transient
    public static final Parcelable.Creator<BookShelfBean> CREATOR = new Creator<BookShelfBean>() {
        @Override
        public BookShelfBean createFromParcel(Parcel in) {
            return new BookShelfBean(in);
        }

        @Override
        public BookShelfBean[] newArray(int size) {
            return new BookShelfBean[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDurChapter() {
        return durChapter;
    }

    public void setDurChapter(int durChapter) {
        this.durChapter = durChapter;
    }

    public int getDurChapterPage() {
        return durChapterPage;
    }

    public void setDurChapterPage(int durChapterPage) {
        this.durChapterPage = durChapterPage;
    }

    public long getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(long finalDate) {
        this.finalDate = finalDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public BookInfoBean getBookInfoBean() {
        return bookInfoBean;
    }

    public void setBookInfoBean(BookInfoBean bookInfoBean) {
        this.bookInfoBean = bookInfoBean;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BookShelfBean bookShelfBean = (BookShelfBean) super.clone();
        bookShelfBean.url = url;
        bookShelfBean.from = from;
        bookShelfBean.bookInfoBean = (BookInfoBean) bookInfoBean.clone();
        return bookShelfBean;
    }
}