package com.example.assignmentjava.dbconfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC {
	private static Connection con = null;

	static {
		String databaseURL = "jdbc:ucanaccess://C://Users//USER//Downloads//test//accountsdb.accdb";
		try {
			con = DriverManager.getConnection(databaseURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return con;
	}
}
