/**
 *    Copyright 2013 Christian Hilmersson
 *    
 *    This file is part of config-bootstrapper (https://github.com/chilmers/config-bootstrapper)
 *    
 *    config-bootstrapper is free software; you can redistribute it and/or modify
 *    it under the terms of version 2.1 of the GNU Lesser General Public
 *    License as published by the Free Software Foundation.
 *    
 *    config-bootstrapper is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *    
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with config-bootstrapper; if not, write to the
 *    Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *    Boston, MA 02111-1307  USA
 */
package com.chilmers.configbootstrapper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;

import org.apache.commons.lang.StringUtils;

public class ConfigHelper {
    
    private String applicationName;
    
    public ConfigHelper(String applicationName) {
        this.applicationName = applicationName;
    }
    
    /**
     * Used for reading the application from it's location after initialization is done
     * using the default application config location key.
     * See {@link ConfigServletContextListener#DEFAULT_CONFIG_LOCATION_PROPERTY_KEY}
     * @return A property resource bundle holding the current configuration.
     */
    public static PropertyResourceBundle readApplicationConfiguration() {
        return readApplicationConfiguration(null);
    }
    
    /**
     * Used for reading the application from it's location after initialization is done.
     * @param propertyKey the name of the property for the application config location or
     * null for the default key.
     * Can be null in most cases. If it is blank or null the default key will be used. 
     * See {@link ConfigServletContextListener#DEFAULT_CONFIG_LOCATION_PROPERTY_KEY}
     * @return A property resource bundle holding the current configuration.
     */
    public static PropertyResourceBundle readApplicationConfiguration(String propertyKey) {
        if (StringUtils.isBlank(propertyKey)) {
            propertyKey = ConfigServletContextListener.DEFAULT_CONFIG_LOCATION_PROPERTY_KEY;
        }
        String applicationConfigLocation = System.getProperty(propertyKey);
        return new ConfigHelper("config-bootstrapper").getApplicationConfiguration(applicationConfigLocation);       
    }
    
    public PropertyResourceBundle getApplicationConfiguration(String applicationConfigLocation) {
        InputStream is = null;
        try {
            if (applicationConfigLocation.startsWith("classpath:")) {
                applicationConfigLocation = applicationConfigLocation.replaceFirst("classpath:", "");
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(applicationConfigLocation); 
            } else if (applicationConfigLocation.startsWith("file:")) {
                applicationConfigLocation = applicationConfigLocation.replaceFirst("file:", "");
                is = new FileInputStream(applicationConfigLocation);    
            } else {
                logToSystemOut("The application configuration location must start with file: or classpath:");
            }
            return new PropertyResourceBundle(is);
            
        } catch (Exception e) {
            logToSystemOut("There was a problem reading the application configuration at location: " 
                    + applicationConfigLocation +"\n"
                    + "Exception:" + e.getClass().toString() + "\n"
                    + "Message:" + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                logToSystemOut("WARNING! Exception while trying to close configuration file.\n"
                        + "Exception:" + e.getClass().toString() + "\n"
                        + "Message:" + e.getMessage());
            }
        }
        return null;
    }
    
    /**
     * Helper method that logs to System.out.
     * Good to have before and after the logging framework is configured
     * and after it has been shut down. 
     * @param text the text to log to System.out
     */
    public void logToSystemOut(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(sdf.format(new Date()) + " [" + applicationName + "] " + text);
    }
    
}
