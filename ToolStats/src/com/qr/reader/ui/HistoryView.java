package com.qr.reader.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qr.reader.ui.R;
import com.qr.reader.utils.MoldDetails;

/**
 * @author JRD Systems
 * 
 */

public class HistoryView extends LinearLayout {

	MoldDetails moldDetails;
	TextView tvProjNo;
	TextView tvScanDate;

	public HistoryView(Context context) {
		// TODO Auto-generated constructor stub

		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.qr_history_list_item, this);
		tvProjNo = (TextView) view.findViewById(R.id.tv_project_number);
		tvScanDate = (TextView) view.findViewById(R.id.tv_scan_date);
	}

	/**
	 * Set the project number in history view.
	 * 
	 * @param projectNo
	 *            the project number
	 */

	public void setProjectNumber(String projectNo) {
		tvProjNo.setText(projectNo);
	}

	/**
	 * Set the scanned date in history view.
	 * 
	 * @param scanDate
	 *            the scanned date
	 */

	public void setScanDate(String scanDate) {
		tvScanDate.setText(scanDate);
	}

	/**
	 * @return returns mold details class object.
	 */

	public MoldDetails getMoldDetails() {
		return moldDetails;
	}

	/**
	 * Set the mold details class object.
	 * 
	 */

	public void setMoldDetails(MoldDetails moldDetails) {
		this.moldDetails = moldDetails;
	}
}