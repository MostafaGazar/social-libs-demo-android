/**
 * Created at: May 12, 2011
 */
package com.mostafaGazar.android.share.task;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.ITwitterManagerCaller;
import com.mostafaGazar.android.util.Constants;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Mostafa Gazar
 */
public class TwitterShareTask extends AsyncTask<Void, Void, Integer> {
	public static final String TAG = TwitterShareTask.class.getSimpleName();

	public static enum Task {
		IsOauth, Login, UpdateStatus, Logout
	}
	
	private ITwitterManagerCaller mTwitterManagerCaller;
	
	private Task mTask;
	private String mStatus;
	
	public TwitterShareTask(ITwitterManagerCaller twitterManagerCaller, Task task) {
		this(twitterManagerCaller, task, "");
	}
	
	public TwitterShareTask(ITwitterManagerCaller twitterManagerCaller, Task task, String status) {
		mTwitterManagerCaller = twitterManagerCaller;
		mTask = task;
		mStatus = status;
	}

	@Override
	protected void onPreExecute() {
		mTwitterManagerCaller.showProgressBar(true);
	}

	@Override
	protected Integer doInBackground(final Void... voids) {
		try {
			if (mTask == Task.IsOauth) {
				mTwitterManagerCaller.isOAuth();
			} else if (mTask == Task.Login) {
				mTwitterManagerCaller.login();
			} else if (mTask == Task.UpdateStatus) {
				mTwitterManagerCaller.updateStatus(mStatus);
			} else if (mTask == Task.Logout) {
				mTwitterManagerCaller.logout();
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
		mTwitterManagerCaller.showProgressBar(false);

		switch (result) {
		case Constants.RESPONSE_OK:

			break;
		case Constants.RESPONSE_UNKNOWN_ERR:
			Toast.makeText(mTwitterManagerCaller.getContext(), R.string.error_occurred, Toast.LENGTH_SHORT);
			break;
		default:
			break;
		}
	}
}
