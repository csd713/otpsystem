/**
 * File created by csd on Oct 16, 2017
 */
package com.nullwelldev.twilio.otpsystem.model;

public class OtpSystem {
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the otpMessage
	 */
	public String getOtpMessage() {
		return otpMessage;
	}

	/**
	 * @param otpMessage
	 *            the otpMessage to set
	 */
	public void setOtpMessage(String otpMessage) {
		this.otpMessage = otpMessage;
	}

	/**
	 * @return the otpExpiryTime
	 */
	public long getOtpExpiryTime() {
		return otpExpiryTime;
	}

	/**
	 * @param otpExpiryTime
	 *            the otpExpiryTime to set
	 */
	public void setOtpExpiryTime(long otpExpiryTime) {
		this.otpExpiryTime = otpExpiryTime;
	}

	private String mobileNumber;
	private String otpMessage;
	private long otpExpiryTime;

}
