bluemix-cf-client
=================

Wrapper of the [cloudfoundry-client-lib](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-client-lib) for IBM Bluemix.

This wrapper make it easier to deploy and manage applications in Bluemix by providing a Java client capable of some basic cloud operations.

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

After that, you must include this repository source code in your project's classpath.

Usage
-----

First, you need to create a new instance of the **BluemixClient** informing your user, password, organization and space:

```java
BluemixClient client = new BluemixClient(user, password, orgName, spaceName);
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
