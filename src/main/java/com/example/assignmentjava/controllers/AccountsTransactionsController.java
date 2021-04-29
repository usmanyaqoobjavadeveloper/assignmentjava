package com.example.assignmentjava.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.assignmentjava.dbconfig.JDBC;
import com.example.assignmentjava.impl.Utils;
import com.example.assignmentjava.model.BankStatement;
import com.example.assignmentjava.model.UserLogin;

@RestController
public class AccountsTransactionsController {
	static Connection con = JDBC.getConnection();

	private Logger LOGGER = LoggerFactory.getLogger(AccountsTransactionsController.class);

	@PostMapping("/login")
	public UserLogin login(@RequestParam("username") String name, @RequestParam("password") String password)
			throws SQLException {
		if (Utils.getCurrentUser().equals("")) {
			String sql = "INSERT INTO users (username, userpassword, usertype) VALUES (?, ?, ?)";

			String userType = "";
			if (name.equals("admin") && password.equals("admin")) {
				userType = "admin";
			} else if (name.equals("user") && password.equals("user")) {
				userType = "user";
			}
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, userType);

			int row = preparedStatement.executeUpdate();

			if (row > 0) {
				LOGGER.info("User new record saved successfully");
				UserLogin u1 = new UserLogin();
				u1.setStatus("SUCCESS");
				u1.setMessage("New User Of Type= " + userType + " Logged IN");
				return u1;
			}
		}
		UserLogin u2 = new UserLogin();
		u2.setStatus("FAILED");
		u2.setMessage("USER Already Logged IN , Please Logout..");
		return u2;
	}

	@GetMapping("/getAccountStatement/{accountid}")
	public List<BankStatement> getAccountStatement(@PathVariable(value = "accountid") int accountid)
			throws ParseException {

		if (Utils.getCurrentUser().equals("admin") || Utils.getCurrentUser().equals("user")) {
			List<BankStatement> lstBankSt = new ArrayList<BankStatement>();
			try {
				String sql = "SELECT A.ID,S.AMOUNT,S.DATEFIELD FROM account A inner join statement S on A.ID=S.account_id WHERE A.ID="
						+ accountid;

				Statement statement = con.createStatement();
				ResultSet result = statement.executeQuery(sql);

				while (result.next()) {
					int id = result.getInt("id");
					String amount = result.getString("amount");
					String date = result.getString("datefield");
					Date actualDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);
					Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse("30.04.2021");
					Date endDate = new SimpleDateFormat("dd.MM.yyyy").parse("30.07.2021");
					int resultDateStart = startDate.compareTo(actualDate);
					int resultDateEnd = endDate.compareTo(actualDate);
					if (resultDateStart < 0 && resultDateEnd > 0) {
						BankStatement bk = new BankStatement();
						bk.setAccountid(id);
						bk.setAccountNumber("#####");
						bk.setAmount(amount);
						bk.setDate(actualDate);
						lstBankSt.add(bk);
					}
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return lstBankSt;
		}
		return null;
	}

	@GetMapping("/logout")
	public UserLogin logout() {

		try {
			String sql = "delete from users";

			Statement statement = con.createStatement();
			int result = statement.executeUpdate(sql);
			UserLogin u2 = new UserLogin();
			u2.setStatus("SUCCESS");
			u2.setMessage("USER Logged Out");
			return u2;

		} catch (SQLException ex) {
			ex.printStackTrace();
			UserLogin u2 = new UserLogin();
			u2.setStatus("FAILED");
			u2.setMessage("USER CANNOT Logged Out");
			return u2;
		}
	}

	@GetMapping("/getAccountStatement/{accountid}/{startDate}/{endDate}")
	public List<BankStatement> getAccountStatementWithDates(@PathVariable(value = "accountid") int accountid,
			@PathVariable(value = "startDate") String startDate, @PathVariable(value = "endDate") String endDate)
			throws ParseException {

		if (Utils.getCurrentUser().equals("admin")) {
			List<BankStatement> lstBankSt = new ArrayList<BankStatement>();
			try {
				String sql = "SELECT A.ID,S.AMOUNT,S.DATEFIELD FROM account A inner join statement S on A.ID=S.account_id WHERE A.ID="
						+ accountid;

				Statement statement = con.createStatement();
				ResultSet result = statement.executeQuery(sql);

				while (result.next()) {
					int id = result.getInt("id");
					String amount = result.getString("amount");
					String date = result.getString("datefield");
					Date actualDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);
					Date startDateT = new SimpleDateFormat("dd.MM.yyyy").parse(startDate);
					Date endDateT = new SimpleDateFormat("dd.MM.yyyy").parse(endDate);
					int resultDateStart = startDateT.compareTo(actualDate);
					int resultDateEnd = endDateT.compareTo(actualDate);
					if (resultDateStart < 0 && resultDateEnd > 0) {
						BankStatement bk = new BankStatement();
						bk.setAccountid(id);
						bk.setAccountNumber("#####");
						bk.setAmount(amount);
						bk.setDate(actualDate);
						lstBankSt.add(bk);
					}
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return lstBankSt;
		}
		return null;
	}
	
	@GetMapping("/getAccountStatement/{accountid}/{minAmount}/{maxAmount}")
	public List<BankStatement> getAccountStatementWithAmounts(@PathVariable(value = "accountid") int accountid,
			@PathVariable(value = "minAmount") double minAmount, @PathVariable(value = "maxAmount") double maxAmount)
			throws ParseException {

		if (Utils.getCurrentUser().equals("admin")) {
			List<BankStatement> lstBankSt = new ArrayList<BankStatement>();
			try {
				String sql = "SELECT A.ID,S.AMOUNT,S.DATEFIELD FROM account A inner join statement S on A.ID=S.account_id WHERE A.ID="
						+ accountid;

				Statement statement = con.createStatement();
				ResultSet result = statement.executeQuery(sql);

				while (result.next()) {
					int id = result.getInt("id");
					String amount = result.getString("amount");
					double actualAmount=Double.parseDouble(amount);
					String date = result.getString("datefield");
					Date actualDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);
					if (actualAmount>minAmount && actualAmount<maxAmount) {
						BankStatement bk = new BankStatement();
						bk.setAccountid(id);
						bk.setAccountNumber("#####");
						bk.setAmount(amount);
						bk.setDate(actualDate);
						lstBankSt.add(bk);
					}
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return lstBankSt;
		}
		return null;
	}
}
