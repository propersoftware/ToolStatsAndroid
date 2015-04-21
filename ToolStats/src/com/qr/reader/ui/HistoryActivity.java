package com.qr.reader.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qr.reader.services.QRDbAdapter;
import com.qr.reader.utils.MoldDetails;

/**
 * @author JRD Systems
 * 
 */

public class HistoryActivity extends Activity implements OnClickListener {

	protected static final int RESULT_CLOSE_ALL = 0;

	private boolean isPaused = true;
	private boolean isHome = false;

	private MoldDetails moldDetails;
	private QRDbAdapter adapter;
	private LinearLayout lvHistoryView;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.history_table);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_label);
		moldDetails = (MoldDetails) getApplication();

		try {
			adapter = new QRDbAdapter(this);
			adapter.open();
			
			int count = adapter.getMoldHistoryMaxID();

			TextView tvHistorycaption = (TextView) findViewById(R.id.history_caption);
			TextView tvRecentScan = (TextView) findViewById(R.id.tv_recent_scan);
			lvHistoryView = (LinearLayout) findViewById(R.id.history_view);

			int historyCount = 0;

			if (moldDetails.getMoldHistoryCount() == -1) {

				if (count <= 5) {
					historyCount = 0;
				} else {
					historyCount = count - 5;
					adapter.deleteLastHistoryByID(historyCount);
				}
				moldDetails.setMoldHistoryCount(historyCount);
			} else {
				historyCount = moldDetails.getMoldHistoryCount();
			}

			if (count == 0) {
				tvHistorycaption.setPadding(0, 15, 0, 0);
				tvHistorycaption.setText("No recent scans...");
				tvHistorycaption.setVisibility(View.VISIBLE);
			} else {
				tvRecentScan.setVisibility(View.VISIBLE);
			}
			for (int i = count; i >= 1 + historyCount; i--) {
				MoldDetails details = adapter.getDetailsById(i);
				HistoryView historyView = new HistoryView(this);
				historyView.setProjectNumber(details.getProjectNo());
				historyView.setScanDate(details.getScanDate());
				historyView.setMoldDetails(details);
				historyView.setOnClickListener(this);
				lvHistoryView.addView(historyView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			adapter.close();
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		HistoryView historyView = (HistoryView) v;
		MoldDetails details = (MoldDetails) historyView.getMoldDetails();

		moldDetails.setResult("");
		moldDetails.setProjectNo(details.getProjectNo());
		moldDetails.setScanned(false);
		moldDetails.setClassname("HistoryActivity");
		Intent webIntent = new Intent(HistoryActivity.this,
				ResultActivity.class);
		startActivity(webIntent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_HOME
					|| keyCode == KeyEvent.KEYCODE_BACK) {

				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					Intent hisIntent = new Intent(HistoryActivity.this,
							QRReaderActivity.class);
					startActivity(hisIntent);
					finish();

					return true;
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

	@Override
	public boolean onSearchRequested() {
		return true;
	}

	@Override
	public void onUserLeaveHint() {

		isHome = false;
		super.onUserLeaveHint();
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void onPause() {

		super.onPause();
		if (isHome && !moldDetails.isScreenLocked() && isPaused) {

			moldDetails.setReason(false);
			setResult(RESULT_CLOSE_ALL);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		switch (resultCode) {
		case RESULT_CLOSE_ALL:
			setResult(RESULT_CLOSE_ALL);
			finish();
		}
	}
}