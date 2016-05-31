package com.hnlylgj.sensor;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import com.hnlylgj.LGJNIOSocketAPI.ChannelServiceBundle;
import com.hnlylgj.LGJNIOSocketAPI.LGJNIOSocketBase;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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

public class LGJCloudBusChannelService2 extends Service 
{
	
	    LGJNIOSocketBase MyLGJNIOSocketBase;
	   Intent MySnapResutintent;
	  
	    public static final String NotifyAction = "cloudbus.broadcast.action"; 
	    public static final String restartselfNotifyAction = "cloudbus.restart.action"; 
		public static String GlobalChannelIDStr;	
		//public static String NameIDStr;	
		//public static String PassWordIDStr;
		public static int CloseFlagID;
		
		public static int ChannelIDState;	
		public static Handler MeCallBackHandler;
		public static LGJCloudBusChannelService2 CurrentLGJCloudBusChannelService;
		
		private Timer mTimer;
	    private TimerTask mTimerTask;
		
		
		 //========委托定义============================================    
	    public Handler MyCallBackHandler = new Handler()
	 	{  
	 	       @Override  
	 	       public void handleMessage(Message msg)
	 	      {  
	 	    	  	    	   	    	   
	 	    	   if(msg.what<0x52)
	 	    	   { 	    		   
	 	    		   //--广播机制--------------------------
	 	    		  Bundle Mybundle = msg.getData();  	 	    	   
	 	 	    	  Intent intent = new Intent(NotifyAction); 
	 	 	    	  //intent.putExtra("data", "yes i am data"); 
	 	 	    	  intent.putExtra("mybundle",Mybundle); 
	 	 	    	  intent.putExtra("mywhat",msg.what); 
	 	 	    	  //intent.putExtra("myMessage",msg); 
	 	 	    	  sendBroadcast(intent); 
	 	 	    	  //return;
	 	    	   }
	 	    	
	 	    	  //--同步操作---------------------------------- 
	 	          if (msg.what == 0x10) 
	 	          {  
	 	             Bundle bundle = msg.getData();  
	 	             //textview.append("ChannelID:"+bundle.getString("msg")+"\n");  
	 	             GlobalChannelIDStr=bundle.getString("msg");
	 	             //LGJCloudBusChannelService.GlobalChannelIDStr=GlobalChannelIDStr;
	 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	 	          }  
	 	          
	 	          if (msg.what == 0x11) 
	 	          {  
	 	             //Bundle bundle = msg.getData();  
	 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	 	          }  
	 	          
	 	          if (msg.what == 0x12) 
	 	          {  
	 	             //Bundle bundle = msg.getData();  
	 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	 	             //Intent MySnapResutintent;//=new Intent();
	 	             //MySnapResutintent=new Intent(this,SnapViewActivity.class);
	 	             //MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
	 	             //MySnapResutintent.putExtra("SnapID", bundle.getString("msg")); 	             
	 	             //startActivity( MySnapResutintent);      
	 	             
	 	             
	 	          }  
	 	          //-------异步操作-----------------------------------------------
	 	          if (msg.what == 0x50) //建立连接/关闭连接
	 	          {  
	 	              Bundle bundle = msg.getData();  
	 	              String Str=bundle.getString("msg");
	 	        	  if(Str.indexOf("close")>-1)
	 	        	  {
	 	        		// MyLGJNIOSocketBase.CloseConnect();
	 	        		MyLGJNIOSocketBase=null;
	 	        	  }
	 	        	  if(Str.indexOf("open")>-1)
	 	        	  {
	 	        		 if(GlobalChannelIDStr==null)return;
						 if(GlobalChannelIDStr=="")return;
			   		     MyLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
	 	        		  
	 	        	  }
	 	        	        	  
	 	          }
	 	           
	 	          if (msg.what == 0x51) //消息响应
	 	          {  
	 	             //Bundle bundle = msg.getData();  
	 	             //ResponseMessage(bundle.getString("msg")); 
	 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	 	          }  
	 	          
	 	          //================================================================
	 	          
	 	          if (msg.what == 0x52) //消息推送
	 	          {  
	 	             Bundle bundle = msg.getData();  
	 	             PushMessage(bundle.getString("msg")); 
	 	             	             
	 	            
	 	          }  
	 	        
	 	          if (msg.what == 0x53) //定时器心跳命令发送
	 	          {  
	 	        	  //btnKeepChannel.callOnClick();	
	 	        	 if(MyLGJNIOSocketBase!=null)
			   	     {  
						 //String ChannelIDStr=ActionPara;
						 //ChannelIDStr=GlobalChannelIDStr;
						 if(GlobalChannelIDStr==null)
						 {
						 //SendMessageCallBack(0x50,"通道ID未初始化");  
						 return;
						 }
					     if(GlobalChannelIDStr=="")
						 {
						 //SendMessageCallBack(0x50,"通道ID未初始化");  
						 return;
						 }
			   		     MyLGJNIOSocketBase.KeepChannel(GlobalChannelIDStr);
			   	    } 
					 else
					 {
						 //SendMessageCallBack(0x50,"通道未初始化");  
					 }
	 	        	  
	 	            
	 	          }  
	 	          if (msg.what == 0x54) //定时器心跳命令应答
		          { 
	 	        	//SendMessageCallBack(0x51,"ping...");  
	 	        	  Bundle Mybundle = msg.getData();  	 	    	   
		 	    	  Intent intent = new Intent(NotifyAction); 
		 	    	  //intent.putExtra("data", "yes i am data"); 
		 	    	  intent.putExtra("mybundle",Mybundle); 
		 	    	  intent.putExtra("mywhat",0x51); 
		 	    	  //intent.putExtra("myMessage",msg); 
		 	    	  sendBroadcast(intent); 
		          }
	 	          //----------------------------------------------------
	 	          
	 	       }  
	 	 
	 	   };   
	 	   
