package project.banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	private Connection con;
	private Scanner sc;
	
	public User(Connection con, Scanner sc) {
		// TODO Auto-generated constructor stub
		this.con = con;
		this.sc = sc;
	}

	public void register() throws SQLException {
		System.out.println("_____Registration_____");
		System.out.println("Enter Name : ");
		String name = sc.next();
		System.out.println("Enter Email : ");
		String email = sc.next();
		System.out.println("Enter Password : ");
		String password = sc.next();
		if(user_exist(email)) {
			System.out.println("User Already Exist for this Email!");
			return;
		}
		String reg_Query = "INSERT INTO register_user(name,email,password) VALUES (?,?,?)";
		PreparedStatement pst = con.prepareStatement(reg_Query);
		pst.setNString(1, name);
		pst.setNString(2, email);
		pst.setNString(3, password);
		int rowAffected = pst.executeUpdate();
		if(rowAffected>0) {
			System.out.println("Registration successful");
		}else {
			System.out.println("Registration failed");
		}
	}
	
	public String login() throws SQLException {
		System.out.println("_____Login_____");
		
		System.out.println("Enter Email : ");
		String email = sc.next();
		System.out.println("Enter Password : ");
		String password = sc.next();
		String log_Query = "SELECT * FROM register_user WHERE email=? AND password=?";
		PreparedStatement pst = con.prepareStatement(log_Query);
		pst.setNString(1, email);
		pst.setNString(2, password);
		ResultSet rs = pst.executeQuery();
		if(rs.next())
			return email;
		else
			return null;
	}
	
	public boolean user_exist(String email) throws SQLException {
		String userExistQuery = "SELECT * FROM register_user where email=?";
		PreparedStatement pst = con.prepareStatement(userExistQuery);
		pst.setNString(1, email);
		ResultSet rs = pst.executeQuery();
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
	}

}
