package com.test.sixpro.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import static android.content.Context.ACTIVITY_SERVICE;

public class ActivityTracker {

	private Activity currentActivity = null;
	private static final String TAG = "ActivityTracker";
	private Vector<Activity> allocated = new Vector<Activity>();
	private Vector<Activity> stale = new Vector<Activity>();
	private volatile Context appContext;
	private String KILLALL = "ActivityTracker.killall";
	private volatile long currStackSeq = -1;
	private volatile long staleStackSeq = 0;
	private String myProcessName = null;

	private ActivityTrackerHelper helper = new ActivityTrackerHelper(this);

	public ActivityTrackerHelper getATHelper() {
		return helper;
	}

	private ActivityTracker() {
		at = this;
	}

	private BroadcastReceiver killRx = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String pname = intent.getStringExtra("process");
			Log.d(TAG, KILLALL + ":" + pname);
			if (myProcessName != null && myProcessName.equals(pname)) {
				finishAll();
			}
		}

	};

	private static ActivityTracker at = new ActivityTracker();

	public static ActivityTracker getAT() {
		return at;
	}

	public void ActivityOnCreate(Activity currAct) {
		Log.d(TAG, "ActivityOnCreate:" + currAct);
		Log.d(TAG, "ActivityOnCreate, situation is now: " + this);
		currentActivity = currAct;
		
//		ImageManager.revalidateCaller(currAct.toString());
		allocated.add(0, currAct);

		if (appContext == null) {
			appContext = currAct.getApplicationContext();
			appContext.registerReceiver(killRx, new IntentFilter(KILLALL));
		}

		if (myProcessName == null)
			myProcessName = getCurProcessName(appContext);

		if (allocated.size() == 1) {
			// new stack
			currStackSeq = System.currentTimeMillis();
			init();
			// no need to deinit.
			stale.clear();
		}
	}
	public  String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		return getProcessNameByPid(context, pid);
	}

	public  String getProcessNameByPid(Context context, int pid) {
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	public void ActivityOnResume(Activity currAct) {
		Log.d(TAG, "ActivityOnResume:" + currAct);
		Log.d(TAG, "ActivityOnResume, situation is now: " + this);
		currentActivity = currAct;
		allocated.remove(currAct);
		allocated.add(0, currAct);
	}
	@SuppressWarnings("unchecked")
	public <T extends Activity> ArrayList<T> getActivityInStack(Class<T> classOfT ) {
		ArrayList ret = new ArrayList();
		String qualifiedname = classOfT.getName();
		for (int i = 0; i < allocated.size(); i++) {
			Activity a = allocated.get(i);
			if (a.getClass().getName().equals(qualifiedname)&&!a.isFinishing()) {
				ret.add(a);
			}
		}
		return ret;
	}

	public <T extends Activity> T getUniqueActivityInStack(Class<T> classOfT) {
		ArrayList<T> list = getActivityInStack(classOfT);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	public void ActivityOnDestroy(Activity destroyed) {
		Log.d(TAG, "ActivityOnDestroy:" + destroyed);
		Log.d(TAG, "ActivityOnDestroy, situation is now: " + this);

//		ImageManager.invalidateCaller(destroyed.toString());
		if (destroyed == currentActivity) {
			int idx = allocated.indexOf(destroyed);
			if (idx != -1 && allocated.size() > idx + 1)
				currentActivity = allocated.get(idx + 1);
			else
				currentActivity = null;
		}

		if (allocated.remove(destroyed) && allocated.isEmpty()) {
			deinit();
		} else {
			onStaleActivityDestroy(destroyed);
		}
	}

	private synchronized void onStaleActivityDestroy(Activity destroyed) {
		Log.d(TAG, "onStaleActivityDestroy:" + destroyed);
		if (stale.remove(destroyed) && stale.isEmpty() && allocated.isEmpty() && staleStackSeq == currStackSeq) {
			deinit();
			staleStackSeq = 0;
		}
	}

	private synchronized void deinit() {
		Log.d(TAG, "deinit:" + this);
//		ImageManager.close();
//		CmdCoordinator.shutdown();
		currStackSeq = -1;

		// Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);
	}

	private synchronized void init() {
		Log.d(TAG, "init:" + this);
		// HttpExHandler.initHandler(appContext);

//		ImageManager.init();
	}

	public Activity getPossibleTop() {
		return currentActivity;
	}

	/**
	 * detemine which activity is the one above the other
	 * 
	 * @param a1
	 * @param a2
	 * @return 1 a1 above a2, 0 the same, -1 a2 above a1
	 */
	public int compareActivitiesInStack(Activity a1, Activity a2) {
		if (a1 == a2)
			return 0;
		int idx1 = allocated.indexOf(a1);
		int idx2 = allocated.indexOf(a2);
		if (idx1 == -1 && idx2 == -1)
			return 0;
		return idx1 < idx2 ? 1 : -1;
	}

	public void killall(String pName) {
		Log.d(TAG, "killall:" + pName);
		if (pName == null || pName.equals(myProcessName)) {
			finishAll();
		} else if (appContext != null) {
			appContext.sendBroadcast(new Intent(KILLALL).putExtra("process", pName));
		}
	}

	public void killall() {
		Log.d(TAG, "killall");
		killall(null);
	}
	@SuppressWarnings("unchecked")
	public <T extends Activity> ArrayList<T> getAboveActivities(Class<T> classOfT) {
		String qname=classOfT.getName();
		ArrayList result = new ArrayList();
		Activity unique = getUniqueActivityInStack(classOfT);
		if (unique == null)
			return result;
		for (Iterator iter = allocated.iterator(); iter.hasNext();) {
			Activity a = (Activity) iter.next();
			if (a != unique)
				result.add(a);
			else
				break;
		}
		return result;
	}

	private synchronized void finishAll() {
		Log.d(TAG, "finishAll:  " + this);
		if (allocated != null) {
			for (Iterator iter = this.allocated.iterator(); iter.hasNext();) {
				Activity a = (Activity) iter.next();
				if (!a.isFinishing())
					a.finish();
			}
			stale = allocated;
			if (currStackSeq != -1)
				staleStackSeq = currStackSeq;
			allocated = new Vector<Activity>();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("currStackSeq:" + currStackSeq + ";staleStackSeq:" + staleStackSeq + ";allocated:" + allocated + ";stale:" + stale);
		return super.toString() + ";" + sb.toString();


	}


	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			killall();
			LogInfo.log("wwn","kill");
//			System.exit(0);

			ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器
			manager.killBackgroundProcesses(context.getPackageName());
//			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			LogInfo.log("wwn","printStackTrace");
		}
	}
}
