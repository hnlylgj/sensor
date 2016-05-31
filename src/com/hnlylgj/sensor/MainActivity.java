package com.hnlylgj.sensor;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.hnlylgj.sensor.SnapViewActivity;
import com.hnlylgj.sensor.R;
import com.hnlylgj.sensor.SocketServerThread;

import com.hnlylgj.LGJNIOSocketAPI.LGJNIOSocketBase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
{

	public String GlobalChannelIDStr;	
	LGJNIOSocketBase MyLGJNIOSocketBase;
	private int ChannelIDState;
	
    private static final int NOTIFICATION_ID = 1;

	
	private TextView MyLabeltextview;
	private EditText MyNameIDEditText;
	private EditText MyPassWordEditText;
	
	
	private  Button btnlogin;
	private  Button btnCreateChannel;
	private  Button btnCloseChannel;
	private  Button btnLoginChannel;
	private  Button btnKeepChannel;
	
	
	private  Button btnsynchtime;
	
	private  Button btnremoteopen;
	private  Button btnremotesnap;	
	private  Button btngetpower;
	
	private  Button btntestcreate;
	private  Button btntestclose;
		
	private Intent MySnapResutintent;
	
	private Timer mTimer;
    private TimerTask mTimerTask;
    
    String CloudBusServerIP,CloudBusServerPort;

	
	public Handler myHandler = new Handler()
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
	          if (msg.what == 0x50) //建立连接
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
	        	  btnKeepChannel.callOnClick();	             	             
	            
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
	 
	   
	   public void showNotification(int icon,String tickertext,String title,String content)
	   {

		  
	  

	    }
	   
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //---------------------------------------
        //MyLGJNIOSocketBase=new LGJNIOSocketBase();
        ChannelIDState=1;
        
        MyLabeltextview = (TextView)findViewById(R.id.labeltitle);         
        MyNameIDEditText=(EditText)findViewById(R.id.editText1);
        MyPassWordEditText=(EditText)findViewById(R.id.editText2);
        
        btnlogin=(Button)this.findViewById(R.id.btnlogin);
        //btnsynchtime=(Button)this.findViewById(R.id.btnsynchtime);
        btnremoteopen=(Button)this.findViewById(R.id.btnremoteopen);
        btngetpower=(Button)this.findViewById(R.id.btngetpower);
        btnremotesnap=(Button)this.findViewById(R.id.btnremotesnap);
        
        btnCreateChannel=(Button)this.findViewById(R.id.btnCreateChannel);
        btnLoginChannel=(Button)this.findViewById(R.id.btnLoginChannel);
        btnCloseChannel=(Button)this.findViewById(R.id.btnCloseChannel);
        btnKeepChannel=(Button)this.findViewById(R.id.btnKeepChannel);
        
        
        btntestcreate=(Button)this.findViewById(R.id.btntestcreate);
        btntestclose=(Button)this.findViewById(R.id.btntestclose);
                
       // MySnapResutintent=new Intent(this,FuncActivity.class);
        //--------------------------------------------
        //textview.setTextColor(Color.RED);
    	//textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    	//textview.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
     
        //---------------------------------------------
        MyNameIDEditText.setText("一代天骄");
        MyPassWordEditText.setText("88888888");   
        
        //-------------------------------------------------
        CloudBusServerIP="121.42.45.167";
        CloudBusServerPort="9910";
        
        //-----设置定时维持通道命令----------------------
        mTimer = new Timer();
        mTimerTask = new TimerTask() 
        {
            @Override
            public void run()
            {
                Log.d("AndroidTimerDemo", "timer");
                //Calendar cal = Calendar.getInstance();
                //mButton.setText(cal.toString());                
                Message msg = new Message();  
                msg.what = 0x53;  
                Bundle bundle = new Bundle();  
                bundle.clear();     	  		  
       	  	
       	  	    bundle.putString("msg", "keepalive");  
                msg.setData(bundle);  
                //发送消息 修改UI线程中的组件  
                myHandler.sendMessage(msg); 
                //btnKeepChannel.callOnClick();
            }
        };

        mTimer.schedule(mTimerTask,10000 , 300000);
        //------------------------------------------------------------------- 
        btnlogin.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        	//setTitle("登陆按钮 被点击了");  
        	//textview.setText("登陆按钮 被点击");
        	//Log.i("widgetDemo", "登陆按钮 被用户点击了。");
        	// startActivity(Myintent);
        	//--------------------
        	//  Authtication();
        	  MyLabeltextview.setText("wait...");
        	  String MyNameID, MyPassWord;
        	  MyNameID=MyNameIDEditText.getText().toString();
        	  MyPassWord=MyPassWordEditText.getText().toString();
        	  SocketServerThread MySocketServerThread=  new SocketServerThread(myHandler,MyNameID, MyPassWord);
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
        	
        	  if(GlobalChannelIDStr==null)
    		  {
        		  MyLabeltextview.setText("PreLoginAuth...");
    		    return;
    		    
    		  }
        	  MyLabeltextview.setText("wait...");
        	  if(MyLGJNIOSocketBase==null)
        	  {  
        		  MyLGJNIOSocketBase=new LGJNIOSocketBase();
        	      MyLGJNIOSocketBase.OpenConnect(myHandler);
        	  }
        	  else
        	  {
        		  
        		  MyLabeltextview.setText("Created!...");
        	  }

        	

         }
		
        });
        btnCloseChannel.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        	  if(MyLGJNIOSocketBase==null)
    		  {
    		    MyLabeltextview.setText("PreCtreate...");
    		    return;
    		    
    		  }   	  
        	 
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
        		  
        		  MyLabeltextview.setText("Created!...");
        	  }

        	

         }
		
        });
        
        
        
        btnLoginChannel.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        		
        	  if(GlobalChannelIDStr==null)
    		  {
        		  MyLabeltextview.setText("PreLoginAuth...");
    		    return;
    		    
    		  }
        	  if(MyLGJNIOSocketBase==null)
    		  {
    		    MyLabeltextview.setText("PreCtreate...");
    		    return;
    		    
    		  }
        	 
        	  MyLabeltextview.setText("wait...");
        	  MyLGJNIOSocketBase.LoginChannel(GlobalChannelIDStr);
        	  

         }
		
        });
        
        
        btnKeepChannel.setOnClickListener(new OnClickListener()
        {        	
            @Override       			
            public void onClick(View view)
            {
          				// TODO Auto-generated method stub
          	  if(GlobalChannelIDStr==null)
      		  {
      		    //MyLabeltextview.setText("PreLoginAuth...");
      		    return;
      		    
      		  }
          	  if(MyLGJNIOSocketBase==null)
      		  {
      		    //MyLabeltextview.setText("PreCtreate...");
      		    return;
      		    
      		  }
          	  if(ChannelIDState!=0)
      		  {
      		    //MyLabeltextview.setText("PreRegister...");
      		    return;
      		    
      		  }
          	  //MyLabeltextview.setText("wait...");
          	  //new SocketServerThread(11,myHandler,GlobalChannelIDStr).start();
          	 //textview.setText(GlobalChannelIDStr);
          	  MyLGJNIOSocketBase.KeepChannel(GlobalChannelIDStr);
          	  

          	

           }
  		
          });
        btnremoteopen.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        				// TODO Auto-generated method stub
        	  if(GlobalChannelIDStr==null)
    		  {
        		  MyLabeltextview.setText("PreLoginAuth...");
    		    return;
    		    
    		  }
        	  if(MyLGJNIOSocketBase==null)
    		  {
    		    MyLabeltextview.setText("PreCtreate...");
    		    return;
    		    
    		  }
        	  if(ChannelIDState!=0)
    		  {
    		    MyLabeltextview.setText("PreRegister...");
    		    return;
    		    
    		  }
        	  MyLabeltextview.setText("wait...");
        	  //new SocketServerThread(11,myHandler,GlobalChannelIDStr).start();
        	 //textview.setText(GlobalChannelIDStr);
        	  //MyLGJNIOSocketBase.SynchTime(GlobalChannelIDStr);
        	  MyLGJNIOSocketBase.RemoteOpen(GlobalChannelIDStr);

        	

         }
		
        });
        
        btngetpower.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        				// TODO Auto-generated method stub
        	  if(GlobalChannelIDStr==null)
        		  {
        		  MyLabeltextview.setText("PreLoginAuth...");
        		    return;
        		    
        		  }
        	  if(MyLGJNIOSocketBase==null)
    		  {
    		    MyLabeltextview.setText("PreCtreate...");
    		    return;
    		    
    		  }
        	  if(ChannelIDState!=0)
    		  {
    		    MyLabeltextview.setText("PreRegister...");
    		    return;
    		    
    		  }
        	  MyLabeltextview.setText("wait...");
        	  //new SocketServerThread(12,myHandler,GlobalChannelIDStr).start();
        	  MyLGJNIOSocketBase.GetPower(GlobalChannelIDStr);

        	

         }
		
        });
        
        btnremotesnap.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        				// TODO Auto-generated method stub
        	  if(GlobalChannelIDStr==null)
    		  {
        		MyLabeltextview.setText("PreLoginAuth...");
    		    return;
    		    
    		  }
        	  if(MyLGJNIOSocketBase==null)
    		  {
    		    MyLabeltextview.setText("PreCtreate...");
    		    return;
    		    
    		  }
        	  if(ChannelIDState!=0)
    		  {
    		    MyLabeltextview.setText("PreRegister...");
    		    return;
    		    
    		  }
        	  MyLabeltextview.setText("wait...");
        	  //new SocketServerThread(13,myHandler,GlobalChannelIDStr).start();
        	  MyLGJNIOSocketBase.RemoteSnap(GlobalChannelIDStr);
        	

        	

         }
		
        });
        
        btntestcreate.setOnClickListener(new OnClickListener()
        {        	
          @Override       			
          public void onClick(View view)
          {
        				// TODO Auto-generated method stub
              startService(new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE"));

  			//finish();

        	

         }
		
        });
        
    }

    
    @Override
    protected void onDestroy()
    {
    	try 
    	 {
    		if(MyLGJNIOSocketBase!=null)
    		{
			MyLGJNIOSocketBase.CloseConnect();
			GlobalChannelIDStr=null;	
		    ChannelIDState=1;;
    	}
		} 
    	 catch (IOException e) 
    	 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	 super.onDestroy();
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
