package org.usfirst.frc.team3932.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class robotrealmnetworktable2 {




	public static void main(String args[]) {
		// TODO Auto-generated method stub


		//========================================
		//Robot Code
		// declare object
		//System.out.println("Hello");	



		System.out.println("Hello");
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("127.0.0.1");

		NetworkTable table = null;
		//try{
		while(true){
			table = NetworkTable.getTable("dad");
			double[] defaultValue = new double[0];
			double [] x = table.getNumberArray("BLOBS",defaultValue) ;
			//System.out.print("blobs: ");
			//System.out.println("hghgjhghjghjg");
			//if (x.length> 0)
			//{
			double Distance = table.getNumber("Distance", 0.0);
			System.out.println("distance" + Distance);	
			if (x.length > 0){	
			System.out.println("XCenterBlob" + x[0]);
				System.out.println(' ');
				System.out.println("YCenterBlob"+ x[1]);}	
			else
			System.out.print("nothing");
	}}}
	/*	catch(Exception e) {
			e.printStackTrace();
			System.out.println("Hello");
		}*/


