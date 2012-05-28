/**
 * Created at: Apr 15, 2012
 */
package com.mostafaGazar.android.share.interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

/**
 * @author Mostafa Gazar
 */
public interface IFragmentActivity {
	
	Context getContext();
	
	void showProgressBar(boolean isShow);
	
	FragmentManager getActivityFragmentManager();
	
	void openActivity(Intent intent);
	
	void makeText(final int stringId, final int duration);
    
}
