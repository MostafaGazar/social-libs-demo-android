/**
 * Created at: May 13, 2012
 */
package com.mostafaGazar.android.share.view.dialog;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.ITwitterManagerCaller;
import com.mostafaGazar.android.share.task.TwitterShareTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Mostafa Gazar
 */
public class TwitterLogoutDialogFragment extends DialogFragment {

	private ITwitterManagerCaller mTwitterManagerCaller;

	public TwitterLogoutDialogFragment(final ITwitterManagerCaller twitterManagerCaller) {
		mTwitterManagerCaller = twitterManagerCaller;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.twitter_status)
				.setMessage(R.string.twitter_logout_message)
				.setPositiveButton(R.string.twitter_logout,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								new TwitterShareTask(
										mTwitterManagerCaller,
										TwitterShareTask.Task.Logout).execute();
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								// Do nothing.
							}
						}).create();
	}
}
