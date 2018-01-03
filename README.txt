1) There are two folders 
	a) Web App Code
		i) This holds the code for the web application that was hosted in the google app engine which serves the requests redirected from the twilio server
		ii) There is a file named “Package_Structure.png” to describe the package structure of the code in this folder
	b) Android App Code
		i) This holds the code for the android application that was written to obtain the current GPS location of the mobile phone
		ii) Location of MainActivity.java => /Android App Code/src/main/java/com/example/surajrox/project

2) No hard coding on data in code was done, all data was maintained in a neat structured Constants and Error Constants classes and Enum classes were created as required.

3) Industry level coding standards was followed, in terms of class and method documentation and creation of logical package structures.

4) Steps were taken to fix issues related to parsing the Google Maps Services API Response due to bug in Google App Engine code.
	i) Reference: https://github.com/googlemaps/google-maps-services-java/issues/218
	ii) Two classes of the google app engine code was recreated and we fixed the bug ourselves. These two files are GaePendingResult.java and EncodedPolylineInstanceCreator.java
	    and are in the location /Web App Code/src/main/java/com/google/maps/internal

5) There were issues in the in order arrival of messages by Twilio PaaS Services and hence we created ticket to twilio and followed the solution provided by them which was adding the page reference to the messages.

6) A Customer documentation was created keeping the customer in mind and train them to use the product. (Please refer to Travel_SMS_Customer_Handout.pdf in the same folder as this README file)

7) The allowed values for “avoid” and “mode” parameters are taken dynamically from google maps, by this way, when google provides more allowed values for support in the future, the customer will come to know that automatically and he will start using it, thus requiring no code changes for our product thus making this application a more adaptive type application by itself.

8) We have added a file called “Screenshots.pdf” which has all the screenshots of the app.