config-bootstrapper
===================

Helps separating application and logging configuration for different environments in Servlet based web Java applications.

Config-bootsrapper is licensed under LGPL 2.1 as described in the LICENSE file. <br>

<a href="http://htmlpreview.github.com/?https://raw.github.com/chilmers/config-bootstrapper/master/target/site/apidocs/com/chilmers/configbootstrapper/ConfigServletContextListener.html">Link to API documentation (javadoc)</a>

Maven dependency
===============
Add the following dependency to your pom.xml to get the latest version from Maven Central Repo.
<pre>
	&lt;dependency&gt;
		&lt;groupId&gt;com.chilmers.config-bootstrapper&lt;/groupId&gt;
		&lt;artifactId&gt;config-bootstrapper&lt;/artifactId&gt;
		&lt;version&gt;1.2&lt;/version&gt;
	&lt;/dependency&gt;
</pre>

<div class="block">Config Bootstrapper - https://github.com/chilmers/config-bootstrapper/<br>
        Helps initializing logging and application configuration for a system.<br>
  This class implements a ServletContextListener and initializes 
  config-bootstrapper upon servlet context initialization.<br>
 <br>
 
 <h1>Quick start</h1> 
 A more comprehensive usage guide is found further down.<br>
 <br>
 Add this to your web.xml before any other listeners that need to use the configuration or that needs to log.<br>
<pre>  &lt;listener&gt;
      &lt;listener-class&gt;com.chilmers.configbootstrapper.ConfigServletContextListener&lt;/listener-class&gt;
  &lt;/listener&gt;
</pre>
 You can now use the system property "application.config.location" to read your config location, for example to inject your config in Spring.
<pre>  &lt;context:property-placeholder location="${application.config.location}"/&gt;
</pre>
 Or use readApplicationConfiguration in ConfigHelper:<br>
<pre>  See <a href="../../../com/chilmers/configbootstrapper/ConfigHelper.html#readApplicationConfiguration()"><code>ConfigHelper.readApplicationConfiguration()</code></a>
</pre>
 
 The application will now read <i>application.properties from the classpath</i>, if you need to read from another location
 you might specify this with a system property (or environment variable or servlet context parameter) at startup.<br/>
<br/>For example:
<pre> mvn jetty:run -Dapplication.config.location=file:/Users/chilmers/myApp/app-config.properties
</pre>
 or if you want to read config from classpath (other than application.properties)
<pre> mvn jetty:run -Dapplication.config.location=classpath:app-config.properties
</pre>

 At this stage the application will try to find a default log4j configuration file i.e. <i>log4j.xml or log4j.properties on the classpath</i>
 If you need to change this, add an entry like this to your application configuration file:
<pre>  application.log4j.config.location=file:/Users/chilmers/myApp/app-log4j.xml
</pre>
 or if you want to read logging config from the classpath (other than log4j.xml/log4j.properties)
