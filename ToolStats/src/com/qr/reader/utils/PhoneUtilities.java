package com.qr.reader.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PhoneUtilities implements IPhoneUtilities {

	private final Context context;
	public PhoneUtilities(Context c)
	{
		this.context = c;
	}
	public boolean isConnectedToNetwork() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;
	    
	    ConnectivityManager cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
	public boolean isScreenLocked() {
		KeyguardManager kgMgr = (KeyguardManager) this.context.getSystemService(Context.KEYGUARD_SERVICE);
		boolean isLocked = kgMgr.inKeyguardRestrictedInputMode();
		return isLocked;
	}
	
	public boolean isGPSEnabled() {
		LocationManager manager = (LocationManager) this.context.getSystemService( Context.LOCATION_SERVICE );
		return manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
	}
	
}