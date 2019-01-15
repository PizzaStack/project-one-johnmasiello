package com.revature.project_1.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.jetbrains.annotations.Nullable;

public class ConnectionHelper {
	private String url;
	private String username;
	private String password;
	private Connection connection;
	
	private final static ConnectionHelper connectionHelper = new ConnectionHelper();
	private final static boolean IS_WEB_APP = true;
	
	protected ConnectionHelper() {
		Properties properties = new Properties();
		String filepath = null;
		String propertiesLoadFailure = null;
		
		if (IS_WEB_APP) {
			filepath = "C:\\Users\\jmasg\\Documents\\PizzaStack-Workspace\\project-1\\connection.properties";
			propertiesLoadFailure = "Connection properties failed to load with path "
					+ filepath;
		} else {
			filepath = "connection.properties";
			propertiesLoadFailure = "Connection properties failed to load";
		}
		try {
			properties.load(new FileInputStream(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(propertiesLoadFailure);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(propertiesLoadFailure);
		}

		url = properties.getProperty("url");
		username = properties.getProperty("username");
		password = properties.getProperty("password");
	}
	
	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("org.postgresql.Driver");
				connection =  DriverManager.getConnection(url, username, password);
			}
		} catch (SQLException e) {
			closeConnection();
			connection = null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		return connection;		
	}
	
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				
			} finally {
				connection = null;
			}
		}
	}
	
	public void closeThing(@Nullable AutoCloseable thing) {
		try {
			if (thing == null)
				return;
			thing.close();
		} catch (Exception ignore) {
		}
	}
	
	public static ConnectionHelper getinstance() {
		return connectionHelper;
	}
}
