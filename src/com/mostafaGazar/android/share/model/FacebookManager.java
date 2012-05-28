package com.mostafaGazar.android.share.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.IFacebookManagerCaller;
import com.mostafaGazar.android.share.model.FacebookSessionEvents.AuthListener;
import com.mostafaGazar.android.share.model.FacebookSessionEvents.LogoutListener;
import com.mostafaGazar.android.share.util.Constants.FacebookConstants;

/**
 * @author Mostafa Gazar
 */
public class FacebookManager {
	private static final String TAG = FacebookManager.class.getSimpleName();
	
	public static Facebook mFacebook;
	
	private IFacebookManagerCaller mFacebookManagerCaller;
	private Context mContext;
	
	private Handler mHandler;
	
	public FacebookManager(IFacebookManagerCaller facebookManagerCaller) {
		mFacebookManagerCaller = facebookManagerCaller;
		mContext = facebookManagerCaller.getContext();
		
		mFacebook = new Facebook(FacebookConstants.APP_ID);
		mFacebook.setAccessToken(FacebookConstants.ACCESS_TOKEN);
       	mHandler = new Handler();

        FacebookSessionStore.restore(mFacebook, mContext);
        FacebookSessionEvents.addAuthListener(new AuthListenerImp());
        FacebookSessionEvents.addLogoutListener(new LogoutListenerImp());
	}
	
	public boolean isLogin() {
		return mFacebook.isSessionValid();
	}
	
	public void login() {
		login(new String[] {});
	}
	public void login(String[] permissions) {
		if (mFacebook.isSessionValid()) {
			// Do nothing.
		} else {
        	mFacebook.authorize(mFacebookManagerCaller.getParentActivity(), permissions,
                          new LoginDialogListener());
        }
	}
	
	public void post(String message) {
		Bundle params = new Bundle();
        params.putString("message", message);

        // Uses the Facebook Graph API
        try {
            mFacebook.request("/me/feed", params, "POST");
        } catch (Exception e) {
        	mFacebookManagerCaller.makeText(R.string.facebook_status_update_failed, Toast.LENGTH_SHORT);
            Log.e(TAG, e.toString());
        }
	}
	
	public void logout() {
		FacebookSessionEvents.onLogoutBegin();
		AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
		asyncRunner.logout(mContext, new LogoutRequestListener());
	}
	
	private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            FacebookSessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            FacebookSessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            FacebookSessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            FacebookSessionEvents.onLoginError("Action Canceled");
        }
    }
	
	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response, final Object state) {
			// callback should be run in the original thread,
			// not the background thread
			mHandler.post(new Runnable() {
				public void run() {
					FacebookSessionEvents.onLogoutFinish();
				}
			});
		}
	}
	
	private class AuthListenerImp implements AuthListener {
        public void onAuthSucceed() {
        	FacebookSessionStore.save(mFacebook, mContext);
        	
        	mFacebookManagerCaller.makeText(R.string.facebook_login_succesful, Toast.LENGTH_SHORT);
        }

        public void onAuthFail(String error) {
        	mFacebookManagerCaller.makeText(R.string.facebook_login_failed, Toast.LENGTH_SHORT);
        }
    }

    private class LogoutListenerImp implements LogoutListener {
        public void onLogoutBegin() {
        	// mFacebookManagerCaller.makeText("Logging out...", Toast.LENGTH_SHORT);
        	mFacebookManagerCaller.showProgressBar(true);
        }

        public void onLogoutFinish() {
        	FacebookSessionStore.clear(mContext);
        	
        	mFacebookManagerCaller.showProgressBar(false);
        	mFacebookManagerCaller.makeText(R.string.facebook_logout_succesful, Toast.LENGTH_SHORT);
        }
    }
    
    /**
     * Skeleton base class for RequestListeners, providing default error 
     * handling. Applications should handle these error conditions.
     *
     */
    public abstract class BaseDialogListener implements DialogListener {
        public void onFacebookError(FacebookError e) {
            e.printStackTrace();
        }

        public void onError(DialogError e) {
            e.printStackTrace();        
        }

        public void onCancel() {        
        }
    }
    
    /**
     * Skeleton base class for RequestListeners, providing default error 
     * handling. Applications should handle these error conditions.
     *
     */
    public abstract class BaseRequestListener implements RequestListener {
        public void onFacebookError(FacebookError e, final Object state) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        public void onFileNotFoundException(FileNotFoundException e,
                                            final Object state) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        public void onIOException(IOException e, final Object state) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        public void onMalformedURLException(MalformedURLException e, final Object state) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
