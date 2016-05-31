package com.hnlylgj.sensor;

import com.hnlylgj.LGJNIOSocketAPI.LGJNIOSocketBase;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestmainActivity extends Activity implements OnClickListener
{
	
	private String GlobalChannelIDStr;	
	//LGJNIOSocketBase MyLGJNIOSocketBase;
	private int ChannelIDState;
	//------------------------------------------
	private TextView MyLabeltextview;
    //private  Button btnsynchtime;	
	private  Button btnremoteopen;
	private  Button btnremotesnap;	
	private  Button btngetpower;	
	private  Button btntempkey;	
	private  Button btnaccountset;
	
	private Intent MySnapResutintent;
	
	
	 //====================================================    
    public Handler MyCallBackHandler = new Handler()
 	{  
 	       @Override  
 	       public void handleMessage(Message msg)
 	      {  
 	    	   //--同步操作-------
 	          if (msg.what == 0x10) 
 	          {  
 	             //Bundle bundle = msg.getData();  
 	             //textview.append("ChannelID:"+bundle.getString("msg")+"\n");  
 	             //GlobalChannelIDStr=bundle.getString("msg");
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
 	             //MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
 	            // MySnapResutintent.putExtra("SnapID", bundle.getString("msg"));
 	             
 	            // startActivity( MySnapResutintent);      
 	             
 	             
 	          }  
 	          //-------异步操作----------------------------------------
 	          if (msg.what == 0x50) //建立连接/关闭连接
 	          {  
 	             //Bundle bundle = msg.getData();  
 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
 	          }  
 	           
 	          if (msg.what == 0x51) //消息响应
 	          {  
 	             Bundle bundle = msg.getData();  
 	             ResponseMessage(bundle.getString("msg")); 
 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
 	          }  
 	          
 	          if (msg.what == 0x52) //消息推送
 	          {  
 	             //Bundle bundle = msg.getData();  
 	             //PushMessage(bundle.getString("msg")); 
 	             	             
 	            
 	          }  
 	        
 	          if (msg.what == 0x53) //定时器心跳命令
 	          {  
 	        	  ///btnKeepChannel.callOnClick();	             	             
 	            
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
	       
	       	       
	       if(MessageTypeIDStr.equalsIgnoreCase("remotesnap"))
	       { 
	    	   if(ReceiveMessageStr.indexOf("true")>-1)
				{
	    		   StartIndex=ReceiveMessageStr.indexOf("[")+1;
	    	       EndIndex=ReceiveMessageStr.indexOf(",");
	    	       String SnapID=ReceiveMessageStr.substring(StartIndex, EndIndex);	
	    	       GlobalChannelIDStr=LGJCloudBusChannelService.GlobalChannelIDStr;
	    	       MySnapResutintent=new Intent(this,SnapViewActivity.class);
	    	       MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
		           MySnapResutintent.putExtra("SnapID", SnapID);
		           MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);
		           startActivity(MySnapResutintent);  
	    	    	
				}	    	   
	    	   return;   
       }
	       
	     
		   
		   
	   
	   }
	
	   BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver()
	   { 
		  
		         @Override 
		         public void onReceive(Context context, Intent intent) 
		         { 
		             // TODO Auto-generated method stub 
		            //textView.setText(intent.getExtras().getString("data"));
		        	// Message msg=intent.get
		        	 //Bundle Mybundle = intent.getBundleExtra("mybundle"); 
		             //MyLabeltextview.setText(""+Mybundle.getString("msg"));		            
		            //ResponseMessage(Mybundle.getString("msg")); 
		        	 
		        	 Bundle bundle = intent.getBundleExtra("mybundle"); 
		        	 int Mywhat=intent.getIntExtra("mywhat",0x0);
		        	 Message msg = new Message();  
		 	         msg.what = Mywhat;  
		 	         msg.setData(bundle);  
		 	         MyCallBackHandler.sendMessage(msg);
		        	 
		        	 
		            
		         } 
		    }; 
	   
	   
	   
	  //=============BroadcastReceiver End==================================================== 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testmain);
		
	  	   
		    //ChannelIDState=1;	        
	        MyLabeltextview = (TextView)findViewById(R.id.labeltitle);  
		   
	        btnremoteopen=(Button)this.findViewById(R.id.btnremoteopen);
	        btngetpower=(Button)this.findViewById(R.id.btngetpower);
	        btnremotesnap=(Button)this.findViewById(R.id.btnremotesnap); 
	        btntempkey=(Button)this.findViewById(R.id.btntempkey);
	        
	        btnaccountset=(Button)this.findViewById(R.id.btnaccountset);
	        
	        
	        //----------------------------------------------------------------
	        btnremoteopen.setOnClickListener(this);
	        btngetpower.setOnClickListener(this);
	        btnremotesnap.setOnClickListener(this);
	        btntempkey.setOnClickListener(this);
	        btnaccountset.setOnClickListener(this);
	        
	        
	        
	        //----------------------------------------------------------------
		     IntentFilter MyFilter = new IntentFilter(LGJCloudBusChannelService.NotifyAction); 
		     registerReceiver(MyBroadcastReceiver, MyFilter); 
		  	        
	        //--------------------------------------------------------------	        
	        /*        
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
	    
	     btntempkey.setOnClickListener(new OnClickListener()
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
	        	  //MyLGJNIOSocketBase.TempKey(GlobalChannelIDStr,"888888");
	        	
	         }
			
	        });
	     
	        btnaccountset.setOnClickListener(new OnClickListener()
	        {        	
	          @Override       			
	          public void onClick(View view)
	          {
	        	// TODO Auto-generated method stub
	        	 
	        	    //MySnapResutintent=new Intent(this,SnapViewActivity.class);
	        	    Intent Myintent=new Intent();
	        	    //Myintent.setClassName(LoginBusActivity.class);
	        	    //Myintent.setClassName(this, getLocalClassName());
	    	       //MySnapResutintent.removeExtra("SnapID");
	  			   //MySnapResutintent.removeExtra("SnapType");
	  			   //MySnapResutintent.removeExtra("GlobalChannelID");
	    	       //MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
		           //MySnapResutintent.putExtra("SnapID", SnapID);
		           //MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);
		           
		           startActivity(Myintent);  	
	         }
			
	        });
		*/
		
		
	}

	
	public void onClick(View view)
	{

		// TODO Auto-generated method stub
		 //MyLGJNIOSocketBase=LGJNIOSocketBase.CurrentLGJNIOSocketBase;
		 Log.d("set:","pop.......");
		 Intent MyIntentToService;
		switch (view.getId()) 
		{
		case R.id.btnaccountset:
			 Log.d("set:","accountset...");
			 MySnapResutintent=new Intent(this,LoginBusActivity.class);//TestActivity--LoginBusActivity
			 TestmainActivity.this.startActivity(MySnapResutintent);  	
			break;
		case R.id.btnremoteopen:
			//Log.d(TAG, "pause.......");
			 MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
       	     MyIntentToService.putExtra("ActionID", "remoteopen");
       	     MyIntentToService.putExtra("ParaStr", "1234567890");  
             startService(MyIntentToService);
			
			
			break;
		case R.id.btnremotesnap:
			//Log.d(TAG, "stop.......");
			 MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
       	     MyIntentToService.putExtra("ActionID", "remotesnap");
       	     MyIntentToService.putExtra("ParaStr", "1234567890");	
       	     startService(MyIntentToService);
			break;
		case R.id.btngetpower:
			//Log.d(TAG, "close.......");
			 MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
       	     MyIntentToService.putExtra("ActionID", "getpower");
       	     MyIntentToService.putExtra("ParaStr", "1234567890");	
       	     startService(MyIntentToService);
       	     
			break;
		case R.id.btntempkey:
			//Log.d(TAG, "exit.......");
			 MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
       	     MyIntentToService.putExtra("ActionID", "tempkey");
       	     MyIntentToService.putExtra("ParaStr", "1234567890");	
       	     startService(MyIntentToService);
	
		}

	}
	
	@Override
	public void onDestroy() 
    {
		// TODO Auto-generated method stub
		 super.onDestroy();
		 unregisterReceiver(MyBroadcastReceiver); 
		//LGJCloudBusChannelService.MeCallBackHandler=null;
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.testmain, menu);
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
