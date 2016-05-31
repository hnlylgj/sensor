package com.hnlylgj.sensor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartIntentReceiver extends BroadcastReceiver
{
	  @Override
	  public void onReceive(Context context, Intent intent)
	  {
	    // TODO Auto-generated method stub
		  Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	        
   	      MyIntentToService.putExtra("ActionID", "create");
   	      //MyIntentToService.putExtra("ParaStr", "1234567890");	  
   	      /* 设置Intent打开为FLAG_ACTIVITY_NEW_TASK */ 
   	      MyIntentToService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   	      context.startService(MyIntentToService);
   	      
	    /* 当收到Receiver时，指定打开此程序（EX06_16.class） */
	    //Intent mBootIntent = new Intent(context, EX049.class);
	    
	    /* 设置Intent打开为FLAG_ACTIVITY_NEW_TASK */ 
	    //mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
	    /* 将Intent以startActivity传送给操作系统 */ 
	    //context.startActivity(mBootIntent);
	  }
}
