package com.qr.reader.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.qr.reader.gpsServices.AddressServices;
import com.qr.reader.gpsServices.GPSTracker;
import com.qr.reader.services.QRDbAdapter;
import com.qr.reader.utils.GpsScanInfoModel;
import com.qr.reader.utils.MoldDetails;

/**
 * @author JRD Systems
 * 
 */


class UserLoginForGps
{
	boolean isRemMe = false;
	EditText txtName;
	EditText txtCustomer;
	CheckBox chkRemMe;
	private MoldDetails moldDetails;
	Context context;
	private String name = "";
	private String company = "";
	private Dialog dialog;
	private Toast alert;
	private Activity tmpActivity;
	public UserLoginForGps(Context c)
	{
		context = c;
	}
		
	public void showUserLoginScreen()
	{
		tmpActivity = (Activity) context;
		moldDetails = (MoldDetails) tmpActivity.getApplication();
		
		alert = Toast.makeText(tmpActivity, "", Toast.LENGTH_LONG);
		alert.setGravity(Gravity.BOTTOM, 0, 0);
		
		LayoutInflater inflater = (LayoutInflater) tmpActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.customer_ui, null, false);
		txtName = (EditText) view.findViewById(R.id.txt_name);
		txtCustomer = (EditText) view.findViewById(R.id.txt_customer);
		chkRemMe = (CheckBox) view.findViewById(R.id.chk_rem_me);
		chkRemMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					isRemMe = true;
				} else {
					isRemMe = false;
				}
			}
		});
		getUserDetails();
		if (hasUserLoggedIn()) {
	
			getUserDetails();
			name = moldDetails.getName();
			company = moldDetails.getCompany();
			txtCustomer.setText(company);
			txtName.setText(name);
			if (moldDetails.getUserFlag().equalsIgnoreCase("Yes")) {
				chkRemMe.setChecked(true);
			} else {
				chkRemMe.setChecked(false);
			}
		}
		
		if (moldDetails.getDialog() == null) {
			dialog = new Dialog(tmpActivity, R.style.CustomDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			moldDetails.setDialog(dialog);
		} else {
			dialog = moldDetails.getDialog();
		}
	
		dialog.setContentView(view);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialog.show();
	
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH)
					return true;
				return false;
			}
		});
		
		Button btnCustomer = (Button) view
				.findViewById(R.id.btn_customer_submit);
		btnCustomer.setOnClickListener(new OnClickListener() {
	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = txtName.getText().toString().trim();
				company = txtCustomer.getText().toString().trim();
	
				if (!name.equalsIgnoreCase("") && !company.equalsIgnoreCase("")) {
	
					moldDetails.setName(name);
					moldDetails.setCompany(company);
					if (isRemMe) {
						moldDetails.setUserFlag("Yes");
						insertUserDetails();
					} else {
						moldDetails.setUserFlag("No");
						insertUserDetails();
					}
					dialog.dismiss();
					
					
					
					GPSTracker gpsTracker = new GPSTracker(v.getContext());
					if(gpsTracker.getLocation() == null)
					{
						gpsTracker.showSettingsAlert();
					}
					if(gpsTracker.getLocation() == null)
					{
						return;
					}
					GpsScanInfoModel gpsScanModel = new GpsScanInfoModel();
					AddressServices addServices = new AddressServices(v.getContext());
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

					Intent gpsIntent = new Intent(v.getContext(), SendGPSActivity.class);
					
					
					
					MoldDetails moldDetails = MoldDetails.getInstance();
					moldDetails.setGpsScanInfoModel(gpsScanModel);
					tmpActivity.startActivity(gpsIntent);
					
//					Intent gpsIntent = new Intent(tmpActivity, ShowLocationByLatLongActivity.class);
//					tmpActivity.startActivity(gpsIntent);
	//				if (!categoryDialog.isShowing())
	//					categoryDialog.show();
				} else {
	
					alert.setText("Please enter the details.");
					alert.show();
				}
			}
		});
	
	}
	
	private void insertUserDetails() {
		QRDbAdapter adapter = new QRDbAdapter(context.getApplicationContext());
		adapter.open();
		if (adapter.userTableCount() > 0) {
			adapter.deleteUserHistory();
		}
		adapter.insertUserInfo(moldDetails.getName(),
				moldDetails.getCompany(), moldDetails.getUserFlag());
		adapter.close();
	}
	private void getUserDetails() {
		QRDbAdapter adapter = new QRDbAdapter(context.getApplicationContext());
		adapter.open();
		if (adapter.userTableCount() > 0) {
			adapter.getUserDetailsMike(moldDetails);
		}
		adapter.close();
	}
	
	private boolean hasUserLoggedIn() {
		boolean isUserLoggedIn = false;
		QRDbAdapter adapter = new QRDbAdapter(context.getApplicationContext());
		adapter.open();
		isUserLoggedIn = adapter.userTableCount() > 0;
		adapter.close();
		return isUserLoggedIn;
	}
}