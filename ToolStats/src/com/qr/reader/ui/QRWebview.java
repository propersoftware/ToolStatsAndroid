package com.qr.reader.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.qr.reader.utils.MoldDetails;
import com.qr.reader.utils.ToastExpander;
import com.qr.reader.webclient.CustomWebChromeClient;

/**
 * @author JRD Systems
 * 
 */

public class QRWebview extends Activity {

	private int count = 0;
	protected static final int RESULT_CLOSE_ALL = 0;
	private int LONG_TIME = 1000000;
	private String currCaption = "";
	private boolean isPaused = true;
	private boolean isHome = false;
	private boolean isWebApp = false;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private String url;
	private boolean isLoaded = false;
	private boolean isPreviousLoaded = false;
	private boolean isNoNetwork = false;

	private ValueCallback<Uri> mUploadMessage;
	private WebView qrWebview;
	private Toast toast;
	private MoldDetails moldDetails;
	Activity activity = this;
	ToastExpander toastExpander;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.qr_web_view);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				com.google.zxing.client.android.R.layout.title_label);

		moldDetails = (MoldDetails) getApplication();
		toastExpander = new ToastExpander(this.getApplicationContext());
		url = moldDetails.getUrlContent();

		toast = Toast.makeText(this, "Page loading...", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		qrWebview = (WebView) findViewById(R.id.qr_webview);
		qrWebview.getSettings().setJavaScriptEnabled(true);
		qrWebview.requestFocus(View.FOCUS_DOWN);

		qrWebview.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		qrWebview.setLongClickable(false);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		qrWebview.setInitialScale(getScale());

		qrWebview.getSettings().setSavePassword(false);
		qrWebview.getSettings().setDefaultZoom(ZoomDensity.FAR);
		qrWebview.getSettings().setAppCacheEnabled(false);
		qrWebview.getSettings().setLoadWithOverviewMode(true);
		qrWebview.getSettings().setUseWideViewPort(false);
		qrWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		qrWebview.getSettings().setRenderPriority(RenderPriority.HIGH);
		qrWebview.getSettings().setBuiltInZoomControls(true);
		qrWebview.getSettings().setSupportZoom(true);
		qrWebview.getSettings().setLoadsImagesAutomatically(true);
		qrWebview.getSettings().setPluginsEnabled(true);
		qrWebview.getSettings().setAllowFileAccess(true);

		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		CookieManager.getInstance().setAcceptCookie(true);
		CookieManager.getInstance().removeExpiredCookie();

		qrWebview.setWebViewClient(new WebViewClient() {

			public void onPageStarted(WebView view, String url,
					android.graphics.Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				toastExpander.dismiss();
				String projectHome = "CustomerInfo/CustIndex".toLowerCase();

				if (!isNoNetwork) {
					if (!checkNetwork(view)) {

						isNoNetwork = true;
						return;
					}
				}
				isNoNetwork = false;
				String home = moldDetails.getWebURL();
				if (url.toLowerCase().endsWith(projectHome)) {
					view.clearCache(true);
					view.clearHistory();
					isPaused = false;
					isLoaded = true;
					Intent hisIntent = new Intent(QRWebview.this,
							ResultActivity.class);
					startActivity(hisIntent);
					finish();
					return;
				}

				if (url.equalsIgnoreCase(home)) {
					// view.stopLoading();
					view.clearCache(true);
					view.clearHistory();
					isPaused = false;
					isLoaded = true;
					moldDetails.setReason(false);
					Intent hisIntent = new Intent(QRWebview.this,
							QRReaderActivity.class);
					startActivity(hisIntent);
					finish();
					return;
				}

				if (count != 0) {
					isPreviousLoaded = true;
				} else {
					isPreviousLoaded = false;
				}
				currCaption = "Page loading...";
				toast.setText(currCaption);
				// toastExpander = new ToastExpander();
				toastExpander.showFor(QRWebview.this, currCaption, LONG_TIME);

			}

			public void onPageFinished(WebView view, String url) {

				super.onPageFinished(view, url);

				toastExpander.dismiss();
				if (!isNoNetwork) {
					if (!checkNetwork(view)) {
						isNoNetwork = true;
						return;
					}
				}
				isNoNetwork = false;
				if (qrWebview.canGoBack()) {
					count++;
				} else {
					count = 0;
					isPreviousLoaded = false;
				}

				isPreviousLoaded = false;
				isLoaded = false;
				CookieSyncManager.getInstance().sync();
			}

			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				toastExpander.dismiss();
				handler.proceed();
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				toastExpander.dismiss();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
			ProgressDialog progressDialog;
			private Dialog dialog = null;
			public void showADialog(String dialogMessage)
			{
				progressDialog = new ProgressDialog(qrWebview.getContext());
				progressDialog.setTitle("ToolStats GPS");
				progressDialog.setMessage(dialogMessage);
				progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
				progressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//wasCanceled = true;
						dialog.dismiss();
					}
				});
				dialog = progressDialog;
				dialog.show();
				
			}
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				isPreviousLoaded = true;
				toastExpander.dismiss();
				if (!isLoaded) {

					if (url.toLowerCase().startsWith("mailto:")) {
						MailTo mt = MailTo.parse(url);
						Intent i = newEmailIntent(mt.getTo(), mt.getSubject(),
								mt.getBody(), mt.getCc());
						isWebApp = true;
						startActivity(i);
						return true;
					} else if (url.toLowerCase().startsWith("tel:")) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(url));
						isWebApp = true;
						startActivity(intent);
						return true;
					} else if (url.toLowerCase().contains(".xlsx")
							|| url.toLowerCase().contains(".pdf")
							|| url.toLowerCase().contains(".doc")
							|| url.toLowerCase().contains(".xls")) {
						//showADialog("True elseif excel");
						currCaption = "Page loading...";
						toast.setText(currCaption);
						// toastExpander = new ToastExpander();
						toastExpander.showFor(QRWebview.this, currCaption,
								LONG_TIME);
						isLoaded = true;
						view.loadUrl("http://docs.google.com/gview?embedded=true&url="
								+ url);
						return true;
					} else {
						//showADialog(url);
						currCaption = "Page loading...";
						toast.setText(currCaption);
						// toastExpander = new ToastExpander();
						toastExpander.showFor(QRWebview.this, currCaption,
								LONG_TIME);
						isLoaded = false;
						view.loadUrl(url);
						return true;
					}

				}
				//showADialog("False");
				return false;
			}
		});

		qrWebview.setWebChromeClient(new CustomWebChromeClient(
				new WebChromeClient()) {

			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				if (mUploadMessage != null)
					return;
				isWebApp = true;
				toastExpander.dismiss();
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);

				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);

			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				if (mUploadMessage != null)
					return;
				isWebApp = true;
				toastExpander.dismiss();
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);

				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);
			}
		});
		Log.i("TS", "URL : " + url);
		qrWebview.loadUrl(url);
	}

	private int getScale() {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		Double val = Double.valueOf(width) / Double.valueOf(5.345);
		return val.intValue();
	}

	private boolean checkNetwork(final WebView webView) {
		if (!moldDetails.isNetworkAvailable()) {

			this.toastExpander.dismiss();
			currCaption = "Unable to connect to the remote server.\nPlease check internet connection.";
			toast.setText(currCaption);
			// toastExpander = new ToastExpander();
			this.toastExpander.showFor(QRWebview.this, currCaption, LONG_TIME);
			return false;
		}
		return true;
	}

	private static Intent newEmailIntent(String address, String subject,
			String body, String cc) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
		intent.putExtra(Intent.EXTRA_TEXT, body);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_CC, cc);
		intent.setType("message/rfc822");
		return intent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		switch (requestCode) {
		case RESULT_CLOSE_ALL:
			setResult(RESULT_CLOSE_ALL);
			finish();
			break;
		case FILECHOOSER_RESULTCODE:

			if (null == mUploadMessage)
				return;

			isWebApp = false;
			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {

			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_BACK) {

					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:

						//this.toastExpander.dismiss();

						if (qrWebview.canGoBack()) {

							qrWebview.stopLoading();
							qrWebview.goBack();
							return true;

						} else {
							this.toastExpander.dismiss();

							if (!isPreviousLoaded) {
								
								qrWebview.clearCache(true);
								qrWebview.clearHistory();
								isPaused = false;
								isLoaded = true;
								Intent hisIntent = new Intent(QRWebview.this,
										ResultActivity.class);

								startActivity(hisIntent);
								finish();
							} else {

								isPreviousLoaded = false;
								qrWebview.requestFocus();
							}
						}
						return true;
					}
				} else {

					event.startTracking();
					return true;
				}
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

	@Override
	public void onResume() {

		CookieSyncManager.getInstance().stopSync();
		if (qrWebview != null && qrWebview.getProgress() != 100) {
			toast.setText(currCaption);
			// toastExpander = new ToastExpander();
			toastExpander.showFor(QRWebview.this, currCaption, LONG_TIME);
		}
		super.onResume();
	}

	public void onUserLeaveHint() {
		if (!isWebApp) {

			isHome = false;
			super.onUserLeaveHint();
		}
	}

	@Override
	public void onPause() {

		CookieSyncManager.getInstance().stopSync();
		toastExpander.dismiss();
		isWebApp = false;

		if (isHome && !moldDetails.isScreenLocked() && isPaused) {

			moldDetails.setReason(false);
			setResult(RESULT_CLOSE_ALL);
			finish();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		toastExpander.dismiss();
		super.onDestroy();		
	}
}