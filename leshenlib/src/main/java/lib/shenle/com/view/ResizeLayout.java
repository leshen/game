package lib.shenle.com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 自定义布局变化(多指软键盘)监听的RelativeLayout
 * 
 * @author shenle
 * 
 */
public class ResizeLayout extends RelativeLayout {

	public ResizeLayout(Context context) {
		super(context);

	}

	public ResizeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}

	private OnResizeListener mListener;

	public interface OnResizeListener {
		void OnResize(int w, int h, int oldw, int oldh);
	};

	private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;
	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;

	private boolean mHasInit = false;
	private boolean mHasKeyboard = false;
	private int mHeight;

	public void setOnKeyboardStateChangedListener(
			IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
		this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener
						.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;
		}

		if (mHasInit && mHeight > b) {
			mHasKeyboard = true;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener
						.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
			}
		}
		if (mHasInit && mHasKeyboard && mHeight == b) {
			mHasKeyboard = false;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener
						.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
			}
		}
	}

	public interface IOnKeyboardStateChangedListener {
		public void onKeyboardStateChanged(int state);
	}
}
