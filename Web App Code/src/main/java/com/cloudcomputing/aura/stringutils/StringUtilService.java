package com.cloudcomputing.aura.stringutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudcomputing.aura.maps.utils.MapsDirectionsService;
import com.cloudcomputing.aura.utils.Constants;
import com.cloudcomputing.aura.utils.ErrorMessageConstants;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.TravelMode;

/**
 * @author Ramanathan and Suraj
 * Service class that supports string operations in the context of this product
 * 
 */
public class StringUtilService {
	/**
	 * Default Constructor
	 */
	public StringUtilService() {
	}

	/**
	 * This function is used to convert the original message into a map
	 * in order to facilitate the access of objects like origin, destination etc.
	 * This function performs a very raw manual parsing of the input message.
	 * This will be deprecated in the future.
	 * 
	 * @param input which is the message from the customer through SMS
	 * @return HashMap of the parsed message
	 */
	public static HashMap<String, String> parseMessageInput(String input) {
		String[] text = input.split(" ");
		HashMap<String, String> parsedInputMap = new HashMap<String, String>();
		for (String t : text) {
			System.out.println(t);
			String[] strings = t.split(":");
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].equalsIgnoreCase("origin")) {
					System.out.println("Origin is :" + strings[i + 1]);
					parsedInputMap.put("origin", strings[i+1]);
				}
				if (strings[i].equalsIgnoreCase("destination")) {
					System.out.println("Destination is :" + strings[i + 1]);
					parsedInputMap.put("destination", strings[i+1]);
				}
				if (strings[i].equalsIgnoreCase("mode")) {
					System.out.println("Mode is :" + strings[i + 1]);
					try {
						TravelMode mode = TravelMode.valueOf(strings[i + 1].toUpperCase());
						System.out.println(mode);
						System.out.println("Mode is :" + mode);
						parsedInputMap.put("mode", strings[i + 1].toUpperCase());
					} catch (Exception e) {
						System.out.println("Invalid Travel Mode");
						System.out.println("Allowed URLS are : ");
						TravelMode[] modes = TravelMode.values();
						for (TravelMode m : modes) {
							System.out.print(m.toString().toUpperCase() + " ");
						}
					}
				}
			}
		}
		return parsedInputMap;
	}

	/**
	 * This function is used to convert the original message into a json object 
	 * in order to facilitate the access of objects like origin, destination etc.
	 * This function reduces the efforts in parsing the raw string from customer
	 * 
	 * @param input which is the message from the customer through SMS
	 * @return Json Object of the message
	 * 
	 */
	public static JSONObject convertMessageInputIntoJson(String input) {
		StringBuilder jsonMatcher = new StringBuilder("{");
		jsonMatcher.append(input).append("}");
		JSONObject jsonObject = new JSONObject(jsonMatcher.toString());
		return jsonObject;
	}

	/**
	 * 
	 * This function is used to split the html instructions by sentences whose length is
	 * less than or equal to 122. In paid account of twilio this lenth will be converted into 
	 * 160
	 * 
	 * @param instructions - the cleaned instructions from MapDirectionsService 
	 * @return an array list of logically split instructions 
	 *   
	 */
	public static ArrayList<String> logicallySplitInstructions(String instructions) {
		System.out.println("BEFORE:");
		System.out.println(instructions);
		String[] sentences = instructions.split("\n");
		System.out.println("\nAFTER:");
		ArrayList<String> messages = new ArrayList<String>();
		StringBuilder message = new StringBuilder("\n");
		for(int i = 0 ; i< sentences.length ; i++) {
			message.append("\n").append(i+1).append(". ").append(sentences[i]).append("\n");
			if((i+1 < sentences.length) && (message.length()+sentences[i+1].length() > Constants.allowedMessageSize)) { 
				//this shall be changed to 150 later after converting to premium account in twilio
				messages.add(message.toString());
				message = new StringBuilder();
			}
		}
		messages.add(message.toString());
		System.out.println(messages.size());
		for(String m : messages) {
			System.out.println("Messages:");
			System.out.println(m);
		}
		System.out.println("Done");
		return messages;
	}

	/**
	 * This function checks for the semantics of the input message from the customer 
	 * 
	 * @param Json object of the input message from the customer
	 * @return null or the error message pertaining to the issue in the verification of the semantics
	 * 
	 */
	public static String checkSemanticsOfParsedInput(JSONObject parsedInput) {
		String errorMessage = null;
		try {
			parsedInput.get("origin");
			parsedInput.get("destination");
		} catch(JSONException jsonException) {
			errorMessage = ErrorMessageConstants.SemanticsError;
			return errorMessage;
		}
		
		try {
			TravelMode mode = TravelMode.valueOf(parsedInput.get("mode").toString().toUpperCase());
		} catch(IllegalArgumentException illegalArgumentException) {
			errorMessage = "Invalid Mode. Allowed Values are: ";
			errorMessage += MapsDirectionsService.getAllowedTravelModes();
			return errorMessage;
		} catch(JSONException jsonException) {
		}
		
		try {
			RouteRestriction routeRestriction = RouteRestriction.valueOf(parsedInput.getString("avoid").toString().toUpperCase()); 
		} catch(IllegalArgumentException illegalArgumentException) {
			errorMessage = "Invalid Avoid Paramater. Allowed Values are: ";
			errorMessage += MapsDirectionsService.getAllowedRouteRestrictionParameters();
			return errorMessage;
		} catch(JSONException jsonException) {
		}
		return errorMessage;
	}

	/**
	 * This function formats the first N results of the Places Result Array
	 * 
	 * @param placesSearchResult - array of results from Google Places
	 * @param limit - number of results to be formatted in order to send to customer
	 * 
	 * @return The final list of ordered results of the various places that the customer might be 
	 * searching for.
	 * 
	 */
	
	public static String formatFirstNPlacesResults(PlacesSearchResult[] placesSearchResult, int limit) {
		StringBuilder places = new StringBuilder(Constants.SPACE);
		int maxResultLimit = (limit < placesSearchResult.length) ? limit : placesSearchResult.length;
		for (int i = 0; i < maxResultLimit; i++) {
			places.append(i + 1).append(Constants.DOT);
			places.append(Constants.placeName).append(placesSearchResult[i].name).append(Constants.COMMA);
			places.append(Constants.placeAddress).append(placesSearchResult[i].formattedAddress).append(Constants.COMMA);
			if(placesSearchResult[i].openingHours != null) {
				if(placesSearchResult[i].openingHours.openNow) {
					places.append(Constants.placeOpenStatus).append(Constants.placeOpenStatus_Open);
				} else {
					places.append(Constants.placeOpenStatus).append(Constants.placeOpenStatus_Close);
				}
			} else {
				places.append(Constants.placeOpenStatus).append(Constants.placeOpenStatus_Unavailable);
			}
			places.append(Constants.NEWLINE);
		}
		return places.toString();
	}

	/**
	 * This function obtains the current date time of the server as a string object in the given format.
	 * @return string form of the current time
	 */
	public static String getCurrentTimeAsString() {
		DateFormat dateFormat = new SimpleDateFormat(Constants.dateFormat);
		Date date = new Date();
		return dateFormat.format(date);
	}
}
