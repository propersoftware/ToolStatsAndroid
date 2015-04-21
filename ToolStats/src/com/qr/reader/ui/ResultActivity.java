package com.qr.reader.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qr.reader.gpsServices.GPSTracker;
import com.qr.reader.services.QRDbAdapter;
import com.qr.reader.utils.Crypto;
import com.qr.reader.utils.MoldDetails;
import com.qr.reader.webclient.TSWsCalls;

/**
 * @author JRD Systems
 * 
 */

public class ResultActivity extends Activity implements OnClickListener {

	protected static final int RESULT_CLOSE_ALL = 0;

	private boolean isPaused = true;
	private boolean isHome = false;
	private boolean isReasonClosed = false;
	private boolean isRemMe = false;

	private static String WEBAPP_FLAG = "1";
	private static String MOBILE_FLAG = "0";

	private String prjStatus = "";
	private String name = "";
	private String company = "";
	private String reason = "";
	private String scanDate = "";
	private Bundle savedInstanceState;
	private Handler handler = new Handler();
	private MoldDetails moldDetails;
	private Dialog dialog;
	private Dialog categoryDialog;
	private ProgressDialog progressDialog;
	private EditText txtName;
	private EditText txtCustomer;
	private CheckBox chkRemMe;
	private Toast alert;
	private Crypto crypto = new Crypto();

