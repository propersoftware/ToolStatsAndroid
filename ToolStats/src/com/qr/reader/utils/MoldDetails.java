package com.qr.reader.utils;

import java.util.Map;

import android.app.Application;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author JRD Systems
 * 
 *         This is the Global class to get and set the values.
 */

public class MoldDetails extends Application {

	private boolean isScanned = true;

	private int moldHistoryCount = -1;

	private String result = "";
	private String urlContent = "";
	private String customerName = "";
	private String programName = "";
	private String part = "";
	private String partName = "";
	private String toolDescription = "";
	private String projectNo = "";
	private String scanDate = "";
	private String reason = "";

	private String shopName = "";
	private String chkComment = "";
	private String chkStatus = "";
	private String coutDate = "";
	private String pjtStatus = "";
	private String proToolNo = "";

	private String classname = "QRReaderActivity";

	private static MoldDetails appInstance;

	private String name = "";
	private String company = "";
	private String hName = "";
	private String hCompany = "";
	private String hProNo = "";
	private String hProToolno = "";
	private String userFlag = "No";
	private String chkResponse = "";
	private boolean isReason = false;
	private boolean isValidResponse = false;

	private Map<String, String> resMap;

	private ProgressDialog progressDialog;
	private Dialog dialog;
	private Dialog categoryDialog;
	private Dialog checkIODialog;

	private GpsScanInfoModel gpsScanInfoModel;
	
	
	public GpsScanInfoModel getGpsScanInfoModel() { return gpsScanInfoModel; }
	
