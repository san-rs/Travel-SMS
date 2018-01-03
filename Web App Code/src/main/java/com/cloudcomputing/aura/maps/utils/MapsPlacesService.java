package com.cloudcomputing.aura.maps.utils;

import java.io.IOException;

import javax.naming.Context;

import com.cloudcomputing.aura.stringutils.StringUtilService;
import com.cloudcomputing.aura.utils.Constants;
import com.cloudcomputing.aura.utils.ErrorMessageConstants;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

/**
 * @author Ramanathan and Suraj
 * Service class that supports operations related Google Maps Places API
 * 
 */

public class MapsPlacesService {
	GeoApiContext context;

	/**
	 * @param apiKey
	 * Constructor that instantiates the GeoApiContext with the API KEY to authenticate the Maps API operations
	 * 
	 */
	public MapsPlacesService(String apiKey) {
		context = new GeoApiContext(new GaeRequestHandler());
		context.setApiKey(apiKey);
	}

	/**
	 * Function to retrieve the various places using google maps places API 
	 * and clean it for customer readability
	 * 
	 * @param query
	 * 
	 * @return the extracted places search result with ordering and formatting 
	 * for customer understandability
	 */
	public String getPlaces(String query) {
		String places = "";
		try {
			PlacesSearchResponse placesSearchResponse = PlacesApi.textSearchQuery(context, query).await();
			PlacesSearchResult[] results = placesSearchResponse.results;
			places = StringUtilService.formatFirstNPlacesResults(results, Constants.resultLimit);
			System.out.println("No Error");
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			places = ErrorMessageConstants.NoPlacesAvailable;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			places = ErrorMessageConstants.NoPlacesAvailable;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			places = ErrorMessageConstants.NoPlacesAvailable;
		}
		if(places.length() == 0) {
			places = ErrorMessageConstants.NoPlacesAvailable;
		}
		return places;
	}
}
