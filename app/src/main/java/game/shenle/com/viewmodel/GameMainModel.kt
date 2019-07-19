package game.shenle.com.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import com.example.android.observability.persistence.JbContentTable
import game.shenle.com.db.repository.JbContentRepository
import javax.inject.Inject
import game.shenle.com.db.repository.GameUserRepository
import game.shenle.com.db.repository.Status
import game.shenle.com.view.OnPrintListener
import game.shenle.com.view.contentswitchview.BookContentView
import java.util.regex.Pattern
import android.arch.lifecycle.MutableLiveData
import lib.shenle.com.utils.UIUtils
import java.util.*


/**
 * Created by shenle on 2017/11/15.
 */
class GameMainModel : BaseViewModel {
    private var jbContentRepository: JbContentRepository
    private var gameUserRepository: GameUserRepository

    @Inject
    constructor(jbContentRepository: JbContentRepository, gameUserRepository: GameUserRepository) : super() {
        this.jbContentRepository = jbContentRepository
        this.gameUserRepository = gameUserRepository
    }

    //    private var jbContent: LiveData<Resource<List<JbContentTable>>>? = null
    private var zjContent: LiveData<JbContentTable>? = MediatorLiveData<JbContentTable>()
    private var zlStr = MediatorLiveData<String>()

    private var current_zl_process = 0
    private lateinit var jbId: String

    private var jbContentList: List<JbContentTable>? = null

    private var zl_list = HashMap<Int, String>()
    private var state = MediatorLiveData<String>()//状态更改

    fun init(jbId: String) {
        this.jbId = jbId
        zjContent = Transformations.switchMap(zl_do, { zl ->
            Transformations.switchMap(jbContentRepository.getJbContent(jbId, 1L, 10L), {
                if (it?.status == Status.SUCCESS && it?.data != null && it?.data.size != 0) {
                    jbContentList = it?.data
                    var str_copy = jbContentList!![0].bg
                    val jbContentTable = jbContentList!![0]
                    zjContent = Transformations.map(gameUserRepository.getGameUserContent("w9rxDDDi", jbId), {
                        if (it.status == Status.SUCCESS) {
                            str_copy = str_copy.replace("[*初始化数据*]", it.data.toString()).replace("[*用户名*]", it.data?.gameUserName!!)
                            if (zl_list.containsKey(current_zl_process))
                                str_copy = str_copy.replaceFirst(zl_list.get(current_zl_process)!!, zl)!!
                            jbContentTable.bg = str_copy
                            var str = jbContentTable.bg
                            val pz = "\\[\\*(.+?)\\*\\]"
                            val compile = Pattern.compile(pz)
                            val matcher = compile.matcher(str)
                            var i = 0
                            var zl_list_copy = HashMap<Int,String>()
                            while (matcher.find()) {
                                val group = matcher.group()
                                if (group.contains("[*指令_")) {
                                    if (!zl_list.containsValue(group))
                                    str = str.replace(group, "\n$group\n")
                                    i = str.indexOf(group, i)
                                    zl_list_copy.put(i, group)
                                }
                            }
                            zl_list.clear()
                            zl_list = zl_list_copy
                            jbContentTable.bg = str
                            if (!UIUtils.isEmpty(zl)) {
                                bookContentView?.restartPrint(current_zl_process)
                            }else{
                                state.postValue("开始")
                            }
                            jbContentTable
                        } else {
                            null
                        }
                    })
                    zjContent
                } else {
                    null
                }
            })

        })
    }

    fun getZjInput(): LiveData<JbContentTable>? {
        return zjContent
    }
    fun getState(): LiveData<String>? {
        return state
    }

    //控制器
    fun getZlStr(): LiveData<String>? {
        return zlStr
    }

    //保存进度
    fun saveProgress() {
        jbContentRepository.upDataTable(zjContent?.value)
    }

    private var bookContentView: BookContentView? = null

    fun loadContent(bookContentView: BookContentView, qtag: Long, chapterIndex: Int, pageIndex: Int) {
        this.bookContentView = bookContentView
        zjContent?.value?.let {
            val tempCount = Math.ceil((it.bg?.lines().size
                    ?: 0) * 1.0 / pageLineCount).toInt() - 1
            var pageIndex = pageIndex
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
            val end = if (pageIndex == tempCount) it.bg.length
                    ?: 0 else start + pageLineCount
            if (bookContentView != null && qtag == bookContentView!!.getqTag()) {
//                val pz = "\\[\\*(.+?)\\*\\]"
//                val compile = Pattern.compile(pz)
//                val matcher = compile.matcher(it.bg)
//                while (matcher.find()) {
//                    val group = matcher.group()
//                    when (group) {
//                        "[*指令_方向*]" -> {
//
//                        }
//                    }
//                }
                bookContentView.updateDataForPrint(qtag, it.jbTitle, it?.bg?.toList().map { it.toString() }.subList(start, end), it.totalZj, chapterIndex, pageIndex, tempCount + 1, object : OnPrintListener {
                    override fun over() {
                    }

                    override fun start(mPrintStr: String) {

                    }

                    override fun printing(process: Int) {
                        if (zl_list.containsKey(process)) {
                            //触发指令
                            bookContentView.pausePrint()
                            current_zl_process = process
                            zlStr?.value = zl_list.get(process)
                        }
                    }
                })
            }
        }
    }

    private val zl_do = MutableLiveData<String>()
    //执行指令
    fun doZl(zl: String?="") {
        //TODO 触发指令
        zl_do.postValue(zl)
//        bookContentView?.restartPrint()

    }

    fun getChapterTitle(chapterIndex: Int): String {
        jbContentList?.let {
            if (jbContentList!!.size == 0)
                return "无章节"
            else
                if (jbContentList!!.size > chapterIndex)
                    return jbContentList!![chapterIndex].zj_name
        }
        return "无章节"
    }

    fun updateProgress(chapterIndex: Int, pageIndex: Int) {
        zjContent?.value?.let {
            it.zj_index = chapterIndex
            it.zj_self_index = pageIndex
        }
    }

    private var pageLineCount: Int = 1

    fun setPageLineCount(lineCount: Int) {
        this.pageLineCount = lineCount
    }

}


