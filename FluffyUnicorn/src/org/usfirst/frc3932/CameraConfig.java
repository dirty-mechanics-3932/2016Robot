package org.usfirst.frc3932;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class CameraConfig {
	private AxisCamera camera;
	private String URL;
	
	public CameraConfig(String URL){
		this.URL = URL;
		this.camera = new AxisCamera(this.URL);
    	camera.writeExposurePriority(50);
	}
	
	public void sendImage() {
		Image image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		try {
			camera.getImage(image);
			CameraServer.getInstance().setImage(image);		
		} catch (Exception e) {
			System.err.println("Failed to get image from camera");
			System.err.println(e.getStackTrace());
		} finally {
			image.free();
		}
	}
	
	public boolean exists() {
    	boolean flag = false;
    	Image image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    	try {
    		camera.getImage(image);
    		flag = true;
    	}
    	catch (Exception e){
    		
    	}
    	finally {
    		image.free();
    	}
    	return flag;
	}
}
