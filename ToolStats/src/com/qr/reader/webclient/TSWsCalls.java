package com.qr.reader.webclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * @author JRD Systems
 * 
 */
public class TSWsCalls {

	public TSWsCalls(Context context) {
		this.httpClient = new TSHttpClient(context);
	}

	private final TSHttpClient httpClient;

	// web service url
	private static final String BASE_URL = "http://toolstatsinfo.com/";//"http://jrdsys.com/";

	/**
	 * Authenticate the scanned project number.
	 * 
	 * @param webURL
	 *            the web app URL
	 * @param projectNo
	 *            the project number
	 * @return response from server.
	 */
	public String authenticateProjectNo(String webURL, String projectNo) {

		String res = "";
		try {

			HttpGet get = new HttpGet(BASE_URL + webURL + projectNo);
			HttpResponse response = httpClient.execute(get);
			res = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
