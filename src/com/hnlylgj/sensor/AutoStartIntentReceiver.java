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
   	      /* ����Intent��ΪFLAG_ACTIVITY_NEW_TASK */ 
   	      MyIntentToService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   	      context.startService(MyIntentToService);
   	      
	    /* ���յ�Receiverʱ��ָ���򿪴˳���EX06_16.class�� */
	    //Intent mBootIntent = new Intent(context, EX049.class);
	    
	    /* ����Intent��ΪFLAG_ACTIVITY_NEW_TASK */ 
	    //mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
	    /* ��Intent��startActivity���͸�����ϵͳ */ 
	    //context.startActivity(mBootIntent);
	  }
}
