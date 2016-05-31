package com.hnlylgj.sensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hnlylgj.SynchSocketBaseLib.LoginAuthtication;
import com.hnlylgj.SynchSocketBaseLib.OpenDataService;
import com.hnlylgj.SynchSocketBaseLib.SmartBusServiceLib;


//Local
public class SocketServerThread extends Thread 
{
	Integer MesActionID;
	Handler myHandler; 
	String GlobalChannelIDStr;
	String SnapID;
	String MyNameID, MyPassWord;
	String CloudBusServerIP,CloudBusServerPort;
	
	
	
	public SocketServerThread (Handler InHandler,String InMyNameID,String InMyPassWord) 
	{
		MesActionID=10;//login
		myHandler=InHandler;
		MyNameID=InMyNameID;
		MyPassWord=InMyPassWord;
	}
	public SocketServerThread (Integer InMesActionID,Handler InHandler,String InChannelIDStr) 
	{
		MesActionID=InMesActionID;
		myHandler=InHandler;
		GlobalChannelIDStr=InChannelIDStr;
	}
	
	public SocketServerThread (Integer InMesActionID,Handler InHandler,String InChannelIDStr,String InSnapID) 
	{
		MesActionID=InMesActionID;
		myHandler=InHandler;
		GlobalChannelIDStr=InChannelIDStr;
		SnapID=InSnapID;
		
	}
	public void SetSocketPara (String InIPStr,String InPortStr) 
	{
		CloudBusServerIP=InIPStr;
		CloudBusServerPort=InPortStr;
	}
	
	public void run() 
	{
		switch(MesActionID)
		{
		case 10:
			ActionAuthtication();
					
			break;
		case 11:
			ActionSynchTime();
			break;
		case 12:
			ActionGetPower(); 
			break;
		
		case 13:
			ActionRemoteSnap(); 
			break;
		case 14:
			ActionGetSanpImage(); 
			break;
		
		}
		
		
	  	  
    }
	
	private  void ActionAuthtication() 
	{
		 GlobalChannelIDStr=null;
		 Message msg = new Message();  
         msg.what = 0x10;  
         Bundle bundle = new Bundle();  
         bundle.clear();  

	  	  //Authentication();	
	      MobileLogin();	
	  	  if(GlobalChannelIDStr==null)GlobalChannelIDStr="Socket read/write Error!";
	  	  bundle.putString("msg", GlobalChannelIDStr);  
          msg.setData(bundle);  
         //发送消息 修改UI线程中的组件  
          myHandler.sendMessage(msg);  
	
	}

	private  void ActionSynchTime() 
	{
		 String ResultStr="同步时间失败";
		 Message msg = new Message();  
         msg.what = 0x11;  
         Bundle bundle = new Bundle();  
         bundle.clear();  

         if(SynchTime(GlobalChannelIDStr)==0)ResultStr="同步时间成功";	  	  
	     bundle.putString("msg", ResultStr);  
         msg.setData(bundle);  
         //发送消息 修改UI线程中的组件  
          myHandler.sendMessage(msg);  
	
	}
	
	private  void ActionGetPower() 
	{
		 String ResultStr="查询电量：失败";
		 Message msg = new Message();  
         msg.what = 0x11;  
         Bundle bundle = new Bundle();  
         bundle.clear();  
         SmartBusServiceLib MySmartBusServiceLib=GetPower(GlobalChannelIDStr);
         if(MySmartBusServiceLib.ReturnCode==0)
          {
        	  if(MySmartBusServiceLib.PowerValue==0)
        	  {
        		  ResultStr="查询电量：正常";
        	  }
        	  else
        	  {
        		  
        		  ResultStr="查询电量：不足";
        	  }
        	 
        	 
         }
             
	      bundle.putString("msg", ResultStr);  
          msg.setData(bundle);  
         //发送消息 修改UI线程中的组件  
          myHandler.sendMessage(msg);  
	
	}
	
	private  void ActionRemoteSnap() 
	{
		 String ResultStr="查询电量：失败";
		 Message msg = new Message();  
         msg.what = 0x12;  
         Bundle bundle = new Bundle();  
         bundle.clear();  
         SmartBusServiceLib MySmartBusServiceLib=RemoteSnap();
         if(MySmartBusServiceLib.ReturnCode!=0)
         {
        	  
        	 ResultStr="0000";
          }
          else
          {
        		  
            ResultStr=MySmartBusServiceLib.SnapIDStr;
          }
        	 
        	 
        
             
	     bundle.putString("msg", ResultStr);  
          msg.setData(bundle);  
         //发送消息 修改UI线程中的组件  
          myHandler.sendMessage(msg);  
	
	}
	
