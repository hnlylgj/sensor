package com.hnlylgj.sensor;
import com.hnlylgj.LGJNIOSocketAPI.*;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class SocketChannelService extends Service 
{
	
	LGJNIOSocketBase MyLGJNIOSocketBase;
	Handler MeCallBackHandler;
	
	public SocketChannelService() 
	{
		
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	@Override
	public void onDestroy() 
    {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void onStart(Intent intent, int startId)
    {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	    if(intent!=null)
		{
			try 
			{	
			  // Handler MyCallBackHandler=(Handler)intent.getSerializableExtra("myhander");
			   String ActionID=intent.getStringExtra("ActionID");
			   String ActionPara=intent.getStringExtra("ParaStr");
			   Log.i("request ActionID:", ActionID); 
			   Log.i("request ActionPara:", ActionPara); 
			   
			   //create
			    if(ActionID.equalsIgnoreCase("create"))
				 {
					 if(MyLGJNIOSocketBase==null)
			   	     {  
			   		  MyLGJNIOSocketBase=new LGJNIOSocketBase();
			   	      MyLGJNIOSocketBase.OpenConnect(null);
			   	    } 
				 }
			    //close
				 if(ActionID.equalsIgnoreCase("close"))
				 {
					 if(MyLGJNIOSocketBase!=null)
			   	     {  
			   		  MyLGJNIOSocketBase.CloseConnect();
			   		  MyLGJNIOSocketBase=null;
			   	    } 
				 }
				 //login
				 if(ActionID.equalsIgnoreCase("login"))
				 {
					 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
			   	     {  
						String GlobalChannelIDStr=ActionPara;
			   		    MyLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
			   	    } 
				 }
				 //keeplife
				 if(ActionID.equalsIgnoreCase("keeplife"))
				 {
					 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
			   	     {  
						String GlobalChannelIDStr=ActionPara;
			   		    MyLGJNIOSocketBase.KeepChannel(GlobalChannelIDStr);
			   	    } 
				 }
				 //
				 
				
				 
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				Log.i("error:", "faile"); 
				e.printStackTrace();
			}		
		}
		
		
	}
	
}