	public void setGpsScanInfoModel(GpsScanInfoModel _gpsScanInfoModel) { gpsScanInfoModel = _gpsScanInfoModel; }
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (appInstance == null) {
			appInstance = this;
		}
	}

	/**
	 * @return instance of MoldDetails class.
	 */

	public static MoldDetails getInstance() {
		return appInstance;
	}

	/**
	 * Check the Network and WIFI status.
	 * 
	 * @return true if network connectivity exists, false otherwise.
	 */

	public boolean isNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager) getInstance()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		return (wifi.isConnected() || mobile.isConnected());
	}

	/**
	 * Check keyguard screen is locked or unlocked.
	 * 
	 * @return true if in keyguard restricted input mode.
	 */

	public boolean isScreenLocked() {
		KeyguardManager kgMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		boolean isLocked = kgMgr.inKeyguardRestrictedInputMode();
		return isLocked;
	}

	/*
	 * @return true if scanned barcode.
	 */
	public boolean isScanned() {
		return isScanned;
	}

	/*
	 * @param isScanned set true If scanned barcode.
	 */
	public void setScanned(boolean isScanned) {
		this.isScanned = isScanned;
	}

	/*
	 * @return the recent history count in DB
	 */
	public int getMoldHistoryCount() {
		return moldHistoryCount;
	}

	/*
	 * @param moldHistoryCount set recent history count in DB
	 */
	public void setMoldHistoryCount(int moldHistoryCount) {
		this.moldHistoryCount = moldHistoryCount;
	}

	/*
	 * @return the customer name
	 */
	public String getCustomerName() {
		return customerName;
	}

	/*
	 * @param customerName the customer name to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/*
	 * @return the program name
	 */
	public String getProgramName() {
		return programName;
	}

	/*
	 * @param programName the program name to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	/*
	 * @return the part number
	 */
	public String getPart() {
		return part;
	}

	/*
	 * @param part the part number to set
	 */
	public void setPart(String part) {
		this.part = part;
	}

	/*
	 * @return the part name
	 */
	public String getPartName() {
		return partName;
	}

	/*
	 * @param partName the part name to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/*
	 * @return the tool description
	 */
	public String getToolDescription() {
		return toolDescription;
	}

	/*
	 * @param toolDescription the tool description to set
	 */
	public void setToolDescription(String toolDescription) {
		this.toolDescription = toolDescription;
	}

	/*
	 * @return the scanned date
	 */
	public String getScanDate() {
		return scanDate;
	}

	/*
	 * @param scanDate the scanned date to set
	 */
	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}

	/*
	 * @return the project number
	 */
	public String getProjectNo() {
		return projectNo;
	}

	/*
	 * @param projectNo the project number to set
	 */
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	/*
	 * @return the result string
	 */
	public String getResult() {
		return result;
	}

	/*
	 * @param result the result string to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/*
	 * @return the current web URL
	 */
	public String getUrlContent() {
		return urlContent;
	}

	/*
	 * @param urlContent the current web URL to set
	 */
	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}

	/*
	 * @return the current class name
	 */
	public String getClassname() {
		return classname;
	}

	/*
	 * @param classname the current class name to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/*
	 * @return the customer
	 */
	public String getName() {
		return name;
	}

	/*
	 * @param name the customer name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * @return the company name
	 */
	public String getCompany() {
		return company;
	}

	/*
	 * @param company the company name to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/*
	 * @return the user flag
	 */
	public String getUserFlag() {
		return userFlag;
	}

	/*
	 * @param userFlag the user flag to set
	 */
	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}

	/*
	 * @return the reason string
	 */
	public String getReason() {
		return reason;
	}

	/*
	 * @param reason the reason string to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/*
	 * @return true if reason scan
	 */
	public boolean isReason() {
		return isReason;
	}

	/*
	 * @param isReason set true if it is reason scan
	 */
	public void setReason(boolean isReason) {
		this.isReason = isReason;
	}

	/*
	 * @return the progress dialog object
	 */
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	/*
	 * @param progressDialog the progress dialog object to set
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	/*
	 * @return the customer dialog object
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/*
	 * @param dialog the customer dialog object to set
	 */
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	/*
	 * @return the the category dialog object
	 */
	public Dialog getCategoryDialog() {
		return categoryDialog;
	}

	/*
	 * @param categoryDialog the category dialog object to set
	 */
	public void setCategoryDialog(Dialog categoryDialog) {
		this.categoryDialog = categoryDialog;
	}

	/*
	 * @return the check i/o dialog object
	 */
	public Dialog getCheckIODialog() {
		return checkIODialog;
	}

	/*
	 * @param checkIODialog the check i/o dialog object to set
	 */
	public void setCheckIODialog(Dialog checkIODialog) {
		this.checkIODialog = checkIODialog;
	}

	/*
	 * @return the customer name
	 */
	public String gethName() {
		return hName;
	}

	/*
	 * @param hName the customer name from URL barcode to set
	 */
	public void sethName(String hName) {
		this.hName = hName;
	}

	/*
	 * @return the company name
	 */
	public String gethCompany() {
		return hCompany;
	}

	/*
	 * @param hCompany the company name from URL barcode to set
	 */
	public void sethCompany(String hCompany) {
		this.hCompany = hCompany;
	}

	/*
	 * @return the project number
	 */
	public String gethProNo() {
		return hProNo;
	}

	/*
	 * @param hProNo the project number from URL barcode to set
	 */
	public void sethProNo(String hProNo) {
		this.hProNo = hProNo;
	}

	/*
	 * @return the production tool number
	 */
	public String gethProToolno() {
		return hProToolno;
	}

	/*
	 * @param hProToolno the production tool number from URL barcode to set
	 */
	public void sethProToolno(String hProToolno) {
		this.hProToolno = hProToolno;
	}

	/*
	 * @return the check i/o response values Map
	 */
	public Map<String, String> getResMap() {
		return resMap;
	}

	/*
	 * @param resMap the check i/o response values to set as Map.
	 */
	public void setResMap(Map<String, String> resMap) {
		this.resMap = resMap;
	}

	/*
	 * @return the web URL
	 */
	public String getWebURL() {

		 String webURL =  "http://toolstatsinfo.com/"; //"http://172.31.3.95:51089/";
		//String webURL = "http://jrdsys.com/toolstats3/";
		return webURL;
	}

	/*
	 * @return the check i/o shop name
	 */
	public String getShopName() {
		return shopName.trim();
	}

	/*
	 * @param shopName the check i/o shop name to set.
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/*
	 * @return the check i/o comment
	 */
	public String getChkComment() {
		return chkComment.trim();
	}

	/*
	 * @param chkComment the check i/o comment to set.
	 */
	public void setChkComment(String chkComment) {
		this.chkComment = chkComment;
	}

	/*
	 * @return the check i/o status
	 */
	public String getChkStatus() {
		return chkStatus;
	}

	/*
	 * @param chkStatus the check i/o status to set
	 */
	public void setChkStatus(String chkStatus) {
		this.chkStatus = chkStatus;
	}

	/*
	 * @return the check out date
	 */
	public String getCoutDate() {
		return coutDate;
	}

	/*
	 * @param coutDate the check out date to set
	 */
	public void setCoutDate(String coutDate) {
		this.coutDate = coutDate;
	}

	/*
	 * @return the project status
	 */
	public String getPjtStatus() {
		return pjtStatus;
	}

	/*
	 * @param pjtStatus the project status to set
	 */
	public void setPjtStatus(String pjtStatus) {
		this.pjtStatus = pjtStatus;
	}

	/*
	 * @return the production tool number
	 */
	public String getProToolNo() {
		return proToolNo;
	}

	/*
	 * @param proToolNo the production tool number to set
	 */
	public void setProToolNo(String proToolNo) {
		this.proToolNo = proToolNo;
	}

	/*
	 * @return true if its valid response
	 */
	public boolean isValidResponse() {
		return isValidResponse;
	}

	/*
	 * @param isValidResponse set true If its valid response.
	 */
	public void setValidResponse(boolean isValidResponse) {
		this.isValidResponse = isValidResponse;
	}

	/*
	 * @return the check i/o response
	 */
	public String getChkResponse() {
		return chkResponse;
	}

	/*
	 * @param chkResponse the check i/o response to set.
	 */
	public void setChkResponse(String chkResponse) {
		this.chkResponse = chkResponse;
	}
}