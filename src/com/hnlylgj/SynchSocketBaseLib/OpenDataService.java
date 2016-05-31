package com.hnlylgj.SynchSocketBaseLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class OpenDataService
{

	private String SendMessageStr ;
	private String ReceiveMessageStr;
	
	public byte[] ReceiveImageBuffer;	
	public  int ReturnCode;	
	public  String ChannelIDStr;	
	public  String ResultParaString;
	
	public String ServiceBusIP;
	public int PortNumber;
	public int MaxReceiveBufferSize=65536;
	public int MaxReceiveTimeOut=60000;
	
	public OpenDataService ()
	{
		
		ReturnCode=-1;		
		ServiceBusIP="127.0.0.1";
		PortNumber=9910;
	}
	public OpenDataService (String InServiceBusIP,int InPortNumber)
	{
		
		ReturnCode=-1;		
		ServiceBusIP=InServiceBusIP;
		if(InPortNumber==0)			
		PortNumber=9910;
		else
		PortNumber=	InPortNumber;
	}
	public  OpenDataService (String InChannelIDStr,String InServiceBusIP,int InPortNumber)
	{
		ReturnCode=1;
		ServiceBusIP=InServiceBusIP;
		
		if(InPortNumber==0)			
		PortNumber=9910;
		else
		PortNumber=	InPortNumber;
		
		ChannelIDStr=InChannelIDStr;
		
		
	}
	
	public void GetImage(String SanpID,String LockID)
	{
		
		SendMessageStr = "getimage#"+LockID+"-"+SanpID+"#!";
		try
		{
	    	SocketIOProc();
	    	if(ReceiveMessageStr!=null)
			{
				if(ReceiveMessageStr.indexOf("true")>-1)
				{
	    	    	ReturnCode=0;	
	    	    	
	    	    	
				}
				else
		     	{
				 ReturnCode=4;
				}
				
				
			}
	    	else
	    	{
	    		ReturnCode=5;
	    	}
	    	
		}
		catch(Exception Exp)
		{
			ReturnCode=6;	
			
		}
		
		ResultParaString=ReceiveMessageStr;
		
		
	}
	
	public void GetKeyList(String  LockID)
	{
		
		
		
	
	}
	
	
	public void GetOpenList(String  LockID,int NameFlag,int TimeFlag)
	{
		
		
		
	
	}
	
	
	public void GetSnapList(String  LockID, int TypeFlag,int TimeFlag)
	{
		
		
		
	
	}
	
	private void SocketIOProc() throws UnknownHostException, IOException 	
	{
		// TODO Auto-generated method stub	
		
		 byte[] SendUTF8MessageBytes= SendMessageStr.getBytes("UTF-8");
		 
		 int nCount=SendUTF8MessageBytes.length ;
	     byte[] SendAllBytes = new byte[nCount + 3];
       
	     for(int i=0;i<nCount;i++)
         {        	 
        	 SendAllBytes[i+3]= SendUTF8MessageBytes[i];
         }
	     
	     SendAllBytes[1] = 1; //同步Socket客户端标志   
         SendAllBytes[2] = (byte)255;//移动端请求标志
         
         byte[] ReadBufferBytes = new byte[MaxReceiveBufferSize];
         
         int RecieveCount=-1;
         
       //MySocket=new Socket("192.166.130.26",8910);
     	Socket MySocket=new Socket(ServiceBusIP,PortNumber);
     	MySocket.setKeepAlive(false);
     	MySocket.setSoTimeout(MaxReceiveTimeOut);
     	
		try
		{
		
			
		OutputStream MyOutputStream=MySocket.getOutputStream();
		InputStream MyInputStream=MySocket.getInputStream();
		
		MyOutputStream.write(SendAllBytes);	
		
		//------------------------------------------------------------
		RecieveCount = MyInputStream.read(ReadBufferBytes);		
		//System.out.println("RecieveCount :"+RecieveCount );	
		byte MessageCount=ReadBufferBytes[2];
        int MessageHeaderLenght=MessageCount+3;	        
        ReceiveMessageStr= new String(ReadBufferBytes, 3, MessageCount,"UTF-8");
        
        if(MessageHeaderLenght==RecieveCount)
        {
        	return;
        }
               
    	int StartPointID=ReceiveMessageStr.indexOf("[")+1;
    	int EndPointID=ReceiveMessageStr.indexOf("]");
        String ImageByteslenghtStr=ReceiveMessageStr.substring(StartPointID,EndPointID);
		int ImageByteslenght=Integer.parseInt(ImageByteslenghtStr);
		
		ReceiveImageBuffer=new byte[ImageByteslenght];
		
		//----第一次读到的图像数据.-------------------------------------------------
		int RecieveImageCount=RecieveCount-MessageHeaderLenght;		
		for(int i=0;i<RecieveImageCount;i++)
		{
			 
			 ReceiveImageBuffer[i]=ReadBufferBytes[MessageHeaderLenght+i]; 
		 
		 }
		//------2,3.4.n---------------------------------------------
		
		while(RecieveImageCount<ImageByteslenght)
		{
		
			RecieveCount = MyInputStream.read(ReadBufferBytes);	
			
		   for(int i=0;i<RecieveCount;i++)
		   {
			 
			 ReceiveImageBuffer[RecieveImageCount+i]=ReadBufferBytes[i]; 
		 
		   }
		   RecieveImageCount=RecieveImageCount+RecieveCount;
		   
		   
		}
		
		//-----------------------------------------------------------
		
		MySocket.close();
		
		//System.out.println("RecieveImageCount :"+RecieveImageCount);
		
		}
		catch(UnknownHostException Exp)
		{
			ReturnCode=1;
			 
		}
		
		catch(IOException Exp)
		{
			ReturnCode=2;
			if(MySocket!=null){MySocket.close();}
			
		}
		catch(Exception Exp)
		{
			ReturnCode=3;
			if(MySocket!=null){MySocket.close();}
			
		}
			

	}
	
	
}

