package project.banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class Accounts {
	private Connection con;
	private Scanner sc;
	
	public Accounts(Connection con, Scanner sc) {
		this.con=con;
		this.sc=sc;
	}
	
	public long open_account(String email) throws SQLException {
		if(!account_exist(email)) {
			
			System.out.println("Enter Name :");
			String name = sc.next();
			System.out.println("Enter Balance :");
			double balance = sc.nextDouble();
			System.out.println("Enter Security Pin :");
			String pin = sc.next();
			long accountNumber = generateAccountNumber();
			String open_account_query = "INSERT INTO accountid(accountNumber, name, email, balance, pin) VALUES(?, ?, ?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(open_account_query);
			pst.setLong(1, accountNumber);
			pst.setNString(2, name);
			pst.setNString(3, email);
			pst.setDouble(4, balance);
			pst.setNString(5, pin);
			int rowAffected = pst.executeUpdate();
			if(rowAffected>0) {
				return accountNumber;
			}
		}
		return 0;
		
	}
	public long getAccount_number(String email) throws SQLException {
		String account_number_query = "SELECT accountNumber FROM accountid WHERE email=?";
		PreparedStatement pst = con.prepareStatement(account_number_query);
		pst.setNString(1, email);
		ResultSet rs = pst.executeQuery();
		if(rs.next()) {
			return rs.getLong("accountNumber");
		}
		return 0;
	}
	
	private long generateAccountNumber() throws SQLException {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT accountNumber from accountid ORDER BY accountNumber DESC LIMIT 1");
		if(rs.next()) {
			long last_acc_number = rs.getLong("accountNumber");
			return last_acc_number + 1;
		}else {
			return 50001001;
		}
	}
	
	
	public boolean account_exist(String email) throws SQLException {
		String accountExist_Query = "SELECT accountNumber FROM accountid where email = ?";
		PreparedStatement pst = con.prepareStatement(accountExist_Query);
		pst.setNString(1, email);
		ResultSet rs = pst.executeQuery();
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
	}
}
