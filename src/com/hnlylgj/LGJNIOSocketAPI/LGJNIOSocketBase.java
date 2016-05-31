package com.hnlylgj.LGJNIOSocketAPI;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;

import com.hnlylgj.sensor.SocketServerThread;

import android.os.Handler;
import android.util.Log;

public class LGJNIOSocketBase
{
	 private String  ServerBusIPStr; 
	 private int PortNumber;
	 public SocketChannel MySocketChannel; 
	 private volatile boolean ThreadIsExit;  
	 
	 AsyncCallbackHander MyAsyncCallbackHander;
	 
	 public static LGJNIOSocketBase CurrentLGJNIOSocketBase;
	 
	 public String GlobalChannelIDStr;
	 
	 
	 public  LGJNIOSocketBase() 
	 {
		 ServerBusIPStr="121.42.45.167";
		 PortNumber=9910;
		  try
		  { 
			 MySocketChannel = SocketChannel.open(); 
			 MySocketChannel.configureBlocking(false);  
			 MySocketChannel.socket().setReceiveBufferSize(65536);
			 MySocketChannel.socket().setSendBufferSize(65536);
		    }
		   catch (IOException e) 
		   { 		 
		        e.printStackTrace(); 
		        System.exit(1); 

		  } 
		  CurrentLGJNIOSocketBase=this;
		
	 }
	 public  LGJNIOSocketBase(String InServerBusIPStr,int InPortNumber)
	 {
		 ServerBusIPStr=InServerBusIPStr;
		 PortNumber=InPortNumber;
		 try
		  { 
			 MySocketChannel = SocketChannel.open(); 
			 MySocketChannel.configureBlocking(false);  
			 MySocketChannel.socket().setReceiveBufferSize(65536);
			 MySocketChannel.socket().setSendBufferSize(65536);
		    }
		   catch (IOException e) 
		   { 
		 
		        e.printStackTrace(); 
		        System.exit(1); 

		  } 
		 
		 CurrentLGJNIOSocketBase=this;
	 }
	 
	 public void OpenConnect(Handler InMyHandler)
	 {
		
		 MyAsyncCallbackHander=new AsyncCallbackHander(MySocketChannel);
		 MyAsyncCallbackHander.ThreadIsExit=false; 
		 
		 if(InMyHandler!=null)
		 MyAsyncCallbackHander.MyReadHandler=InMyHandler;
		 
		 Thread MySocketThread=new  Thread(MyAsyncCallbackHander);
		 MySocketThread.start();
		 
	 }
	
     public  void CloseConnect() throws IOException
	 {
		 if(MyAsyncCallbackHander!=null)
		 {
			 MyAsyncCallbackHander.ThreadIsExit=true; 
		 }
		 if( MySocketChannel.socket().isConnected())
		 {
			 MySocketChannel.socket().close();
			 Log.i("MySocketChannel Close:", "ok!");
		 }
	
		 
	 
	 }
	 
