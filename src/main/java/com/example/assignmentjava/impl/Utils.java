package com.example.assignmentjava.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.assignmentjava.controllers.AccountsTransactionsController;
import com.example.assignmentjava.dbconfig.JDBC;

public class Utils {
	private static Logger LOGGER = LoggerFactory.getLogger(AccountsTransactionsController.class);

	static Connection con = JDBC.getConnection();
	public static String getCurrentUser()
	{
		String userType="";
		try {
			String sql = "SELECT * FROM users";

			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				 userType = result.getString("usertype");
			}

			LOGGER.info("User Current type= "+userType);
			return userType;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
