package com.qr.reader.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.qr.reader.services.HttpJsonService;
import com.qr.reader.utils.GpsScanInfoModel;
import com.qr.reader.utils.MoldDetails;


public class SendGPSActivity extends Activity {
	
	private final String baseURL = "http://toolstatsinfo.com:80/";
	private final String jsonFunction = "Json/PostGpsDataAndroid";
	private final int TEMP_DIALOG = 0;
	private GpsScanInfoModel originalGpsScan;
	protected android.app.Dialog onCreateDialog(int id) 
	{
		
		
		AlertDialog.Builder builder = new Builder(this);
		
		switch(id)
		{
		case TEMP_DIALOG :
			return builder.setMessage("Successfully sent GPS data.").
					setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					onBackPressed();
				}
			})
			.create();	
		}
		return null;	
	};
	
	private OnClickListener sendGpsClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			EditText txtAddy1 = getEditTextById(R.id.txtAddy1);
			EditText txtAddy2 = getEditTextById(R.id.txtAddy2); 
			EditText txtCity = getEditTextById(R.id.txtCity); 
			EditText txtZip = getEditTextById(R.id.txtZip); 
			EditText txtState = getEditTextById(R.id.txtState); 
			Spinner spnCountries = getSpinnerById(R.id.spnCountries);
			EditText txtComments = getEditTextById(R.id.txtComments);
			
			
			GpsScanInfoModel sendGpsScanModel = new GpsScanInfoModel();
			
			sendGpsScanModel.setUserName(originalGpsScan.getUserName());
			sendGpsScanModel.setCompanyName(originalGpsScan.getCompanyName());
			sendGpsScanModel.setLat(originalGpsScan.getLat());
			sendGpsScanModel.setLong(originalGpsScan.getLong());
			sendGpsScanModel.setProjectNo(originalGpsScan.getProjectNo());
			sendGpsScanModel.setStreetAddress1(txtAddy1.getText().toString());
			sendGpsScanModel.setStreetAddress2(txtAddy2.getText().toString());
			sendGpsScanModel.setCity(txtCity.getText().toString());
			sendGpsScanModel.setStateProvince(txtState.getText().toString());
			sendGpsScanModel.setZipCode(txtZip.getText().toString());
			sendGpsScanModel.setComment(txtComments.getText().toString());
			
			sendGpsScanModel.setCountry(spnCountries.getSelectedItem().toString().substring(0,2));
			
			sendGpsScanModel.setIsManualEntry(sendGpsScanModel.equals(originalGpsScan));
			HttpJsonService httpService = new HttpJsonService(sendGpsScanModel);
			
			String postResonse = httpService.PostDataReturnResponse(baseURL + jsonFunction);
			
			if(postResonse.equals("true"))
			{
				onCreateDialog(TEMP_DIALOG).show();
				//Toast.makeText(SendGPSActivity.this, "GPS data sent succesfully!", 40).show();
			}
			else
			{
				Toast.makeText(SendGPSActivity.this, "GPS not sent!\n Error: " + postResonse , 40).show();
			}
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.gps_view);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_label);
		
		Button btnSendGpsClick = (Button) findViewById(R.id.sendGPS);
		btnSendGpsClick.setOnClickListener(sendGpsClick);
		MoldDetails moldDetails = MoldDetails.getInstance();
		
		originalGpsScan = moldDetails.getGpsScanInfoModel();
		
		originalGpsScan.setUserName(moldDetails.getName());
		originalGpsScan.setCompanyName(moldDetails.getCompany());
		originalGpsScan.setProjectNo(moldDetails.getProjectNo());
		
		setGuiTextAndOptions();
		
		if(originalGpsScan.getLong() == 0.0 && originalGpsScan.getLat() == 0.0)
		{
			Toast.makeText(SendGPSActivity.this, "Could not retrieve GPS location!", 40).show();
		}
		else
		{
			Toast.makeText(SendGPSActivity.this, "Retrieved GPS location!", 40).show();
		}
		
	}
	
	private EditText getEditTextById(int id)
	{
		return (EditText)findViewById(id);
	}
	
	private Spinner getSpinnerById(int id)
	{
		return (Spinner)findViewById(id);
	}
	
	private void setEditTextString(EditText editText, String value)
	{	
		editText.setText(value);
	}
	
	private void setCountrySpinner(Spinner spinner, String value)
	{
		if(value.equals("US"))
		{
			spinner.setSelection(0);
		}
		if(value.equals("CA"))
		{
			spinner.setSelection(1);
		}
		if(value.equals("ME"))
		{
			spinner.setSelection(2);
		}
	}
	
	private void setGuiTextAndOptions() {
		
		EditText txtAddy1 = getEditTextById(R.id.txtAddy1);
		EditText txtAddy2 = getEditTextById(R.id.txtAddy2); 
		EditText txtCity = getEditTextById(R.id.txtCity); 
		EditText txtZip = getEditTextById(R.id.txtZip); 
		EditText txtState = getEditTextById(R.id.txtState); 
		Spinner spnCountries = getSpinnerById(R.id.spnCountries);
		
		setEditTextString(txtAddy1, originalGpsScan.getStreetAddress1());
		setEditTextString(txtAddy2, originalGpsScan.getStreetAddress2());
		setEditTextString(txtCity, originalGpsScan.getCity());
		setEditTextString(txtZip, originalGpsScan.getZipCode());
		setEditTextString(txtState, originalGpsScan.getStateProvince());

		
		setCountrySpinner(spnCountries, originalGpsScan.getCountry());
			
		
	}
	
	// Should we save the instance state? We should probably save it....
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}
