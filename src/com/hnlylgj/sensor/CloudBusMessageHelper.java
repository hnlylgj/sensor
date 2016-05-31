package com.hnlylgj.sensor;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class CloudBusMessageHelper
{
	
	
	public static void ResponseMessage(Context context,TextView MyLabeltextview,String ReceiveMessageStr) 
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
	      //------------------------------------------------------------------
	       MyLabeltextview.setText(MessageTypeIDStr+":"+MessageResultStr);
	       
	     
	       String GlobalChannelIDStr=LGJCloudBusChannelService.GlobalChannelIDStr;	
	       if(MessageTypeIDStr.equalsIgnoreCase("remotesnap"))
	       { 
	    	   if(ReceiveMessageStr.indexOf("true")>-1)
				{
	    		   StartIndex=ReceiveMessageStr.indexOf("[")+1;
	    	       EndIndex=ReceiveMessageStr.indexOf(",");
	    	       String SnapID=ReceiveMessageStr.substring(StartIndex, EndIndex);	
	    	       
	    	       Intent MySnapResutintent;
	    	       MySnapResutintent=new Intent(context,SnapViewActivity.class);
	    	       MySnapResutintent.putExtra("GlobalChannelID", GlobalChannelIDStr);
		           MySnapResutintent.putExtra("SnapID", SnapID);
		           MySnapResutintent.putExtra("SnapType",  MessageTypeIDStr);
		           context.startActivity(MySnapResutintent);  
	    	    	
				}	    	   
	    	   return;   
          }
	       
	     
		   
		   
	   
	   }
	
	
	   public static void LocalStatusMessage(Context context,TextView MyLabeltextview,String ReceiveMessageStr) 
	   {
		
		   MyLabeltextview.setText(ReceiveMessageStr);
	   }
	   

}
