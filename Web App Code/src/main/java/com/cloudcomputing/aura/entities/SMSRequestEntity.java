package com.cloudcomputing.aura.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * @author Ramanathan and Suraj
 * 
 * This class is defined as an entity which will be saved in the 
 * google cloud datastore. This class refers to the request object that will
 * be sent to this server from Twilio server
 * 
 */
@Entity
public class SMSRequestEntity {
	@Id
	private Long Id;

	String RequestType;
	String From;
	String Message;
	String ResponseStatus;
	String Time;
	String TypeOfErrorMessage;

	public String getFrom() {
		return From;
	}

	public void setFrom(String from) {
		From = from;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getTypeOfErrorMessage() {
		return TypeOfErrorMessage;
	}

	public void setTypeOfErrorMessage(String errorMessage) {
		TypeOfErrorMessage = errorMessage;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getRequestType() {
		return RequestType;
	}

	public void setRequestType(String requestType) {
		RequestType = requestType;
	}

	public String getResponseStatus() {
		return ResponseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		ResponseStatus = responseStatus;
	}

}
