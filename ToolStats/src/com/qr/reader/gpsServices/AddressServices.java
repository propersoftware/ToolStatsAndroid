package com.qr.reader.gpsServices;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class AddressServices {
	private final Context context;
	private String countryCode = "";
	private String zip = "";
	private String city = "";
	private String state = "";
	private String address1 = "";
	private String address2 = "";
	private double latitude;
	private double longitude;
	public String getCountryCode(){ return countryCode; }
	public String getZip(){ return zip; }
	public String getCity(){ return city; }
	public String getState(){ return state; }
	public String getAddress1(){ return address1; }
	public String getAddress2(){ return address2; }
	public double getLatitude() { return latitude;}
	public double getLongitude() { return longitude;}
	
	public AddressServices(Context c)
	{
		context = c;
	}
	public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        longitude = LONGITUDE;
        latitude = LATITUDE;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
            	countryCode = addresses.get(0).getCountryCode();
            	 Address address=null;
            	 
            	 
            	 // If the reverse geocode returned an address
            	 if (addresses != null && addresses.size() > 0) {
//	            	 String       add=addresses.get(0).getAddressLine(0)+","
//	            	  +addresses.get(0).getSubAdminArea()+","
//	            	  +addresses.get(0).getSubLocality();
	            	  city=addresses.get(0).getLocality();
	            	  state=addresses.get(0).getAdminArea();
	            	     // Get the first address
	            	  for(int k=0 ;k<addresses.size();k++){
	            		  address = addresses.get(k);
	            	   if(address.getPostalCode()!=null){
	            		  zip=address.getPostalCode();
	            		  }
	            	   }
	            	  }
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                	if(i==0)
                		address1 = returnedAddress.getAddressLine(i);
                	if(i==1 && returnedAddress.getMaxAddressLineIndex() - 1 != 1 )
                		address2 = returnedAddress.getAddressLine(i);
                	if(i == 2) // must be Mexican, therefore we need to retrieve the zip code.....
                	{
                		 String[] splitLines = returnedAddress.getAddressLine(i).toString().split("\\s+");
                		 zip = splitLines[0];
                	}
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Cannot get Address!");
        }
        return strAdd;
    }
}
