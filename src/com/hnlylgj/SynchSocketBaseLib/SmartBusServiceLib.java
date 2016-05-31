package com.hnlylgj.SynchSocketBaseLib;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class SmartBusServiceLib 
{
	private String MobileID;
	private String CloudLockID;
	
	
	private String SendMessageStr ;
	private String ReceiveMessageStr;
	
	public String ServiceBusIP;
	public int PortNumber;
	
	public  int ReturnCode;	
	public  String ResultParaString;
	
	public  String OldKeyStr;
	public  int PowerValue;
	public  String SnapIDStr;
	public  SmartBusServiceLib ()
	{
		
		ServiceBusIP="127.0.0.1";
		PortNumber=9910;
		
	}
	
    public  SmartBusServiceLib (String InMobileID,String InCloudLockID)
	{
		MobileID=InMobileID;
		CloudLockID=InCloudLockID;
		ReturnCode=1;
		ServiceBusIP="127.0.0.1";
		PortNumber=9910;
		
		
	}
	
	public  SmartBusServiceLib (String GlobalChannelID)
	{
		
		CloudLockID=GlobalChannelID.substring(0,15);
		MobileID=GlobalChannelID.substring(16);
		ReturnCode=1;
		ServiceBusIP="127.0.0.1";
		PortNumber=9910;
		
	}
	
	public  SmartBusServiceLib (String GlobalChannelID,String InServiceBusIP,int InPortNumber)
	{
		
		CloudLockID=GlobalChannelID.substring(0,15);
		MobileID=GlobalChannelID.substring(16);
		ReturnCode=1;
		if(InServiceBusIP!=null)
		ServiceBusIP=InServiceBusIP;
		else
		ServiceBusIP="121.42.45.167";
		
		if(InPortNumber==0)			
		PortNumber=9910;
		else
		PortNumber=	InPortNumber;
		
	}
    public void LoginBus()
	{
		//SendMessageStr = "login#89765432BCDA820-9876DDDD8989100#中!";测试中文传输
		SendMessageStr = "login#"+CloudLockID+"-"+MobileID+"#!";
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
	
    public void GetPower()
     {
    	
    	ReceiveMessageStr=null;
    	
		
       SendMessageStr = "getpower#" + CloudLockID + "-" + MobileID + "#!";
       try
  		{
  	    	SocketIOProc();
  	    	if(ReceiveMessageStr!=null)
  			{
  				if(ReceiveMessageStr.indexOf("true")>-1)
  				{
  	    	    	ReturnCode=0;
  	    	    	int StartPointID=ReceiveMessageStr.indexOf(",")+1;
	    	    	int EndPointID=ReceiveMessageStr.indexOf("]");
  	    	    	String tempStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
  	    	    	//PowerFlag=temp;
  	    	    	PowerValue= Integer.parseInt(tempStr);
  	    	    	
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
  		
          ResultParaString = ReceiveMessageStr;
      }
	
	public void SynchTime()
    {
		
     SendMessageStr = "synchtime#" + CloudLockID + "-" + MobileID + "#!";
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
		
        ResultParaString = ReceiveMessageStr;
    }
		
	public void ClearInfor()
    {
		
     SendMessageStr = "clearlockkey#" + CloudLockID + "-" + MobileID + "!";
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
		
        ResultParaString = ReceiveMessageStr;
    }
	
	public void UpdateKey(String KeyIDStr)
    {
		if(KeyIDStr.length()<6)
		{
			
			ReturnCode=7;return;
		
		}
		if(KeyIDStr.length()>6)
		{
			
			ReturnCode=8;return;
		
		}
     SendMessageStr = "updatekey#" + CloudLockID + "-" + MobileID + "#"+ KeyIDStr +"!";
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
		
        ResultParaString = ReceiveMessageStr;
    }
	
	public void GetOldKey()
    {
	
     SendMessageStr = "getoldkey#" + CloudLockID + "-" + MobileID + "!";
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
	    	    	OldKeyStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
	    	    	
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
		
        ResultParaString = ReceiveMessageStr;
    }
	
    public void AddElectKey(String NameIDStr, String KeyIDStr)
     {
     
		 SendMessageStr = "addkey#" + CloudLockID + "-" + MobileID + "#" + NameIDStr +","+KeyIDStr+ "!";
		 
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
			
	        ResultParaString = ReceiveMessageStr;
		 
     }
	
	 public void TempElectKey(String TempKeyIDStr)
     {
		   
		 SendMessageStr = "tempkey#" + CloudLockID + "-" + MobileID + "#"+TempKeyIDStr+"!";
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
			
	        ResultParaString = ReceiveMessageStr;
		 
		 
     }
	
	 public void DeleteElectKey(String  NumberIDStr)
     {
		 
		 SendMessageStr = "deletekey#" + CloudLockID + "-" + MobileID + "#" + NumberIDStr + "!";
		 
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
			
	        ResultParaString = ReceiveMessageStr;
		 

     }
	 
	 public void RemoteSnap()
     {

		 SnapIDStr=null;
         SendMessageStr = "remotesnap#" + CloudLockID + "-" + MobileID + "#"  + "1!";
         try
			{
		    	SocketIOProc();
		    	if(ReceiveMessageStr!=null)
				{
					if(ReceiveMessageStr.indexOf("true")>-1)
					{
		    	    	ReturnCode=0;	
		    	    	int StartPointID=ReceiveMessageStr.indexOf("[")+1;
		    	    	int EndPointID=ReceiveMessageStr.indexOf(",");
		    	    	SnapIDStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
		    	    	
		    	    	
		    	    	
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
			
	        ResultParaString = ReceiveMessageStr;
		 
		 
     }
	
     public void OpenDoorList()
     {
		 SendMessageStr = "getopen#" + CloudLockID + "-" + MobileID + "#" + "!";
		 
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
			
	        ResultParaString = ReceiveMessageStr;
		 
		 
		 
     }
	 
     
     public void TimerGetInfor()
     {

         SendMessageStr = "timergetinfor#" + CloudLockID + "-" + MobileID + "#"  + "!";
         try
			{
		    	SocketIOProc();
		    	if(ReceiveMessageStr!=null)
				{
					if(ReceiveMessageStr.indexOf("true")>-1)
					{
		    	    	ReturnCode=0;	
		    	    	//int StartPointID=ReceiveMessageStr.indexOf("[")+1;
		    	    	//int EndPointID=ReceiveMessageStr.indexOf(",");
		    	    	//SnapIDStr=ReceiveMessageStr.substring(StartPointID, EndPointID);
		    	    	
		    	    	
		    	    	
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
			
	        ResultParaString = ReceiveMessageStr;
		 
		 
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
         
     	//Socket MySocket=new Socket("192.166.129.252",8910);//192.166.129.252--127.0.0.1
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
