/**
 * Created at: May 12, 2012
 */
package com.mostafaGazar.android.share.view.dialog;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.IFragmentActivity;
import com.mostafaGazar.android.share.interfaces.ITwitterManagerCaller;
import com.mostafaGazar.android.share.model.TwitterManager;
import com.mostafaGazar.android.share.task.TwitterShareTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

/**
 * @author Mostafa Gazar
 */
public class TwitterDialogFragment extends DialogFragment implements ITwitterManagerCaller {

	private final IFragmentActivity mIFragmentActivity;
	
	private static TwitterManager mTwitterManager = null;
	
	private String mMessage;

	public TwitterDialogFragment(final IFragmentActivity fragmentActivity, String message) {
		mIFragmentActivity = fragmentActivity;
		mMessage = message;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		if (mTwitterManager == null) { // Init.
			mTwitterManager = new TwitterManager(this);
	    	mTwitterManager.handleCallback(getActivity().getIntent().getData());
		}
    	
    	Builder builder;
    	
    	if (mTwitterManager.isOAuth()) {
			final EditText resourceNotesEditText = new EditText(getActivity()
					.getApplicationContext());
			if (mMessage != null) {
				resourceNotesEditText.setText(mMessage);
			}
	
			builder = new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.twitter_status)
			.setView(resourceNotesEditText)
			.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int whichButton) {
							new TwitterShareTask(
									TwitterDialogFragment.this,
									TwitterShareTask.Task.UpdateStatus,
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
			
				builder.setNeutralButton(R.string.twitter_logout,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int whichButton) {
						final DialogFragment newFragment = new TwitterLogoutDialogFragment(TwitterDialogFragment.this);
						newFragment.show(mIFragmentActivity.getActivityFragmentManager(),
								TwitterLogoutDialogFragment.class.getName());
					}
				});
		} else {
			builder = new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.twitter_status)
			.setMessage(R.string.twitter_login_message)
			.setPositiveButton(R.string.twitter_login,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int whichButton) {
							new TwitterShareTask(
									TwitterDialogFragment.this,
									TwitterShareTask.Task.Login).execute();
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
	public void showProgressBar(boolean isShow) {
		mIFragmentActivity.showProgressBar(isShow);
	}
	
	@Override
	public void startOAuthActivity(Uri uri) {
		mTwitterManager = null;
		
		mIFragmentActivity.openActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
	@Override
	public boolean isOAuth() {
		return mTwitterManager.isOAuth();
	}
	
	@Override
	public void login() {
		mTwitterManager.oAuth();
	}
	
	@Override
	public void updateStatus(String status) {
		if (mTwitterManager.isOAuth()) {
			mTwitterManager.login();
			
			mTwitterManager.updateStatus(status);
		} else {
			final DialogFragment newFragment = new TwitterLoginDialogFragment(this);
			newFragment.show(mIFragmentActivity.getActivityFragmentManager(),
					TwitterLoginDialogFragment.class.getName());
		}
	}
	
	@Override
	public void logout() {
		mTwitterManager.logout();
	}
}
