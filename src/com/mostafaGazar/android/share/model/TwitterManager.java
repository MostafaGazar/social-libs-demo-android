/**
 * Apr 11, 2012
 */
package com.mostafaGazar.android.share.model;

import com.mostafaGazar.android.share.R;
import com.mostafaGazar.android.share.interfaces.ITwitterManagerCaller;
import com.mostafaGazar.android.share.util.Constants;
import com.mostafaGazar.android.util.PreferenceHelpers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Mostafa Gazar
 */
public class TwitterManager {
	private static final String TAG = TwitterManager.class.getSimpleName();

	private static Twitter mTwitter;
	private static RequestToken mRequestToken;
	
	private ITwitterManagerCaller mTwitterManagerCaller;
	private Context mContext;
	
	public TwitterManager(ITwitterManagerCaller twitterManagerCaller) {
		mTwitterManagerCaller = twitterManagerCaller;
		mContext = mTwitterManagerCaller.getContext();
	}
	
	public boolean isOAuth() {
		return PreferenceHelpers.getPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_TOKEN) != "";
	}
	
	public void oAuth() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(Constants.TwitterConstants.TWITTER_CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(Constants.TwitterConstants.TWITTER_CONSUMER_SECRET);
		Configuration configuration = configurationBuilder.build();
		mTwitter = new TwitterFactory(configuration).getInstance();
		
		try {
			mRequestToken = mTwitter.getOAuthRequestToken(Constants.TwitterConstants.TWITTER_CALLBACK_URL);
			mTwitterManagerCaller.startOAuthActivity(Uri.parse(mRequestToken.getAuthenticationURL()));
		} catch (TwitterException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void login() {
		String oAuthAccessToken = (String) PreferenceHelpers.getPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_TOKEN);
		String oAuthAccessTokenSecret = (String) PreferenceHelpers.getPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_SECRET);
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(Constants.TwitterConstants.TWITTER_CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(Constants.TwitterConstants.TWITTER_CONSUMER_SECRET);
		configurationBuilder.setOAuthAccessToken(oAuthAccessToken);
		configurationBuilder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		Configuration configuration = configurationBuilder.build();
		mTwitter = new TwitterFactory(configuration).getInstance();
	}
	
	public void handleCallback(Uri uri) {
		if (uri != null && uri.toString().startsWith(Constants.TwitterConstants.TWITTER_CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(Constants.TwitterConstants.IEXTRA_TWITTER_OAUTH_VERIFIER);
            try { 
                AccessToken accessToken = mTwitter.getOAuthAccessToken(mRequestToken, verifier); 
                
                // Set Twitter key token and secret token.
        		PreferenceHelpers.setPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_TOKEN, accessToken.getToken());
        		PreferenceHelpers.setPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_SECRET, accessToken.getTokenSecret());
        		Toast.makeText(mContext, R.string.twitter_login_succesful, Toast.LENGTH_LONG).show();
	        } catch (Exception e) { 
                Log.e(TAG, e.getMessage()); 
                Toast.makeText(mContext, R.string.twitter_login_failed, Toast.LENGTH_LONG).show();
			}
        }
	}
	
	public void updateStatus(String status) {
		if(status != null) {
			try {
				mTwitter.updateStatus(status);
				Toast.makeText(mContext, R.string.twitter_status_updated, Toast.LENGTH_LONG).show();
			} catch (TwitterException e) {
				Log.e(TAG, e.getMessage()); 
			}
		}
	}
	
	public void logout() {
		// Reset Twitter key token and secret token.
		PreferenceHelpers.setPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_TOKEN, "");
		PreferenceHelpers.setPref(mContext, Constants.TwitterConstants.PREF_TWITTER_KEY_SECRET, "");
		
		Toast.makeText(mContext, R.string.twitter_logout_succesful, Toast.LENGTH_SHORT).show();
	}
}
