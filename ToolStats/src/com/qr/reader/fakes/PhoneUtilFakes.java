package com.qr.reader.fakes;

import com.qr.reader.utils.IPhoneUtilities;

public class PhoneUtilFakes implements IPhoneUtilities {

	@Override
	public boolean isConnectedToNetwork() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isScreenLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGPSEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
