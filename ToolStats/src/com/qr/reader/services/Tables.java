package com.qr.reader.services;

/**
 * @author JRD Systems
 * 
 */

public class Tables {
	public static final String TABLE_MOLDHISTORY = "scan_history";
	public static final String TABLE_USER_INFO = "user_info";

	public static class ScanHistory {
		public static final String ID = "_id";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String PROGRAM_NAME = "programName";
		public static final String PART_NO = "partNumber";
		public static final String PART_NAME = "partName";
		public static final String PROJECT_NO = "projectNumber";
		public static final String NAME = "name";
		public static final String COMPANY = "company";
		public static final String REASON = "reason";
		public static final String URL = "url";
		public static final String SCAN_DATE = "date";
		public static final String USER_NAME = "uname";
		public static final String COMP_NAME = "cname";
		public static final String USER_INFO_FLAG = "uinfo_flag";

		/**
		 * History table create command.
		 * 
		 * @return the String format of history table create command
		 */

		public static String getCreateCommand() {
			return "CREATE TABLE " + TABLE_MOLDHISTORY + " (" + ID
					+ " INTEGER PRIMARY KEY, " + CUSTOMER_NAME + " TEXT,"
					+ PROGRAM_NAME + " TEXT," + PART_NO + " TEXT," + PART_NAME
					+ " TEXT," + PROJECT_NO + " TEXT," + NAME + " TEXT,"
					+ COMPANY + " TEXT," + REASON + " TEXT," + URL + " TEXT,"
					+ SCAN_DATE + " TEXT" + ");";
		}

		/**
		 * User table create command.
		 * 
		 * @return the String format of user table create command
		 */

		public static String getUserCreateCommand() {
			return "CREATE TABLE " + TABLE_USER_INFO + " (" + ID
					+ " INTEGER PRIMARY KEY, " + USER_NAME + " TEXT,"
					+ COMP_NAME + " TEXT," + USER_INFO_FLAG + " TEXT" + ");";
		}
	}
}
