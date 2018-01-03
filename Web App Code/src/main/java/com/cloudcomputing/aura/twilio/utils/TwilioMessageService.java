package com.cloudcomputing.aura.twilio.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.cloudcomputing.aura.utils.Constants;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;

/**
 * @author Ramanathan and Suraj
 * Service class that supports operations related Twilio APIs
 *
 */
public class TwilioMessageService {
	private String twilioAccountSid;
	private String twilioAuthToken;

	/**
	 * @return Twilio Account Secure ID
	 */
	public String getTwilioAccountSid() {
		return twilioAccountSid;
	}

	/**
	 * @param twilioAccountSid
	 */
	public void setTwilioAccountSid(String twilioAccountSid) {
		this.twilioAccountSid = twilioAccountSid;
	}

	/**
	 * @return Twilio Account Authorization token
	 */
	public String getTwilioAuthToken() {
		return twilioAuthToken;
	}

	/**
	 * @param twilioAuthToken
	 */
	public void setTwilioAuthToken(String twilioAuthToken) {
		this.twilioAuthToken = twilioAuthToken;
	}

	/**
	 * Constructor that initializes the authorization token and account ID of the twilio account 
	 * to authenticate the twilio API operations
	 * 
	 * @param twilioAccountSid
	 * @param twilioAuthToken
	 */
	public TwilioMessageService(String twilioAccountSid, String twilioAuthToken) {
		this.twilioAccountSid = twilioAccountSid;
		this.twilioAuthToken = twilioAuthToken;
	}

	/**
	 * Function to send SMS to the intended customer's mobile mobile with the message information
	 * 
	 * @param message
	 * @param toNumber - destination mobile number
	 * @param twilioNumber - source mobile number which is allocated by twilio
	 * @throws TwilioRestException
	 */
	public void sendSmsTo(String message, String toNumber, String twilioNumber) throws Exception {
		TwilioRestClient client = new TwilioRestClient(twilioAccountSid, twilioAuthToken);
		Account account = client.getAccount();
		MessageFactory messageFactory = account.getMessageFactory();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("To", toNumber));
		params.add(new BasicNameValuePair("From", twilioNumber));
		params.add(new BasicNameValuePair("Body", message));
		try {
			Message sms = messageFactory.create(params);
			System.out.println(sms.getBody());
		} catch (TwilioRestException e) {
			throw new Exception("Twilio error", e);
		}
	}

	/**
	 * Function to add page reference to messages in order to give the 
	 * customer a logical idea of which message is being received currently since twilio doesn't
	 * assure the in order delivery of messages 
	 * 
	 * @param arraylist of messages
	 * @return arraylist of messages with page reference added
	 */
	public ArrayList<String> addPageReferenceToMessages(ArrayList<String> messages) {
		String totalNumberOfMessages = Integer.toString(messages.size());
		StringBuilder pageReference;
		for (int i = 0; i < messages.size(); i++) {
			String currentMessageNumber = Integer.toString(i + 1);
			pageReference = new StringBuilder(currentMessageNumber);
			pageReference.append(Constants.FORWARD_SLASH);
			pageReference.append(totalNumberOfMessages);
			pageReference.append(Constants.SPACE);
			messages.set(i, pageReference.append(messages.get(i)).toString());
		}
		return messages;
	}
}
