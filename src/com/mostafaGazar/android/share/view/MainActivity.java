/**
 * Apr 11, 2012
 */
package com.mostafaGazar.android.share.view;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.IFragmentActivity;
import com.mostafaGazar.android.share.view.dialog.FacebookDialogFragment;
import com.mostafaGazar.android.share.view.dialog.TwitterDialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

/**
 * @author Mostafa Gazar
 */
public class MainActivity extends FragmentActivity implements IFragmentActivity, View.OnClickListener {
	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private View mTwitterImageButton;
	private View mFacebookImageButton;
	private View mIntentShareImageButton;
	
	private View mProgressBarContainer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        widgetsInit();
    }

	private void widgetsInit() {
		mTwitterImageButton = findViewById(R.id.twitterImageButton);
		mFacebookImageButton = findViewById(R.id.facebookImageButton);
		mIntentShareImageButton = findViewById(R.id.intentShareImageButton);
		
		mProgressBarContainer = findViewById(R.id.progressBarContainer);

		setControlsEvents();
	}
	
	private void setControlsEvents() {
		mTwitterImageButton.setOnClickListener(this);
		mFacebookImageButton.setOnClickListener(this);
		mIntentShareImageButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.twitterImageButton) {
			shareViaTwitter();
		} else if (v.getId() == R.id.facebookImageButton) {
			shareViaFacebook();
		} else if (v.getId() == R.id.intentShareImageButton) {
			shareViaIntent();
		}
	}
	
	private void shareViaTwitter() {
		String message = "Twitter Message!";
		
		final DialogFragment newFragment = new TwitterDialogFragment(
				this,  message);
		newFragment.show(getSupportFragmentManager(),
				TwitterDialogFragment.class.getName());
	}
	
	private void shareViaFacebook() {
		String message = "Facebook Post!";
		
		final DialogFragment newFragment = new FacebookDialogFragment(
				this, message);
		newFragment.show(getSupportFragmentManager(),
				FacebookDialogFragment.class.getName());
	}
	
	private void shareViaIntent() {
		String message = "Share Intent!";
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, message);
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_intent_title)));
	}

	@Override
	public Context getContext() {
		return getApplicationContext();
	}

	@Override
	public void showProgressBar(boolean isShow) {
		if (isShow) {
			mProgressBarContainer.setVisibility(View.VISIBLE);			
		} else {
			mProgressBarContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public FragmentManager getActivityFragmentManager() {
		return getSupportFragmentManager();
	}

	@Override
	public void openActivity(Intent intent) {
		startActivity(intent);
	}

	@Override
	public void makeText(final int stringId, final int duration) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), stringId, duration).show();
			}
		});
	}
	
}