package com.hnlylgj.LGJNIOSocketAPI;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncCallbackHander implements Runnable
{
	 private String  ServerBusIPStr; 
	 private int PortNumber;
	 private Selector MySelector; 
	 private SocketChannel MySocketChannel; 
	
	 public Handler MyReadHandler; 	 
	 public volatile boolean ThreadIsExit; 
	
	 
	public  AsyncCallbackHander(SocketChannel InSocketChannel)
	{
		ServerBusIPStr="121.42.45.167";
		PortNumber=9910;
		ThreadIsExit=false;
		MySocketChannel=InSocketChannel;
		try
	    { 
			MySelector=Selector.open();	
			Log.i("Selector:", "ServerBusIPStr:"+ServerBusIPStr);
	 
	    }
	   catch (IOException e) 
	   {
	      //e.printStackTrace(); 
	      Log.i("Selector:", "ERROR");
	      System.exit(1); 


	    } 
	 
	   		
	}
	
    public  AsyncCallbackHander(SocketChannel InSocketChannel,String InServerBusIPStr,int InPortNumber)
	{
		ServerBusIPStr=InServerBusIPStr;
		PortNumber=InPortNumber;
		ThreadIsExit=false;
		MySocketChannel=InSocketChannel;
		try
	    { 
			MySelector.open();	       
	 
	    }
	   catch (IOException e) 
	   {
	      //e.printStackTrace(); 
		   Log.i("Selector:", "ERROR");
	      System.exit(1); 


	    } 
	 
	   		
	}
	
	@Override	
    public void run() 
	{
		// TODO Auto-generated method stub
		//-------Connect----------------------------
       try
	   { 

	        CreateConnect(); 	 

	   } 
       catch (IOException e) 
       { 
	       // e.printStackTrace(); 
    	    Log.i("CreateConnect:", "ERROR");
	        //System.exit(1); 
    	    SendMessageCallBack(0x50,"打开连接服务端失败close");
	  } 
        //--------------------------------
        Log.i("CreateConnect:", "Next!");
	    while (!ThreadIsExit)
	    { 
	       try 
	       {
	       	MySelector.select(1000); 
	        Set<SelectionKey> selectedKeys = MySelector.selectedKeys();  
	        Iterator<SelectionKey> it = selectedKeys.iterator(); 
	        SelectionKey key = null; 
	        while (it.hasNext())
	        { 
	            key = it.next(); 
	            it.remove(); 
	            try 
	            { 

	             AsynchReadDataCallback(key); 

	            } 
	           catch (Exception e)
	           { 
	            if (key != null) 
	            { 
	                key.cancel(); 
	                if (key.channel() != null)
	                key.channel().close(); 
	            } 

	          } 
	        } 

	     } 
	     catch (Exception e)
	     { 
	        //e.printStackTrace(); 
	    	 Log.i("Selector LOOP:", "ERROR");
	         //System.exit(1); 
	    	 SendMessageCallBack(0x50,"服务端Socket可能关闭close");
	         break;
	      } 

	    } 

	   //---线程结束，所有注册在上面的Channel和Pipe等资源都要 
	    if (MySelector != null) 
	    try 
	    { 
	    	MySelector.close(); 
	    	Log.i("Selector Close:", "ok!");
	    }
	    catch (IOException e)
	    { 
	        //e.printStackTrace(); 
	    	Log.i("Selector Close:", "no!");

	    } 
	    }
	
	private void AsynchReadDataCallback(SelectionKey key) throws IOException 
	 { 
	     if (key.isValid()) 
	     { 
	        //---1.--判断是否连接成功 
	         SocketChannel  MySocketChannel = (SocketChannel) key.channel();  
	         if (key.isConnectable()) 
	         { 
	           if (MySocketChannel.finishConnect()) 
	            { 
	        	   MySocketChannel.register(MySelector, SelectionKey.OP_READ); 
	        	   //SendBinaryMessage(MySocketChannel); 
	        	   ServerAcceptCallback(0);
	        	   Log.i("ServerAccept:", "ok!");
	           
	            }	        	
	            else 
	            {
	              ServerAcceptCallback(1);
	              System.exit(1);// 连接失败，进程退出 
	             
 	            } 

	         }
         //---2.判断是否可读---------------------------------------
	       if (key.isReadable()) 
	       { 

	         ByteBuffer readBuffer = ByteBuffer.allocate(1024); 
	         int ReadBytesCount = MySocketChannel.read(readBuffer); 
	         if (ReadBytesCount > 0) 
	         { 
	             readBuffer.flip();
	             byte[] bytes = new byte[readBuffer.remaining()];
	             readBuffer.get(bytes); 
	             RecieveMessageParser(bytes,ReadBytesCount);
	             
	             //String body = new String(bytes, "UTF-8"); 
	             //System.out.println("Now is : " + body); 
	             //this.stop = true; 
	             
	             
	             
	          } 

	         else if (ReadBytesCount < 0) 
	         {   
	    	  // 对端链路关闭     
	             key.cancel(); 
	             MySocketChannel.close(); 
	             SendMessageCallBack(0x50,"服务端关闭close");

	         }
	         else 
	         {
	        	 //System.out.println("0字节 "); 
	             //; // 读到0字节，忽略 
	             SendMessageCallBack(0x50,"服务端关闭close");
	             
	             
	             
	         }
	  
	     } 

	   } 
	  
	 }   
	 
	private void CreateConnect() throws IOException 
	 { 
	    // 如果直接连接成功，则注册到多路复用器上
	    if (MySocketChannel.connect(new InetSocketAddress(ServerBusIPStr, PortNumber))) 
	    { 
	    	MySocketChannel.register(MySelector, SelectionKey.OP_READ); 
	        //doWrite(MySocketChannel); 
	    	ServerAcceptCallback(0);
	    	
	    	Log.i("CreateConnect:", "ok!");
	 
	    } 
	    else 
	    {
	    	Log.i("CreateConnect:", "no!");
	    	MySocketChannel.register(MySelector, SelectionKey.OP_CONNECT); 
	  
	    }


	 } 
	 	
	private void ServerAcceptCallback(int ResultID )
     {
		 String ResultStr;
		 if(ResultID==0)
		  ResultStr="建立通道成功open";
		 else
		  ResultStr="建立通道失败close"; 
		  SendMessageCallBack(0x50,ResultStr);
		 
		 /*
			Message msg = new Message();  
	        msg.what = 0x50;  
	        Bundle bundle = new Bundle();  
	        bundle.clear();  
	         	  
		     bundle.putString("msg", ResultStr);  
	         msg.setData(bundle);  
	        //发送消息 修改UI线程中的组件  
	         if(MyReadHandler!=null) 
	         {
	        	    MyReadHandler.sendMessage(msg); 
	         }
	         */
	         //Log.d("connect:",  ResultStr);
 
     }
	
	private void RecieveMessageParser(byte[] RecieveMessageBytes, int RecieveCount)
    {
		
	  String BaseMessageString=null;
	   try 
        {
				BaseMessageString= new String(RecieveMessageBytes, 3, RecieveCount - 3,"UTF-8");
		} 
    	catch (UnsupportedEncodingException e)
    	{
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		 String ResultStr=BaseMessageString;
 		 Message msg = new Message();  
 		
        if (RecieveMessageBytes[2] == -1)//云锁响应消息(包括Ping应答)
        {
       	
       	
           msg.what = 0x51; 
           
           if(ResultStr.indexOf("ping")>-1)
           msg.what = 0x54;  //ping命令
           
       	 
        }
        else //======（250）智能锁推送通知消息===================================
        {
            //SmartLockNotifyMessageParser(RecieveMessageBytes, RecieveCount);         	 
       	  msg.what = 0x52; 

        }
        Bundle bundle = new Bundle();  
        bundle.clear();  
          	  
 	     bundle.putString("msg", ResultStr);  
         msg.setData(bundle);  
         //发送消息 修改UI线程中的组件  
        if(MyReadHandler!=null)
        MyReadHandler.sendMessage(msg); 
        
        Log.d("Read:",  BaseMessageString);
		
		
		
    }
   
    private void SendMessageCallBack(int WhatID,String ResultStr)
	 { 
		
		   Message msg = new Message();  
		   msg.what = WhatID;  
		   Bundle bundle = new Bundle();  
		   bundle.clear();  
		          	  
		   bundle.putString("msg", ResultStr);  
		   msg.setData(bundle);  
		   if(MyReadHandler!=null)
		    MyReadHandler.sendMessage(msg); 
		 
		 
	}
	
	
    private void SendBinaryMessage(byte[] InSendMessageBuffers) throws IOException 
	{ 
	   //发送消息在另外一个线程类实现
	    byte[] req = InSendMessageBuffers;//"QUERY TIME ORDER".getBytes(); 
	    ByteBuffer writeBuffer = ByteBuffer.allocate(req.length); 	 
	    writeBuffer.put(req); 
	    writeBuffer.flip(); 
	    MySocketChannel.write(writeBuffer);
	    if (!writeBuffer.hasRemaining()) 
	    //System.out.println("Send order 2 server succeed."); 
	    Log.i("SendBinaryMessage:", "ok!");

	   } 



}

