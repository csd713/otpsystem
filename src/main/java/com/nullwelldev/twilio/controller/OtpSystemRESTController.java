/**
 * File created by csd on Oct 16, 2017
 */
package com.nullwelldev.twilio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nullwelldev.twilio.otpsystem.model.OtpSystem;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
public class OtpSystemRESTController {

	private Map<String, OtpSystem> otp_data = new HashMap<>();
	private static String ACCOUNT_SID;
	private static String AUTH_ID;
	private static String MY_NUMBER;
	private static String TWILIO_NUMBER;

	static {
		try {
			File file = new File("twilio.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			ACCOUNT_SID = properties.getProperty("TWILIO_ACCOUNT_SID");
			AUTH_ID = properties.getProperty("TWILIO_AUTH_TOKEN");
			MY_NUMBER = properties.getProperty("MY_PHONE_NUMBER");
			TWILIO_NUMBER = properties.getProperty(TWILIO_NUMBER);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Twilio.init(ACCOUNT_SID, AUTH_ID);

	}

	@RequestMapping(value = "/mobilenumber/{mobilenumber}/otp", method = RequestMethod.POST)
	public ResponseEntity<Object> sendOTP(@PathVariable("mobilenumber") String mobilenumber) {

		OtpSystem otpSystem = new OtpSystem();

		otpSystem.setMobileNumber(mobilenumber);
		otpSystem.setOtpMessage(String.valueOf(((int) (Math.random() * (99999 - 10001))) + 10001));
		// setting it to 60 seconds
		otpSystem.setOtpExpiryTime(System.currentTimeMillis() + 60000);

		otp_data.put(mobilenumber, otpSystem);

		Message.creator(new PhoneNumber(MY_NUMBER), new PhoneNumber(TWILIO_NUMBER),
				"your OTP is: " + otpSystem.getOtpMessage()).create();
		return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
	}

	@RequestMapping(value = "/mobilenumber/{mobilenumber}/otp", method = RequestMethod.PUT)
	public ResponseEntity<Object> verifyOTP(@PathVariable("mobilenumber") String mobilenumber,
			@RequestBody OtpSystem requestBodyotpSystem) {
		
		//check if sent OTP is empty
		if(requestBodyotpSystem.getOtpMessage()==null || requestBodyotpSystem.getOtpMessage().trim().length()<=0){
			return new ResponseEntity<>("Please provide OTP!", HttpStatus.BAD_REQUEST);
	
		}

		if (otp_data.containsKey(mobilenumber)) {
			OtpSystem otpSystem = otp_data.get(mobilenumber);
			if (otpSystem != null) {
				if(otpSystem.getOtpExpiryTime()> System.currentTimeMillis()){
					if(requestBodyotpSystem.getOtpMessage().equals(otpSystem.getOtpMessage())){
						otp_data.remove(mobilenumber);
						return new ResponseEntity<>("SUCCESS, OTP matched", HttpStatus.OK); 
					}
					return new ResponseEntity<>("FAILED, OTP did not match!", HttpStatus.BAD_REQUEST);

				}
				return new ResponseEntity<>("OTP has expired!", HttpStatus.BAD_REQUEST);


			}
			return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);

		}
		return new ResponseEntity<>("Mobile Number not found", HttpStatus.NOT_FOUND);

	}
}
