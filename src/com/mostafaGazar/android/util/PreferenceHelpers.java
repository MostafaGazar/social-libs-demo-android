/**
 * Created at: Oct 25, 2011
 */
package com.mostafaGazar.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Mostafa Gazar
 * Gets and sets values in shared preferences.
 */
public class PreferenceHelpers {

	private static SharedPreferences prefs = null;

	private static void initSharedPreferences(Context context) {
		initSharedPreferences(context, Context.MODE_PRIVATE);
	}

	private static void initSharedPreferences(Context context, int mode) {
		if (prefs == null) {
			prefs = context.getSharedPreferences(
					PreferenceConstants.APP_PREFS_NAME, mode);
		}
	}

	public static Object getPref(Context context, String prefKey) {
		return getPref(context, PreferenceConstants.PREFERENCE_TYPE.STRING,
				prefKey);
	}

	public static Object getPref(Context context,
			PreferenceConstants.PREFERENCE_TYPE prefType, String prefKey) {
		Object defaultValue = new Object();

		if (prefType == PreferenceConstants.PREFERENCE_TYPE.STRING) {
			defaultValue = "";
		} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.BOOLEAN) {
			defaultValue = false;
		} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.INT
				|| prefType == PreferenceConstants.PREFERENCE_TYPE.LONG
				|| prefType == PreferenceConstants.PREFERENCE_TYPE.FLOAT) {
			defaultValue = 0;
		}

		return getPref(context, prefType, prefKey, defaultValue);
	}

	public static Object getPref(Context context,
			PreferenceConstants.PREFERENCE_TYPE prefType, String prefKey,
			Object defaultValue) {
		return getPref(context, prefType, prefKey, defaultValue,
				Context.MODE_PRIVATE);
	}

	public static Object getPref(Context context,
			PreferenceConstants.PREFERENCE_TYPE prefType, String prefKey,
			Object defaultValue, int mode) {
		if (defaultValue != null) {
			initSharedPreferences(context, mode);

			if (prefType == PreferenceConstants.PREFERENCE_TYPE.STRING) {
				return prefs.getString(prefKey, defaultValue.toString());
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.BOOLEAN) {
				return prefs.getBoolean(prefKey,
						Boolean.parseBoolean(defaultValue.toString()));
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.INT) {
				return prefs.getInt(prefKey,
						Integer.parseInt(defaultValue.toString()));
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.LONG) {
				return prefs.getLong(prefKey,
						Long.parseLong(defaultValue.toString()));
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.FLOAT) {
				return prefs.getFloat(prefKey,
						Float.parseFloat(defaultValue.toString()));
			}
		}

		return null;
	}

	public static boolean setPref(Context context, String prefKey,
			Object prefValue) {
		return setPref(context, PreferenceConstants.PREFERENCE_TYPE.STRING,
				prefKey, prefValue);
	}

	public static boolean setPref(Context context,
			PreferenceConstants.PREFERENCE_TYPE prefType, String prefKey,
			Object prefValue) {
		return setPref(context, prefType, prefKey, prefValue,
				Context.MODE_PRIVATE);
	}

	public static boolean setPref(Context context,
			PreferenceConstants.PREFERENCE_TYPE prefType, String prefKey,
			Object prefValue, int mode) {
		if (prefValue != null) {
			initSharedPreferences(context, mode);

			SharedPreferences.Editor editor = prefs.edit();

			if (prefType == PreferenceConstants.PREFERENCE_TYPE.STRING) {
				editor.putString(prefKey, prefValue.toString());
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.BOOLEAN) {
				editor.putBoolean(prefKey,
						Boolean.parseBoolean(prefValue.toString()));
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.INT) {
				editor.putInt(prefKey, Integer.parseInt(prefValue.toString()));
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.LONG) {
				editor.putLong(prefKey, Long.parseLong(prefValue.toString()));
			} else if (prefType == PreferenceConstants.PREFERENCE_TYPE.FLOAT) {
				editor.putFloat(prefKey, Float.parseFloat(prefValue.toString()));
			}

			return editor.commit();
		}

		return false;
	}

}
