package com.manmeet.squawker.following;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.manmeet.squawker.R;

import static android.content.ContentValues.TAG;

/**
 * Shows the list of instructors you can follow
 */
public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following_squawker);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if (preference!=null && preference instanceof SwitchPreferenceCompat){
            Boolean isOn = sharedPreferences.getBoolean(s,false);

            if (isOn){
                FirebaseMessaging.getInstance().subscribeToTopic(s);
                Log.d(TAG, "onSharedPreferenceChanged:  Subscribed to "+s);
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(s);
                Log.d(TAG, "onSharedPreferenceChanged:  unsubscribed from "+s);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the shared preference change listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the shared preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
