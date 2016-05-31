package com.hnlylgj.sensor;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SensorMainActivity extends Activity 
{

	private String GlobalChannelIDStr;	
	private TextView MyLabeltextview;
	private Intent MySnapResutintent;
	
	 ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>(); 
	 
	 
	 //====================================================    
	    public Handler MyCallBackHandler = new Handler()
	 	{  
	 	       @Override  
	 	       public void handleMessage(Message msg)
	 	      {  
	 	    	   //--ͬ������-------
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
	 	          //-------�첽����----------------------------------------
	 	          if (msg.what == 0x50) //��������/�ر�����
	 	          {  
	 	             //Bundle bundle = msg.getData();  
	 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	 	          }  
	 	           
	 	          if (msg.what == 0x51) //��Ϣ��Ӧ
	 	          {  
	 	             Bundle bundle = msg.getData();  
	 	             ResponseMessage(bundle.getString("msg")); 
	 	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	 	          }  
	 	          
	 	          if (msg.what == 0x52) //��Ϣ����
	 	          {  
	 	             //Bundle bundle = msg.getData();  
	 	             //PushMessage(bundle.getString("msg")); 
	 	             	             
	 	            
	 	          }  
	 	        
	 	          if (msg.what == 0x53) //��ʱ����������
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
		 setContentView(R.layout.activity_sensor_main);
		
		  MyLabeltextview = (TextView)findViewById(R.id.labeltitle);  
		 //-----------------------------------------------------------
	      IntentFilter MyFilter = new IntentFilter(LGJCloudBusChannelService.NotifyAction); 
	      registerReceiver(MyBroadcastReceiver, MyFilter); 	  	        
        //--------------------------------------------------------------	
		
	   
	   //��List����������
	   //ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();  
	   String Str=getString(R.string.app_name);	   
	   InitListData();
	    /*
	    for(int i = 0;i < 12;i++)  
	    {  
		     HashMap<String, Object> map = new HashMap<String, Object>();  
		     map.put("ItemImage", R.drawable.dooropen);  
		     map.put("ItemText", "Զ�̿���");  
		     meumList.add(map);  
		
	    
	    
	    }  
		 */
		  //-----------------------------------------------------------------
		   SimpleAdapter saItem = new SimpleAdapter(this,
		    		     meumList, //����Դ  
		                 R.layout.item, //xmlʵ��  
		                 new String[]{"ItemImage","ItemText"}, //��Ӧmap��Key  
		                 new int[]{R.id.Image_item,R.id.Text_item});  //��ӦR��Id  
		  
		   //------------------------------------------------------------------
		  
		   //��ʼ������ gridview 
		    GridView gridview = (GridView) findViewById(R.id.MainActivityGrid); 		        
		    gridview.setAdapter(saItem);  
		   //--------------------------��ӵ���¼�---------------------------  		            
		   gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		     {
		           @Override  
		        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)  
		          {  
		        	   Intent MyIntentToService;
		             switch(arg2)
		               {
		                 case 0:
		                	////int index=arg2+1;//id�Ǵ�0��ʼ�ģ�������Ҫ+1  
			                //Toast.makeText(getApplicationContext(), "�㰴����ѡ�"+arg2, 0).show();  
		                	 MyIntentToService=GetRunningService() ;//new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
		               	     MyIntentToService.putExtra("ActionID", "remoteopen");
		               	     MyIntentToService.putExtra("ParaStr", "1234567890");  
		                     startService(MyIntentToService);       
		                	 break;
		           		  case 1:
		                	//int index=arg2+1;//id�Ǵ�0��ʼ�ģ�������Ҫ+1  
			                 //Toast.makeText(getApplicationContext(), "�㰴����ѡ�"+arg2, 0).show();  
			                 //Toast�������û���ʾһЩ����/��ʾ  
		           			 MyIntentToService=GetRunningService() ;//new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
		               	     MyIntentToService.putExtra("ActionID", "remotesnap");
		               	     MyIntentToService.putExtra("ParaStr", "1234567890");	
		               	     startService(MyIntentToService);
		               	     
		                    break;
		                		
		           		 case 2:
			                	//int index=arg2+1;//id�Ǵ�0��ʼ�ģ�������Ҫ+1  
				                
			               	     
			                    break; 
			                    
		           		 case 3:
		           			 MyIntentToService=GetRunningService() ;//new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
		               	     MyIntentToService.putExtra("ActionID", "getpower");
		               	     MyIntentToService.putExtra("ParaStr", "1234567890");	
		               	     startService(MyIntentToService);
			               	     
			                    break; 
			                    
			                    
		           		 case 4:
			                	
			               	     
			                    break;
			                    
			                    
		           		 case 5:
			                	
			               	     
			                    break;
			                    
		           		 case 6:
			                	
		               	     
			                    break;
			                    
		           		 case 7:
			                	
		               	     
			                    break;
			                    
			                    
		           		 case 8:
		           			 MyIntentToService=GetRunningService() ;//new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");	  	        
		               	     MyIntentToService.putExtra("ActionID", "tempkey");
		               	     MyIntentToService.putExtra("ParaStr", "1234567890");	
		               	     startService(MyIntentToService);	
		               	     
			                    break;
			                    
		           		 case 9:
		           		 	 Intent MyLoginBusIntent=new Intent(getApplicationContext(),LoginBusActivity.class);//TestActivity--LoginBusActivity
		        			 startActivity(MyLoginBusIntent); 
		               	     
			                    break;
			                    
		           		 case 10:
			                	
		               	     
			                    break;
			                    
		           		 case 11:
			                	
		               	     
			                    break;
			                    
			                    
		           		 case 12:
			                	
		               	     
			                    break;
			                    
		                		 }
		                          
		                     }  
		              });  
                        
		
		
		
		
		
	}
	
  private Intent GetRunningService()  
  {
	    String RunningServiceStr=null;
	    Intent MyIntentToService=null;
	    ActivityManager manager = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);   
	    for (RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE))
	    {  
	    	 RunningServiceStr=service.service.getClassName();
	    	 if("com.hnlylgj.sensor.LGJCloudBusChannelService".equals(RunningServiceStr))  
	         {   
	 	     
	 	    	 MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE");
	 	    	 break;	 	    	
	 	    	
	         }
	 	    if("com.hnlylgj.sensor.LGJCloudBusChannelService2".equals(RunningServiceStr))  
	         {   
	 	     
	 	       	 MyIntentToService=new Intent("com.hnlylgj.sensor.MY_CHANNNEL_SERVICE2");
	 	         break;	
	         } 
	    }
	    	
	   
	   
	    return  MyIntentToService;
	      
	      
  }
	public void InitListData() 
	{
		 //��List����������
		   //ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();  
		   String Str=getString(R.string.app_name);
		   
		   HashMap<String, Object> map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.dooropen);  
		   map.put("ItemText", "Զ�̿���");  
		   meumList.add(map); 
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.snapimage);  
		   map.put("ItemText", "Զ������");  
		   meumList.add(map);  
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.sound);  
		   map.put("ItemText", "�����Խ�");  
		   meumList.add(map);  
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.battery);  
		   map.put("ItemText", "��ص���");  
		   meumList.add(map);  
		   
		   
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.dynamic);  
		   map.put("ItemText", "��̬��Ϣ");  
		   meumList.add(map);  
		   
		 		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.locksttus);  
		   map.put("ItemText", "��̬��Ϣ");  
		   meumList.add(map);  
		   
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.lockinfor);  
		   map.put("ItemText", "������Կ");  
		   meumList.add(map);  
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.lockinfor);  
		   map.put("ItemText", "ɾ����Կ");  
		   meumList.add(map);  
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.lockinfor);  
		   map.put("ItemText", "��ʱ��Կ");  
		   meumList.add(map); 
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.account);  
		   map.put("ItemText", "�˺�����");  
		   meumList.add(map);  
		   
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.rootkey);  
		   map.put("ItemText", "ĸ������");  
		   meumList.add(map);  
		   
		   map = new HashMap<String, Object>();  
		   map.put("ItemImage", R.drawable.timersync);  
		   map.put("ItemText", "ʱ��ͬ��");  
		   meumList.add(map);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor_main, menu);
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
