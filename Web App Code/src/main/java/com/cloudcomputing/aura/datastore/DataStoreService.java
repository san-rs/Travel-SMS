package com.cloudcomputing.aura.datastore;

import com.cloudcomputing.aura.entities.SMSRequestEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.ObjectifyService;

/**
 * @author Ramanathan and Suraj
 * 
 * This class is a wrapper on top of the Google Cloud's Datastore service
 *
 */
public class DataStoreService {
	private DatastoreService ds;
	
	public DataStoreService() {
		ds = DatastoreServiceFactory.getDatastoreService();
	}

	/**
	 * @param Entity
	 * Method to save the Entity object sent
	 * 
	 */
	public void saveEntity(Entity entity) {
		ds.put(entity);
	}

	public void saveSMSRequestEntity(SMSRequestEntity smsRequestEntity) {
		ObjectifyService.ofy().save().entity(smsRequestEntity).now();
	}
}
