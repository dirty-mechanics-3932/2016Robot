// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3932.commands;

//import org.usfirst.frc3932.pid.AhrsYawPIDSource;
//import org.usfirst.frc3932.pid.DriveSystemRotatePIDOutput;
import java.util.Date;

import org.usfirst.frc3932.Robot;

//import edu.wpi.first.wpilibj.PIDController;

//import edu.wpi.first.wpilibj.PIDOutput;
//import edu.wpi.first.wpilibj.PIDSource;
//import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TurnToYawPid extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// oscillation period .44 -- Dave's p=.045,i=.007,d=0,f=1.3 for West Palm
	// Mini of working p=.045,i=0,d=.006,0 error 1, buffer 10
	// Mini working with Elias .036,.0025,.00025,0,.1,0 --Take 3 seconds to move 90
	// Big 0.036,.0025,.0025,10,.1,5,Speed=.4
	// Elias .045,0,.040,1.3, error = .1 Works well
	
	// Start on 4/8 Oscillates .045,0,.006,0,10, .4
	// .045,0,.08,0, 10,.4

	private double P = .045;
	private double I = .00;
	private double D = .1;  // was .08
	private double F = 0;
	private double MaxError = 0.4;
	private int Samples = 10;

	private Date m_turnToCreate;
	private Date m_turnToInit;
	private double m_degrees;
	private double m_timeout = 0;

	private ourTurnPid m_pid;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public TurnToYawPid(double degrees, double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_degrees = degrees;
		m_timeout = timeout;
		m_turnToCreate = new Date();

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

	}

	public TurnToYawPid(double degrees) { // Overloaded for 5 second time out
		this(degrees, 5);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.logf(
				"Turnto init Degrees:%.2f Timeout:%.2f p:%.4f i:%.4f d:%.4f f:%.4f AllowedError:%.2f Samples:%d Version:%s %n",
				m_degrees, m_timeout, P, I, D, F, MaxError, Samples, Robot.ahrs.getFirmwareVersion());
		m_turnToInit = new Date();
		m_pid = new ourTurnPid(P, I, D, MaxError, Samples);
		m_pid.setTarget(m_degrees);

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		m_pid.execute();
		double convTime = (new Date().getTime() - m_turnToInit.getTime());
		SmartDashboard.putNumber("Time to Turn", convTime);
		Robot.logf(
				"TurnTo onTarget:%s YAW:%.3f AvgYaw:%.3f Error:%.3f AvgError: %.3f Output:%.5f AvgOutput:%.5f ConvergeTime:%.0f %n",
				bool(m_pid.onTarget()), Robot.ahrs.getYaw(), m_pid.getAvgYaw(), m_pid.getError(), m_pid.getAvgError(), m_pid.lastOuput(),
				m_pid.getAvgOutput(), convTime);
		//return m_pid.onTarget() || (convTime > m_timeout * 1000) || Math.abs(m_pid.getAvgOutput()) < .03;
		return m_pid.onTarget() || (convTime > m_timeout * 1000);
	}

	// Called once after isFinished returns true
	protected void end() {
		m_pid.disable();
		Robot.log("TurnTo end degrees:" + m_degrees + " timeOut:" + m_timeout + " Yaw:" + Robot.ahrs.getYaw()
				+ " Total Autonomous Time:" + (new Date().getTime() - m_turnToCreate.getTime()));
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

	protected String bool(boolean val) {
		return val ? "True" : "False";
	}

	protected double round4(double d) {
		return Math.round(d * 10000) / 10000.0;
	}

	// Class for Running Average
	public class RunningAverage {
		private int bufferSize;
		private int lastSample = 0;
		private int numSamples = 0;
		private double[] samples;

		public RunningAverage(int size) {
			samples = new double[size];
			bufferSize = size;
		}

		public void putNew(double d) {
			if (lastSample >= bufferSize) lastSample = 0;
			samples[lastSample] = d;
			lastSample++;
			numSamples++;
			if (numSamples >= bufferSize) numSamples = bufferSize;
		}

		public double getAverage() {
			double sum = 0;
			for (int i = 0; i < numSamples; i++)
				sum += samples[i];
			return sum / numSamples;
		}
		
		public double variance(){
			double variance = 0;
			double avg = getAverage();
			for (int i = 0; i < numSamples; i++)
				variance += Math.abs(samples[i] - avg);
			return variance / numSamples;
		}
		
		public boolean isAverageValid() {
			return numSamples >= bufferSize;
		}
		
		/**
		 * 
		 * @return Returns true if last x averages are the same within a specified error.
		 */
		public boolean compareLast(int lastNum, double maxError){ 
			if (lastNum > samples.length)
				lastNum = samples.length;
			double lastSample = samples[samples.length - 1], currentSample;
			for (int i = 1; i <= lastNum; i++) {
				currentSample = samples[samples.length - i];
				if (lastSample - currentSample < maxError)
					lastSample = currentSample;
				else
					return false;
			}
			return true;
		}
	}

	class ourTurnPid {

		private double iSum;
		private double Kp, Ki, Kd;
		private double initialTarget = 0;
		private double outMax = .4;
		private double outMin = -.4;
		private double maxError = .25;
		private RunningAverage averageError;
		private RunningAverage averageOutput;
		private RunningAverage averageYaw;
		private double lastError = 0;
		private double lastInput = 0;
		private double lastOut;
		private int numberExecutes = 0;
		private double origKp;

		public ourTurnPid(double Kp, double Ki, double Kd, double MaxError, int numSamples) {
			this.Ki = Ki;
			this.Kp = Kp;
			this.Kd = Kd;
			this.origKp = Kp;
			this.maxError = MaxError;
			averageError = new RunningAverage(numSamples);
			averageOutput = new RunningAverage(numSamples);
			averageYaw = new RunningAverage(numSamples);
		}

		public void setTarget(double target) {
			initialTarget = target;
		}
		
		public void disable() {
			Robot.driveSystem.drivePercent(0, 0);
		}
		
		public boolean onTarget() {
			double err = (Math.abs(averageError.getAverage()) + Math.abs(lastError)) / 2;
			return (err < maxError) ;
		}
		

		public boolean onTarget1() {
			double err = (Math.abs(averageError.getAverage()) + Math.abs(lastError)) / 2;
			return (err < maxError);

		}
	
		public boolean onTarget2() {
			if (averageError.isAverageValid()) {
				double err = (Math.abs(averageError.getAverage()) + Math.abs(lastError)) / 2;
				if (err < maxError) return true;
			} else {
				if (numberExecutes > 2 && Math.abs(averageError.getAverage()) < maxError) return true;
			}
			return false;

		}

		public double getAvgError() {
			return averageError.getAverage();
		}

		public double getAvgOutput() {
			return averageOutput.getAverage();
		}
		
		public double getAvgYaw() {
			return averageYaw.getAverage();
		}

		public double getError() {
			return lastError;
		}

		public double lastOuput() {
			return lastOut;
		}

		public void execute() {
			double input = Robot.ahrs.getYaw();
			averageYaw.putNew(input);

			// Use current value, no derivative action on first sample
			if (lastInput == 0) lastInput = input;

			double target = initialTarget;

			double outputMax = outMax;
			double outputMin = outMin;

			double error = target - input;
			lastError = error;
			averageError.putNew(lastError);

			iSum += Ki * error;

			if (iSum > outputMax) iSum = outputMax;
			else if (iSum < outputMin) iSum = outputMin;

			double inputDelta = input - lastInput;

			double output = (Kp * error) + iSum - (Kd * inputDelta);

			if (output > outputMax) output = outputMax;
			else if (output < outputMin) output = outputMin;
			
			
			// Bad code to try and adjust for the discontiuity when out is < .15 and wheels do not move
			double cap = .15;
			if (Math.abs(output) < cap) {
				//output *= 2; // Changed from 1.8 Keith 4/8
				// if (averageYaw.compareLast(4, 0.01))
				//	cap += 0.1;
				output = output < 0 ? -cap : cap;
				Robot.log("Updated Output variance:" + averageError.variance());
			}
			
			Robot.driveSystem.drivePercent(-output, output);
			averageOutput.putNew(output);
			lastOut = output;
			lastInput = input;
			numberExecutes++;
		}
	}
}
