package game.shenle.com.viewmodel

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.android.observability.persistence.BookContentBeanDao
import com.example.android.observability.persistence.BookInfoBeanDao
import com.example.android.observability.persistence.BookShelfBeanDao
import com.example.android.observability.persistence.ChapterListBeanDao
import com.trello.rxlifecycle2.android.ActivityEvent
import game.shenle.com.BitIntentDataManager
import game.shenle.com.ReadBookActivity
import game.shenle.com.common.RxBusTag
import game.shenle.com.db.repository.ImportBookRepository
import game.shenle.com.db.repository.WebBookModelRepository
import game.shenle.com.db.table.BookContentBean
import game.shenle.com.db.table.BookShelfBean
import game.shenle.com.common.bean.ReadBookContentBean
import game.shenle.com.view.contentswitchview.BookContentView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import lib.shenle.com.utils.PremissionCheck
import lib.shenle.com.utils.RxBus
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Created by shenle on 2017/11/15.
 */
class ReadBookViewModel : BaseViewModel {
    private var importBookRepository: ImportBookRepository

    @Inject
    constructor(importBookRepository: ImportBookRepository) : super() {
        this.importBookRepository = importBookRepository
    }

    companion object {
        val OPEN_FROM_OTHER = 0
        val OPEN_FROM_APP = 1
    }

    private var isAdd: Boolean = false //判断是否已经添加进书架
    private var open_from: Int = 0
    private var bookShelf: BookShelfBean? = null

    private var pageLineCount = 5   //假设5行一页
    @Inject
    lateinit var chapterListBeanDao: ChapterListBeanDao
    @Inject
    lateinit var bookContentBeanDao: BookContentBeanDao
    @Inject
    lateinit var bookShelfBeanDao: BookShelfBeanDao
    @Inject
    lateinit var bookInfoBeanDao: BookInfoBeanDao
    @Inject
    lateinit var webBookModelRepository: WebBookModelRepository
    private lateinit var activity: ReadBookActivity

