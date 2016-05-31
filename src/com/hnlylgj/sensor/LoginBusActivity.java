package com.hnlylgj.sensor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import com.hnlylgj.LGJNIOSocketAPI.ChannelServiceBundle;
import com.hnlylgj.LGJNIOSocketAPI.LGJNIOSocketBase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginBusActivity extends Activity 
{
	
	private String GlobalChannelIDStr;	
	//LGJNIOSocketBase MyLGJNIOSocketBase;
	private int ChannelIDState;
	String MyNameID, MyPassWord;
	
    private static final int NOTIFICATION_ID = 1;
	
	private TextView MyLabeltextview;
	private EditText MyNameIDEditText;
	private EditText MyPassWordEditText;
	
	
	private  Button btnauthenlogin;
	private  Button btnCreateChannel;
	private  Button btnCloseChannel;
	private  Button btnLoginChannel;
	private  Button btnKeepChannel;
	
	private  Button btnstopservice;
	
	private  Button btnfullystopservice;
	private String CloudBusServerIP,CloudBusServerPort;
	
	
    private Intent MySnapResutintent;
	
	//private Timer mTimer;
    //private TimerTask mTimerTask;  
    
    //=======委托定义=============================================    
    public Handler MyCallBackHandler = new Handler()
 	{  
 	       @Override  
 	       public void handleMessage(Message msg)
 	      {  
 	    	   //--同步操作-------
 	          if (msg.what == 0x10) 
 	          {  
 	             Bundle bundle = msg.getData();  
 	             //textview.append("ChannelID:"+bundle.getString("msg")+"\n");  
 	             GlobalChannelIDStr=bundle.getString("msg");
 	             LGJCloudBusChannelService.GlobalChannelIDStr=GlobalChannelIDStr;
 	             MyLabeltextview.setText(""+bundle.getString("msg"));
 	          }  
 	          
 	          if (msg.what == 0x11) 
 	          {  
 	             Bundle bundle = msg.getData();  
 	             MyLabeltextview.setText(""+bundle.getString("msg"));
 	          }  
 	          
 	          if (msg.what == 0x12) 
 	          {  
 	             Bundle bundle = msg.getData();  
 	             MyLabeltextview.setText(""+bundle.getString("msg"));
 	             MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
 	             MySnapResutintent.putExtra("SnapID", bundle.getString("msg"));
 	             
 	             startActivity( MySnapResutintent);      
 	             
 	             
 	          }  
 	          //-------异步操作----------------------------------------
 	          if (msg.what == 0x50) //建立连接/关闭连接
 	          {  
 	             Bundle bundle = msg.getData();  
 	             MyLabeltextview.setText(""+bundle.getString("msg"));
 	          }  
 	           
 	          if (msg.what == 0x51) //消息响应
 	          {  
 	             Bundle bundle = msg.getData();  
 	             ResponseMessage(bundle.getString("msg")); 
 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
 	          }  
 	          
 	          if (msg.what == 0x52) //消息推送
 	          {  
 	             Bundle bundle = msg.getData();  
 	             PushMessage(bundle.getString("msg")); 
 	             	             
 	            
 	          }  
 	        
 	          if (msg.what == 0x53) //定时器心跳命令
 	          {  
 	        	  //btnKeepChannel.callOnClick();	             	             
 	            
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
	       MyLabeltextview.setText(MessageTypeIDStr+":"+MessageResultStr);
	       
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
 	   //-------------------------
 	      MyLabeltextview.setText(MessageTypeIDStr+":"+MessageResultStr);
 	      if(MessageTypeIDStr.equalsIgnoreCase("mailsnap"))
	       { 
	    	  
	    		   StartIndex=PushMessageStr.indexOf("[")+1;
	    	       EndIndex=PushMessageStr.indexOf(",");
	    	       String SnapID=PushMessageStr.substring(StartIndex, EndIndex);	   	       
	    	      
	    	       MySnapResutintent=new Intent(this,SnapViewActivity.class);
	    	       //MySnapResutintent.removeExtra("SnapID");
	  			   //MySnapResutintent.removeExtra("SnapType");
	  			   //MySnapResutintent.removeExtra("GlobalChannelID");
	    	       MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
		           MySnapResutintent.putExtra("SnapID", SnapID);
		           MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);
		           
		           startActivity(MySnapResutintent);  		          
		           // MyNotificationManager(null);
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
 	   if(MessageTypeIDStr.equalsIgnoreCase("lockstatus"))
	       { 
	    	  
 		     MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
		          return;
			 	   
         	   
        }
 	    if(MessageTypeIDStr.equalsIgnoreCase("locklogin"))
	       { 
	    	  
 		     MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
		          return;
			 	   
         	   
        }
 	      if(MessageTypeIDStr.equalsIgnoreCase("lockoutoff"))
	       { 
	    	  
 		     MyToastManager(MessageTypeIDStr+":"+MessageResultStr); 
		          return;
			 	   
         	   
        }
		 
		 
		 
		   
	   
	   }
	   
	  protected void  MyToastManager(String WarnStr)
	   {
		   Toast MyToast = Toast.makeText(this, "云智能告警:"+WarnStr, Toast.LENGTH_LONG);
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
	 //============Hander End============================================
	   
	   //---------广播定义-----------------------------------------------------
	   BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver()
	   { 		  
		         @Override 
		         public void onReceive(Context context, Intent intent) 
		         { 
		             // TODO Auto-generated method stub 
		            //textView.setText(intent.getExtras().getString("data"));
		        	// Message msg=intent.get
		        	 Bundle bundle = intent.getBundleExtra("mybundle"); 
		        	 int Mywhat=intent.getIntExtra("mywhat",0x0);
		        	 Message msg = new Message();  
		 	         msg.what = Mywhat;  
		 	         msg.setData(bundle);  
		 	         MyCallBackHandler.sendMessage(msg);
		        	 /*
		        	 if(Mywhat==0x50)
		        	 {
		        		 CloudBusMessageHelper.LocalStatusMessage(null, MyLabeltextview, Mybundle.getString("msg")); 
		        	 }
		        	 if(Mywhat==0x51)
		        	 {
		        		 CloudBusMessageHelper.ResponseMessage(this, MyLabeltextview, Mybundle.getString("msg")); 
		        	 }
		        	 */
		             //MyLabeltextview.setText(""+Mybundle.getString("msg"));		            
		            //ResponseMessage(Mybundle.getString("msg")); 
		            
		         } 
		    }; 
	   
	   
	   
	  //=============BroadcastReceiver End==================================================== 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_bus);
	    //----------------------------------------------
		 btnauthenlogin=(Button)this.findViewById(R.id.btnlogin);
	     btnCreateChannel=(Button)this.findViewById(R.id.btnCreateChannel);
	     btnLoginChannel=(Button)this.findViewById(R.id.btnLoginChannel);
	     btnCloseChannel=(Button)this.findViewById(R.id.btnCloseChannel);
	     btnKeepChannel=(Button)this.findViewById(R.id.btnKeepChannel);
	     btnstopservice=(Button)this.findViewById(R.id.btnstopservice);
	     btnfullystopservice=(Button)this.findViewById(R.id.btnfullystopservice);
	     //---------------------------------------------
	     MyLabeltextview = (TextView)findViewById(R.id.labeltitle);  
	     MyNameIDEditText=(EditText)findViewById(R.id.editText1);
	     MyPassWordEditText=(EditText)findViewById(R.id.editText2);
	    
	     //MyNameIDEditText.setText("一代天骄");
	     //MyPassWordEditText.setText("88888888");   
	        
	     //-----------------------------------------------
	     CloudBusServerIP="121.42.45.167";
	     CloudBusServerPort="9910";
	     //=================================================
	     
	     //LGJCloudBusChannelService.MeCallBackHandler=MyCallBackHandler;
	     
	     //--------广播注册------------------------------------------------------
	     
	     IntentFilter MyFilter = new IntentFilter(LGJCloudBusChannelService.NotifyAction); 
	     registerReceiver(MyBroadcastReceiver, MyFilter); 
	     
	     //------------------------------------------------------------------------
	     SharedPreferences setinfo =getSharedPreferences("cloudbusshareset", Activity.MODE_PRIVATE);
	     MyNameID=setinfo.getString("NameID", "");
	     MyPassWord=setinfo.getString("PassWordID", "");
	     
	     MyNameIDEditText.setText(MyNameID);
   	     MyPassWordEditText.setText(MyPassWord);
	     
	        
	      btnauthenlogin.setOnClickListener(new OnClickListener()
	       {        	
	          @Override       			
	          public void onClick(View view)
	          {
	        		//--------------------
	           	  MyLabeltextview.setText("wait...");
	        	  //String MyNameID, MyPassWord;
	        	  MyNameID=MyNameIDEditText.getText().toString();
	        	  MyPassWord=MyPassWordEditText.getText().toString();
	        	  
	        	  //LGJCloudBusChannelService.NameIDStr=MyNameID;
	        	  //LGJCloudBusChannelService.PassWordIDStr= MyPassWord;
	        	
	        	  //单独线程启动
	        	  SocketServerThread MySocketServerThread=  new SocketServerThread(MyCallBackHandler,MyNameID, MyPassWord);
	        	  MySocketServerThread.SetSocketPara(CloudBusServerIP, CloudBusServerPort);
	           	  MySocketServerThread.start();
	        	 //textview.setText(GlobalChannelIDStr);

	        	

	         }
			
	        });
	     
	      btnCreateChannel.setOnClickListener(new OnClickListener()
	        {        	
	          @Override       			
	          public void onClick(View view)
	          {
	        	
	        	  //if(LGJCloudBusChannelService.GlobalChannelIDStr==null)
	    		  //{
	        		//  MyLabeltextview.setText("PreLoginAuth...");
	    		    //  return;
	    		    
	    		  //}
	        	  MyLabeltextview.setText("wait...");
	        	  
	        	  /*
	        	  if(MyLGJNIOSocketBase==null)
	        	  {  
	        		  MyLGJNIOSocketBase=new LGJNIOSocketBase();
	        	      MyLGJNIOSocketBase.OpenConnect(MyCallBackHandler);
	        	  }
	        	  else
	        	  {
	        		  
	        		  MyLabeltextview.setText("Created!...");
	        	  }
                  */
	        	  
	        	   //LGJCloudBusChannelService.MeCallBackHandler=MyCallBackHandler;
	        	   Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	        
	        	   MyIntentToService.putExtra("ActionID", "create");
	        	   MyIntentToService.putExtra("ParaStr", GlobalChannelIDStr);	        	   
	        	   startService(MyIntentToService);
	          
	        	   //Bundle bundle = new Bundle();
	        	   //bundle.putSerializable("myhander",(Serializable) MyCallBackHandler);
	        	   //MyIntentToService.putExtras(bundle);
	        	   
	        	   //ChannelServiceBundle MyChannelServiceBundle=new ChannelServiceBundle();
	        	  // MyChannelServiceBundle.ActionID="create";
	        	   //MyChannelServiceBundle.MyUISynchHandler=MyCallBackHandler;
	        	   //MyIntentToService.putExtra("inoutPara", MyChannelServiceBundle);
	        	           	   
	        	 
	        	  
	        	  
	        	

	         }
			
	        });
	        btnCloseChannel.setOnClickListener(new OnClickListener()
	        {        	
	          @Override       			
	          public void onClick(View view)
	          {
	        	  if(LGJNIOSocketBase.CurrentLGJNIOSocketBase==null)
	    		  {
	    		    MyLabeltextview.setText("PreCtreate...");
	    		    return;
	    		    
	    		  }   	  
	        	 /*
	        	  if(MyLGJNIOSocketBase!=null)
	        	  {  
	        		  try
	        		  {
						MyLGJNIOSocketBase.CloseConnect();
						MyLGJNIOSocketBase=null;
						ChannelIDState=1; 
					    MyLabeltextview.setText("Closed...");
					  } 
	        		  catch (IOException e)
	        		  {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	  }
	        	  else
	        	  {
	        		  
	        		  MyLabeltextview.setText("Closed!...");
	        	  }
                 */
	        	  
	        	  Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
	  	        
	        	   MyIntentToService.putExtra("ActionID", "close");
	        	   MyIntentToService.putExtra("ParaStr", "1234567890");
	        	   //Bundle bundle = new Bundle();
	        	   //bundle.putSerializable("myhander",(Serializable) MyCallBackHandler);
	        	   //MyIntentToService.putExtras(bundle);
	        	 
	        	  startService(MyIntentToService);
	        	

	         }
			
	        });
	        
	        
	        
	        btnLoginChannel.setOnClickListener(new OnClickListener()
	        {        	
	          @Override       			
	          public void onClick(View view)
	          {
	        		
	        	  if( LGJCloudBusChannelService.GlobalChannelIDStr==null)
	    		  {
	        		  //MyLabeltextview.setText("PreLoginAuth...");
	    		      //return;
	    		    
	    		  }
	        	  if(LGJNIOSocketBase.CurrentLGJNIOSocketBase==null)
	    		  {
	    		    //MyLabeltextview.setText("PreCtreate...");
	    		    //return;
	    		    
	    		  }
	        	 
	        	  MyLabeltextview.setText("wait...");
	        	  GlobalChannelIDStr=LGJCloudBusChannelService.GlobalChannelIDStr;
	        	  //MyLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
	        	  //LGJNIOSocketBase.CurrentLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
	        	  Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
		  	        
	        	  MyIntentToService.putExtra("ActionID", "login");
	        	  MyIntentToService.putExtra("ParaStr",  GlobalChannelIDStr);	          
	        	
	        	  startService(MyIntentToService);
	        	  
	         }
			
	        });
	        
	        
	        btnKeepChannel.setOnClickListener(new OnClickListener()
	        {        	
	            @Override       			
	            public void onClick(View view)
	            {
	          				// TODO Auto-generated method stub
	          	  if( LGJCloudBusChannelService.GlobalChannelIDStr==null)
	      		  {
	      		    //MyLabeltextview.setText("PreLoginAuth...");
	      		    //return;
	      		    
	      		  }
	          	  
	          	  if(LGJNIOSocketBase.CurrentLGJNIOSocketBase==null)
	    		  {
	    		    //MyLabeltextview.setText("PreCtreate...");
	    		    //return;
	    		    
	    		  }
	          	  /*
	          	  if(ChannelIDState!=0)
	      		  {
	      		    //MyLabeltextview.setText("PreRegister...");
	      		    return;
	      		    
	      		  }
	      		  */
	          	  //MyLabeltextview.setText("wait...");
	          	  //new SocketServerThread(11,myHandler,GlobalChannelIDStr).start();
	          	 //textview.setText(GlobalChannelIDStr);
	          	 // MyLGJNIOSocketBase.KeepChannel(GlobalChannelIDStr);
	          	  
	          	  Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
		  	        
	        	  MyIntentToService.putExtra("ActionID", "keeplife");
	        	  MyIntentToService.putExtra("ParaStr", GlobalChannelIDStr);
	        	  startService(MyIntentToService);
	          	  

	          	

	           }
	  		
	          });
	        
	        //btnstopservice
	        btnstopservice.setOnClickListener(new OnClickListener()
	        {        	
	            @Override       			
	            public void onClick(View view)
	            {
	          		// TODO Auto-generated method stub 
	            	  //检查Service状态   		        
	    		    ActivityManager manager = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);   
	    		    for (RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE))
	    		    {  
	    		    	
	    		    	//LGJCloudBusChannelService
	    		    	//com.hnlylgj.sensor.MY_CHANNNEL_SERVIC
	    		      if("com.hnlylgj.sensor.LGJCloudBusChannelService".equals(service.service.getClassName()))  
	    	          {   
	    		     
	    		    	Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
	    		    	stopService(MyIntentToService);
	    	          }
	    		      if("com.hnlylgj.sensor.LGJCloudBusChannelService2".equals(service.service.getClassName()))  
	    	          {   
	    		     
	    		       	Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE2");
	    		    	stopService(MyIntentToService);
	    	          } 
	    		      /*
	    		      if("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE2".equals(service.service.getClassName()))  
	    	          {   
	    		     
	    		    	//isServiceRunning = true; 
	    		    	  Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE2");
	    		    	stopService(MyIntentToService);
	    	          } 
	    		      */
	    		      
	    		        
	    		     }   
	            	
	          	  //Intent MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
	          	  //MyIntentToService.putExtra("ActionID", "stop");
	        	  //startService(MyIntentToService);
	          	  
	          	  
	          	  //stopService(MyIntentToService);
	        	  MyLabeltextview.setText("stop Service...");

	          	

	           }
	  		
	          });
	        
	        //btnfullystopservice
	        btnfullystopservice.setOnClickListener(new OnClickListener()
	        {        	
	            @Override       			
	            public void onClick(View view)
	            {
	          	 // TODO Auto-generated method stub          	
	          	  
	          	  Intent MyIntentToService;//=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
	             
	          	  MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
	          	  MyIntentToService.putExtra("ActionID", "stop");
	        	  startService(MyIntentToService);	          	  
	          	  stopService(MyIntentToService);
	          	  
	          	  MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE2");
	          	  MyIntentToService.putExtra("ActionID", "stop");
	        	  startService(MyIntentToService);
	        	  stopService(MyIntentToService);
	          	  
	          	  
	        	  MyLabeltextview.setText("stop Service...");

	          	

	           }
	  		
	          });
	
	
	
	
	}
	
	
	//=======================================================
	
	@Override
	public void onDestroy() 
    {
		// TODO Auto-generated method stub
		 super.onDestroy();
		 unregisterReceiver(MyBroadcastReceiver); 
		//LGJCloudBusChannelService.MeCallBackHandler=null;
		 
		 SharedPreferences setinfo =getSharedPreferences("cloudbusshareset", Activity.MODE_PRIVATE);
   		 setinfo.edit()
         //.putString("GlobalChannelID",GlobalChannelIDStr)
   		  //LGJCloudBusChannelService.NameIDStr=MyNameID;
	      //LGJCloudBusChannelService.PassWordIDStr= MyPassWord;
         .putString("NameID",MyNameID)
         .putString("PassWordID",MyPassWord)
         .commit();
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_bus, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
