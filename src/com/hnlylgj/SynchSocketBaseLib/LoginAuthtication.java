package com.hnlylgj.SynchSocketBaseLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginAuthtication 
{
	private String SendMessageStr ;
	private String ReceiveMessageStr;
	
	public  int ReturnCode;	
	public  String CustomerIDStr;	
	public  String ChannelIDStr;	
	public  String ResultParaString;
	
	public String ServiceBusIP;
	public int PortNumber;
	
	public LoginAuthtication ()
	{
		
		ReturnCode=-1;		
		ServiceBusIP="127.0.0.1";
		PortNumber=9910;
	}
	public LoginAuthtication (String InServiceBusIP,int InPortNumber)
	{
		
		ReturnCode=-1;		
		ServiceBusIP=InServiceBusIP;
		if(InPortNumber==0)			
		PortNumber=9910;
		else
		PortNumber=	InPortNumber;
	}
	public  LoginAuthtication (String Name,String PassWord,String InServiceBusIP,int InPortNumber)
	{
		ReturnCode=1;
		ServiceBusIP=InServiceBusIP;
		
		if(InPortNumber==0)			
		PortNumber=9910;
		else
		PortNumber=	InPortNumber;
		
		Authtication(Name,PassWord);
		
	}
	
	public void Authtication(String Name,String PassWord)
	{
		ReturnCode=-1;
		CustomerIDStr=null;
		SendMessageStr = "authtication#"+Name+"-"+PassWord+"#!";
		try
		{
	    	SocketIOProc();
	    	if(ReceiveMessageStr!=null)
			{
				if(ReceiveMessageStr.indexOf("true")>-1)
				{
	    	    	ReturnCode=0;	
	    	    	int StartPointID=ReceiveMessageStr.indexOf("[")+1;
	    	    	int EndPointID=ReceiveMessageStr.indexOf("]");
	    	    	int Count=EndPointID-StartPointID;
	    	    	//System.out.println("StartPointID:"+StartPointID);	
	    	    	//System.out.println("EndPointID:"+EndPointID);	
	    	    	//System.out.println("Count:"+ Count);	
	    	    	//System.out.println(ReceiveMessageStr.substring(18, 22));
	    	    	CustomerIDStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
	    	    	
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
		
		//return 0;
	}
		
	public void MobileLogin(String Name,String PassWord)
	{
		ReturnCode=-1;
		CustomerIDStr=null;
		SendMessageStr = "mobilelogin#"+Name+"-"+PassWord+"#!";
		try
		{
	    	SocketIOProc();
	    	if(ReceiveMessageStr!=null)
			{
				if(ReceiveMessageStr.indexOf("true")>-1)
				{
	    	    	ReturnCode=0;	
	    	    	//int StartPointID=ReceiveMessageStr.indexOf("[")+1;
	    	    	//int EndPointID=ReceiveMessageStr.indexOf("]");
	    	    	//int Count=EndPointID-StartPointID;
	    	    	//System.out.println("StartPointID:"+StartPointID);	
	    	    	//System.out.println("EndPointID:"+EndPointID);	
	    	    	//System.out.println("Count:"+ Count);	
	    	    	//System.out.println(ReceiveMessageStr.substring(18, 22));
	    	    	//CustomerIDStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
	    	    	
	    	    	int StartPointID=ReceiveMessageStr.indexOf("[")+1;
	    	    	int EndPointID=ReceiveMessageStr.indexOf("]");
	    	    	int Count=EndPointID-StartPointID;
	    	    	ChannelIDStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
	    	    	
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
		
		//return 0;
	}
	
	public  String GetChannelID(String InCustomerIDStr)
	{
		ReturnCode=-1;
		ReceiveMessageStr=null;
		if(InCustomerIDStr!=null)
		{
		 SendMessageStr = "getchannel#"+InCustomerIDStr+"#!";
		}
		else
		{
			
		  SendMessageStr = "getchannel#"+CustomerIDStr+"#!";	
		}
		
		try
		{
	    	SocketIOProc();
	    	if(ReceiveMessageStr!=null)
			{
				if(ReceiveMessageStr.indexOf("true")>-1)
				{
	    	    	ReturnCode=0;	
	    	    	int StartPointID=ReceiveMessageStr.indexOf("[")+1;
	    	    	int EndPointID=ReceiveMessageStr.indexOf("]");
	    	    	int Count=EndPointID-StartPointID;
	    	    	ChannelIDStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
	    	    	
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
		
		return ChannelIDStr;
	}
	
	private void SocketIOProc() throws UnknownHostException, IOException 	
	{
		// TODO Auto-generated method stub		
		
		//测试输出默认字符编码
		//System.out.println(System.getProperties().getProperty("file.encoding")); 
		//System.out.println(Charset.defaultCharset()); 		
		 		
		 byte[] SendUTF8MessageBytes= SendMessageStr.getBytes("UTF-8");
		 
		 int nCount=SendUTF8MessageBytes.length ;
	     byte[] SendAllBytes = new byte[nCount + 3];
       
	     for(int i=0;i<nCount;i++)
         {        	 
        	 SendAllBytes[i+3]= SendUTF8MessageBytes[i];
         }
	     
	     SendAllBytes[1] = 1; //同步Socket客户端标志   
         SendAllBytes[2] = (byte)255;//移动端请求标志
         
         byte[] ReadBufferBytes = new byte[1024];
         //String ReceiveStr=null;
         int RecieveCount=-1;
         
     	//Socket MySocket=new Socket("192.166.129.252",8910);	
     	Socket MySocket=new Socket(ServiceBusIP,PortNumber);
     	MySocket.setKeepAlive(false);
     	MySocket.setSoTimeout(65000);
     	
		try
		{
		
		//MySocket=new Socket("192.166.130.26",8910);	
		OutputStream MyOutputStream=MySocket.getOutputStream();
		InputStream MyInputStream=MySocket.getInputStream();
		
		MyOutputStream.write(SendAllBytes);			
		RecieveCount = MyInputStream.read(ReadBufferBytes);
	    ReceiveMessageStr= new String(ReadBufferBytes, 3, RecieveCount - 3,"UTF-8");
	   
	    //ReceiveMessageStr= new String()
	    //--最后释放资源---------------------
	    MyOutputStream.close();
	    MyInputStream.close();
		MySocket.close();
		
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