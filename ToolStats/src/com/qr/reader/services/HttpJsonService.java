package com.qr.reader.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.qr.reader.utils.GpsScanInfoModel;

public class HttpJsonService implements IHttpJson {
	GpsScanInfoModel gpsScanInfoModel;
	
	public HttpJsonService(GpsScanInfoModel gpsScanModel)
	{
		gpsScanInfoModel = gpsScanModel;
	}
	public String PostDataReturnResponse(String url) {
		String returnValue = "";
		
		JSONObject postBackReturn = postJsonObject(url, makingJson());
		
		try {
			returnValue = postBackReturn.get("Data").toString();
			Log.d("Json Test", postBackReturn.get("Data").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnValue = "Uknown Error";
		}
		return returnValue;
	}
	
	public JSONObject makingJson() {

		JSONObject jobj = new JSONObject();

		try {
			jobj.put("userName", gpsScanInfoModel.getUserName());
			jobj.put("companyName", gpsScanInfoModel.getCompanyName());
			jobj.put("projectNo", gpsScanInfoModel.getProjectNo());
			jobj.put("address1", gpsScanInfoModel.getStreetAddress1());
			jobj.put("address2", gpsScanInfoModel.getStreetAddress2());
			jobj.put("city", gpsScanInfoModel.getCity());
			jobj.put("stateProv", gpsScanInfoModel.getStateProvince());
			jobj.put("zipCode", gpsScanInfoModel.getZipCode());
			jobj.put("country", gpsScanInfoModel.getCountry());
			jobj.put("lat", gpsScanInfoModel.getLat().toString());
			jobj.put("longitude", gpsScanInfoModel.getLong().toString());
			jobj.put("isManualInput", gpsScanInfoModel.getIsManualEntry().toString());
			jobj.put("comment", gpsScanInfoModel.getComment());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj;
	}
	
	public JSONObject postJsonObject(String url, JSONObject loginJobj){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            
            //http://localhost:9000/api/products/GetAllProducts
            HttpPost httpPost = new HttpPost(url);
            
            System.out.println(url);
            String json = "";

            // 4. convert JSONObject to JSON to String
            
            json = loginJobj.toString();
            
            System.out.println(json);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            
            
            
            
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Unable to Upload GPS Info!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        JSONObject json = null;
		try {
		
			json = new JSONObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
        // 11. return result

        return json;
    }

private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
