package com.revature.project_1;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class App 
{
    public static void main( String[] args )
    {
    	try {
    		// Start Tomcat
    		String webappBase = "src/main/webapp";
    		Tomcat tomcat = new Tomcat();
    		tomcat.setPort(8888);
    		tomcat.getHost().setAppBase(".");
    		// First parameter is the context
			tomcat.addWebapp("/project-1", webappBase);
			tomcat.start();
			tomcat.getServer().await();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (LifecycleException e) {
			e.printStackTrace();
		} 
        System.out.println( "Hello World!" );
    }
}
