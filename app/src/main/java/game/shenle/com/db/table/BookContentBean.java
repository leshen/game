//Copyright (c) 2017. 章钦豪. All rights reserved.
package game.shenle.com.db.table;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.Transient;

/**
 * 书本缓存内容
 */
@Entity(tableName = "book_content_info")
public class BookContentBean implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String url=""; //对应BookInfoBean noteUrl;

    public int currentIndex;   //当前章节  （包括番外）

    public String durCapterContent; //当前章节内容

    public String from;   //来源  某个网站/本地

    public Boolean isRight = true;

    public List<String> lineContent = new ArrayList<>();

    public float lineSize;

    public BookContentBean(){

    }

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    protected BookContentBean(Parcel in) {
        url = in.readString();
        currentIndex = in.readInt();
        durCapterContent = in.readString();
        from = in.readString();
        lineContent = in.createStringArrayList();
        isRight = in.readByte()!=0;
    }

    public BookContentBean(String url, int currentIndex,
                           String durCapterContent, String from) {
        this.url = url;
        this.currentIndex = currentIndex;
        this.durCapterContent = durCapterContent;
        this.from = from;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(currentIndex);
        dest.writeString(durCapterContent);
        dest.writeString(from);
        dest.writeStringList(lineContent);
        dest.writeByte((byte) (isRight ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Transient
    public static final Parcelable.Creator<BookContentBean> CREATOR = new Creator<BookContentBean>() {
        @Override
        public BookContentBean createFromParcel(Parcel in) {
            return new BookContentBean(in);
        }

        @Override
        public BookContentBean[] newArray(int size) {
            return new BookContentBean[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getDurCapterContent() {
        return durCapterContent;
    }

    public void setDurCapterContent(String durCapterContent) {
        this.durCapterContent = durCapterContent;
        if(durCapterContent==null || durCapterContent.length()==0)
            this.isRight = false;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getLineContent() {
        return lineContent;
    }

    public void setLineContent(List<String> lineContent) {
        this.lineContent = lineContent;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }
}
