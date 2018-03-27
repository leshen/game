package game.shenle.com.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import com.example.android.observability.persistence.GameUserDataTable
import com.example.android.observability.persistence.JbContentTable
import game.shenle.com.db.repository.JbContentRepository
import game.shenle.com.db.repository.Resource
import java.util.regex.Pattern
import javax.inject.Inject
import android.arch.lifecycle.MutableLiveData
import game.shenle.com.db.repository.GameUserRepository
import game.shenle.com.db.repository.Status
import game.shenle.com.view.contentswitchview.BookContentView


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
    private var strLive: LiveData<String>? = MediatorLiveData<String>()

    private lateinit var jbId: String

    fun init(jbId: String) {
        this.jbId = jbId
        zjContent = Transformations.map(jbContentRepository.getJbContent(jbId, 1L, 10L), {
            if (it?.status == Status.SUCCESS) {
                if (it?.data != null && it?.data.size != 0) {
                    var str = it?.data!![0].bg
                    var str_copy = str
                    val pz = "\\[\\*(.+?)\\*\\]"
                    val compile = Pattern.compile(pz)
                    val matcher = compile.matcher(str_copy)
                    val jbContentTable = it?.data[0]
//                    (zjContent as MediatorLiveData).postValue(jbContentTable)
                    while (matcher.find()) {
                        val group = matcher.group()
                        when (group) {
                            "[*初始化数据*]" -> {
                                zjContent = Transformations.map(gameUserRepository.getGameUserContent("w9rxDDDi", jbId), {
                                    if (it.status == Status.SUCCESS) {
                                        jbContentTable.bg = str_copy.replace("[*初始化数据*]", it.data.toString()).replace("[*用户名*]", it.data?.gameUserName!!)
                                        jbContentTable
                                    } else {
                                        jbContentTable
                                    }
                                })
                            }
//                            "[*指令_方向*]" -> {jbContentTable.bg = str_copy.replace("[*用户名*]", it.data.)}
                            else -> {
//                                zjContent?.value
                                jbContentTable
                            }

                        }
                    }
                    jbContentTable
//                    zjContent
                } else {
                    zjContent?.value
                }
            } else {
                zjContent?.value
            }
        })
//        strLive = Transformations.switchMap(jbContentRepository.getJbContent(jbId, 1L, 10L), {
//            if (it?.status == Status.SUCCESS) {
//                if (it?.data != null && it?.data.size != 0) {
//                    var str = it?.data!![0].bg
//                    var str_copy = str
//                    val pz = "\\[\\*(.+?)\\*\\]"
//                    val compile = Pattern.compile(pz)
//                    val matcher = compile.matcher(str_copy)
//                    val jbContentTable = it?.data[0]
//                    while (matcher.find()) {
//                        val group = matcher.group()
//                        when (group) {
//                            "[*初始化数据*]" -> {
//                                strLive =  Transformations.map(gameUserRepository.getGameUserContent("w9rxDDDi", jbId), {
//                                    if (it.status == Status.SUCCESS) {
//                                       str_copy.replace("[*初始化数据*]", it.data.toString())
//                                    } else {
//                                        str_copy
//                                    }
//                                })
//                            }
//                            else -> {
//                                strLive
//                            }
//
//                        }
//                    }
//                    strLive
//                } else {
//                    strLive
//                }
//            } else {
//                strLive
//            }
//        })
    }

    fun getZjInput(): LiveData<JbContentTable>? {
        return zjContent
    }

    fun getStrLive(): LiveData<String>? {
        return strLive
    }

    //保存进度
    fun saveProgress() {
        jbContentRepository.upDataTable(zjContent?.value)
    }

    fun loadContent(bookContentView: BookContentView, qtag: Long, chapterIndex: Int, pageIndex: Int) {

    }

}


