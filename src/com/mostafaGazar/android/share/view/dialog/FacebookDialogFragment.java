/**
 * Created at: May 12, 2012
 */
package com.mostafaGazar.android.share.view.dialog;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.IFacebookManagerCaller;
import com.mostafaGazar.android.share.interfaces.IFragmentActivity;
import com.mostafaGazar.android.share.model.FacebookManager;
import com.mostafaGazar.android.share.task.FacebookShareTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

/**
 * @author Mostafa Gazar
 */
public class FacebookDialogFragment extends DialogFragment implements IFacebookManagerCaller {

	private final IFragmentActivity mIFragmentActivity;
	
	private FacebookManager mFacebookManager;
	
	private String mMessage;

	public FacebookDialogFragment(final IFragmentActivity fragmentActivity, String message) {
		mIFragmentActivity = fragmentActivity;
		mMessage = message;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		mFacebookManager = new FacebookManager(this);
    	
		Builder builder = new AlertDialog.Builder(getActivity());
		if (mFacebookManager.isLogin()) {
			final EditText resourceNotesEditText = new EditText(getActivity()
					.getApplicationContext());
			if (mMessage != null) {
				resourceNotesEditText.setText(mMessage);
			}
	
			builder.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.facebook_status)
			.setView(resourceNotesEditText)
			.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int whichButton) {
							new FacebookShareTask(
									FacebookDialogFragment.this,
									FacebookShareTask.Task.UpdateStatus,
									resourceNotesEditText.getText()
											.toString()).execute();
						}
					})
			.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int whichButton) {
							// Do nothing.
						}
					});
			
				builder.setNeutralButton(R.string.facebook_logout,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int whichButton) {
						final DialogFragment newFragment = new FacebookLogoutDialogFragment(FacebookDialogFragment.this);
						newFragment.show(mIFragmentActivity.getActivityFragmentManager(),
								FacebookLogoutDialogFragment.class.getName());
					}
				});
		} else {
			builder.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.facebook_status)
			.setMessage(R.string.facebook_login_message)
			.setPositiveButton(R.string.facebook_login,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int whichButton) {
							mFacebookManager.login();
//							new FacebookShareTask(
//									FacebookDialogFragment.this,
//									FacebookShareTask.Task.Login).execute();
						}
					})
			.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int whichButton) {
							// Do nothing.
						}
					});
		}
		
		return builder.create();
	}
	
	@Override
	public Context getContext() {
		return mIFragmentActivity.getContext();
	}
	
	@Override
	public Activity getParentActivity() {
		return getActivity();
	}

	@Override
	public void showProgressBar(boolean isShow) {
		mIFragmentActivity.showProgressBar(isShow);
	}
	
	@Override
	public boolean isOAuth() {
		return mFacebookManager.isLogin();
	}
	
	@Override
	public void login() {
		mFacebookManager.login();
	}
	
	@Override
	public void updateStatus(String status) {
		if (mFacebookManager.isLogin()) {
			mFacebookManager.post(status);
		} else {
			mFacebookManager.login();
		}
	}
	
	@Override
	public void logout() {
		mFacebookManager.logout();
	}
	
	@Override
	public void makeText(final int stringId, final int duration) {
		mIFragmentActivity.makeText(stringId, duration);
    }
}
