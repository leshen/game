package lib.shenle.com.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by ${zyj} on 2016/5/6.
 */
public class RxBus {

    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;
    public static boolean DEBUG = false;

    public static RxBus get() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject;
        subjectList.add(subject = PublishSubject.create());
        if (DEBUG) Log.d(TAG, "[register]subjectMapper: " + subjectMapper);
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            if ( observable != null  && subjects.contains( observable )){
                subjects.remove((Subject) observable);
            }

            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
            }
        }

        if (DEBUG) Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper);
    }

    public void post(@NonNull Object content) {
        post( content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);

        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
        if (DEBUG) Log.d(TAG, "[send]subjectMapper: " + subjectMapper);
    }

    private boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }
}