package com.qr.reader.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.qr.reader.gpsServices.AddressServices;
import com.qr.reader.gpsServices.GPSTracker;
import com.qr.reader.utils.GpsScanInfoModel;

class GetGPS extends AsyncTask<String, String, String>
{
	private Dialog dialog = null;
	private Handler handler = null;
	private Thread thread = null;
	private GpsScanInfoModel gpsScanModel = new GpsScanInfoModel();
	Context context;
	private boolean wasCanceled = false; 
	public boolean getWasCanceld(){ return wasCanceled; }
	String jobNum;
	Activity activity;
	ProgressDialog progressDialog;
	public GetGPS(Context c, String JobNum, Activity a)
	{
		context = c;
		jobNum = JobNum;
		activity = a;
	}
	
	public GpsScanInfoModel getGpsScanInfoModel(){ return gpsScanModel; }
	
	
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("ToolStats GPS");
		progressDialog.setMessage("Obtaining GPS coordinates");
		progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		progressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				wasCanceled = true;
				dialog.dismiss();
			}
		});
		dialog = progressDialog;
		dialog.show();
		
		wasCanceled = false;
		GPSTracker gpsTracker = new GPSTracker(context);
		gpsTracker.getLocation();
		while(!gpsTracker.canGetLocation())
		{}
		
		AddressServices addServices = new AddressServices(context);
		String Address = addServices.getCompleteAddressString(gpsTracker.getLatitude(), gpsTracker.getLongitude());
		gpsScanModel.setLat(gpsTracker.getLatitude());
		gpsScanModel.setLong(gpsTracker.getLongitude());
		
		gpsScanModel.setCity(addServices.getCity());
		gpsScanModel.setCountry(addServices.getCountryCode());
		gpsScanModel.setLat(addServices.getLatitude());
		gpsScanModel.setLong(addServices.getLongitude());
		gpsScanModel.setStateProvince(addServices.getState());
		gpsScanModel.setStreetAddress1(addServices.getAddress1());
		gpsScanModel.setStreetAddress2(addServices.getAddress2());
		gpsScanModel.setZipCode(addServices.getZip());
		gpsScanModel.setProjectNo("41392");
		gpsScanModel.setCompanyName("Electrolux");
		gpsScanModel.setUserName("Mike Taulbee");
		
		gpsScanModel.setProjectNo(jobNum);
		
		dialog.dismiss();
	
		
		if(this.getWasCanceld() == false)
		{
			Intent gpsIntent = new Intent(activity.getApplicationContext(), SendGPSActivity.class);
			
			gpsIntent.putExtra("gpsInfo", this.getGpsScanInfoModel());
			activity.startActivity(gpsIntent);
		}
	}
	@Override
	protected String doInBackground(String... params) {
		
		return "";
	}
	
}