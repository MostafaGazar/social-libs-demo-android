package com.mostafaGazar.android.share.interfaces;

import android.app.Activity;
import android.content.Context;

/**
 * @author Mostafa Gazar
 *
 */
public interface IFacebookManagerCaller {
	
	Context getContext();
	
	Activity getParentActivity();
	
	void showProgressBar(boolean isShow);
	
	boolean isOAuth();
	
	void login();
	
	void updateStatus(String status);
	
	void logout();
	
	void makeText(final int stringId, final int duration);
    
}
