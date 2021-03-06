package cf.client.bluemix;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.Staging;

/**
 * A Java client to deploy applications on Bluemix.
 * 
 * @author Mariana Ramos Franco
 *
 */
public class BluemixClient {
	
	private static final Logger LOGGER = Logger.getLogger(BluemixClient.class.getName());

	private CloudFoundryClient client;
	
	/**
	 * Client constructor.
	 * 
	 * @param user
	 * 			the user name.
	 * @param password
	 * 			the password.
	 * @param orgName
	 * 			the organization.
	 * @param spaceName
	 * 			the space (e.g. "dev").
	 * @param api
	 * 			the API endpoint URL to access Bluemix (e.g. "https://api.ng.bluemix.net").
	 * 
	 * @throws BluemixClientException
	 */
	public BluemixClient(String user, String password, String orgName, String spaceName,
			String api) throws BluemixClientException {
		CloudCredentials credentials = new CloudCredentials(user, password);
		URL target = getTarget(api);
		client = new CloudFoundryClient(credentials, target, orgName, spaceName);
	}
	
	/**
	 * Logging to Bluemix.
	 */
	public void login() {
		LOGGER.info("Logging to BlueMix...");
        client.login();
	}
	
	/**
	 * Logging out from Bluemix.
	 */
	public void logout() {
		LOGGER.info("Logging out from BlueMix...");
        client.logout();
	}
	
	/**
	 * Creates a new application in Bluemix.
	 * 
	 * @param appName
	 * 			the application's name.
	 * @param app
	 * 			the application's file (e.g. a WAR file or zip file).
	 * @param serviceNames
	 * 			array with the name of the services that will be bind to this application or NULL. 
	 * @param command
	 * 			the command used to start the application.
	 * @param buildpack
	 * 			the buildpack that will be used to deploy the application (e.g. liberty-for-java).
	 * 			You can see the list of available buildpacks through the command "cf buildpacks".
	 * @param memory
	 * 			the amount of memory in MB (128, 256, 512, etc) available to the application.
	 * 			If NULL a default value of 512 is used.
	 * 			
	 * @throws BluemixClientException
	 */
	public void createApp(String appName, File app, List<String> serviceNames,
			String command, String buildpack, Integer memory)
					throws BluemixClientException {

		if (null == appName || null == app) {
			String message = "Invalid arguments. appName and app can not be null.";
			throw new BluemixClientException(message);
		}
		
		if (!app.exists()) {
        	String message = "File " + app + "does not exists.";
			throw new BluemixClientException(message);
        }
		
        if (null == memory) {
        	memory = 512;
        }
        LOGGER.info("Creating a new app...");
        String baseUrl = "http://" + appName + ".mybluemix.net/";
        List<String> uris = Arrays.asList(baseUrl);
        client.createApplication(appName, new Staging(command, buildpack),
        		memory, uris, serviceNames);

        try {
        	LOGGER.info("Uploading app...");
			client.uploadApplication(appName, app);
		} catch (IOException e) {
			String message = "Deploy failed. Error: " + e.getLocalizedMessage();
			throw new BluemixClientException(message, e);
		}
        
        client.startApplication(appName);
	}
	
	/**
	 * Updates an application in Bluemix.
	 * 
	 * @param appName
	 * 			the application's name.
	 * @param app
	 * 			the application's file (e.g. a WAR file or zip file).
	 * 
	 * @throws BluemixClientException
	 */
	public void updateApp(String appName, File app) throws BluemixClientException {

		if (null == appName || null == app) {
			String message = "Invalid arguments. appName and app can not be null.";
			throw new BluemixClientException(message);
		}
		
		if (!app.exists()) {
        	String message = "File " + app + "does not exists.";
			throw new BluemixClientException(message);
        }
		
        try {
        	LOGGER.info("Uploading app...");
			client.uploadApplication(appName, app);
		} catch (IOException e) {
			String message = "Deploy failed. Error: " + e.getLocalizedMessage();
			throw new BluemixClientException(message, e);
		}
        
        client.startApplication(appName);
	}
	
	/**
	 * Creates a new service in Bluemix.
	 * Note that you can get the services and plans available in Bluemix through the
	 * "cf marketplace" command. 
	 * 
	 * @param label
	 * 			the service's label (e.g. "sqldb"). 
	 * @param name
	 * 			the service's name.
	 * @param plan
	 * 			the service's plan.
	 * 
	 * @throws BluemixClientException
	 */
	public void createService(String label, String name, String plan)
			throws BluemixClientException {
		
		if (null == label || null == name || null == plan) {
			String message = "Invalid arguments. label, name and plan can not be null.";
			throw new BluemixClientException(message);
		}
		
		LOGGER.info("Creating a new service...");
        CloudService service = new CloudService();
        service.setLabel(label);
        service.setName(name);
        service.setPlan(plan);
        client.createService(service);
	}
	
	/**
	 * Starts an application in Bluemix.
	 * 
	 * @param appName
	 * 			the application's name.
	 */
	public void startApp(String appName) {
		client.startApplication(appName);
	}
	
	/**
	 * Stops an application in Bluemix.
	 * 
	 * @param appName
	 * 			the application's name.
	 */
	public void stopApp(String appName) {
		client.stopApplication(appName);
	}
	
	/**
	 * Deletes an application from Bluemix.
	 * 
	 * @param appName
	 * 			the application's name.
	 */
	public void deleteApp(String appName) {
		LOGGER.info("Deleting app...");
		client.deleteApplication(appName);
	}
	
	/**
	 * Deletes a service from Bluemix.
	 * 
	 * @param service
	 * 			the service's name.
	 */
	public void deleteService(String service) {
		LOGGER.info("Deleting service...");
		client.deleteService(service);
	}
	
	/**
	 * Returns the application's state (e.g. START or STOP).
	 * 
	 * @param appName
	 * 			the application's name.
	 * @return the application's state.
	 * 
	 * @throws BluemixClientException
	 */
	public String getAppState(String appName) throws BluemixClientException {
		try {
			return client.getApplication(appName).getState().toString();
		} catch (Exception e) {
			String message = "Could not get the app state. Error: " + e.getLocalizedMessage();
			throw new BluemixClientException(message, e);
		}
	}
	
	/**
	 * Returns the CloudFoundClient instance that can be used to perform other operations
	 * in Bluemix.
	 * 
	 * @return the CloundFoundClient instance
	 */
	public CloudFoundryClient getClient() {
		return client;
	}
	
	private static URL getTarget(String api) throws BluemixClientException {
		try {
			return URI.create(api).toURL();
		} catch (MalformedURLException e) {
			String message = "Invalid target URI. Error: " + e.getLocalizedMessage();
			throw new BluemixClientException(message, e);
		}
	}
}
