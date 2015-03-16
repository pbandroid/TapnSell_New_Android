package com.convertingoffers.tapnsell.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class UtilsImage {
	public static ArrayList<Integer> qtys = new ArrayList<Integer>();
	public static ArrayList<String> options = new ArrayList<String>();
	public static ArrayList<Float> pricess = new ArrayList<Float>();
    
	public static void CreateOrder(int qty, String option, float price){
    	qtys.add(qty);
    	options.add(option);
    	pricess.add(price);
    }

	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
}