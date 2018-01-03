/*
 * Copyright (c) 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.cloudcomputing.aura;

import com.cloudcomputing.aura.datastore.DataStoreService;
import com.cloudcomputing.aura.entities.SMSRequestEntity;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.ObjectifyService;

// [START example]
/**
 * Generate some simple information.
 */
/**
 * @author Ramanathan and Suraj
 * Basic Class of Google App Engine Maven Project - scaffolding 
 */
public class HelloInfo {

	/**
	 * @returns the basic information of the Project Team 
	 */
	public static String getInfo() {
		StringBuilder projectMates = new StringBuilder();
		projectMates.append("Ramanathan Nachiappan - 1210822532").append("\n");
		projectMates.append("Suraj Ravishankar - 1210973176").append("\n");
		ObjectifyService.register(SMSRequestEntity.class);
		return projectMates.toString();
	}
}
// [END example]
