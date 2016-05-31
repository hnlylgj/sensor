package com.hnlylgj.LGJNIOSocketAPI;

import android.os.Handler;
import android.os.Parcelable;
import android.os.Parcel; 

public class ChannelServiceBundle implements Parcelable 
{
	
	public String ActionID;  
	public Handler MyUISynchHandler;
	
	public  ChannelServiceBundle()
	{
		
	}
	 public ChannelServiceBundle(Parcel in)
	 {  
	     //顺序要和writeToParcel写的顺序一样  
		 ActionID = in.readString();  
		 MyUISynchHandler = (Handler) in.readValue(Handler.class.getClassLoader());  
		   
	 }  
	 @Override
	public int describeContents()
	{
			// TODO Auto-generated method stub
			return 0;

	}

		
	@Override
   	public void writeToParcel(Parcel dest, int flags)
	    {
		
		// TODO Auto-generated method stub
		   dest.writeString(ActionID); 
		   dest.writeValue(MyUISynchHandler);
		
		   

		}
		

		
	public static final Parcelable.Creator<ChannelServiceBundle> CREATOR = new Parcelable.Creator<ChannelServiceBundle>() 
	{
		   @Override  
			public ChannelServiceBundle createFromParcel(Parcel source) 
	       {
			 
	            //return new ChannelServiceBundle(in);
			   ChannelServiceBundle MyChannelServiceBundle = new ChannelServiceBundle();   
			   MyChannelServiceBundle.MyUISynchHandler=(Handler) source.readValue(Handler.class.getClassLoader());//.readHashMap(HashMap.class.getClassLoader());   
			   MyChannelServiceBundle.ActionID=source.readString();   
	           return MyChannelServiceBundle;   

			
	        }
			
		
		 @Override  
		public ChannelServiceBundle[] newArray(int size) 
		{
			
	         //return new ChannelServiceBundle[size];
	         return null;  
		
		}

	};
	

}
