package com.google.maps.internal;

import com.google.gson.InstanceCreator;
import com.google.maps.model.EncodedPolyline;

import java.lang.reflect.Type;

/**
 * @author Ramanathan and Suraj
 * This class was written in order to define the Instance Creator which was missing for gson to decode the information
 * sent as part of this class by the google maps API
 *
 */
public class EncodedPolylineInstanceCreator implements InstanceCreator<EncodedPolyline> {
	private String points;

	public EncodedPolylineInstanceCreator(String points) {
		this.points = points;
	}

	@Override
	public EncodedPolyline createInstance(Type type) {
		return new EncodedPolyline(points);
	}
}
