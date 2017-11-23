package lib.shenle.com.utils;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.view.View;

/**
 * 动画工具类
 * @author shenle
 *
 */
public class MyAnimationUtils {
//	public static ValueAnimator verticalRun(final View view, long time, float... values) {
//		ValueAnimator animator = ValueAnimator.ofFloat(values);
//		animator.setTarget(view);
//		animator.setDuration(time).start();
//		animator.addUpdateListener(new AnimatorUpdateListener() {
//			@Override
//			public void onAnimationUpdate(ValueAnimator animation) {
//				view.setTranslationY((Float) animation.getAnimatedValue());
//			}
//		});
//		return animator;
//	}
	/**
	 * 万能动画
	 * @param v_bg
	 * @param defaultAnimationInterface
	 * @param duration
	 * @param fromV
	 * @param toV
	 */
	public static ValueAnimator defaultAnimation(final View v_bg,
										final DefaultAnimationInterface defaultAnimationInterface, Long duration, final int fromV, final int toV) {
		v_bg.clearAnimation();
		ValueAnimator va = ValueAnimator.ofInt(1, 100);;
		va.addUpdateListener(new AnimatorUpdateListener() {
			// 持有一个IntEvaluator对象，方便下面估值的时候使用
			private IntEvaluator mEvaluator = new IntEvaluator();

			@SuppressLint("NewApi")
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				/// 获得当前动画的进度值，0到toY
				  int currentValue = (int) animation.getAnimatedValue();
				   // 计算当前进度占整个动画过程的比例，浮点型，0-1之间
	                float fraction = currentValue / 100f;
	                Integer evaluate = mEvaluator.evaluate(fraction,
	                		fromV, toV);
	                defaultAnimationInterface.onDoView(evaluate,v_bg);
			}
		});
		va.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		va.setDuration(duration).start();
		return va;
	}

	/**
	 * 万能动画
	 * @param v_bg
	 * @param defaultAnimationInterface
	 * @param duration
	 * @param fromV
	 * @param toV
	 */
	public static ValueAnimator defaultAnimation(final View v_bg,
										final DefaultAnimationInterface defaultAnimationInterface, Long duration, final int fromV, final int toV,Animator.AnimatorListener animatorListener) {
		v_bg.clearAnimation();
		ValueAnimator va = ValueAnimator.ofInt(1, 100);;
		va.addUpdateListener(new AnimatorUpdateListener() {
			// 持有一个IntEvaluator对象，方便下面估值的时候使用
			private IntEvaluator mEvaluator = new IntEvaluator();

			@SuppressLint("NewApi")
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				/// 获得当前动画的进度值，0到toY
				  int currentValue = (int) animation.getAnimatedValue();
				   // 计算当前进度占整个动画过程的比例，浮点型，0-1之间
	                float fraction = currentValue / 100f;
	                Integer evaluate = mEvaluator.evaluate(fraction,
	                		fromV, toV);
	                defaultAnimationInterface.onDoView(evaluate,v_bg);
			}
		});
		va.addListener(animatorListener);
		va.setDuration(duration).start();
		return va;
	}

	interface DefaultAnimationInterface {
		void onDoView(Integer evaluate, View view);//根据比例值操作
	}

}
