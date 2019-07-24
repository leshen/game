//Copyright (c) 2017. 章钦豪. All rights reserved.
package game.shenle.com.db.repository;


import javax.inject.Inject;
import javax.inject.Singleton;

import game.shenle.com.db.table.BookContentBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
@Singleton
public class WebBookModelRepository{
    @Inject
    public WebBookModelRepository(){
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 网络请求并解析书籍信息
     * return BookShelfBean
     */
//    public Observable<BookShelfBean> getBookInfo(BookShelfBean bookShelfBean) {
//        if(bookShelfBean.getTag().equals(GxwztvBookModelImpl.TAG)){
//            return GxwztvBookModelImpl.getInstance().getBookInfo(bookShelfBean);
//        }
//        else if(bookShelfBean.getTag().equals(LingdiankanshuStationBookModelImpl.TAG)){
//            return LingdiankanshuStationBookModelImpl.getInstance().getBookInfo(bookShelfBean);
//        }
//        else {
//            return null;
//        }
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 网络解析图书目录
     * return BookShelfBean
     */
//    public void getChapterList(final BookShelfBean bookShelfBean, OnGetChapterListListener getChapterListListener) {
//        if(bookShelfBean.getTag().equals(GxwztvBookModelImpl.TAG)){
//            GxwztvBookModelImpl.getInstance().getChapterList(bookShelfBean, getChapterListListener);
//        }
//        else if(bookShelfBean.getTag().equals(LingdiankanshuStationBookModelImpl.TAG)){
//            LingdiankanshuStationBookModelImpl.getInstance().getChapterList(bookShelfBean, getChapterListListener);
//        }
//        else{
//            if(getChapterListListener!=null)
//                getChapterListListener.success(bookShelfBean);
//        }
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 章节缓存
     */
    public Observable<BookContentBean> getBookContent(String durChapterUrl, int durChapterIndex, String tag) {
//        if(tag.equals(GxwztvBookRepository.TAG)){
//            return gxwztvBookRepository.getBookContent(durChapterUrl,durChapterIndex);
//        }
//        else if(tag.equals(LingdiankanshuStationBookModelImpl.TAG)){
//            return LingdiankanshuStationBookModelImpl.getInstance().getBookContent(durChapterUrl, durChapterIndex);
//        }
//        else
            return Observable.create(new ObservableOnSubscribe<BookContentBean>() {
                @Override
                public void subscribe(ObservableEmitter<BookContentBean> e) throws Exception {
                    e.onNext(new BookContentBean());
                    e.onComplete();
                }
            });
    }
//
//    /**
//     * 其他站点集合搜索
//     */
//    public Observable<List<SearchBookBean>> searchOtherBook(String content, int page, String tag){
//        if(tag.equals(GxwztvBookModelImpl.TAG)){
//            return GxwztvBookModelImpl.getInstance().searchBook(content, page);
//        }
//        else if(tag.equals(LingdiankanshuStationBookModelImpl.TAG)){
//            return LingdiankanshuStationBookModelImpl.getInstance().searchBook(content, page);
//        }
//        else{
//            return Observable.create(new ObservableOnSubscribe<List<SearchBookBean>>() {
//                @Override
//                public void subscribe(ObservableEmitter<List<SearchBookBean>> e) throws Exception {
//                    e.onNextP(new ArrayList<SearchBookBean>());
//                    e.onComplete();
//                }
//            });
//        }
//    }
//    /**
//     * 获取分类书籍
//     */
//    public Observable<List<SearchBookBean>> getKindBook(String url, int page) {
//        return GxwztvBookModelImpl.getInstance().getKindBook(url,page);
//    }
}
