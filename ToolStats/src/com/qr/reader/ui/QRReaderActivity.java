package com.qr.reader.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.qr.reader.gpsServices.GPSTracker;
import com.qr.reader.services.HttpJsonService;
import com.qr.reader.utils.MoldDetails;

public class QRReaderActivity extends Activity {
	private final int TEMP_DIALOG = 0;
	private MoldDetails moldDetails;
	
	private OnClickListener tempButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			GPSTracker gpsTracker = new GPSTracker(v.getContext());
//			if(gpsTracker.getLocation() == null)
//			{
//				gpsTracker.showSettingsAlert();
//			}
//			if(gpsTracker.getLocation() == null)
//			{
//				return;
//			}
//			GpsScanInfoModel gpsScanModel = new GpsScanInfoModel();
//			AddressServices addServices = new AddressServices(v.getContext());
//			String Address = addServices.getCompleteAddressString(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//			gpsScanModel.setLat(gpsTracker.getLatitude());
//			gpsScanModel.setLong(gpsTracker.getLongitude());
//			
//			gpsScanModel.setCity(addServices.getCity());
//			gpsScanModel.setCountry(addServices.getCountryCode());
//			gpsScanModel.setLat(addServices.getLatitude());
//			gpsScanModel.setLong(addServices.getLongitude());
//			gpsScanModel.setStateProvince(addServices.getState());
//			gpsScanModel.setStreetAddress1(addServices.getAddress1());
//			gpsScanModel.setStreetAddress2(addServices.getAddress2());
//			gpsScanModel.setZipCode(addServices.getZip());
//			gpsScanModel.setProjectNo("41392");
//			gpsScanModel.setCompanyName("Electrolux");
//			gpsScanModel.setUserName("Mike Taulbee");
//
			//Intent intent = new Intent(this, ResultActivity.class);
			moldDetails = (MoldDetails) getApplication();
			moldDetails.setResult("http://toolstatsinfo.com/CustomerInfo/Index?ProjectNo=14079&ProductionToolNo=");
			moldDetails.setScanned(true);
			moldDetails.setMoldHistoryCount(-1);
			
			
			
			Intent gpsIntent = new Intent(v.getContext(), ResultActivity.class);
//			
//			
//			
//			MoldDetails moldDetails = MoldDetails.getInstance();
//			moldDetails.setGpsScanInfoModel(gpsScanModel);
			startActivity(gpsIntent);
			finish();
			
		}
	};
	
//	private OnClickListener latLongButtonClick = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//
//			GPSTracker GP = new GPSTracker(v.getContext());
//			if(!GP.canGetLocation())
//			{
//				GP.showSettingsAlert();
//				if(!GP.canGetLocation())
//				{
//					return;
//				}
//			}
//			UserLoginForGps userLogin = new UserLoginForGps(v.getContext());
//			userLogin.showUserLoginScreen();
////			sendInfo();
////			Intent gpsIntent = new Intent(QRReaderActivity.this, ShowLocationByLatLongActivity.class);
////			startActivity(gpsIntent);
//		}
//	};
	
	protected android.app.Dialog onCreateDialog(int id) 
	{
		
		
		AlertDialog.Builder builder = new Builder(this);
		
		switch(id)
		{
		case TEMP_DIALOG:
			return builder.setMessage("Test Dialog").
					setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(QRReaderActivity.this, "Did not reset!", 5).show();
				}
			}).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(QRReaderActivity.this, "Did reset!", 5).show();
				}
			})
			.create();	
		}
		return null;	
	};
	
	@Override
	public void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		// Going to just start the GPS on the phone....
		//GPSTracker GP = new GPSTracker(this.getApplicationContext());
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home_screen);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_label);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		moldDetails = MoldDetails.getInstance();
		Button btnScan = (Button) findViewById(R.id.btn_scan);
		Button btnRecentScan = (Button) findViewById(R.id.btn_recent_scan);
		Button btnHomeWebApp = (Button) findViewById(R.id.btn_home_webapp);
		
//		Button btnTempClick = (Button) findViewById(R.id.tempClick);
//		btnTempClick.setOnClickListener(tempButtonClick);
//		
//		Button btnLatLongClick = (Button) findViewById(R.id.tempLatLong);
//		
//		registerForContextMenu(btnTempClick);
//		btnLatLongClick.setOnClickListener(latLongButtonClick);

		btnScan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent scanIntent = new Intent(QRReaderActivity.this,
						QRScanActivity.class);
				moldDetails.setClassname("QRReaderActivity");
				startActivity(scanIntent);
				finish();
			}
		});

		btnRecentScan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent hisIntent = new Intent(QRReaderActivity.this,
						HistoryActivity.class);

				startActivity(hisIntent);
				finish();
			}
		});

		btnHomeWebApp.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tsIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(moldDetails.getWebURL() + "/CustomerInfo/Index"));
				startActivity(tsIntent);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_HOME
					|| keyCode == KeyEvent.KEYCODE_BACK) {

				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
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
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		//return super.onCreateOptionsMenu(menu);
//		
//		MenuInflater menuInflater = getMenuInflater();
//		
//		menuInflater.inflate(R.menu.main_menu, menu);
//		
//		return true;
//		
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	
//		//return super.onOptionsItemSelected(item);
//		
//		switch(item.getItemId())
//		{
//			case R.id.settingsMenuItem:
//				//Intent intent = new Intent(QRReaderActivity.this, QR)
//			default:
//		
//		}
//		
//		return true;
//	}
//	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		super.onCreateContextMenu(menu, v, menuInfo);
//		MenuInflater menuInflater = getMenuInflater();
//		menuInflater.inflate(R.menu.context_menu, menu);
//	}
}