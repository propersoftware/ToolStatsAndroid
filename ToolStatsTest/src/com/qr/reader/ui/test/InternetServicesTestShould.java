package com.qr.reader.ui.test;

import java.lang.reflect.Method;

import junit.framework.TestCase;
import android.content.Context;
import android.test.ServiceTestCase;

import com.qr.reader.utils.PhoneUtilities;

public class InternetServicesTestShould extends TestCase {
	private Context getTestContext()
	{
	    try
	    {
	        Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
	        return (Context) getTestContext.invoke(this);
	    }
	    catch (final Exception exception)
	    {
	        exception.printStackTrace();
	        return null;
	    }
	}
	public void testThatWeCanConnectToInternet()
	{
			
		PhoneUtilities Inets = new PhoneUtilities(this.getTestContext());
		assertTrue(Inets.isConnectedToNetwork() == true);
	}
}
