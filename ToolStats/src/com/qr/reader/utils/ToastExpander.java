package com.qr.reader.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author JRD Systems
 * 
 *         This is the class for shows the alert.
 */
public class ToastExpander {

	private Toast toast;
	CountDownTimer timer;
	Context c;

	public ToastExpander(Context c) {
		// TODO Auto-generated constructor stub
		this.c = c;
	}

	/**
	 * Show the toast for the given time
	 * 
	 * @param aToast
	 *            A toast is a view containing a quick little message for the
	 *            user
	 * @param durationInMilliseconds
	 *            Seconds to display the toast
	 */

	public void showFor(final Context context, final String caption,
			final int time) {
		if (c != null) {
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			if (toast != null) {
				toast.cancel();
				toast = null;
			}
			timer = new CountDownTimer(time, 2000) {
				public void onTick(long millisUntilFinished) {

					toast = new Toast(c.getApplicationContext());
					ToastExpander.this.toast = toast;
					TextView textView = new TextView(c);
					textView.setBackgroundColor(Color.BLACK);
					textView.setTextSize(18);
					textView.setPadding(15, 15, 15, 15);
					toast.setGravity(Gravity.BOTTOM, 0, 10);
					textView.setText(caption);
					toast.setView(textView);
					toast.show();
				}

				public void onFinish() {
					if (timer != null)
						timer.cancel();
					if (toast != null)
						toast.cancel();
				}

			}.start();
		}
	}

	/**
	 * dismiss the toast
	 * 
	 */
	public void dismiss() {

		if (c != null) {
			if (timer != null) {
				
				if (toast != null) {
					toast.cancel();
				}
				timer.cancel();
				timer = null;
			}
		}
	}
}