<pre>  application.log4j.config.location=classpath:app-log4j.xml
</pre>
 <br>
 <h1>Main functionalities:</h1><br>
 <ul>
  <li><strong>Determines which configuration file to use</strong><br>
      Looks in the given order in system properties, environment variables, and servlet context parameters for the location of a 
      properties file to use for configuration.<br>
      By default it looks for an entry named <strong>"application.config.location"</strong>. <br>
      If no such entry was found the location <strong>defaults to classpath:application.properties</strong><br>
      The location that was determined will be written to the system property (by default "application.config.location").<br>
      The obvious benefit of doing this is in when no location has been specified, you will still be able to read the configuration
      location from this system property. In other environments where you want to add a configuration on the file system, you can do
      this and add the location as a system property, environment variable or servlet context parameter and still read the location
      from the system property.<br>
      This makes it easy to use separate configurations for separate environments.
      Use classpath: for files on the classpath and file: to read from an external location.
  </li>
  <li><strong>Loads logging (log4J) configuration</strong><br>
      Uses the given application configuration to specify a location of a log4j configuration file.
      By default it looks for an entry named <strong>"application.log4j.config.location"</strong> in the application configuration.<br>
      In this way it is easy to provide different logging configurations for different environments.<br>
      If no specific log4j-configuration is configured, it falls back to log4j's default configuration handling (e.g. log4j.xml or log4j.properties on the classpath)<br>
  </li>
  <li><strong>Possibility to set system properties from application configuration</strong><br>
      Entries in the configuration file starting with "system.property." will automatically be written to
      the system properties.<br>
      Example:
      <br>
      <pre>          system.property.foo=bar
      </pre>
      Will write <tt>foo=bar</tt> as a system property, which is handy in some circumstances.<br>
      Don't use this feature if you don't understand what it is, since it might clutter your system properties.<br>
  </li>
 </ul>
 <br>
 
 <b>Finding correct application configuration</b><br>
 <br>
 This context listener will in the given order look in the system properties, environment variables<br>
 or the servlet context parameters for the location of a configuration file to use in the application.<br>
 By default it will look for an entry with the key "application.config.location". (This key name can be overridden, see Overriding defaults below)<br>
 If no such entry is found it will by default fall back to using "classpath:application.properties" as configuration location. (The fallback location can also be overridden if necessary, see below)<br>
 <br>
 Whichever configuration location string is decided, will be set in the system properties using the same key as above, 
 i.e by default it will be set in system property "application.config.location"<br> 
 This makes it possible to locate the configuration from within the application, for example by reading it into a 
 PropertyResourceBundle or by using Spring's PropertyPlaceholderConfigurer like this:
 <br>
  <pre>  
  &lt;bean id="placeholderConfig" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"&gt;
      &lt;property name="location" value="${application.config.location}" /&gt;
  &lt;/bean&gt;
  </pre>
  Or, if you have the context namespace defined, simply:<br>
  <pre>  &lt;context:property-placeholder location="${application.config.location}"/&gt;
  </pre>
  Or use readApplicationConfiguration in ConfigHelper for non-Spring applications:<br>
  See <a href="../../../com/chilmers/configbootstrapper/ConfigHelper.html#readApplicationConfiguration()"><code>ConfigHelper.readApplicationConfiguration()</code></a>
 <br>
 <br> 
 <b>Logging configuration (i.e. Log4j)</b><br>
 This mechanism configures Log4j using a given file whose location is stated in the application configuration, 
 or if no such file is available falls back to Log4j's default configuration behavior, <br>
 i.e. looks for log4j.xml or log4j.properties on the classpath.<br>
 Use this format classpath:my-log4j-config.xml to point out a file on the classpath.<br>
 If you want to point out a file on the file system you can either prefix with file: or just write the location as it is. 
 <br>
 To use an external log4j file you will have to state the location of the file in the application configuration as 
 a property with the key given by "application.log4j.config.location". (This key name can be overriden, see Overriding defaults below)<br>
 <br>
 <br> 
 <b>Usage</b><br>
 Add this to your web.xml and make sure it is located before any application specific listeners that need
 to use the configuration location property or that needs to log. E.g. before Spring's ContextLoaderListener<br>
 <pre>  &lt;listener&gt;
      &lt;listener-class&gt;com.chilmers.configbootstrapper.ConfigServletContextListener&lt;/listener-class&gt;
  &lt;/listener&gt;
 </pre>
 
 If you want to specify an external configuration file (instead of the default "classpath:application.properties"), <br>
 add a context-param or more likely a system property or environment variable stating the location of your application configuration. <br> 
 For example:<br>
 <br>
  As context param:<br>
  <pre>  &lt;context-param&gt;
      &lt;description&gt;The location of the application configuration. 
          If not set it defaults to classpath:application.properties&lt;/description&gt;
      &lt;param-name&gt;application.config.location&lt;/param-name&gt;
      &lt;param-value&gt;file:/Users/myusername/my-app-config/app.properties&lt;/param-value&gt;
  &lt;/context-param&gt;
  </pre>
 <br>
  As environment variable in a bash shell:<br>
  <pre>export application.config.location=file:/Users/myusername/my-app-config/app.properties</pre>
  <br>
  As a system property upon starting your container:<br>
  <pre>java [your application] -Dapplication.config.location=file:/Users/myusername/my-app-config/app.properties</pre><br>
 <br>
 <br>
 <b>Overriding defaults</b><br>
 The following context-parameters can be set to configure the listener.<br>
 All of them have default values so they don't have to be set if not needed<br>
 <pre>  &lt;context-param&gt;
      &lt;description&gt;Sets the key for the entry that holds the application configuration location. 
          If not set it defaults to application.config.location&lt;/description&gt;
      &lt;param-name&gt;configServletContextListener.configLocationPropertyKey&lt;/param-name&gt;
      &lt;param-value&gt;myown.config.location&lt;/param-value&gt;
  &lt;/context-param&gt;
  </pre>
 <pre>  &lt;context-param&gt;
      &lt;description&gt;Sets the key for where in the application configuration file to look for a log4j
          configuration file location.
          If not set it defaults to application.log4j.config.location&lt;/description&gt;
      &lt;param-name&gt;configServletContextListener.log4jConfigLocationPropertyKey&lt;/param-name&gt;
      &lt;param-value&gt;myown.log4j.config.location&lt;/param-value&gt;
  &lt;/context-param&gt;
  </pre>
 <pre>  &lt;context-param&gt;
      &lt;description&gt;Sets the location of the application configuration to fall back to if no other configuration
          file location was set. E.g. a bundled configuration on the classpath. 
          If not set it defaults to classpath:application.properties&lt;/description&gt;
      &lt;param-name&gt;configServletContextListener.fallbackConfigLocation&lt;/param-name&gt;
      &lt;param-value&gt;classpath:myown.properties&lt;/param-value&gt;
  &lt;/context-param&gt;
  </pre>
 <pre>  &lt;context-param&gt;
      &lt;description&gt;
      Application name that is printed when using System.out logging when no logging manager is available.
      Defaults to the display-name of the web.xml or if no display-name exists it will be ConfigServletContextListener
      &lt;/description&gt;
      &lt;param-name&gt;configServletContextListener.applicationName&lt;/param-name&gt;
      &lt;param-value&gt;My Application&lt;/param-value&gt;
  &lt;/context-param&gt;
  </pre></div>