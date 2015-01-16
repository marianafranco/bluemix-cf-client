bluemix-cf-client
=================

Wrapper of the [cloudfoundry-client-lib](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-client-lib) for IBM Bluemix.

This wrapper make it easier to deploy and manage applications in Bluemix by providing a Java client library capable of some basic cloud operations.

Configuration
-------------
In order to get started you must add the **cloudfoundry-client-lib** dependency to your project's pom.xml:

```xml
<dependencies>
  <dependency>
    <groupId>org.cloudfoundry</groupId>
    <artifactId>cloudfoundry-client-lib</artifactId>
    <version>1.0.2</version>
  </dependency>
</dependencies>
```

After that, you must include the **bluemix-cf-client-0.0.1.jar** library in your project's classpath.

Usage
-----
First, you need to create a new instance of the **BluemixClient** informing your user, password, organization,  space and the Bluemix API endpoint URL (e.g. "https://api.ng.bluemix.net"):

```java
BluemixClient client = new BluemixClient(user, password, orgName, spaceName, api);
```

Next, you need to login to Bluemix:

```java
client.login();
```

After logged in, you will be able to perform some basic operations using the client instance, such as:
- create or delete an application in Bluemix;
- create or delete a service;
- and start or stop an application.

Check the [**BluemixClient**](https://github.com/marianafranco/bluemix-cf-client/blob/master/src/cf/client/bluemix/BluemixClient.java) class to get more details about the possible operations.

Examples
--------
You can find some examples about how to use this library on the [**BluemixClientTest**](https://github.com/marianafranco/bluemix-cf-client/blob/master/test/cf/client/bluemix/test/BluemixClientTest.java) class.

For now, there are two tests showing how to deploy a NodeJS and Java application. To run the tests you need to set some environment variables:
- the BLUEMIX_USER and BLUEMIX_PASSWORD with your credentials;
- and the BLUEMIX_API with the API endpoint URL used to access Bluemix (e.g. "https://api.ng.bluemix.net").
