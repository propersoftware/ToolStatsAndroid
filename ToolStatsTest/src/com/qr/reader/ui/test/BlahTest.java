package com.qr.reader.ui.test;

import junit.framework.TestCase;

import com.qr.reader.services.HttpJsonService;
import com.qr.reader.utils.GpsScanInfoModel;

public class BlahTest extends TestCase {
	
	GpsScanInfoModel OrigGpsModel;
	GpsScanInfoModel NewGpsModel;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		OrigGpsModel = new GpsScanInfoModel();
		NewGpsModel = new GpsScanInfoModel();
	}
	
	private void setCityToNothing() {
		NewGpsModel.setCity("");
	}
	
	private void setCityToTest() {
		NewGpsModel.setCity("TEST");
	}
	
	public void testCompareGpsModelFalse()
	{
		setCityToTest();
		assertFalse(OrigGpsModel.equals(NewGpsModel));
	}
	
	
	public void testCompareGpsModelTrue()
	{
		setCityToNothing();
		assertTrue(OrigGpsModel.equals(NewGpsModel));
	}
	
	public void testDoesIsManualGetSetToTrue()
	{
		setCityToNothing();
		NewGpsModel.setIsManualEntry(OrigGpsModel.equals(NewGpsModel));
		assertTrue(NewGpsModel.getIsManualEntry());
	}
	
	
}
