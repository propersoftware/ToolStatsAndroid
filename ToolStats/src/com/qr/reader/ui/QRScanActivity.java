package com.qr.reader.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;
import com.qr.reader.utils.MoldDetails;

/**
 * @author JRD Systems
 * 
 */

public class QRScanActivity extends CaptureActivity {

	protected static final int RESULT_CLOSE_ALL = 0;
	private boolean isHome;
	private MoldDetails moldDetails;

	@Override
	public void handleDecodeInternally(Result rawResult,
			ResultHandler resultHandler, Bitmap barcode) {
		try
		{
			CharSequence displayContents = resultHandler.getDisplayContents();
			String value = displayContents.toString().trim();
	
			if (!value.equalsIgnoreCase("")) {
				String resVal = value.toLowerCase().toString();
				if (resVal.startsWith("Toolmaker ProjectNo".toLowerCase()
						.toString())
						|| (resVal.contains("customerinfo") && resVal
								.contains("projectno"))) {
					Intent intent = new Intent(this, ResultActivity.class);
					moldDetails = (MoldDetails) getApplication();
					moldDetails.setResult(displayContents.toString());
					moldDetails.setScanned(true);
					moldDetails.setMoldHistoryCount(-1);
					startActivity(intent);
					finish();
				} else {
					showAlert();
				}
			} else {
				showAlert();
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
		}
	}

	/**
	 * Show the alert information in a Dialog.
	 */

	private void showAlert() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Information");
		alertDialog
				.setMessage("Unable to retrieve project details from this bar code. Please contact Tool Stats.");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent i = getApplicationContext().getPackageManager()
						.getLaunchIntentForPackage(
								getApplicationContext().getPackageName());

				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

			}
		});
		alertDialog.show();
	}

	@Override
	public void onUserLeaveHint() {

		isHome = false;
		super.onUserLeaveHint();
	}

	@Override
	public void onPause() {

		if (moldDetails == null)
			moldDetails = (MoldDetails) getApplication();

		if (isHome && !moldDetails.isScreenLocked()) {

			moldDetails.setReason(false);

			setResult(RESULT_CLOSE_ALL);
			finish();
		}
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_HOME
					|| keyCode == KeyEvent.KEYCODE_BACK) {

				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					Intent scanIntent = new Intent(QRScanActivity.this,
							QRReaderActivity.class);

					startActivity(scanIntent);
					finish();
				}
			} else {
				event.startTracking();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			openOptionsMenu();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}