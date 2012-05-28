/**
 * Created at: May 12, 2011
 */
package com.mostafaGazar.android.share.task;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.IFacebookManagerCaller;
import com.mostafaGazar.android.util.Constants;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Mostafa Gazar
 */
public class FacebookShareTask extends AsyncTask<Void, Void, Integer> {
	public static final String TAG = FacebookShareTask.class.getSimpleName();

	public static enum Task {
		IsOauth, Login, UpdateStatus, Logout
	}
	
	private IFacebookManagerCaller mFacebookManagerCaller;
	
	private Task mTask;
	private String mStatus;
	
	public FacebookShareTask(IFacebookManagerCaller facebookManagerCaller, Task task) {
		this(facebookManagerCaller, task, "");
	}
	
	public FacebookShareTask(IFacebookManagerCaller twitterManagerCaller, Task task, String status) {
		mFacebookManagerCaller = twitterManagerCaller;
		mTask = task;
		mStatus = status;
	}

	@Override
	protected void onPreExecute() {
		mFacebookManagerCaller.showProgressBar(true);
	}

	@Override
	protected Integer doInBackground(final Void... voids) {
		try {
			if (mTask == Task.IsOauth) {
				mFacebookManagerCaller.isOAuth();
			} else if (mTask == Task.Login) {
				mFacebookManagerCaller.login();
			} else if (mTask == Task.UpdateStatus) {
				mFacebookManagerCaller.updateStatus(mStatus);
			} else if (mTask == Task.Logout) {
				mFacebookManagerCaller.logout();
			}
		} catch (final Exception e) {
			Log.e(TAG, "doInBackground, e.getMessage() :: " + e.getMessage());
			return Constants.RESPONSE_UNKNOWN_ERR;
		}

		return Constants.RESPONSE_OK;
	}

	@Override
	protected void onPostExecute(final Integer result) {
		super.onPostExecute(result);
		mFacebookManagerCaller.showProgressBar(false);

		switch (result) {
		case Constants.RESPONSE_OK:

			break;
		case Constants.RESPONSE_UNKNOWN_ERR:
			Toast.makeText(mFacebookManagerCaller.getContext(), R.string.error_occurred, Toast.LENGTH_SHORT);
			break;
		default:
			break;
		}
	}
}
