package com.hnlylgj.sensor;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class AutoKeepLifeCheckReceiver extends BroadcastReceiver
{

	
	 @Override
	  public void onReceive(Context context, Intent intent)
	  {
	      // TODO Auto-generated method stub
		  //Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	        
  	      //MyIntentToService.putExtra("ActionID", "create");
  	      //MyIntentToService.putExtra("ParaStr", "1234567890");	  
  	      /* 设置Intent打开为FLAG_ACTIVITY_NEW_TASK */ 
  	      //MyIntentToService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  	      //context.startService(MyIntentToService);
  	      
	    /* 当收到Receiver时，指定打开此程序（EX06_16.class） */
	    //Intent mBootIntent = new Intent(context, EX049.class);
	    
	    /* 设置Intent打开为FLAG_ACTIVITY_NEW_TASK */ 
	    //mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
	    /* 将Intent以startActivity传送给操作系统 */ 
	    //context.startActivity(mBootIntent);
		 
		     boolean isServiceRunning = false;  
		    //if (intent.getAction().equals(Intent.ACTION_TIME_TICK))
		    //{  
		    //}
		        
		    //检查Service状态   		        
		      ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);   
		      for (RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE))
		      {   
		         if("com.hnlylgj.sensor.LGJCloudBusChannelService".equals(service.service.getClassName()))  
	              {   
		     
		    	   isServiceRunning = true;
		    	   break;		    			
		  
	             }   
		        
		     }  
		    //======================================================
		    if (!isServiceRunning) 
		    {   
		  
		    	//Intent i = new Intent(context, xxxService.class);   
		        //context.startService(i);   
		           
		         Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	        
		         MyIntentToService.putExtra("ActionID", "create");
		   	     MyIntentToService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   	     context.startService(MyIntentToService);
		  
		    }   
		  
		  

		}    
	 
	   
	   
	    
		 
		 
	  //}
}
