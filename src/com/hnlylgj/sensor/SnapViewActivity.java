package com.hnlylgj.sensor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.hnlylgj.sensor.R;
import com.hnlylgj.sensor.SocketServerThread;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class SnapViewActivity extends Activity 
{

private TextView MyLabeltextview;
	
	String GlobalChannelIDStr;
	String SnapID;
	String SnapType;//10、11、12、13	
	
	 ImageView myImageView;	 
	 Bitmap mySnapBmp;

	 String CloudBusServerIP,CloudBusServerPort;
	
	public Handler myHandler = new Handler()
	{  
	       @Override  
	       public void handleMessage(Message msg)
	      {  
	          if (msg.what == 0x13) 
	          {  
	             Bundle bundle = msg.getData();  
	             //MyLabeltextview.setText(""+bundle.getString("msg"));
	             DisplayImage(bundle);
	          }  
	          
	         
	       }  
	 
	   };  
	   
    protected void DisplayImage(Bundle InBundle)
	{
    
    	byte[] ReceiveImageBuffer;
    	ReceiveImageBuffer=InBundle.getByteArray("snapimage");
    	mySnapBmp=BitmapFactory.decodeByteArray(ReceiveImageBuffer , 0, ReceiveImageBuffer.length);
    	myImageView.setImageBitmap(mySnapBmp);
	
	}
    protected void DisplayHttpImage(String LockID,String SnapID)
   	{
       
       	//byte[] ReceiveImageBuffer;
       	//ReceiveImageBuffer=InBundle.getByteArray("snapimage");
       	//mySnapBmp=BitmapFactory.decodeByteArray(ReceiveImageBuffer , 0, ReceiveImageBuffer.length);
       	//myImageView.setImageBitmap(mySnapBmp);
    	//SharedPreferences setinfo =this.getSharedPreferences("cloudbusshare", Service.MODE_PRIVATE);
  	    //GlobalChannelIDStr=setinfo.getString("GlobalChannelID", null);  	    
    	String NewTileImagePath = "http://121.42.45.167:6680//GetImageHandler.ashx?LockID=" + LockID + "&SnapID=" + SnapID + "&SizeFlag=0";
    	//String UrlStr="http://blog.3gstdy.com/wp-content/themes/twentyten/images/headers/path.jpg";
    	FileInputStream fis;
		try 
		{
			fis = new FileInputStream(NewTileImagePath);
			mySnapBmp= BitmapFactory.decodeStream(fis);
			myImageView.setImageBitmap(mySnapBmp);
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
   	
   	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snap_view);
		
		 CloudBusServerIP="121.42.45.167";
	     CloudBusServerPort="9910";
		
		MyLabeltextview = (TextView)findViewById(R.id.labeltitle);
		myImageView = (ImageView) findViewById(R.id.imageviewFisrt);
		 
		Intent MySnapResutintent;
		MySnapResutintent=getIntent();
		SnapID=MySnapResutintent.getStringExtra("SnapID");
		GlobalChannelIDStr=MySnapResutintent.getStringExtra("GlobalChannelID");
		SnapType=MySnapResutintent.getStringExtra("SnapType");
		
		SetMainTitle(SnapType); 		
		//MyLabeltextview.setText(""+SnapID);
		MyLabeltextview.setText(""+SnapID);
		
	
		if(SnapID==null)return;
		if(GlobalChannelIDStr==null)
			{
			  //LGJCloudBusChannelService.GlobalChannelIDStr
			 // MyLabeltextview.setText("ChannelID:nulll,"+LGJCloudBusChannelService.GlobalChannelIDStr+":"+SnapID);
			  //return;
			SharedPreferences setinfo =this.getSharedPreferences("cloudbusshare", Service.MODE_PRIVATE);
	  	    GlobalChannelIDStr=setinfo.getString("GlobalChannelID", null);
		}
		  //new SocketServerThread(14,myHandler,GlobalChannelIDStr,SnapID).start();
		 
		  //----第一种方法Tcp读取-----------------------------------------------
		  SocketServerThread MySocketServerThread=  new SocketServerThread(14,myHandler,GlobalChannelIDStr,SnapID);
    	  MySocketServerThread.SetSocketPara(CloudBusServerIP, CloudBusServerPort);
    	  MySocketServerThread.start();
		
		//----第二种方法Http读取-----------------------------------------
		//String LockID="000000000000000";
		//if(GlobalChannelIDStr!=null)
		//{
		//	LockID=GlobalChannelIDStr.substring(0,15);
		//	DisplayHttpImage(LockID,SnapID);
		//}
		
		
		
		
	}
	 protected void SetMainTitle(String SnapType) 
	  {
		  if(SnapType==null)return;
		   if(SnapType.equalsIgnoreCase("mailsnap"))
	       { 
			this.setTitle("收件抓拍通知");   
			return;
	       }
		   if(SnapType.equalsIgnoreCase("exceptsnap"))
	       { 
			   this.setTitle("异常抓拍通知");
			   return;
	       }
		   if(SnapType.equalsIgnoreCase("remotesnap"))
	       { 
			   this.setTitle("远程抓拍结果");  
			   return;
	       }
		   if(SnapType.equalsIgnoreCase("xxxxxx"))
	       { 
			   
	       }
		 
		 
	   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snap_view, menu);
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