	private OnClickListener gpsButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			GPSTracker GP = new GPSTracker(v.getContext());
			if(!GP.canGetLocation())
			{
				GP.showSettingsAlert();
				if(!GP.canGetLocation())
				{
					return;
				}
			}
			UserLoginForGps userLogin = new UserLoginForGps(v.getContext());
			userLogin.showUserLoginScreen();
		}
	};

	public void onCreate(Bundle icicle) {
		try
		{
			
		
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.qr_result_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_label);

		moldDetails = (MoldDetails) getApplication();

		Button btnCopy = (Button) findViewById(R.id.btn_copy);
		Button btnWebapp = (Button) findViewById(R.id.btn_webapp);

		Button btnGps = (Button) findViewById(R.id.btn_gps);
		btnGps.setOnClickListener(gpsButtonClick);
		
		String result = moldDetails.getResult();
		
		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//		return;
		
		alert = Toast.makeText(this, "", Toast.LENGTH_LONG);
		alert.setGravity(Gravity.BOTTOM, 0, 0);
		if ((result.toLowerCase().contains("customerinfo") && result
				.toLowerCase().contains("projectno"))) {
			String[] resultArr = result.split(Pattern.quote("?"));

			if (resultArr.length > 0) {
				String[] innerArr = resultArr[1].split(Pattern.quote("&"));

				if (innerArr.length > 0) {
					for (int j = 0; j < innerArr.length; j++) {
						String value = innerArr[j];
						String[] innArr = value.split("=");
						if (innArr.length == 2
								&& innArr[0].toLowerCase().trim()
										.equals("ProjectNo".toLowerCase())) {
							if (innArr[1] != null
									&& !innArr[1].equalsIgnoreCase("")) {

								moldDetails.setProjectNo(innArr[1].trim());
							}
						} else if (innArr.length == 2
								&& innArr[0]
										.toLowerCase()
										.trim()
										.equals("ProductionToolNo"
												.toLowerCase())) {

							if (innArr[1] != null
									&& !innArr[1].equalsIgnoreCase("")) {
								moldDetails.sethProToolno(innArr[1].trim());
							}
						}
					}
				}
			}
		} else {

			String[] resultArr = result.split(Pattern.quote("~"));
			if (resultArr.length > 0) {
				for (int i = 0; i < resultArr.length; i++) {
					String value = resultArr[i];

					String[] innerArr = value.split(Pattern.quote("="));
					if (innerArr.length > 0) {
						if (innerArr[0].toLowerCase().trim()
								.equals("Toolmaker ProjectNo".toLowerCase())) {
							if (innerArr[1] != null
									&& !innerArr[1].equalsIgnoreCase("")) {
								moldDetails.setProjectNo(innerArr[1].trim());
							}
						} else if (innerArr[0].toLowerCase().trim()
								.equals("CustomerName".toLowerCase())) {
							if (innerArr[1] != null
									&& !innerArr[1].equalsIgnoreCase("")) {
								moldDetails.setCustomerName(innerArr[1].trim());
							}
						} else if (innerArr[0].toLowerCase().trim()
								.equals("ProgramName".toLowerCase())) {
							if (innerArr[1] != null
									&& !innerArr[1].equalsIgnoreCase("")) {
								moldDetails.setProgramName(innerArr[1].trim());
							}
						} else if (innerArr[0].toLowerCase().trim()
								.equals("Part".toLowerCase())) {
							if (innerArr[1] != null
									&& !innerArr[1].equalsIgnoreCase("")) {
								moldDetails.setPart(innerArr[1].trim());
							}
						} else if (innerArr[0].toLowerCase().trim()
								.equals("PartName".toLowerCase())) {
							if (innerArr[1] != null
									&& !innerArr[1].equalsIgnoreCase("")) {
								moldDetails.setPartName(innerArr[1].trim());
							}
						}
					}
				}
			}
		}

		final Activity a = ResultActivity.this;

		if (moldDetails.isScanned()) {

			moldDetails.setScanDate(scanDate);
			insertDetails();
			moldDetails.setScanned(false);
		}

		LayoutInflater inflater = (LayoutInflater) this
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
		if (moldDetails.isReason()) {

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

		TextView tvProjNo = (TextView) findViewById(R.id.tv_proj_no);
		tvProjNo.setText(moldDetails.getProjectNo());

		if (moldDetails.getDialog() == null) {
			dialog = new Dialog(a, R.style.CustomDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			moldDetails.setDialog(dialog);
		} else {
			dialog = moldDetails.getDialog();
		}

		dialog.setContentView(view);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (moldDetails.isReason()) {
			if (dialog.isShowing())
				dialog.dismiss();

			dialog.show();
		}

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
					if (!categoryDialog.isShowing())
						categoryDialog.show();
				} else {

					alert.setText("Please enter the details.");
					alert.show();
				}
			}
		});

		btnCopy.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub

						ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						clipboard.setText(moldDetails.getUrlContent());
						alert.setText("URL copied to clipboard...");
						alert.show();
					}
				});
			}
		});

		final View v = inflater.inflate(R.layout.category_view, null, false);
		final Button btnGenInfo = (Button) v
				.findViewById(R.id.btn_general_info);
		btnGenInfo.setOnClickListener(this);
		final Button btnToolDesign = (Button) v
				.findViewById(R.id.btn_tool_design_info);
		btnToolDesign.setOnClickListener(this);
		final Button btnToolMaintenance = (Button) v
				.findViewById(R.id.btn_tool_maintenance);
		btnToolMaintenance.setOnClickListener(this);
		final Button btnToolSetup = (Button) v
				.findViewById(R.id.btn_tool_setup);
		btnToolSetup.setOnClickListener(this);

		if (moldDetails.getCategoryDialog() == null) {
			categoryDialog = new Dialog(a, R.style.CustomDialog);
			categoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			moldDetails.setCategoryDialog(categoryDialog);
		} else {
			categoryDialog = moldDetails.getCategoryDialog();
		}

		categoryDialog.setContentView(v);
		categoryDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		categoryDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_BACK) {
					return false;
				}

				return true;
			}
		});

		if (moldDetails.isReason()) {
			if (categoryDialog.isShowing())
				categoryDialog.dismiss();

			categoryDialog.show();
		}

		if (moldDetails.getProgressDialog() == null || progressDialog == null) {

			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("please wait...");
			progressDialog.setCancelable(false);
			moldDetails.setProgressDialog(progressDialog);

		} else {

			progressDialog = moldDetails.getProgressDialog();
		}

		btnWebapp.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub

						if (moldDetails.isNetworkAvailable()) {

							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									a.runOnUiThread(new Runnable() {

										@Override
										public void run() {

											new Loading(false, false, a)
													.execute();
										}
									});
								}
							}).start();

						} else {
							alert.setText("Unable to connect to the remote server.\nPlease check internet connection.");
							alert.show();
						}
					}
				});
			}
		});
		}
		catch(Exception ee)
		{
			Toast.makeText(this, ee.getMessage(), Toast.LENGTH_LONG);
		}
	}

	/**
	 * Insert the scan values into table.
	 */
	private void insertDetails() {
		QRDbAdapter adapter = new QRDbAdapter(getApplicationContext());
		adapter.open();
		adapter.insertCustomer(moldDetails.getCustomerName(),
				moldDetails.getProgramName(), moldDetails.getPart(),
				moldDetails.getPartName(), moldDetails.getProjectNo(), name,
				company, reason, moldDetails.getUrlContent(), getCurrentDate());

		adapter.close();
	}
	
	private void goToNewView()
	{
		
	}
	/**
	 * Insert the user information values into table.
	 */
	private void insertUserDetails() {
		QRDbAdapter adapter = new QRDbAdapter(getApplicationContext());
		adapter.open();
		if (adapter.userTableCount() > 0) {
			adapter.deleteUserHistory();
			adapter.insertUserInfo(moldDetails.getName(),
					moldDetails.getCompany(), moldDetails.getUserFlag());
		}
		adapter.close();
	}

	/**
	 * Retrieve the user values from table.
	 */
	private void getUserDetails() {
		QRDbAdapter adapter = new QRDbAdapter(getApplicationContext());
		adapter.open();
		if (adapter.userTableCount() > 0) {
			adapter.getUserDetailsMike(moldDetails);
		}
		adapter.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_HOME
					|| keyCode == KeyEvent.KEYCODE_BACK) {

				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:

					isPaused = false;
					moldDetails.setResMap(null);
					moldDetails.setReason(false);
					moldDetails.setCategoryDialog(null);
					moldDetails.setCheckIODialog(null);
					moldDetails.setDialog(null);
					moldDetails.setValidResponse(false);
					try {
						Intent resIntent = new Intent(ResultActivity.this,
								Class.forName("com.qr.reader.ui."
										+ moldDetails.getClassname()));
						startActivity(resIntent);
						finish();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return true;
				}
			} else {
				event.startTracking();
				return true;
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

	/**
	 * Encode the given URL.
	 * 
	 * @param url
	 *            the string to be encoded.
	 * @return the String format of encoded url.
	 */
	private String urlEncode(String url) {

		String encoded = "";

		try {
			encoded = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encoded;
	}

	@Override
	public void onClick(View v) {
		if (moldDetails.isNetworkAvailable()) {
			final String data = moldDetails.getProjectNo() + "~" + WEBAPP_FLAG;

			isPaused = false;
			Button b = (Button) v;
			reason = b.getText().toString();
			try {

				String value = "RegisterCustomer?"
						+ "name="
						+ urlEncode(name)
						+ "&company="
						+ urlEncode(company)
						+ "&reason="
						+ urlEncode(reason)
						+ "&qstring="
						+ urlEncode(android.util.Base64.encodeToString(
								crypto.encrypt(data.getBytes()),
								android.util.Base64.DEFAULT));

				moldDetails.setUrlContent(moldDetails.getWebURL() + value);

			} catch (Exception e) {
				e.printStackTrace();
			}

			moldDetails.setReason(true);
			moldDetails.setCategoryDialog(null);
			moldDetails.setCheckIODialog(null);
			moldDetails.setDialog(null);
			moldDetails.setValidResponse(false);
			Intent webIntent = new Intent(ResultActivity.this, QRWebview.class);
			startActivity(webIntent);
			dialog.dismiss();
			categoryDialog.dismiss();
			finish();
		} else {
			alert.setText("Unable to connect to the remote server.\nPlease check internet connection.");
			alert.show();
		}
	}

	/**
	 * Get the current date and time.
	 * 
	 * @return returns String format of date and time
	 */

	private String getCurrentDate() {

		Calendar c = Calendar.getInstance();

		String day = String.valueOf(c.get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String year = String.valueOf(c.get(Calendar.YEAR));

		String hours = String.valueOf(c.get(Calendar.HOUR));
		if (hours.length() == 1) {
			hours = "0" + hours;
		}
		String minutes = String.valueOf(c.get(Calendar.MINUTE));
		if (minutes.length() == 1) {
			minutes = "0" + minutes;
		}
		String seconds = String.valueOf(c.get(Calendar.SECOND));
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		String am_pm;
		if (c.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";

		return month + "/" + day + "/" + year + " " + hours + ":" + minutes
				+ ":" + seconds + " " + am_pm;
	}

	@Override
	public void onUserLeaveHint() {

		isHome = false;
		super.onUserLeaveHint();
	}

	protected void onSaveInstanceState(Bundle saveState) {

		super.onSaveInstanceState(saveState);
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			saveState.putBoolean("isLoading", true);
		} else {
			saveState.putBoolean("isLoading", false);
		}

		if (dialog.isShowing()) {

			dialog.dismiss();
			name = txtName.getText().toString().trim();
			company = txtCustomer.getText().toString().trim();
			if (chkRemMe.isChecked()) {
				moldDetails.setUserFlag("Yes");
			} else {
				moldDetails.setUserFlag("No");
			}

			moldDetails.setName(name);
			moldDetails.setCompany(company);
			saveState.putBoolean("isUser", true);
		} else {

			saveState.putBoolean("isUser", false);
		}

		if (categoryDialog.isShowing() && !isReasonClosed) {

			categoryDialog.dismiss();
			saveState.putBoolean("isCategory", true);
		} else {

			saveState.putBoolean("isCategory", false);
		}
		this.savedInstanceState = saveState;
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		if (progressDialog != null) {
			if (!progressDialog.isShowing()
					&& savedInstanceState.getBoolean("isLoading")) {
				if (!progressDialog.isShowing())
					progressDialog.show();
			} else {
				progressDialog.dismiss();
			}
		}

		if (dialog != null) {
			if (savedInstanceState.getBoolean("isUser")) {

				if (!dialog.isShowing())
					progressDialog.dismiss();
				dialog.show();

				if (txtName != null) {
					name = moldDetails.getName();
					txtName.setText(name);
				}
				if (txtCustomer != null) {
					company = moldDetails.getCompany();
					txtCustomer.setText(company);
				}
				if (chkRemMe != null) {
					String flag = moldDetails.getUserFlag();
					if (flag.equalsIgnoreCase("Yes")) {
						chkRemMe.setChecked(true);
					} else {
						chkRemMe.setChecked(false);
					}
				}
			} else {
				if (dialog.isShowing())
					dialog.dismiss();
				if (txtName != null) {
					name = "";
					txtName.setText("");
				}
				if (txtCustomer != null) {
					company = "";
					txtCustomer.setText("");
				}
			}
		}

		if (categoryDialog != null) {
			if (savedInstanceState.getBoolean("isCategory")) {
				categoryDialog.show();

			} else {
				categoryDialog.dismiss();
			}
		}
	}

	@Override
	public void onResume() {

		super.onResume();

		if (!isHome && savedInstanceState != null) {
			onRestoreInstanceState(savedInstanceState);
		}
		/*
		 * if (savedInstanceState != null) {
		 * 
		 * if (progressDialog != null) { if (!progressDialog.isShowing() &&
		 * savedInstanceState.getBoolean("isLoading")) { if
		 * (!progressDialog.isShowing()) progressDialog.show(); } else {
		 * progressDialog.dismiss(); } }
		 * 
		 * if (!progressDialog.isShowing() && dialog != null) { if
		 * (savedInstanceState.getBoolean("isUser")) {
		 * 
		 * if (!dialog.isShowing()) dialog.show(); if (txtName != null) { name =
		 * moldDetails.getName(); txtName.setText(name); } if (txtCustomer !=
		 * null) { company = moldDetails.getCompany();
		 * txtCustomer.setText(company); } if (chkRemMe != null) { String flag =
		 * moldDetails.getUserFlag(); if (flag.equalsIgnoreCase("Yes")) {
		 * chkRemMe.setChecked(true); } else { chkRemMe.setChecked(false); } } }
		 * else { if (dialog.isShowing()) dialog.dismiss(); if (txtName != null)
		 * { name = ""; txtName.setText(""); } if (txtCustomer != null) {
		 * company = ""; txtCustomer.setText(""); } } }
		 * 
		 * if (!progressDialog.isShowing() && categoryDialog != null) { if
		 * (savedInstanceState.getBoolean("isCategory")) {
		 * categoryDialog.show(); } else { categoryDialog.dismiss(); } }
		 */

		// if (!progressDialog.isShowing() && checkioDialog != null) {
		// if (savedInstanceState.getBoolean("isCheckIO")) {
		// if (!checkioDialog.isShowing())
		// checkioDialog.show();
		// // resMap = (HashMap<String, String>)
		// // moldDetails.getResMap();
		// // if (resMap != null) {
		// // i("JSON",
		// // "save data 1 ::> " + resMap.get("shopNameTemp"));
		// // i("JSON",
		// // "save data 2 ::> " + resMap.get("CheckInTime"));
		// // i("JSON", "save data 3 ::> " +
		// // resMap.get("comments"));
		// // if (txtShopName != null)
		// // txtShopName.setText(resMap.get("shopNameTemp"));
		// // if (txtChkinDate != null)
		// // txtChkinDate.setText(resMap.get("CheckInTime"));
		// // if (txtComments != null)
		// // txtComments.setText(resMap.get("comments"));
		// if (moldDetails.getChkStatus().equalsIgnoreCase("IN")) {
		// tvCheckIO.setText(getResources().getString(
		// R.string.check_out));
		// llChkinView.setVisibility(View.VISIBLE);
		// if (txtChkinDate != null)
		// txtChkinDate.setText(moldDetails.getCoutDate());
		// } else {
		// tvCheckIO.setText(getResources().getString(
		// R.string.check_in));
		// llChkinView.setVisibility(View.GONE);
		// }
		// if (txtShopName != null)
		// txtShopName.setText(moldDetails.getShopName());
		//
		// if (txtComments != null)
		// txtComments.setText(moldDetails.getChkComment());
		// // i("TS", "title : " + btnCheckIO.getText());
		//
		// // }
		// } else {
		// checkioDialog.dismiss();
		// }
		// }
		// savedInstanceState.clear();
		// }
	}

	@Override
	public void onPause() {

		super.onPause();

		if (this.alert != null)
			this.alert.cancel();

		if (isHome && !moldDetails.isScreenLocked() && isPaused) {

			isHome = false;
			dialog = null;
			categoryDialog = null;
			moldDetails.setReason(false);
			setResult(RESULT_CLOSE_ALL);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		switch (resultCode) {
		case RESULT_CLOSE_ALL:
			setResult(RESULT_CLOSE_ALL);
			finish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	class Loading extends AsyncTask<Void, Void, Void> {

		Activity a;

		boolean isCheckIO = true;
		boolean isSubmitChkIO = true;

		public Loading(boolean isCheckIO, boolean isSubmitChkIO, Activity a) {
			this.isSubmitChkIO = isSubmitChkIO;
			this.isCheckIO = isCheckIO;
			this.a = a;
		}

		public void onPreExecute() {

			a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					progressDialog.show();
					if (ResultActivity.this.savedInstanceState != null) {
						ResultActivity.this.savedInstanceState.putBoolean(
								"isLoading", true);
					}
				}
			});
		}

		public void onPostExecute(Void unused) {

			progressDialog.dismiss();
			if (ResultActivity.this.savedInstanceState != null) {
				ResultActivity.this.savedInstanceState.putBoolean("isLoading",
						false);
			}

			if (!prjStatus.equals("")) {

				if (!moldDetails.isValidResponse()) {

					alert.setText("Unable to connect to the remote server.");
					alert.show();

				} else {

					if (!isCheckIO) {

						if (prjStatus.equalsIgnoreCase("A")) {

							getUserDetails();
							if (ResultActivity.this.savedInstanceState != null) {
								ResultActivity.this.savedInstanceState
										.putBoolean("isUser", true);
							}
							if (moldDetails.getUserFlag().equalsIgnoreCase(
									"Yes")) {
								name = moldDetails.getName();
								company = moldDetails.getCompany();
								txtCustomer.setText(company);
								txtName.setText(name);
								chkRemMe.setChecked(true);

							} else {
								name = "";
								company = "";
								txtCustomer.setText("");
								txtName.setText("");
								moldDetails.setCompany("");
								moldDetails.setName("");
								chkRemMe.setChecked(false);
							}

							txtName.requestFocus();

							if (dialog.isShowing())
								dialog.dismiss();

							dialog.show();
						} else if (prjStatus.equalsIgnoreCase("I")) {

							alert.setText("Invalid Project No.");
							alert.show();
						} else if (prjStatus.equalsIgnoreCase("N")) {
							alert.setText("Project is either waiting for Approval or Deactivated");
							alert.show();
						} else {
							alert.setText("Unable to connect to the remote server.");
							alert.show();
						}
					}
				}
			} else {

				alert.setText("Unable to connect to the remote server.");
				alert.show();
			}
		}

		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			TSWsCalls client = new TSWsCalls(getApplicationContext());
			prjStatus = moldDetails.getPjtStatus();

			String param = moldDetails.getProjectNo() + "~" + MOBILE_FLAG;

			final String val = client.authenticateProjectNo(
					"TSwcfService/ToolStatsSvc.svc/getTMProjectStatus?prjNo=",
					urlEncode(moldDetails.getProjectNo()));
			Log.i("TS", "value ::> " + val);
			try {
				JSONObject jObject = new JSONObject(val);
				prjStatus = jObject.get("getTMProjectStatusResult").toString();
				Log.i("TS", "prjStatus ::> " + prjStatus);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				moldDetails.setValidResponse(false);
				e.printStackTrace();
			}
			// try {
			// JSONObject jObject = new JSONObject(val);
			// if (jObject.getClass().equals(param.getClass())) {
			//
			// }
			// Log.i("TS", "jObject getclass ::> " + jObject.getClass());
			// JSONArray res = jObject
			// .getJSONArray("getTMProjectStatusResult");
			//
			// for (int i = 0; i < res.length(); i++) {
			//
			// String e = res.getString(i);
			// String[] arr = e.split(":", 2);
			// if (arr[0].equalsIgnoreCase("ProjectStatus")) {
			//
			// moldDetails.setPjtStatus(arr[1]);
			//
			// prjStatus = arr[1];
			// }
			// if (arr[0].equalsIgnoreCase("ProductionToolNo")) {
			//
			// moldDetails.setProToolNo(arr[1]);
			//
			// }
			// if (arr[0].equalsIgnoreCase("ShopName")) {
			//
			// moldDetails.setShopName(arr[1]);
			//
			// }
			// if (arr[0].equalsIgnoreCase("CheckInTime")) {
			//
			// moldDetails.setCoutDate(arr[1]);
			//
			// }
			// if (arr[0].equalsIgnoreCase("MaintenanceStatus")) {
			//
			// moldDetails.setChkStatus(arr[1]);
			// }
			// }
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// moldDetails.setValidResponse(false);
			// e.printStackTrace();
			// }
			try {

				String encrypted = android.util.Base64.encodeToString(
						crypto.encrypt(param.getBytes()),
						android.util.Base64.DEFAULT);
				String value = "CustomerInfo?qstring=" + urlEncode(encrypted);
				moldDetails.setValidResponse(true);
				moldDetails.setUrlContent(moldDetails.getWebURL() + value);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				moldDetails.setValidResponse(false);
				e.printStackTrace();
			}
			return null;
		}
	}
}