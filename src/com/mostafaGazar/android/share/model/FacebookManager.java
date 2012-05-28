/**
 * Apr 11, 2012
 */
package com.mostafaGazar.android.share.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.listener.BaseDialogListener;
import com.mostafaGazar.android.share.listener.BaseRequestListener;
import com.mostafaGazar.android.share.model.SessionEvents.AuthListener;
import com.mostafaGazar.android.share.model.SessionEvents.LogoutListener;
import com.mostafaGazar.android.share.modelView.IFacebookManagerCaller;
import com.mostafaGazar.android.share.util.Constants;

/**
 * @author Mostafa Gazar
 */
public class FacebookManager {
	private static final String TAG = FacebookManager.class.getSimpleName();
	
	public static Facebook mFacebook;
    private static AsyncFacebookRunner mAsyncRunner;
//    private Handler mHandler;
	
	private IFacebookManagerCaller mFacebookManagerCaller;
	private Context mContext;
	
	public FacebookManager(IFacebookManagerCaller facebookManagerCaller) {
		mFacebookManagerCaller = facebookManagerCaller;
		mContext = facebookManagerCaller.getContext();
		
		mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
//       	mHandler = new Handler();

        SessionStore.restore(mFacebook, mContext);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
	}
	
	public boolean isLogin() {
		return mFacebook.isSessionValid();
	}
	
	public void login() {
		login(new String[] {});
	}
	public void login(String[] permissions) {
		if (mFacebook.isSessionValid()) {
//            SessionEvents.onLogoutBegin();
//            AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
//            asyncRunner.logout(mContext, new LogoutRequestListener());
        } else {
        	mFacebook.authorize((Activity) mFacebookManagerCaller, permissions,
                          new LoginDialogListener());
        }
	}
	
	public void post() {
		mFacebookManagerCaller.dialog(new SampleDialogListener());
	}
	
	public void logout() {
		try {
			mFacebook.logout(mContext);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
			
	private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
    
//    private class LogoutRequestListener extends BaseRequestListener {
//        public void onComplete(String response, final Object state) {
//            // callback should be run in the original thread, 
//            // not the background thread
//            mHandler.post(new Runnable() {
//                public void run() {
//                    SessionEvents.onLogoutFinish();
//                }
//            });
//        }
//    }
	
	private class SampleAuthListener implements AuthListener {
        public void onAuthSucceed() {
        	Toast.makeText(mContext, "You have logged in!", Toast.LENGTH_LONG).show();
        	SessionStore.save(mFacebook, mContext);
        }

        public void onAuthFail(String error) {
        	Toast.makeText(mContext, "Login Failed: " + error, Toast.LENGTH_LONG).show();
        }
    }

    private class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
        	Toast.makeText(mContext, "Logging out...", Toast.LENGTH_LONG).show();
        }

        public void onLogoutFinish() {
        	Toast.makeText(mContext, "You have logged out!", Toast.LENGTH_LONG).show();
        	SessionStore.clear(mContext);
        }
    }
    
    private class WallPostRequestListener extends BaseRequestListener {
        public void onComplete(final String response, final Object state) {
            Toast.makeText(mContext, R.string.facebook_wall_updated, Toast.LENGTH_LONG).show();
        }
    }
    
    public class SampleDialogListener extends BaseDialogListener {
        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
                mAsyncRunner.request(postId, new WallPostRequestListener());
            } else {
                Log.d("Facebook-Example", "No wall post made");
            }
        }
    }
}
