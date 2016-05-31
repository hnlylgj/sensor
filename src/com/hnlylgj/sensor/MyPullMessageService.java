package com.hnlylgj.sensor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyPullMessageService extends Service
{
	public MyPullMessageService() 
	{
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
