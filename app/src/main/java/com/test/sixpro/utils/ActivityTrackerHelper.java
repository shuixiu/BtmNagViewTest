package com.test.sixpro.utils;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

public class ActivityTrackerHelper implements ActivityLifecycleCallbacks {

	private ActivityTracker at;

	public ActivityTrackerHelper(ActivityTracker tracker) {
		at = tracker;
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		at.ActivityOnCreate(activity);
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {
		at.ActivityOnResume(activity);
	}

	@Override
	public void onActivityPaused(Activity activity) {
	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		at.ActivityOnDestroy(activity);

	}

}
