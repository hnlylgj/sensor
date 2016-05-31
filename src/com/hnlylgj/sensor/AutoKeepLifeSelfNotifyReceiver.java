package com.hnlylgj.sensor;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class AutoKeepLifeSelfNotifyReceiver extends BroadcastReceiver
{

	 @Override
	  public void onReceive(Context context, Intent intent)
	  {
	  
		     Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	        
	         MyIntentToService.putExtra("ActionID", "create");
	   	     MyIntentToService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   	     context.startService(MyIntentToService);
		 
		 
	  }
	
}
