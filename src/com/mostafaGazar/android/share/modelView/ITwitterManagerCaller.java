/**
 * Apr 11, 2012
 */
package com.mostafaGazar.android.share.modelView;

import android.net.Uri;

/**
 * @author Mostafa Gazar
 * 
 */
public interface ITwitterManagerCaller extends IManagerCaller {
	void startOAuthActivity(Uri uri);
}
