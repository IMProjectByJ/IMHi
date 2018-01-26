package com.example.star.imhi.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star.imhi.R;

/**
 * 自定义等待对话框
 */
public class WaitDialog extends Dialog {
	private Animation animation = null;
	private TextView hintTv;
	private boolean isCanceled;

	private Handler handler;
	private int dismissDelay = 0; // 关闭延迟,单位秒

	public static WaitDialog showProgressDialog(Context context, String message) {
		WaitDialog dialog = new WaitDialog(context, message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}

	public static WaitDialog showProgressDialog(Context context, String message, boolean canceledOnTouchOutside,
			boolean cancelable) {
		WaitDialog dialog = new WaitDialog(context, message);
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		dialog.setCancelable(cancelable);
		dialog.show();
		return dialog;
	}

	public void setHint(String message) {
		hintTv.setText(message);
	}

	public WaitDialog(Context context) {
		super(context, R.style.waiting_dialog_style);
		initView(context, context.getString(R.string.please_wait));
	}

	public WaitDialog(Context context, String message) {
		super(context, R.style.waiting_dialog_style);
		initView(context, message);
	}

	public WaitDialog(Context context, int resId) {
		super(context, R.style.waiting_dialog_style);
		initView(context, context.getString(resId));
	}

	/**
	 * 设置延时关闭
	 * 
	 * @param delay
	 *            - 延时时间，单位秒
	 */
	public void setDismissDelay(int delay) {
		if (delay <= 0) {
			return;
		}
		dismissDelay = delay;
		handler = new Handler() {
		};
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				dismiss();
			}
		}, dismissDelay * 1000);
	}

	private void initView(Context context, String message) {
		View view = LayoutInflater.from(context).inflate(R.layout.dailog_waiting, null);

		if (!TextUtils.isEmpty(message)) {
			hintTv = (TextView) view.findViewById(R.id.progress_message);
			hintTv.setText(message);
		}

		ImageView image = (ImageView) view.findViewById(R.id.progress_view);
		animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		image.startAnimation(animation);

		setContentView(view);
	}

	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
			animation.cancel();
		}
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		isCanceled = true;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

}