	      protected void ResponseMessage(String ReceiveMessageStr) 
		   {
			   //MyLabeltextview.setText(ReceiveMessageStr);
			   String MessageTypeIDStr;
			   String MessageResultStr;
			   int StartIndex=0;//ReceiveMessageStr.indexOf("[")+1;
		       int EndIndex=ReceiveMessageStr.indexOf("#");
		       MessageTypeIDStr=ReceiveMessageStr.substring(StartIndex, EndIndex);
		      
		       StartIndex=ReceiveMessageStr.lastIndexOf("#")+1;
	 	      EndIndex=ReceiveMessageStr.length()-1;
	 	      MessageResultStr=ReceiveMessageStr.substring(StartIndex, EndIndex); 
	 	      //-------------------------
		       //MyLabeltextview.setText(MessageTypeIDStr+":"+MessageResultStr);
		       
		       //----下一步处理--------------------------------------------------------
		       if(MessageTypeIDStr.equalsIgnoreCase("login"))
		       { 
		    	   if(ReceiveMessageStr.indexOf("true")>-1)
					{
		    		   ChannelIDState=0; 
		    	    	
					}	    	   
		    	   return;   
	        }
		       
		       if(MessageTypeIDStr.equalsIgnoreCase("remotesnap"))
		       { 
		    	   if(ReceiveMessageStr.indexOf("true")>-1)
					{
		    		   StartIndex=ReceiveMessageStr.indexOf("[")+1;
		    	       EndIndex=ReceiveMessageStr.indexOf(",");
		    	       String SnapID=ReceiveMessageStr.substring(StartIndex, EndIndex);	
		    	       
		    	       Intent MySnapResutintent=new Intent();
		    	       MySnapResutintent=new Intent(this,SnapViewActivity.class);
		    	       MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
			           MySnapResutintent.putExtra("SnapID", SnapID);
			           MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);
			           startActivity(MySnapResutintent);  
		    	    	
					}	    	   
		    	   return;   
	        }
		       
		     
			   
			   
		   
		   }
		 
	      protected void PushMessage(String PushMessageStr) 
		   {
			   //MyLabeltextview.setText(ReceiveMessageStr);
			   String MessageTypeIDStr;
			   String MessageResultStr;
			   int StartIndex=0;//ReceiveMessageStr.indexOf("[")+1;
		       int EndIndex=PushMessageStr.indexOf("#");
		       MessageTypeIDStr=PushMessageStr.substring(StartIndex, EndIndex);
		      
		       StartIndex=PushMessageStr.lastIndexOf("#")+1;
	 	       EndIndex=PushMessageStr.length()-1;
	 	       MessageResultStr=PushMessageStr.substring(StartIndex, EndIndex); 
	 	     //-----------------------------------------------------------------------
	 	      //MyLabeltextview.setText(MessageTypeIDStr+":"+MessageResultStr);
	 	      if(MessageTypeIDStr.equalsIgnoreCase("mailsnap"))
		       { 
		    	  
		    		   StartIndex=PushMessageStr.indexOf("[")+1;
		    	       EndIndex=PushMessageStr.indexOf(",");
		    	       String SnapID=PushMessageStr.substring(StartIndex, EndIndex);	   	       
		    	      
		    	       //Intent MySnapResutintent=new Intent();
		    	       //MySnapResutintent=new Intent(this,SnapViewActivity.class);
		    	       //MySnapResutintent.removeExtra("SnapID");
		  			   //MySnapResutintent.removeExtra("SnapType");
		  			   //MySnapResutintent.removeExtra("GlobalChannelID");
		    	       //MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
			           //MySnapResutintent.putExtra("SnapID", SnapID);
			           //MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);
			           
			           //startActivity(MySnapResutintent);  		          
			           // MyNotificationManager(null);
			           
			           MyNotificationManager(SnapID,MessageTypeIDStr);
			           return;  
	         	   
	          }
	 	       if(MessageTypeIDStr.equalsIgnoreCase("exceptsnap"))
		       { 
		    	  
		    		   StartIndex=PushMessageStr.indexOf("[")+1;
		    	       EndIndex=PushMessageStr.indexOf(",");
		    	       String SnapID=PushMessageStr.substring(StartIndex, EndIndex);	    	       
		    	     
		    	       //---------------------------------------------
		    	       //MySnapResutintent=new Intent(this,FuncActivity.class);
		    	       //MySnapResutintent.removeExtra("SnapID");
		  			   //MySnapResutintent.removeExtra("SnapType");
		  			   //MySnapResutintent.removeExtra("GlobalChannelID");
		    	       //MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
			           //MySnapResutintent.putExtra("SnapID", SnapID);
			           //MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);		          
			            //startActivity(MySnapResutintent); 	    	       
		    	       //--------------------------------------------
			           MyNotificationManager(SnapID,MessageTypeIDStr);
			           return;
				 	   
	         	   
	        }
	 	   if(MessageTypeIDStr.equalsIgnoreCase("opendoor"))
		       { 
		    	  
			     //MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
	 		     MyStatusNotificationManager(MessageTypeIDStr+":"+MessageResultStr);
			     return;
				 	   
	        	   
	       }
	 	   if(MessageTypeIDStr.equalsIgnoreCase("lockstatus"))
		       { 
		    	  
	 		     //MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
	 		     MyStatusNotificationManager(MessageTypeIDStr+":"+MessageResultStr);
			     return;
				 	   
	         	   
	        }
	 	    if(MessageTypeIDStr.equalsIgnoreCase("locklogin"))
		       { 
		    	  
	 		     //MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
	 	    	 MyStatusNotificationManager(MessageTypeIDStr+":"+MessageResultStr);
	 	    	 MyLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
	 	    	 
			     return;
				 	   
	         	   
	        }
	 	      if(MessageTypeIDStr.equalsIgnoreCase("lockoutoff"))
		       { 
		    	  
	 		     //MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
	 	    	 MyStatusNotificationManager(MessageTypeIDStr+":"+MessageResultStr);
			     return;
				 	   
	         	   
	        }
			 
				   
		   }
		   
		  protected void  MyToastManager(String WarnStr)
		   {
			   Toast MyToast = Toast.makeText(this, "云智能锁告警:"+WarnStr, Toast.LENGTH_LONG);
			   MyToast.setGravity(Gravity.CENTER, 0, 0);				 	
			   MyToast.show();
					 	
			 //MyToast.makeText(NotificationDemoActivity.this, "这是一个Toast演示！", Toast.LENGTH_LONG).show();

		   }
		   
		   @SuppressLint("NewApi") @SuppressWarnings("deprecation")
		  protected void  MyNotificationManager(String SnapID,String MessageTypeIDStr)
		   {
			   NotificationManager MyNotificationManager;
			   Notification MyNotification; 		 
			   int NOTIFICATION_ID = 101;
			  
			   Context MyContext;
			   MyContext = getApplicationContext();
			   
			   String ns = Context.NOTIFICATION_SERVICE;
			   MyNotificationManager = (NotificationManager) getSystemService(ns);
			
			    int icon = R.drawable.ic_launcher;		
				long when = System.currentTimeMillis();
			
				Notification.Builder builder=new Notification.Builder(this);		
				builder.setSmallIcon(icon);
				//builder.setTicker(tickerText);
				builder.setWhen(when);
				MyNotification=builder.getNotification();
				
				//CharSequence tickerText = "这是云智能锁消息通知！";
				//Notification notification = new Notification(icon, tickerText, 10000);
				
				CharSequence MyContentTitle = "云智能锁："+MessageTypeIDStr;
				CharSequence MyContentText = "抓拍图片："+SnapID;

				//Intent notificationIntent = new Intent(context, NoteActivity.class);			
				Intent MySnapResutintent=new Intent(this,SnapViewActivity.class);
				
				MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
		        MySnapResutintent.putExtra("SnapID", SnapID);
		        MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);

				PendingIntent MyPendingIntent = PendingIntent.getActivity(this, 0,  MySnapResutintent, 0);
				MyNotification.setLatestEventInfo(this, MyContentTitle, MyContentText, MyPendingIntent);
				MyNotification.defaults=MyNotification.DEFAULT_SOUND;
				MyNotification.flags|=Notification.FLAG_AUTO_CANCEL;
				MyNotificationManager.cancel(NOTIFICATION_ID);
				MyNotificationManager.cancelAll();
				MyNotificationManager.notify(NOTIFICATION_ID, MyNotification);
				
			
				

		   }
		
		  protected void  MyStatusNotificationManager(String MessageTypeIDStr)
		   {
			   
			   //待完善
			   NotificationManager MyNotificationManager;
			   Notification MyNotification; 		 
			   int NOTIFICATION_ID = 102;
			  
			   Context MyContext;
			   MyContext = getApplicationContext();
			   
			   String ns = Context.NOTIFICATION_SERVICE;
			   MyNotificationManager = (NotificationManager) getSystemService(ns);
			
			    int icon = R.drawable.ic_launcher;		
				long when = System.currentTimeMillis();
			
				Notification.Builder builder=new Notification.Builder(this);		
				builder.setSmallIcon(icon);
				//builder.setTicker(tickerText);
				builder.setWhen(when);
				MyNotification=builder.getNotification();
				
				//CharSequence tickerText = "这是云智能锁消息通知！";
				//Notification notification = new Notification(icon, tickerText, 10000);
				
				CharSequence MyContentTitle = "云智能锁";
				CharSequence MyContentText = "锁态提醒:"+MessageTypeIDStr;

				//Intent notificationIntent = new Intent(context, NoteActivity.class);			
				//Intent MySnapResutintent=new Intent(this,SnapViewActivity.class);
				
				//MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
		        //MySnapResutintent.putExtra("SnapID", SnapID);
		        //MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);

				//PendingIntent MyPendingIntent = PendingIntent.getActivity(this, 0,  null, 0);
				MyNotification.setLatestEventInfo(this, MyContentTitle, MyContentText, null);
				
				MyNotification.defaults=MyNotification.DEFAULT_SOUND;
				MyNotification.flags|=Notification.FLAG_AUTO_CANCEL;
				MyNotificationManager.cancel(NOTIFICATION_ID);
				MyNotificationManager.cancelAll();
				MyNotificationManager.notify(NOTIFICATION_ID, MyNotification);
				
			
				

		   }
		   
		   
		 //============Hander End==============================================
		
		 public LGJCloudBusChannelService2()
		 {
			CurrentLGJCloudBusChannelService=this;	
		 }

		@Override
		public IBinder onBind(Intent intent) 
		{
			// TODO: Return the communication channel to the service.
			throw new UnsupportedOperationException("Not yet implemented");
		}
		
		
		
		
		@Override
		public int onStartCommand(Intent intent, int flags, int startId)
		{
		//防止service 被回收
		//return START_STICKY;
		 flags =  START_STICKY;
	     return super.onStartCommand(intent, flags, startId);

		
		}
		
		@Override
		public void onCreate()
		{
			
			// TODO Auto-generated method stub
			//Log.v(TAG , TAG+ " onCreate()");
			super.onCreate();
			MySnapResutintent=new Intent(this,SnapViewActivity.class);
			
		    SharedPreferences setinfo =this.getSharedPreferences("cloudbusshare2", Service.MODE_PRIVATE);
		    GlobalChannelIDStr=setinfo.getString("GlobalChannelID", null);	 
		   
		 //-----设置定时维持通道命令----------------------
	       mTimer = new Timer();
	       mTimerTask = new TimerTask() 
	       {
	           @Override
	           public void run()
	           {
	               //Log.d("AndroidTimerDemo", "timer");
	               //Calendar cal = Calendar.getInstance();
	               //mButton.setText(cal.toString());                
	               Message msg = new Message();  
	               msg.what = 0x53;  
	               Bundle bundle = new Bundle();  
	               bundle.clear(); 
	      	  	   bundle.putString("msg", "keepalive");  
	               msg.setData(bundle);  
	               MyCallBackHandler.sendMessage(msg); 
	               //btnKeepChannel.callOnClick();
	           }
	       };

	       mTimer.schedule(mTimerTask,20000 , 300000);
	       //------------------------------------------------------------------- 
	      //NameIDStr=setinfo.getString("NameID", "");
	      //PassWordIDStr=setinfo.getString("PassWordID", "");
		  
			
			
		}
		
		

		@Override
		public void onDestroy() 
	    {
			// TODO Auto-generated method stub
			 super.onDestroy();
			 if(MyLGJNIOSocketBase!=null)
	   	     {  
	   		  try 
	   		  {
				MyLGJNIOSocketBase.CloseConnect();
			  } 
	   		  catch (IOException e) 
	   		  {
				// TODO Auto-generated catch block
				e.printStackTrace();
			   }
	   		  MyLGJNIOSocketBase=null;
	   		  CurrentLGJCloudBusChannelService=null;
	   		  MeCallBackHandler=null;
	   		  MySnapResutintent=null;
	   		  
	   		  
	   		 SharedPreferences setinfo =this.getSharedPreferences("cloudbusshare2", Service.MODE_PRIVATE);
	   		  setinfo.edit()
	         .putString("GlobalChannelID",GlobalChannelIDStr)
	         //.putString("NameID",NameIDStr)
	         //.putString("PassWordID",PassWordIDStr)
	         .commit();
	   		  if(CloseFlagID==0)
	   		  {
	   		  ///Intent intent = new Intent(restartselfNotifyAction); 	    	 
	   	      ///sendBroadcast(intent);  
	   			  
	   			  Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	        
	        	   MyIntentToService.putExtra("ActionID", "create");
	        	   MyIntentToService.putExtra("ParaStr", GlobalChannelIDStr);	        	   
	        	   startService(MyIntentToService);
	   		  }
	   		 


	   	  
	   	     }
			
			
		}
		
		 private void SendMessageCallBack(int WhatID,String ResultStr)
		 { 
			
			   Message msg = new Message();  
			   msg.what = WhatID;  
			   Bundle bundle = new Bundle();  
			   bundle.clear();  
			          	  
			   bundle.putString("msg", ResultStr);  
			   msg.setData(bundle);  
			   if(MyCallBackHandler!=null)
				   MyCallBackHandler.sendMessage(msg); 
			 
			 
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
				   
				   //ChannelServiceBundle MyChannelServiceBundle = intent.getParcelableExtra("inoutPara");   
				   //MeCallBackHandler=MyChannelServiceBundle.MyUISynchHandler;
				   //String ActionID2=MyChannelServiceBundle.ActionID;
				   
				   
				   //Log.i("request ActionID:", ActionID); 
				   //Log.i("request ActionPara:", ActionPara); 		   
				   			   
				   //create
				    if(ActionID.equalsIgnoreCase("create"))
					 {			    	  
						 if(MyLGJNIOSocketBase==null)
				   	     {  
						  if(ActionPara!=null)
						  GlobalChannelIDStr=ActionPara;
				   		  MyLGJNIOSocketBase=new LGJNIOSocketBase();
				   	      MyLGJNIOSocketBase.OpenConnect(MyCallBackHandler);
				   	      CloseFlagID=0;
				   	    } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化"); 
						 }
						 
					 }
				    //close
					 if(ActionID.equalsIgnoreCase("close"))
					 {
						 if(MyLGJNIOSocketBase!=null)
				   	     {  
				   		  MyLGJNIOSocketBase.CloseConnect();
				   		  MyLGJNIOSocketBase=null;
				   		  //--------------------
				   		   String ResultStr;
						   ResultStr="关闭通道"; 					  
						   SendMessageCallBack(0x50, ResultStr); 
											   /*
						   Message msg = new Message();  
					       msg.what = 0x50;  
					       Bundle bundle = new Bundle();  
					       bundle.clear();  
					         	  
						     bundle.putString("msg", ResultStr);  
					         msg.setData(bundle);  
					        //发送消息 修改UI线程中的组件  
					          //Bundle Mybundle = msg.getData(); 
					          //Mybundle.putString("msg", ResultStr);  
			 	 	    	  //Intent Myintent = new Intent(NotifyAction); 
			 	 	    	  //Myintent.putExtra("mybundle",Mybundle); 
			 	 	    	  //sendBroadcast(intent); 
					          if(MyCallBackHandler!=null)
					        	 MyCallBackHandler.sendMessage(msg); 
					         */
				   		  
				   		  
				   	    } 
					 }
					 
					  //stop
					 if(ActionID.equalsIgnoreCase("stop"))
					 {
						 CloseFlagID=1;
					 }
					 //login
					 if(ActionID.equalsIgnoreCase("login"))
					 {
						 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
				   	      {  
							   //String ChannelIDStr=ActionPara;
							   //ChannelIDStr=GlobalChannelIDStr;
							    if(GlobalChannelIDStr==null)
								 {
								 SendMessageCallBack(0x50,"通道ID未初始化");  
								 return;
								 }
							    if(GlobalChannelIDStr=="")
								 {
								 SendMessageCallBack(0x50,"通道ID未初始化");  
								 return;
								 }
				   		        MyLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
				   	     } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化");  
						 }
					 }
					 //keeplife
					 if(ActionID.equalsIgnoreCase("keeplife"))
					 {
						 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
				   	     {  
							 //String ChannelIDStr=ActionPara;
							 //ChannelIDStr=GlobalChannelIDStr;
							 if(GlobalChannelIDStr==null)
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
						     if(GlobalChannelIDStr=="")
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
				   		     MyLGJNIOSocketBase.KeepChannel(GlobalChannelIDStr);
				   	    } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化");  
						 }
					 }
					 
					 //remotesnap
					 if(ActionID.equalsIgnoreCase("remotesnap"))
					 {
						 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
				   	     {  
							 //String ChannelIDStr=ActionPara;
							 //ChannelIDStr=GlobalChannelIDStr;
							 if(GlobalChannelIDStr==null)
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
						     if(GlobalChannelIDStr=="")
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
				   		     MyLGJNIOSocketBase.RemoteSnap(GlobalChannelIDStr);
				   	    } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化");  
						 }
					 }
					 
					//remoteopen
					 if(ActionID.equalsIgnoreCase("remoteopen"))
					 {
						 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
				   	     {  
							 //String ChannelIDStr=ActionPara;
							 //ChannelIDStr=GlobalChannelIDStr;
							 if(GlobalChannelIDStr==null)
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
						     if(GlobalChannelIDStr=="")
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
				   		     MyLGJNIOSocketBase.RemoteOpen(GlobalChannelIDStr);
				   	    } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化");  
						 }
					 }
					
					 
					//getpower
					 if(ActionID.equalsIgnoreCase("getpower"))
					 {
						 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
				   	     {  
							 //String ChannelIDStr=ActionPara;
							 //ChannelIDStr=GlobalChannelIDStr;
							 if(GlobalChannelIDStr==null)
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
						     if(GlobalChannelIDStr=="")
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
				   		     MyLGJNIOSocketBase.GetPower(GlobalChannelIDStr);
				   	    } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化");  
						 }
					 }
					 
					//tempkey
					 if(ActionID.equalsIgnoreCase("tempkey"))
					 {
						 if(MyLGJNIOSocketBase!=null&&ActionPara!=null)
				   	     {  
							 //String ChannelIDStr=ActionPara;
							 //ChannelIDStr=GlobalChannelIDStr;
							 if(GlobalChannelIDStr==null)
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
						     if(GlobalChannelIDStr=="")
							 {
							 SendMessageCallBack(0x50,"通道ID未初始化");  
							 return;
							 }
				   		     MyLGJNIOSocketBase.TempKey(GlobalChannelIDStr,"123456");
				   	    } 
						 else
						 {
							 SendMessageCallBack(0x50,"通道未初始化");  
						 }
					 }
					 
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