    fun initData(activity: ReadBookActivity) {
        this.activity = activity
        val intent = activity.intent
        open_from = intent.getIntExtra("from", OPEN_FROM_OTHER)
        if (open_from == OPEN_FROM_APP) {
            val key = intent.getStringExtra("data_key")
            bookShelf = BitIntentDataManager.getInstance().getData(key) as BookShelfBean
            if (!bookShelf!!.getFrom().equals(BookShelfBean.LOCAL_TAG)) {
                activity.showDownloadMenu()
            }
            BitIntentDataManager.getInstance().cleanData(key)
            checkInShelf()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PremissionCheck.checkPremission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //申请权限
                activity.requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0x11)
            } else {
                openBookFromOther(activity)
            }
        }
    }

    fun openBookFromOther(activity: ReadBookActivity) {
        //APP外部打开
        val uri = activity.intent.data
        activity.showLoadBook()
        getRealFilePath(activity, uri)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    importBookRepository.importBook(File(it))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe {
                                if (it.getNew())
                                    RxBus.get().post(RxBusTag.HAD_ADD_BOOK, it)
                                bookShelf = it.getBookShelfBean()
                                activity.dimissLoadBook()
                                checkInShelf()
                            }
                }
    }

    fun detachView() {}

    fun getOpen_from(): Int {
        return open_from
    }

    fun getBookShelf(): BookShelfBean? {
        return bookShelf
    }

    fun initContent() {
        activity.initContentSuccess(bookShelf!!.getDurChapter(), bookShelf!!.getBookInfoBean().getChapterlist().size, bookShelf!!.getDurChapterPage())
    }

    fun loadContent(bookContentView: BookContentView?, bookTag: Long, chapterIndex: Int, pageIndex: Int) {
        var pageIndex = pageIndex
        if (null != bookShelf && bookShelf!!.getBookInfoBean().getChapterlist().size > 0) {
            if (null != bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean() && null != bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getDurCapterContent()) {
                if (bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineSize() === activity.paint.getTextSize() && bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().size > 0) {
                    //已有数据
                    val tempCount = Math.ceil(bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().size * 1.0 / pageLineCount).toInt() - 1

                    if (pageIndex == BookContentView.DURPAGEINDEXBEGIN) {
                        pageIndex = 0
                    } else if (pageIndex == BookContentView.DURPAGEINDEXEND) {
                        pageIndex = tempCount
                    } else {
                        if (pageIndex >= tempCount) {
                            pageIndex = tempCount
                        }
                    }

                    val start = pageIndex * pageLineCount
                    val end = if (pageIndex == tempCount) bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().size else start + pageLineCount
                    if (bookContentView != null && bookTag == bookContentView!!.getqTag()) {
                        bookContentView!!.updateData(bookTag, bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getDurChapterName(), bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().subList(start, end), chapterIndex, bookShelf!!.getBookInfoBean().getChapterlist().size, pageIndex, tempCount + 1)
                    }
                } else {
                    //有元数据  重新分行
                    bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().setLineSize(activity.paint.getTextSize())
                    val finalPageIndex = pageIndex
                    SeparateParagraphtoLines(bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getDurCapterContent())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .compose<List<String>>(activity.bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribe {
                                bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().clear()
                                bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().addAll(it)
                                loadContent(bookContentView, bookTag, chapterIndex, finalPageIndex)
                            }
                }
            } else {
                val finalPageIndex1 = pageIndex
                Observable.create(ObservableOnSubscribe<ReadBookContentBean> { e ->
                    val tempList = bookContentBeanDao.getBookInfoByUrl(bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getDurChapterUrl()).value
                    e.onNext(ReadBookContentBean(tempList
                            ?: ArrayList<BookContentBean>(), finalPageIndex1))
                    e.onComplete()
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe {
                            if (it.getBookContentList() != null && it.getBookContentList().size > 0 && it.getBookContentList().get(0).getDurCapterContent() != null) {
                                bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).setBookContentBean(it.getBookContentList().get(0))
                                loadContent(bookContentView, bookTag, chapterIndex, it.getPageIndex())
                            } else {
                                val finalPageIndex1 = it.getPageIndex()
                                webBookModelRepository.getBookContent(bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getDurChapterUrl(), chapterIndex, bookShelf!!.getFrom()).map(Function<BookContentBean, BookContentBean> { bookContentBean ->
                                    if (bookContentBean.getRight()) {
                                        bookContentBeanDao.insertBookContentBean(bookContentBean)
                                        bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).setHasCache(true)
                                        chapterListBeanDao.update(bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex))
                                    }
                                    bookContentBean
                                })
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.newThread())
                                        .compose(activity.bindUntilEvent<BookContentBean>(ActivityEvent.DESTROY))
                                        .subscribe {
                                            if (it.getUrl() != null && it.getUrl().length > 0) {
                                                bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).setBookContentBean(it)
                                                if (bookTag == bookContentView!!.getqTag())
                                                    loadContent(bookContentView, bookTag, chapterIndex, finalPageIndex1)
                                            } else {
                                                if (bookContentView != null && bookTag == bookContentView!!.getqTag())
                                                    bookContentView!!.loadError()
                                            }
                                        }
                            }
                        }
            }
        } else {
            if (bookContentView != null && bookTag == bookContentView!!.getqTag())
                bookContentView!!.loadError()
        }
    }

    fun updateProgress(chapterIndex: Int, pageIndex: Int) {
        bookShelf!!.setDurChapter(chapterIndex)
        bookShelf!!.setDurChapterPage(pageIndex)
    }

    fun saveProgress() {
        if (bookShelf != null) {
            Observable.create(ObservableOnSubscribe<BookShelfBean> { e ->
                bookShelf!!.setFinalDate(System.currentTimeMillis())
                bookShelfBeanDao.insertBookShelf(bookShelf!!)
                e.onNext(bookShelf!!)
                e.onComplete()
            }).subscribeOn(Schedulers.newThread())
                    .subscribe {
                        RxBus.get().post(RxBusTag.UPDATE_BOOK_PROGRESS, it)
                    }
        }
    }

    fun getChapterTitle(chapterIndex: Int): String {
        return if (bookShelf!!.getBookInfoBean().getChapterlist().size === 0) {
            "无章节"
        } else
            bookShelf!!.getBookInfoBean().getChapterlist().get(chapterIndex).getDurChapterName()
    }

    fun SeparateParagraphtoLines(paragraphstr: String): Observable<List<String>> {
        return Observable.create { e ->
            val mPaint = activity.paint as TextPaint
            mPaint.isSubpixelText = true
            val tempLayout = StaticLayout(paragraphstr, mPaint, activity.contentWidth, Layout.Alignment.ALIGN_NORMAL, 0f, 0f, false)
            val linesdata = ArrayList<String>()
            for (i in 0 until tempLayout.lineCount) {
                linesdata.add(paragraphstr.substring(tempLayout.getLineStart(i), tempLayout.getLineEnd(i)))
            }
            e.onNext(linesdata)
            e.onComplete()
        }
    }

    fun setPageLineCount(pageLineCount: Int) {
        this.pageLineCount = pageLineCount
    }

    private fun checkInShelf() {
        Observable.create(ObservableOnSubscribe<Boolean> { e ->
            val temp = bookShelfBeanDao.getBookInfoByMd5(bookShelf!!.getUrl()).value
            if (temp == null || temp!!.size == 0) {
                isAdd = false
            } else
                isAdd = true
            e.onNext(isAdd)
            e.onComplete()
        }).subscribeOn(Schedulers.io())
                .compose<Boolean>(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    activity.initPop()
                    activity.setHpbReadProgressMax(bookShelf!!.getBookInfoBean().getChapterlist().size)
                    activity.startLoadingBook()
                }
    }

    interface OnAddListner {
        fun addSuccess()
    }

    fun addToShelf(addListner: OnAddListner?) {
        if (bookShelf != null) {
            Observable.create(ObservableOnSubscribe<Boolean> { e ->
                for (item in bookShelf!!.getBookInfoBean().getChapterlist()) {
                    chapterListBeanDao.insertChapterListBean(item)
                }
                bookInfoBeanDao.insertBookInfoBean(bookShelf!!.getBookInfoBean())
                //网络数据获取成功  存入BookShelf表数据库
                bookShelfBeanDao.insertBookShelf(bookShelf!!)
                RxBus.get().post(RxBusTag.HAD_ADD_BOOK, bookShelf!!)
                isAdd = true
                e.onNext(true)
                e.onComplete()
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        addListner?.addSuccess()
                    }
        }
    }

    fun getAdd(): Boolean {
        return isAdd
    }

    fun getRealFilePath(context: Context, uri: Uri?): Observable<String> {
        return Observable.create { e ->
            var data: String? = ""
            if (null != uri) {
                val scheme = uri.scheme
                if (scheme == null)
                    data = uri.path
                else if (ContentResolver.SCHEME_FILE == scheme) {
                    data = uri.path
                } else if (ContentResolver.SCHEME_CONTENT == scheme) {
                    val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
                    if (null != cursor) {
                        if (cursor.moveToFirst()) {
                            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                            if (index > -1) {
                                data = cursor.getString(index)
                            }
                        }
                        cursor.close()
                    }

                    if ((data == null || data.length <= 0) && uri.path != null && uri.path.contains("/storage/emulated/")) {
                        data = uri.path.substring(uri.path.indexOf("/storage/emulated/"))
                    }
                }
            }
            e.onNext(if (data == null) "" else data)
            e.onComplete()
        }
    }
}


