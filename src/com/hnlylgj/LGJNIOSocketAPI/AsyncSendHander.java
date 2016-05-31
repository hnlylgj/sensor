package com.hnlylgj.LGJNIOSocketAPI;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import android.util.Log;


public class AsyncSendHander extends Thread 
{
	  SocketChannel MySocketChannel; 
	  byte[] MySendMessageBytes; 
	 public AsyncSendHander( SocketChannel InMySocketChannel,byte[] InMySendMessageBytes) 
	 {
		 MySocketChannel= InMySocketChannel;
		 MySendMessageBytes=InMySendMessageBytes;
	 
	 }
	public void run() 
	{
		 ByteBuffer MyWriteBuffer = ByteBuffer.allocate(MySendMessageBytes.length); 	 
		 MyWriteBuffer.put(MySendMessageBytes); 
		 MyWriteBuffer.flip(); 
		 try
		 {
			MySocketChannel.write(MyWriteBuffer);
		} 
		 catch (IOException e) 
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (!MyWriteBuffer.hasRemaining()) 
		 //System.out.println("Send order 2 server succeed."); 
		 Log.i("SendBinaryMessage:", "ok!"); 
		 
		
	}
	
}
