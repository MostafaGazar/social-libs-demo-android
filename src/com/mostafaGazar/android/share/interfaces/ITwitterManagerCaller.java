/**
 * Apr 11, 2012
 */
package com.mostafaGazar.android.share.interfaces;

import android.content.Context;
import android.net.Uri;

/**
 * @author Mostafa Gazar
 */
public interface ITwitterManagerCaller {
	
	Context getContext();
	
	void showProgressBar(boolean isShow);
	
	void startOAuthActivity(Uri uri);
	
	boolean isOAuth();
	
	void login();
	
	void updateStatus(String status);
	
	void logout();
	
}
