package com.revature.project_1.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Class.forName("com.revature.project_1.connection.ConnectionHelper");
		} catch (ClassNotFoundException e) {
			System.out.println("Could not find class ConnectionHelper");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Shutting Down, GoodNight, John");
	}

}
