package project.banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	
	private Connection con;
	private Scanner sc;
	
	public AccountManager(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}
	
	public void creditMoney(long accountNumber) throws SQLException {
		System.out.println("Enter amount : ");
		double amount = sc.nextDouble();
		System.out.println("Enter pin : ");
		String pin = sc.next();
		
		con.setAutoCommit(false);
		if(accountNumber!=0) {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM accountid where accountNumber=? and pin=?");
			pst.setLong(1, accountNumber);
			pst.setNString(2, pin);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				String credit_query = "UPDATE accountid SET balance=balance+? WHERE accountNumber=?";
				PreparedStatement pst2 = con.prepareStatement(credit_query);
				pst2.setDouble(1,amount);
				pst2.setLong(2, accountNumber);
				int rowAffected = pst2.executeUpdate();
				if(rowAffected>0) {
					System.out.println("Rs."+amount+"credited in your account");
					con.commit();
					con.setAutoCommit(true);
					return;
				}else {
					System.out.println("Transaction Failed");
					con.rollback();
					con.setAutoCommit(true);
				}
			}else {
				System.out.println("Invalid Security pin");
			}
		}
		con.setAutoCommit(true);
	}
	
	public void debitMoney(long accountNumber) throws SQLException {
		System.out.println("Enter amount : ");
		double amount = sc.nextDouble();
		System.out.println("Enter pin : ");
		String pin = sc.next();
		
		con.setAutoCommit(false);
		if(accountNumber!=0) {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM accountid where accountNumber=? and pin=?");
			pst.setLong(1, accountNumber);
			pst.setNString(2, pin);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				String credit_query = "UPDATE accountid SET balance=balance-? WHERE accountNumber=?";
				PreparedStatement pst2 = con.prepareStatement(credit_query);
				pst2.setDouble(1,amount);
				pst2.setLong(2, accountNumber);
				int rowAffected = pst2.executeUpdate();
				if(rowAffected>0) {
					System.out.println("Rs."+amount+" debited from account " + accountNumber);
					con.commit();
					con.setAutoCommit(true);
					return;
				}else {
					System.out.println("Transaction Failed");
					con.rollback();
					con.setAutoCommit(true);
				}
			}else {
				System.out.println("Invalid Security pin");
			}
		}
		con.setAutoCommit(true);
	}
	
	public void transferMoney(long accountNumber) throws SQLException {
		System.out.println("Enter reciever account Number : ");
		long rec_accNumber = sc.nextLong();
		System.out.println("Enter amount : ");
		double amount = sc.nextDouble();
		System.out.println("Enter Pin : ");
		String pin = sc.next();
		
		con.setAutoCommit(false);
		if(accountNumber != 0 && rec_accNumber != 0) {
			String transfer_query = "SELECT * FROM accountid WHERE accountNumber=? AND pin=?";
			PreparedStatement pst = con.prepareStatement(transfer_query);
			pst.setLong(1, accountNumber);
			pst.setNString(2, pin);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				double currentBal = rs.getDouble("balance");
				if(amount<=currentBal) {
					
					String debit_query = "UPDATE accountid SET balance = balance-? WHERE accountNumber=?";
					String credit_query = "UPDATE accountid SET balance = balance+? WHERE accountNumber=?";
					
					PreparedStatement debit_pst = con.prepareStatement(debit_query);
					PreparedStatement credit_pst = con.prepareStatement(credit_query);
					
					credit_pst.setDouble(1, amount);
					credit_pst.setLong(2, rec_accNumber);
					debit_pst.setDouble(1, amount);
					debit_pst.setLong(2, accountNumber);
					
					int rowAffected_cre = credit_pst.executeUpdate();
					int rowAffected_deb = debit_pst.executeUpdate();
					
					if(rowAffected_cre>0 && rowAffected_deb>0) {
						System.out.println("Rs."+amount+" transferred successfully");
						con.commit();
						con.setAutoCommit(true);
						return;
					}else {
						System.out.println("Transaction Failed");
						con.rollback();
						con.setAutoCommit(true);
					}
				}else {
					System.out.println("Insufficient Balance");
				}
			}else {
				System.out.println("Invalid Pin");
			}
		}else {
			System.out.println("Invalid Account Number");
		}
		con.setAutoCommit(true);
	}
	
	public void checkBalance(long accountNumber) throws SQLException {
		System.out.println("Enter Pin : ");
		String pin = sc.next();
		
		String checkBal_query = "SELECT balance FROM accountid WHERE accountNumber=? && pin=?";
		PreparedStatement pst = con.prepareStatement(checkBal_query);
		
		pst.setLong(1, accountNumber);
		pst.setNString(2, pin);
		ResultSet rs = pst.executeQuery();
		if(rs.next()) {
			System.out.println("Your account Balance is : "+rs.getLong("balance"));
		}else {
			System.out.println("Invalid Pin");
		}
	}
}