package com.hnlylgj.sensor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter 
{  
	 private Context context;  
	   
	 public ImageAdapter(Context context)
	 {  
	  this.context=context;  
	 }  
	   
	 private Integer[] images = 
		 {  
	   //�Ź���ͼƬ������  
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,   
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,  
	   R.drawable.ic_launcher,  
	   };  
	  
	 private String[] texts = {  
	   //�Ź���ͼƬ�·����ֵ�����  
	   "��¼֧��",  
	   "��¼����",  
	   "�˱�����",  
	   "������",  
	   "�鿴ͼ��",  
	   "��֧����",  
	   "��¼�ĵ�",  
	   "���Ź���",  
	   "ϵͳ����",  
	   };  
	   
	 //get the number  
	 @Override  
	 public int getCount() {  
	  return images.length;  
	 }  
	 
	 @Override  
	 public Object getItem(int position) {  
	  return position;  
	 }  
	 
	 //get the current selector's id number  
	 @Override  
	 public long getItemId(int position) {  
	  return position;  
	 }  
	 
	 //create view method  
	 @Override  
	 public View getView(int position, View view, ViewGroup viewgroup) {  
	  ImgTextWrapper wrapper;  
	  if(view==null) {  
	   wrapper = new ImgTextWrapper();  
	   LayoutInflater inflater = LayoutInflater.from(context);  
	   view = inflater.inflate(R.layout.item, null);  
	   view.setTag(wrapper);  
	  view.setPadding(15, 15, 15, 15);  //ÿ��ļ��  
	  }
	  else 
	  {  
	   wrapper = (ImgTextWrapper)view.getTag();  
	  }  
	    
	  //wrapper.imageView = (ImageView)view.findViewById(R.id.MainActivityImage);  
	  //wrapper.imageView.setBackgroundResource(images[position]);  
	  //wrapper.textView = (TextView)view.findViewById(R.id.MainActivityText);  
	  //wrapper.textView.setText(texts[position]);  
	    
	  return view;  
	 }  
	}  
	 
	class ImgTextWrapper 
	{  
	 ImageView imageView;  
	 TextView textView;  
	  
	}  
