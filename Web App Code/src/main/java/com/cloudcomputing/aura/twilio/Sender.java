package com.cloudcomputing.aura.twilio;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudcomputing.aura.twilio.utils.TwilioMessageService;

public class Sender extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		final String twilioAccountSid = System.getenv("TWILIO_ACCOUNT_SID");
		final String twilioAuthToken = System.getenv("TWILIO_AUTH_TOKEN");
		final String twilioNumber = System.getenv("TWILIO_NUMBER");
		//StringBuilder twilioData = new StringBuilder(twilioAccountSid + " ");
		//twilioData.append(twilioAuthToken + " ");
		//twilioData.append(twilioNumber + " ");

		final String toNumber = (String) request.getParameter("to");
		final String message = (String) request.getParameter("message");

		if (toNumber == null) {
			response.getWriter().print("Please provide the number to message in the \"to\" query string parameter.");
			return;
		}
		if (message == null || message.trim().equals("")) {
			response.getWriter().print("Please provide the number to message in the \"to\" query string parameter.");
			return;
		}
		TwilioMessageService twilioMessageService = new TwilioMessageService(twilioAccountSid, twilioAuthToken);
		String messageStatus = "";
		try {
			twilioMessageService.sendSmsTo(message, toNumber, twilioNumber);
			messageStatus = "MESSAGE SENT TO : ";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageStatus = "MESSAGE NOT SENT TO : ";
		}
		response.setContentType("text/plain");
		response.getWriter().println(messageStatus + toNumber);
		
		
		response.setContentType("text/plain");
		response.getWriter().println("DONE");
		
	}

}
