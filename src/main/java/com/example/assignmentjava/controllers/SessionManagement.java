package com.example.assignmentjava.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.assignmentjava.dbconfig.JDBC;

@Component
public class SessionManagement {
	static Connection con = JDBC.getConnection();
	private Logger LOGGER = LoggerFactory.getLogger(AccountsTransactionsController.class);

	// Deleting user from table after every 5 minutes
	@Scheduled(fixedRate = 300000)
	public void scheduleTaskWithFixedRate() {
		LOGGER.info("Fixed Rate Task :: Execution Time ");
		try {
			String sql = "delete from users";

			Statement statement = con.createStatement();
			int result = statement.executeUpdate(sql);
			LOGGER.info("Deleting user from table after every 5 minutes");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
