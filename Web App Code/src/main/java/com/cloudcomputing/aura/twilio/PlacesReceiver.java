package com.cloudcomputing.aura.twilio;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudcomputing.aura.datastore.DataStoreService;
import com.cloudcomputing.aura.entities.SMSRequestEntity;
import com.cloudcomputing.aura.maps.utils.MapsPlacesService;
import com.cloudcomputing.aura.stringutils.StringUtilService;
import com.cloudcomputing.aura.twilio.utils.TwilioMessageService;
import com.cloudcomputing.aura.utils.Constants;
import com.cloudcomputing.aura.utils.ErrorMessageConstants;
import com.cloudcomputing.aura.utils.ResponseStatus;
import com.cloudcomputing.aura.utils.RequestType;
import com.googlecode.objectify.ObjectifyService;

/**
 * @author Ramanathan and Suraj
 * 
 * This Class is a servlet written for the web server. This end point will be configured to the SMS receiver of Twilio server for ASU Account
 * which was created to provide Google Maps Places as a Service through SMS. This ensures the communication from Twilio Server to web server hosted 
 * in google app engine. Hence twilio re directs each message received to the allocated number to this end point
 *
 */
public class PlacesReceiver extends HttpServlet {
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String fromNumber = request.getParameter("From");
		String body = request.getParameter("Body");
		final String apiKey = System.getenv("GOOGLE_MAPS_PLACES_API_KEY");
		MapsPlacesService mapsPlacesService = new MapsPlacesService(apiKey);
		String places = mapsPlacesService.getPlaces(body);
		System.out.println(places);
		System.out.println();
		System.out.println(places.length());
		
		final String twilioAccountSid = System.getenv("TWILIO_ASU_ACCOUNT_SID_LIVE");
		final String twilioAuthToken = System.getenv("TWILIO_ASU_AUTH_TOKEN_LIVE");
		final String twilioNumber = System.getenv("TWILIO_ASU_NUMBER");
		TwilioMessageService twilioMessageService = new TwilioMessageService(twilioAccountSid, twilioAuthToken);
		SMSRequestEntity smsRequestEntity = new SMSRequestEntity();
		smsRequestEntity.setRequestType(RequestType.PLACES.detail());
		smsRequestEntity.setMessage(body);
		smsRequestEntity.setTime(StringUtilService.getCurrentTimeAsString());
		smsRequestEntity.setFrom(fromNumber);
		try {
			twilioMessageService.sendSmsTo(places, fromNumber, twilioNumber);
			smsRequestEntity.setResponseStatus(ResponseStatus.SUCCESS.detail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			smsRequestEntity.setResponseStatus(ResponseStatus.FAILURE.detail());
			smsRequestEntity.setTypeOfErrorMessage(e.getMessage());
		} finally {
			try {
				if(!places.equals(ErrorMessageConstants.NoPlacesAvailable)) {
					twilioMessageService.sendSmsTo(Constants.ThankYouSMSForPlaces, fromNumber, twilioNumber);
					smsRequestEntity.setResponseStatus(ResponseStatus.SUCCESS.detail());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				smsRequestEntity.setResponseStatus(ResponseStatus.FAILURE.detail());
				smsRequestEntity.setTypeOfErrorMessage(e.getMessage());
			}
		}
		DataStoreService ds = new DataStoreService();
		ds.saveSMSRequestEntity(smsRequestEntity);
		System.out.println("Saved it");
		response.setContentType("text/plain");
		response.getWriter().println(places);
	}
}
