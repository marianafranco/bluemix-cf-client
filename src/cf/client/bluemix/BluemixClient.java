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
 * 
 * 
 * @author marianafranco
 *
 */
public class BluemixClient {
	
	private static final Logger LOGGER = Logger.getLogger(BluemixClient.class.getName());

	private CloudFoundryClient client;
	
	public BluemixClient(String user, String password, String orgName, String spaceName, String api)
			throws BluemixClientException {
		CloudCredentials credentials = new CloudCredentials(user, password);
		URL target = getTarget(api);
		client = new CloudFoundryClient(credentials, target, orgName, spaceName);
	}
	
	public void login() {
		LOGGER.info("Loging to BlueMix...");
        client.login();
	}
	
	public void logout() {
		LOGGER.info("Loging out from BlueMix...");
        client.logout();
	}
	
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
	
	public void startApp(String appName) {
		client.startApplication(appName);
	}
	
	public void stopApp(String appName) {
		client.stopApplication(appName);
	}
	
	public void deleteApp(String appName) {
		LOGGER.info("Deleting app...");
		client.deleteApplication(appName);
	}
	
	public void deleteService(String service) {
		LOGGER.info("Deleting service...");
		client.deleteService(service);
	}
	
	public String getAppState(String appName) throws BluemixClientException {
		try {
			return client.getApplication(appName).getState().toString();
		} catch (Exception e) {
			String message = "Could not get the app state. Error: " + e.getLocalizedMessage();
			throw new BluemixClientException(message, e);
		}
	}
	
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
