package com.cloudcomputing.aura.twilio;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudcomputing.aura.datastore.DataStoreService;
import com.cloudcomputing.aura.entities.SMSRequestEntity;
import com.cloudcomputing.aura.maps.utils.MapsDirectionsService;
import com.cloudcomputing.aura.stringutils.StringUtilService;
import com.cloudcomputing.aura.twilio.utils.TwilioMessageService;
import com.cloudcomputing.aura.utils.Constants;
import com.cloudcomputing.aura.utils.ErrorMessageConstants;
import com.cloudcomputing.aura.utils.RequestType;
import com.cloudcomputing.aura.utils.ResponseStatus;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.errors.ApiException;
import com.google.maps.model.TravelMode;
import com.googlecode.objectify.ObjectifyService;

/**
 * @author Ramanathan and Suraj
 * 
 * This Class is a servlet written for the web server. This end point will be configured to the SMS receiver of Twilio server.
 * This ensures the communication from Twilio Server to web server hosted in google app engine. Hence twilio re directs each message 
 * received to the allocated number to this end point
 * 
 *
 */
public class Receiver extends HttpServlet {
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String fromNumber = request.getParameter("From");
		String body = request.getParameter("Body");
		String errorMessage = null;
		String instructions = "";
		String messageStatus = "";
		//HashMap<String, String> map = StringUtilService.parseMessageInput(body);
		JSONObject parsedInput = null;
		final String twilioAccountSid = System.getenv("TWILIO_ACCOUNT_SID");
		final String twilioAuthToken = System.getenv("TWILIO_AUTH_TOKEN");
		final String twilioNumber = System.getenv("TWILIO_NUMBER");
		final String apiKey = System.getenv("GOOGLE_MAPS_DIRECTION_API_KEY");
		TwilioMessageService twilioMessageService = new TwilioMessageService(twilioAccountSid, twilioAuthToken);
		MapsDirectionsService mapsDirectionsService = new MapsDirectionsService(apiKey);
		SMSRequestEntity smsRequestEntity = new SMSRequestEntity();
		smsRequestEntity.setRequestType(RequestType.MAPS.detail());
		smsRequestEntity.setMessage(body);
		smsRequestEntity.setTime(StringUtilService.getCurrentTimeAsString());
		smsRequestEntity.setFrom(fromNumber);
		
		try {
			parsedInput = StringUtilService.convertMessageInputIntoJson(body);
		} catch(JSONException exception) {
			errorMessage = ErrorMessageConstants.SemanticsError;
		}
		
		if(errorMessage == null) {
			errorMessage = StringUtilService.checkSemanticsOfParsedInput(parsedInput);
		}
		
		if(errorMessage == null) {
			TravelMode mode = TravelMode.DRIVING; // Default value
			RouteRestriction routeRestriction = null; 
			try {
				mode = TravelMode.valueOf(parsedInput.get("mode").toString().toUpperCase());
			} catch(JSONException jsonException) {
			}
			try {
				routeRestriction = RouteRestriction.valueOf(parsedInput.get("avoid").toString().toUpperCase());
			} catch(JSONException jsonException) {
			}
			try {
				instructions = mapsDirectionsService.getDirections(parsedInput.get("origin").toString(),
						parsedInput.get("destination").toString(), mode, routeRestriction);
			} catch (ApiException e1) {
				e1.printStackTrace();
				errorMessage = ErrorMessageConstants.InternalError;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				errorMessage = ErrorMessageConstants.InternalError;
			}
			if(instructions.length() < 10) {
				errorMessage = ErrorMessageConstants.UnavailableRouteError;
			}
		}
		
		if(errorMessage == null) {
			ArrayList<String> messages = StringUtilService.logicallySplitInstructions(instructions);
			System.out.println(messages);
			messages = twilioMessageService.addPageReferenceToMessages(messages);
			try {
				for(String m : messages) {
					twilioMessageService.sendSmsTo(m, fromNumber, twilioNumber);
					System.out.println(m);
					Thread.sleep(2000);
				}
				Thread.sleep(2000);
				twilioMessageService.sendSmsTo(Constants.ThankYouSMSForDirections, fromNumber, twilioNumber);
				//String test160 = "I wrote my own Instance Creator for EcodedPolyline class and added it to the library. Then as suggested I registered it wit";
				smsRequestEntity.setResponseStatus(ResponseStatus.SUCCESS.detail());
				messageStatus = "MESSAGE SENT TO : ";
			} catch (Exception e) {
				e.printStackTrace();
				messageStatus = "MESSAGE NOT SENT TO : ";
			}
		} else {
			try {
				System.out.println(errorMessage);
				twilioMessageService.sendSmsTo(errorMessage, fromNumber, twilioNumber);
				smsRequestEntity.setResponseStatus(ResponseStatus.FAILURE.detail());
				smsRequestEntity.setTypeOfErrorMessage(errorMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		DataStoreService ds = new DataStoreService();
		ds.saveSMSRequestEntity(smsRequestEntity);
		System.out.println("Saved it");
		response.setContentType("text/plain");
		if(errorMessage == null) {
			response.getWriter().println(messageStatus + fromNumber + "\n" + instructions);
		} else {
			response.getWriter().println(errorMessage);
		}
	}
}
