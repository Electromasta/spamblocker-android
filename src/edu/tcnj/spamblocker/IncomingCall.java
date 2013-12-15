package edu.tcnj.spamblocker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import com.android.internal.telephony.ITelephony;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class IncomingCall extends BroadcastReceiver {
	Context context = null;
	private ITelephony telephonyService;
	Hashtable blockedNum = new Hashtable(10, 10); //= "1111111111";//"6094687147";
	String incomingNum = "";
	boolean graylist = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if(null == bundle)
			return;
		
		
		File blacklist = new File(Environment.getExternalStorageDirectory(), "blacklist.txt");
		BufferedReader br;
    	try {
    		if(!blacklist.exists()) {
    			blacklist.createNewFile(); 
    		}
    		else	{
    			br = new BufferedReader(new FileReader(blacklist));
    			String inputLine;
    			while ((inputLine = br.readLine()) != null) {
    				blockedNum.put(inputLine, inputLine);
    			}
    			br.close();
    		}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
		if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))	{
			incomingNum = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			Toast.makeText(context, "Num: " + incomingNum, Toast.LENGTH_SHORT).show();
			//Is this a Blacklist User?
			if (blockedNum.containsKey(incomingNum))	{
				blockCaller(context);
			}
			//Could be Whitelist or GrayList
			else	{
				//Is this a Graylist User?
				if (!(contactExists(context, incomingNum)))	{
					//Handle Graylist;
					graylist = true;
				}
			}
		}
		
		if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE))	{
			if (graylist==true)	{
				//Dialog dialog = new Dialog(context);
				//TextView txt = (TextView)dialog.findViewById(R.id.choice);
				//dialog.show();
				graylist=false;
			}
		}
	}



    public boolean contactExists(Context context, String number) {
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
		try {
		   if (cur.moveToFirst()) {
			   Toast.makeText(context, "Does Contact Exist? YES", Toast.LENGTH_SHORT).show();
		      return true;
		   }
		} finally {
		if (cur != null)
		   cur.close();
		}
		Toast.makeText(context, "Does Contact Exist? NO", Toast.LENGTH_SHORT).show();
		return false;
    }
    
    public boolean blockCaller(Context context)	{
    	boolean result = false;
    	TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
        	   Class c = Class.forName(telephony.getClass().getName());
        	   Method m = c.getDeclaredMethod("getITelephony");
        	   m.setAccessible(true);
        	   telephonyService = (ITelephony) m.invoke(telephony);
        	   Toast.makeText(context, "This should be blocked!", Toast.LENGTH_SHORT).show();
        	   telephonyService.endCall();
        } catch (Exception e) {
        	   e.printStackTrace();
        }
    	return result;
    }
}