	private  void ActionGetSanpImage() 
	  {
		 //String ResultStr="操作失败";
		 Message msg = new Message();  
         msg.what = 0x13;  
         Bundle bundle = new Bundle();  
         bundle.clear();  
         OpenDataService MyOpenDataService=GetSanpImage();
         if(MyOpenDataService.ReturnCode!=0)
         {
        	  
        		  //ResultStr="0000";
          }
          else
          {
        		  
        		  //ResultStr=MySmartBusServiceLib.SnapIDStr;
          }
        	 
        	 
        
             
	     bundle.putByteArray("snapimage", MyOpenDataService.ReceiveImageBuffer);  
          msg.setData(bundle);  
         //发送消息 修改UI线程中的组件  
          myHandler.sendMessage(msg);  
	
	  }
		
	//----------------------------------------------------
    private String Authentication()
	 {
		 String ChannelID=null;
		 LoginAuthtication MyLoginAuthtication =new LoginAuthtication();
		 MyLoginAuthtication.ServiceBusIP=CloudBusServerIP;//"121.42.45.167";
		 MyLoginAuthtication.PortNumber=Integer.parseInt(CloudBusServerPort);//CloudBusServerPort
		 MyLoginAuthtication.Authtication(MyNameID, MyPassWord);
			 
		
		 if(MyLoginAuthtication.ReturnCode==0)
	     {
				 
				MyLoginAuthtication.GetChannelID(null);
			    
				if(MyLoginAuthtication.ReturnCode==0)
			     {
			     
			      GlobalChannelIDStr=MyLoginAuthtication.ChannelIDStr;
			      ChannelID=MyLoginAuthtication.ChannelIDStr;
			      Log.i("ChannelID:", GlobalChannelIDStr);
			     
			     }
			     else
			     {
			    	 ChannelID=null;
			   
			     }
			 
			 
			  }
			 else
			 {
				 
				 ChannelID=null;
				 
			 }
			 return ChannelID;
			
		}
	   
	private String MobileLogin()
	 {
		 String ChannelID=null;
		 LoginAuthtication MyLoginAuthtication =new LoginAuthtication();
		 MyLoginAuthtication.ServiceBusIP=CloudBusServerIP;//"121.42.45.167";
		 MyLoginAuthtication.PortNumber=Integer.parseInt(CloudBusServerPort);//CloudBusServerPort
		 MyLoginAuthtication.MobileLogin(MyNameID, MyPassWord);
		
		 if(MyLoginAuthtication.ReturnCode==0)
		  {
			     
			  GlobalChannelIDStr=MyLoginAuthtication.ChannelIDStr;
			  ChannelID=MyLoginAuthtication.ChannelIDStr;
			  //Log.i("ChannelID:", GlobalChannelIDStr);
			     
		    }
		    else
		    {
			    	 ChannelID=null;
			    	 GlobalChannelIDStr=null;
			   
			 }
					
			 return ChannelID;
			
		}
	   
		
	 private int SynchTime(String InGlobalChannelIDStr)
		{
			
		     SmartBusServiceLib  MySmartBusServiceLib=new SmartBusServiceLib (InGlobalChannelIDStr);	
	    	 MySmartBusServiceLib.ServiceBusIP="121.42.45.167";	    	       		
		     MySmartBusServiceLib.SynchTime();	
			 return MySmartBusServiceLib.ReturnCode;
			
			
		
		}	

	 private SmartBusServiceLib GetPower(String InGlobalChannelIDStr)
	   {	
	    	SmartBusServiceLib  MySmartBusServiceLib=new SmartBusServiceLib (InGlobalChannelIDStr);	
	    	MySmartBusServiceLib.ServiceBusIP="121.42.45.167";	    	 
			MySmartBusServiceLib.GetPower();	
			return MySmartBusServiceLib;
			
			//System.out.println("ReturnCode:"+MySmartBusServiceLib.ReturnCode);	
			//System.out.println("PowerValue:"+MySmartBusServiceLib.PowerValue);
			//System.out.println("ResultParaString:"+MySmartBusServiceLib.ResultParaString);	
			
			
		
	  }

	 private SmartBusServiceLib RemoteSnap()
	  {
	    	
	    	SmartBusServiceLib  MySmartBusServiceLib=new SmartBusServiceLib (GlobalChannelIDStr);	
	    	MySmartBusServiceLib.ServiceBusIP="121.42.45.167";		          		
	   		MySmartBusServiceLib.RemoteSnap();	
	   		return MySmartBusServiceLib;
	   		
	    }
	
	 private OpenDataService GetSanpImage()
      {
    	String LockID=GlobalChannelIDStr.substring(0, 15);       	
    	OpenDataService  MyOpenDataService=new OpenDataService();	
    	MyOpenDataService.ServiceBusIP=CloudBusServerIP;//"121.42.45.167";  
    	MyOpenDataService.PortNumber=Integer.parseInt(CloudBusServerPort);
   		MyOpenDataService.GetImage(SnapID, LockID);
   		return MyOpenDataService;
  
     
     }

	
	
	
}
