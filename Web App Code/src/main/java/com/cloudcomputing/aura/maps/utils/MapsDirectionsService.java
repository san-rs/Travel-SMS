package com.cloudcomputing.aura.maps.utils;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

/**
 * @author Ramanathan and Suraj
 * Service class that supports operations related Google Maps Directions API
 * 
 */
public class MapsDirectionsService {
	DirectionsApiRequest directionsApiRequest;
	GeoApiContext context;

	/**
	 * @param apiKey
	 * Constructor that instantiates the GeoApiContext with the API KEY to authenticate the Maps API operations
	 * 
	 */
	public MapsDirectionsService(String apiKey) {
		context = new GeoApiContext(new GaeRequestHandler());
		context.setApiKey(apiKey);
	}

	/**
	 * Function to retrieve the directions using google maps directions API and extract the html instructions 
	 * and clean it for customer readability
	 * 
	 * @param origin
	 * @param destination
	 * @param travelMode
	 * 
	 * @return the extracted html instructions modified with distance and time
	 * as additional modification for customer understandability
	 * 
	 * @throws ApiException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public String getDirections(String origin, String destination, TravelMode travelMode, RouteRestriction routeRestriction)
			throws ApiException, InterruptedException, IOException {
		if (travelMode == null) {
			travelMode = TravelMode.DRIVING;
		}
		DirectionsApiRequest request = DirectionsApi.newRequest(context).origin(origin).destination(destination)
				.mode(travelMode);
		if (routeRestriction != null) {
			request.avoid(routeRestriction);
		}
		DirectionsResult result = request.await();
		DirectionsLeg[] legs = result.routes[0].legs;
		StringBuilder instructions = new StringBuilder();
		for (DirectionsLeg leg : legs) {
			DirectionsStep[] steps = leg.steps;
			for (DirectionsStep step : steps) {
				// System.out.println(step.htmlInstructions);
				String distance = step.distance.humanReadable;
				String time = step.duration.humanReadable;
				String instruction = Jsoup.parse(step.htmlInstructions).text();
				instruction += " and travel for " + distance + " (" + time + ")";
				// System.out.println("\nCLEANED:" + instruction);
				instructions.append(instruction).append("\n");
			}
		}
		System.out.println(instructions.length());
		return instructions.toString();
	}

	/**
	 * @return The various possible allowed travel modes that are supported by Google Maps
	 * By this way, this product will complement to any future updates from Google Maps for
	 * additional travel modes support
	 */
	public static String getAllowedTravelModes() {
		TravelMode[] travelModes = TravelMode.values();
		StringBuilder allowedModes = new StringBuilder();
		for (TravelMode travelMode : travelModes) {
			if (!travelMode.equals(TravelMode.UNKNOWN)) {
				allowedModes.append("\'").append(travelMode.name().toLowerCase()).append("\' ");
			}
		}
		return allowedModes.toString();
	}

	/**
	 * @return The various possible allowed route restrictions that are supported by Google Maps
	 * By this way, this product will complement to any future updates from Google Maps for
	 * additional route restrictions support
	 */
	public static String getAllowedRouteRestrictionParameters() {
		RouteRestriction[] routeRestrictions = RouteRestriction.values();
		StringBuilder allowedRestrictions = new StringBuilder();
		for (RouteRestriction routeRestriction : routeRestrictions) {
			if (!routeRestriction.equals(TravelMode.UNKNOWN)) {
				allowedRestrictions.append("\'").append(routeRestriction.name().toLowerCase()).append("\' ");
			}
		}
		return allowedRestrictions.toString();
	}
}
