package com.example.surfaceviewtest1;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends Activity{
	
	Button camera;
	public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_main);
	       
	       camera = (Button)findViewById(R.id.camera);
	       camera.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this, CameraActivity.class);
					startActivity(i);
				}	    	   
	       });
	}

}