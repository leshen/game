//Copyright (c) 2017. 章钦豪. All rights reserved.
package game.shenle.com.db.table;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kotlin.jvm.Transient;

/**
 * 书本信息
 */
@Entity(tableName = "book_info")
public class BookInfoBean implements Parcelable,Cloneable {

    public static final long REFRESH_DUR = 10*60*1000;
    @PrimaryKey(autoGenerate = false)
    public Long id=0L;
    public String name; //小说名

    public String tag;

    public String noteUrl;  //如果是来源网站   则小说根地址 /如果是本地  则是小说本地MD5

    public String chapterUrl;  //章节目录地址
    @Ignore
    public List<ChapterListBean> chapterlist = new ArrayList<>();    //章节列表

    public long finalRefreshData;  //章节最后更新时间

    public String coverUrl; //小说封面

    public String author;//作者

    public String introduce; //简介

    public String origin; //来源

    public BookInfoBean(){

    }

    protected BookInfoBean(Parcel in) {
        name = in.readString();
        tag = in.readString();
        noteUrl = in.readString();
        chapterUrl = in.readString();
        chapterlist = in.createTypedArrayList(ChapterListBean.CREATOR);
        finalRefreshData = in.readLong();
        coverUrl = in.readString();
        author = in.readString();
        introduce = in.readString();
        origin = in.readString();
    }

    public BookInfoBean(String name, String tag, String noteUrl, String chapterUrl,
                        long finalRefreshData, String coverUrl, String author, String introduce,
                        String origin) {
        this.name = name;
        this.tag = tag;
        this.noteUrl = noteUrl;
        this.chapterUrl = chapterUrl;
        this.finalRefreshData = finalRefreshData;
        this.coverUrl = coverUrl;
        this.author = author;
        this.introduce = introduce;
        this.origin = origin;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tag);
        dest.writeString(noteUrl);
        dest.writeString(chapterUrl);
        dest.writeTypedList(chapterlist);
        dest.writeLong(finalRefreshData);
        dest.writeString(coverUrl);
        dest.writeString(author);
        dest.writeString(introduce);
        dest.writeString(origin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Transient
    public static final Parcelable.Creator<BookInfoBean> CREATOR = new Creator<BookInfoBean>() {
        @Override
        public BookInfoBean createFromParcel(Parcel in) {
            return new BookInfoBean(in);
        }

        @Override
        public BookInfoBean[] newArray(int size) {
            return new BookInfoBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public List<ChapterListBean> getChapterlist() {
        return chapterlist;
    }

    public void setChapterlist(List<ChapterListBean> chapterlist) {
        this.chapterlist = chapterlist;
    }
    public void addChapterlist(List<ChapterListBean> chapterlist){
        this.chapterlist.addAll(chapterlist);
    }

    public long getFinalRefreshData() {
        return finalRefreshData;
    }

    public void setFinalRefreshData(long finalRefreshData) {
        this.finalRefreshData = finalRefreshData;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        BookInfoBean bookInfoBean = (BookInfoBean) super.clone();
        bookInfoBean.name = name;
        bookInfoBean.tag = tag;
        bookInfoBean.noteUrl = noteUrl;
        bookInfoBean.chapterUrl = chapterUrl;
        bookInfoBean.coverUrl = coverUrl;
        bookInfoBean.author = author;
        bookInfoBean.introduce = introduce;
        bookInfoBean.origin = origin;
        if(chapterlist!=null){
            ArrayList<ChapterListBean> newList = new ArrayList<>();
            Iterator<ChapterListBean> iterator = chapterlist.iterator();
            while(iterator.hasNext()){
                newList.add((ChapterListBean) iterator.next().clone());
            }
            bookInfoBean.setChapterlist(newList);
        }
        return bookInfoBean;
    }
}