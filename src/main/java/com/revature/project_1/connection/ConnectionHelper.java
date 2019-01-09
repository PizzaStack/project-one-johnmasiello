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
	
	protected ConnectionHelper() {
		Properties properties = new Properties();
		final String PropertiesLoadFailure = "Connection properties failed to load";
		try {
			properties.load(new FileInputStream("connection.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(PropertiesLoadFailure);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(PropertiesLoadFailure);
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
