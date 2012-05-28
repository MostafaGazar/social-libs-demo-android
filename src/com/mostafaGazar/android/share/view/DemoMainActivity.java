/**
 * Apr 11, 2012
 */
package com.mostafaGazar.android.share.view;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.model.FacebookManager;
import com.mostafaGazar.android.share.model.TwitterManager;
import com.mostafaGazar.android.share.model.FacebookManager.SampleDialogListener;
import com.mostafaGazar.android.share.modelView.IFacebookManagerCaller;
import com.mostafaGazar.android.share.modelView.ITwitterManagerCaller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * @author Mostafa Gazar
 * 
 */
public class DemoMainActivity extends Activity implements ITwitterManagerCaller, IFacebookManagerCaller {
	private static final String TAG = DemoMainActivity.class.getSimpleName();
	
	private TwitterManager mTwitterManager;
	private FacebookManager mFacebookManager;
	
	private View twitterImageButton;
	private View facebookImageButton;
	private View intentShareButton;
	private EditText message;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
        widgetsInit();
    }

    private void init() {
    	mTwitterManager = new TwitterManager(this);
    	mTwitterManager.handleCallback(getIntent().getData());
    	
    	mFacebookManager = new FacebookManager(this);
    }
    
	private void widgetsInit() {
		twitterImageButton = findViewById(R.id.twitterImageButton);
		twitterImageButton.setOnClickListener(twitterImageButtonOnClickListener);
		
		facebookImageButton = findViewById(R.id.facebookImageButton);
		facebookImageButton.setOnClickListener(facebookImageButtonOnClickListener);
		
		intentShareButton = findViewById(R.id.intentShareButton);
		intentShareButton.setOnClickListener(intentShareButtonOnClickListener);
		
		message = (EditText) findViewById(R.id.message);
	}
	
	private OnClickListener twitterImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mTwitterManager.isOAuth()) {
				// mTwitterManager.logout();
				
				mTwitterManager.login();
				
				AlertDialog.Builder alert = new AlertDialog.Builder(DemoMainActivity.this);
				alert.setTitle("Status");
				final EditText input = new EditText(DemoMainActivity.this);
				input.setHint("Status");
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mTwitterManager.updateStutas(input.getText()
										.toString());
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});
				alert.show();
			} else {
				mTwitterManager.oAuth();
			}
		}
	};
	
	private OnClickListener facebookImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mFacebookManager.isLogin()) {
				mFacebookManager.post();
			} else {
				mFacebookManager.login();
			}
		}
	};
	
	private OnClickListener intentShareButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			AlertDialog.Builder alert = new AlertDialog.Builder(DemoMainActivity.this);
			alert.setTitle("Status");
			final EditText input = new EditText(DemoMainActivity.this);
			input.setHint("Status");
			alert.setView(input);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, input.getText()
									.toString());
							startActivity(Intent.createChooser(intent, "Share with"));
						}
					});
			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert.show();
		}
	};

	@Override
	public Context getContext() {
		return this.getApplicationContext();
	}
	
	@Override
	public void startOAuthActivity(Uri uri) {
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

	@Override
	public void dialog(SampleDialogListener sampleDialogListener) {
		FacebookManager.mFacebook.dialog(this, "feed",
				sampleDialogListener);
	}
	
}