	 public  void LoginChannelX(String GlobalChannelIDStr)
	 {
		 String SendMessageStr ;
		 SendMessageStr = "login#"+GlobalChannelIDStr+"#!";
         byte[] SendUTF8MessageBytes;
		 try
		 {
			 SendUTF8MessageBytes = SendMessageStr.getBytes("UTF-8");
			 int nCount=SendUTF8MessageBytes.length ;
		     byte[] SendAllBytes = new byte[nCount + 3];
	       
		     for(int i=0;i<nCount;i++)
	         {        	 
	        	 SendAllBytes[i+3]= SendUTF8MessageBytes[i];
	         }
		     
		     //SendAllBytes[1] = 0; //异步Socket客户端标志   
	         SendAllBytes[2] = (byte)255;//移动端请求标志
	       	//MyAsyncCallbackHander.SendBinaryMessage(SendAllBytes);
	        //异步发送消息
	         //new AsyncSendHander(MySocketChannel,SendAllBytes).start();
	         AsyncSendHander MyAsyncSendHander=new	AsyncSendHander(MySocketChannel,SendAllBytes); 
	         MyAsyncSendHander.start();
	         
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		 
		
		
	 
	 }
	
     public  void SynchTimeX(String GlobalChannelIDStr)
	 {
    	 String SendMessageStr ;
		 SendMessageStr = "synchtime#"+GlobalChannelIDStr+"#!";
         byte[] SendUTF8MessageBytes;
		 try
		 {
			 SendUTF8MessageBytes = SendMessageStr.getBytes("UTF-8");
			 int nCount=SendUTF8MessageBytes.length ;
		     byte[] SendAllBytes = new byte[nCount + 3];
	       
		     for(int i=0;i<nCount;i++)
	         {        	 
	        	 SendAllBytes[i+3]= SendUTF8MessageBytes[i];
	         }
		     
		     //SendAllBytes[1] = 0; //异步Socket客户端标志   
	         SendAllBytes[2] = (byte)255;//移动端请求标志
	       	//MyAsyncCallbackHander.SendBinaryMessage(SendAllBytes);
	        //异步发送消息
	         //new AsyncSendHander(MySocketChannel,SendAllBytes).start();
	         AsyncSendHander MyAsyncSendHander=new	AsyncSendHander(MySocketChannel,SendAllBytes); 
	         MyAsyncSendHander.start();
	         
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 }

     public  void GetPowerX(String GlobalChannelIDStr)
	 {
    	 String SendMessageStr ;
		 SendMessageStr = "getpower#"+GlobalChannelIDStr+"#!";
         byte[] SendUTF8MessageBytes;
		 try
		 {
			 SendUTF8MessageBytes = SendMessageStr.getBytes("UTF-8");
			 int nCount=SendUTF8MessageBytes.length ;
		     byte[] SendAllBytes = new byte[nCount + 3];
	       
		     for(int i=0;i<nCount;i++)
	         {        	 
	        	 SendAllBytes[i+3]= SendUTF8MessageBytes[i];
	         }
		     
		     //SendAllBytes[1] = 0; //异步Socket客户端标志   
	         SendAllBytes[2] = (byte)255;//移动端请求标志
	       	//MyAsyncCallbackHander.SendBinaryMessage(SendAllBytes);
	        //异步发送消息
	         //new AsyncSendHander(MySocketChannel,SendAllBytes).start();
	         AsyncSendHander MyAsyncSendHander=new	AsyncSendHander(MySocketChannel,SendAllBytes); 
	         MyAsyncSendHander.start();
	         
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 }
    //---------------------------------------------------
     public  void LoginChannel(String GlobalChannelIDStr)
	 {
    	 OneSendEntry("login",GlobalChannelIDStr);   
	 }
     
     public  void KeepChannel(String GlobalChannelIDStr)
	 {
    	 OneSendEntry("ping",GlobalChannelIDStr);   
	 }
     
     public  void SynchTime(String GlobalChannelIDStr)
	 {
    	 OneSendEntry("synchtime",GlobalChannelIDStr);  
	 }
    
      public  void GetPower(String GlobalChannelIDStr)
	 {
    	 OneSendEntry("getpower",GlobalChannelIDStr);  
	 }
     
     public void RemoteSnap(String GlobalChannelIDStr) 
     {
    	 
    	 OneSendEntry("remotesnap",GlobalChannelIDStr); 
    	 
     }
     public void RemoteOpen(String GlobalChannelIDStr) 
     {
    	 
    	 OneSendEntry("remoteopen",GlobalChannelIDStr); 
    	 
     }
     public void TempKey(String GlobalChannelIDStr,String TempKeyStr)
   	 {
    	 OneSendEntry("tempkey",GlobalChannelIDStr+"#"+TempKeyStr); 
   	 }
     
     
     
     
     private void OneSendEntry(String MessageType,String GlobalChannelIDStr)
	 {
    	 String SendMessageStr ;
		 SendMessageStr = MessageType+"#"+GlobalChannelIDStr+"!";
         byte[] SendUTF8MessageBytes;
		 try
		 {
			 SendUTF8MessageBytes = SendMessageStr.getBytes("UTF-8");
			 int nCount=SendUTF8MessageBytes.length ;
		     byte[] SendAllBytes = new byte[nCount + 3];
	       
		     for(int i=0;i<nCount;i++)
	         {        	 
	        	 SendAllBytes[i+3]= SendUTF8MessageBytes[i];
	         }
		     
		     //SendAllBytes[1] = 0; //异步Socket客户端标志   [0:异步,1：同步]
	         SendAllBytes[2] = (byte)255;//移动端请求标志
	       	//MyAsyncCallbackHander.SendBinaryMessage(SendAllBytes);
	        //异步发送消息
	         //new AsyncSendHander(MySocketChannel,SendAllBytes).start();
	         AsyncSendHander MyAsyncSendHander=new	AsyncSendHander(MySocketChannel,SendAllBytes); 
	         MyAsyncSendHander.start();
	         
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 }

}


