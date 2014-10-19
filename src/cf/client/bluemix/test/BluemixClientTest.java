package cf.client.bluemix.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cf.client.bluemix.BluemixClient;
import cf.client.bluemix.BluemixClientException;

public class BluemixClientTest {

	// Bluemix credentials
	private static String user = "user@email.com";
	private static String password = "user_password";
	
	private static BluemixClient client; 
	
	@BeforeClass
	public static void init() throws BluemixClientException {
		String orgName = user;
		String spaceName = "dev";
		client = new BluemixClient(user, password, orgName, spaceName);
		client.login();
	}
	
	@AfterClass
	public static void logout() {
		client.logout();
	}
	
	@Test
	public void deployWAR() throws BluemixClientException {
		// creating a db service
		String dbName = "mydb";
		client.createService("sqldb", dbName, "sqldb_small");
		List<String> serviceNames = Arrays.asList(dbName);
		
		// creating a new application
		String appName = "my-java-web-app";
		File warFile = new File("test-apps/webApp.war");
		client.createApp(appName, warFile, serviceNames, null, "liberty-for-java", 128);
		Assert.assertEquals("STARTED", client.getAppState(appName));
		
		// updating app
		client.updateApp(appName, warFile);
		Assert.assertEquals("STARTED", client.getAppState(appName));
		
		// deleting app
		client.deleteApp(appName);
		
		// deleting service
		client.deleteService(dbName);
	}
	
	@Test
	public void deployNode() throws BluemixClientException {
		// creating a new application
		String appName = "my-node-app";
		File appFile = new File("test-apps/nodeApp.zip");
		client.createApp(appName, appFile, null, "node app.js", "sdk-for-nodejs", 128);
		Assert.assertEquals("STARTED", client.getAppState(appName));
		
		// stop app
		client.stopApp(appName);
		Assert.assertEquals("STOPPED", client.getAppState(appName));
		
		// deleting app
		client.deleteApp(appName);
	}
}
