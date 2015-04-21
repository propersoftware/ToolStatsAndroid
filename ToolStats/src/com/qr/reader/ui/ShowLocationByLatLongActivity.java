package com.qr.reader.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.qr.reader.gpsServices.AddressServices;
import com.qr.reader.gpsServices.GPSTracker;
import com.qr.reader.services.HttpJsonService;
import com.qr.reader.utils.GpsScanInfoModel;

public class ShowLocationByLatLongActivity  extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showlocationbylatlong);
		
		super.onCreate(savedInstanceState);
		
		TextView myLatitude = (TextView)findViewById(R.id.mylatitude);
		TextView myLongitude = (TextView)findViewById(R.id.mylongitude);
		TextView myAddress = (TextView)findViewById(R.id.myaddress);
		
 	}
}
