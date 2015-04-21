package com.qr.reader.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.qr.reader.services.Tables.ScanHistory;
import com.qr.reader.utils.MoldDetails;

/**
 * @author JRD Systems
 * 
 *         This class provides to handle the database process
 */

public class QRDbAdapter {
	private Context context;
	private SQLiteDatabase db;
	private QRDbHelper dbHelper;

	public QRDbAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Open a database.
	 * 
	 */

	public QRDbAdapter open() throws SQLException {
		dbHelper = new QRDbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close a database.
	 * 
	 */

	public void close() {
		dbHelper.close();
	}

	/**
	 * insert new customer details in the table.
	 * 
	 * @param customerName
	 *            the customer name
	 * @param programName
	 *            the program name
	 * @param partNo
	 *            the part number
	 * @param partName
	 *            the part name
	 * @param projectNo
	 *            the project number
	 * @param name
	 *            the user name
	 * @param company
	 *            the company name
	 * @param reason
	 *            the customer info name
	 * @param url
	 *            the web app url
	 * @param scanDate
	 *            the date of barcode scan
	 * 
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */

	public int insertCustomer(String customerName, String programName,
			String partNo, String partName, String projectNo, String name,
			String company, String reason, String url, String scanDate) {
		ContentValues cvs = new ContentValues();
		cvs.put(ScanHistory.CUSTOMER_NAME, customerName);
		cvs.put(ScanHistory.PROGRAM_NAME, programName);
		cvs.put(ScanHistory.PART_NO, partNo);
		cvs.put(ScanHistory.PART_NAME, partName);
		cvs.put(ScanHistory.PROJECT_NO, projectNo);
		cvs.put(ScanHistory.NAME, name);
		cvs.put(ScanHistory.COMPANY, company);
		cvs.put(ScanHistory.REASON, reason);
		cvs.put(ScanHistory.URL, url);
		cvs.put(ScanHistory.SCAN_DATE, scanDate);

		int id = (int) db.insert(Tables.TABLE_MOLDHISTORY, null, cvs);
		return id;
	}

	/**
	 * Insert user information details in the table.
	 * 
	 * @param userName
	 *            the user name
	 * @param companyName
	 *            the company name
	 * @param flag
	 *            the remember me flag
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */

	public int insertUserInfo(String userName, String companyName, String flag) {
		ContentValues cvs = new ContentValues();
		if (flag.equalsIgnoreCase("No")) {
			userName = "";
			companyName = "";
		}
		cvs.put(ScanHistory.USER_NAME, userName);
		cvs.put(ScanHistory.COMP_NAME, companyName);
		cvs.put(ScanHistory.USER_INFO_FLAG, flag);

		int id = (int) db.insert(Tables.TABLE_USER_INFO, null, cvs);
		return id;
	}

	/**
	 * Retrieve the scan details from table.
	 * 
	 * @param projectNo
	 *            the project number
	 * @param moldDetails
	 *            the mold details class object
	 */

	public void getDetailsByProjectNo(String projectNo, MoldDetails moldDetails) {

		String query = "select * from " + Tables.TABLE_MOLDHISTORY + " where "
				+ ScanHistory.PROJECT_NO + " = " + projectNo;
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {

			moldDetails.setCustomerName(c.getString(c
					.getColumnIndex(ScanHistory.CUSTOMER_NAME)));
			moldDetails.setProgramName(c.getString(c
					.getColumnIndex(ScanHistory.PROGRAM_NAME)));
			moldDetails.setPart(c.getString(c
					.getColumnIndex(ScanHistory.PART_NO)));
			moldDetails.setPartName(c.getString(c
					.getColumnIndex(ScanHistory.PART_NAME)));
			moldDetails.setProjectNo(c.getString(c
					.getColumnIndex(ScanHistory.PROJECT_NO)));
			moldDetails
					.setName(c.getString(c.getColumnIndex(ScanHistory.NAME)));
			moldDetails.setCompany(c.getString(c
					.getColumnIndex(ScanHistory.COMPANY)));
			moldDetails.setReason(c.getString(c
					.getColumnIndex(ScanHistory.REASON)));
			moldDetails.setUrlContent(c.getString(c
					.getColumnIndex(ScanHistory.URL)));
			moldDetails.setScanDate(c.getString(c
					.getColumnIndex(ScanHistory.SCAN_DATE)));
		}

	}

	/**
	 * Retrieve the user information from table.
	 * 
	 * @param moldDetails
	 *            the mold details class object
	 */

	public void getUserDetails(MoldDetails moldDetails) {

		String query = "select * from " + Tables.TABLE_USER_INFO;
		Cursor c = db.rawQuery(query, null);
		moldDetails.setName(c.getString(c.getColumnIndex(ScanHistory.NAME)));
		moldDetails.setCompany(c.getString(c
				.getColumnIndex(ScanHistory.COMPANY)));
		moldDetails.setUserFlag(c.getString(c
				.getColumnIndex(ScanHistory.USER_INFO_FLAG)));
	}

	public void getUserDetailsMike(MoldDetails moldDetails) {

		String query = "select * from " + Tables.TABLE_USER_INFO;
		Cursor c = db.rawQuery(query, null);
		if(c != null)
		{
			c.moveToFirst();
			int cName = c.getColumnIndex(ScanHistory.USER_NAME);
			int company = c.getColumnIndex(ScanHistory.COMP_NAME);
			int userFlag = c.getColumnIndex(ScanHistory.USER_INFO_FLAG);
			moldDetails.setName(c.getString(cName));
			moldDetails.setCompany(c.getString(company));
			moldDetails.setUserFlag(c.getString(userFlag));	
		}
		
	}
	/**
	 * Retrieve the reason scan details from table.
	 * 
	 * @param id
	 *            the record id
	 */

	public MoldDetails getDetailsById(int id) {
		MoldDetails moldDetails = null;
		String query = "select * from " + Tables.TABLE_MOLDHISTORY + " where "
				+ ScanHistory.ID + " = " + id;
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {
			moldDetails = new MoldDetails();
			moldDetails.setCustomerName(c.getString(c
					.getColumnIndex(ScanHistory.CUSTOMER_NAME)));
			moldDetails.setProgramName(c.getString(c
					.getColumnIndex(ScanHistory.PROGRAM_NAME)));
			moldDetails.setPart(c.getString(c
					.getColumnIndex(ScanHistory.PART_NO)));
			moldDetails.setPartName(c.getString(c
					.getColumnIndex(ScanHistory.PART_NAME)));
			moldDetails.setProjectNo(c.getString(c
					.getColumnIndex(ScanHistory.PROJECT_NO)));
			moldDetails
					.setName(c.getString(c.getColumnIndex(ScanHistory.NAME)));
			moldDetails.setCompany(c.getString(c
					.getColumnIndex(ScanHistory.COMPANY)));
			moldDetails.setReason(c.getString(c
					.getColumnIndex(ScanHistory.REASON)));
			moldDetails.setUrlContent(c.getString(c
					.getColumnIndex(ScanHistory.URL)));
			moldDetails.setScanDate(c.getString(c
					.getColumnIndex(ScanHistory.SCAN_DATE)));
		}
		return moldDetails;
	}

	/**
	 * Delete reason scan details from table.
	 */

	public void deleteAllMoldHistory() {
		db.delete(Tables.TABLE_MOLDHISTORY, null, null);
	}

	/**
	 * Delete user information details from table.
	 */

	public void deleteUserHistory() {

		String deleteSQL = "DELETE FROM " + Tables.TABLE_USER_INFO;

		db.execSQL(deleteSQL);
	}

	/**
	 * Delete last scanned details from table.
	 * 
	 * @param projectNo
	 *            the project number
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */

	public boolean deleteLastHistoryByProjectNumber(String projectNo) {
		return db.delete(Tables.TABLE_MOLDHISTORY, ScanHistory.PROJECT_NO
				+ " = " + projectNo, null) > 0;
	}

	/**
	 * Delete id scanned details from table.
	 * 
	 * @param id
	 *            the record id
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */

	public boolean deleteLastHistoryByID(int id) {
		return db.delete(Tables.TABLE_MOLDHISTORY, ScanHistory.ID + " = " + id,
				null) > 0;
	}

	/**
	 * @return maximum row id number from table.
	 */

	public int getMoldHistoryMaxID() {
		String query = "select max(" + ScanHistory.ID + ") from "
				+ Tables.TABLE_MOLDHISTORY;

		Cursor cursor = db.rawQuery(query, null);

		int id = 0;
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		return id;

	}

	/**
	 * @return total row count in the table.
	 */

	public int userTableCount() {
		return (int) DatabaseUtils.queryNumEntries(db, Tables.TABLE_USER_INFO);
	}
}
