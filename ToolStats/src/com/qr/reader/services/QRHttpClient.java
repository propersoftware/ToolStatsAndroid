package com.qr.reader.services;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import com.qr.reader.ui.R;

import android.content.Context;

/**
 * @author JRD Systems
 * 
 */

public class QRHttpClient extends DefaultHttpClient {
	final Context context;

	public QRHttpClient(Context context) {
		this.context = context;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", newSslSocketFactory(), 443));
		return new SingleClientConnManager(getParams(), registry);
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(
					R.raw.jrdkeystore);
			try {
				trusted.load(in, "jrd".toCharArray());
			} finally {
				in.close();
			}
			return new SSLSocketFactory(trusted);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
