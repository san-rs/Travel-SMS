package com.cloudcomputing.aura.utils;

import com.cloudcomputing.aura.maps.utils.MapsDirectionsService;

/**
 * @author Ramanathan and Suraj
 * This Class defines the error message constants to be used across the code base to maintain uniformity
 *
 */
public class ErrorMessageConstants {
	public final static String InternalError = "Internal Error. Please Try Again Later!";
	public final static String SemanticsError = "Please follow semantics: \'origin:<origin>,destination:<destination>,mode:<mode>\'.Do Not include \',\' within the values.";
	public final static String InvalidModeError = "Invalid Mode. Allowed Values are: "+ MapsDirectionsService.getAllowedTravelModes() +".";
	public final static String InvalidRouteRestrictionError = "Invalid Restriction for \'avoid\' parameter. Allowed Values are: "+ MapsDirectionsService.getAllowedRouteRestrictionParameters() +".";
	public final static String UnavailableRouteError = "Route Not available. Please refine your origin | destination in query.";
	public final static String NoPlacesAvailable = "No Places Available! Please Refine your Query!";
}
