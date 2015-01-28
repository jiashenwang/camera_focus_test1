package com.example.surfaceviewtest1;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class CameraActivity extends Activity {
	
    private Camera mCamera;
    private CameraPreview mPreview;
	private boolean add;
    Button capture;
    
	public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.camera_activity);
	       
	       if(checkCameraHardware(getApplicationContext())){
	    	   mCamera = getCameraInstance();
	    	   initCamera();
	       }
	       
	       capture = (Button)findViewById(R.id.button_capture);
	       mPreview = new CameraPreview(this, mCamera);
	       FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	       preview.addView(mPreview);
    	   
	       preview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				initCamera();
				return false;
			}
		});
	       
	       capture.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					//initCamera();
				}	    	   
	       });
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	        
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	// initialize camera setting
	private void initCamera() {
		
		if (mCamera != null){
			Parameters parameters = mCamera.getParameters();
			
			final List<String> focusModes = parameters.getSupportedFocusModes();
			for(String mode: focusModes){
				Log.d("~~~~", ">>>>>Camera focus mode: " + mode);
			}
			
			
			if (parameters.getFocusMode() != Camera.Parameters.FOCUS_MODE_AUTO) {
				if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
		            parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
				}
	        }
			ArrayList<Area> focusAreas = new ArrayList<Camera.Area>(1);
			focusAreas.add(new Area(new Rect(-50, -50, 50, 50), 750));
			parameters.setFocusAreas(focusAreas);
			
			
			try{
				mCamera.cancelAutoFocus();
				mCamera.setParameters(parameters);
				mCamera.startPreview();
				mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (camera.getParameters().getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                            Parameters parameters = camera.getParameters();
                    		try{
//                    			parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
                    			if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
                    				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    				
                    			}else if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
                    				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                    			}else if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
                    				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    			}
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
                            //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            parameters.setFocusAreas(null);
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }
                    }
                });
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
