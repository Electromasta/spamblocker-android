package edu.tcnj.spamblocker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    //On button press
    public void onButtonClick(View view)
    {  
    	File blacklist = new File(Environment.getExternalStorageDirectory(), "blacklist.txt");
    	//Get reference to textbox
    	if (view.getId()==R.id.blockbutton)	{
	    	EditText editBlock = (EditText)findViewById(R.id.blocktextbox);
	        Toast.makeText(this, "Button Blocked clicked!", Toast.LENGTH_SHORT).show(); 
	    	String phoneNum = editBlock.getText().toString();
	    	BufferedWriter bf;
	    	try {
	    		if(!blacklist.exists()) {
	    			blacklist.createNewFile(); 
	    		}
	    		else	{
	    			bf = new BufferedWriter(new FileWriter(blacklist));
	    			bf.write(phoneNum);
	    			bf.close();
	    		}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	if (view.getId()==R.id.unblockbutton)	{
    		Toast.makeText(this, "Button unBlocked clicked!", Toast.LENGTH_SHORT).show();
	    	String phoneNum = "";
	    	BufferedWriter bf;
	    	try {
	    		if(!blacklist.exists()) {
	    			blacklist.createNewFile(); 
	    		}
	    		else	{
	    			bf = new BufferedWriter(new FileWriter(blacklist));
	    			bf.write(phoneNum);
	    			bf.close();
	    		}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

    	}
    } 
}

