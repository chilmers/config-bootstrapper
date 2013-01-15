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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import junit.framework.Assert;

import org.apache.log4j.LogManager;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

public class ConfigServletContextListenerTest {
    
    @Test
    public void testDefault() {
        ServletContext contextMock = new MockServletContext();
        ConfigServletContextListener testee = new ConfigServletContextListener();
        testee.contextInitialized(new ServletContextEvent(contextMock));
        Assert.assertEquals("classpath:application.properties", System.getProperty("application.config.location"));
        Assert.assertNotNull(LogManager.exists("com.chilmers.configbootstrapper.test"));
    }
    
}
