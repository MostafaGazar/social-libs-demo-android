/**
 * Created at: May 13, 2012
 */
package com.mostafaGazar.android.share.view.dialog;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.IFacebookManagerCaller;
import com.mostafaGazar.android.share.task.FacebookShareTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Mostafa Gazar
 */
public class FacebookLoginDialogFragment extends DialogFragment {

	private IFacebookManagerCaller mFacebookManagerCaller;

	public FacebookLoginDialogFragment(final IFacebookManagerCaller facebookManagerCaller) {
		mFacebookManagerCaller = facebookManagerCaller;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.facebook_status)
				.setMessage(R.string.facebook_login_message)
				.setPositiveButton(R.string.facebook_login,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int whichButton) {
								new FacebookShareTask(
										mFacebookManagerCaller,
										FacebookShareTask.Task.Login).execute();
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
