package project.banking_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BaningApp {
	
	private static String url = "jdbc:mysql://localhost:3306/banking_system";
	private static String username = "root";
	private static String password = "802215";
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
//		System.out.println("connection establish");
		
		Connection con = DriverManager.getConnection(url,username,password);
//		System.out.println("driver loaded");
		
		Scanner sc = new Scanner(System.in);
		
		User user = new User(con, sc);
		Accounts accounts = new Accounts(con, sc);
		AccountManager accountManager = new AccountManager(con, sc);
		String email;
		long accountNumber;
		
		while(true) {
			System.out.println("_____Welcome to the Banking System_____");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.println("Enter your choice : ");
			int choice1 = sc.nextInt();
			switch(choice1) {
			case 1:
				user.register();
				break;
				
			case 2:
				email = user.login();
				if(email!=null) {
					System.out.println("______User Loged In_____");
					
					if(!accounts.account_exist(email)) {
						System.out.println("1. Open Bank Account");
						System.out.println("2. Exit");
						System.out.println("Enter your choice : ");
						if(sc.nextInt()==1) {
							accountNumber = accounts.open_account(email);
							System.out.println("Account created Successfully");
							System.out.println("Your Account Number is : " + accountNumber);
						}else {
							break;
						}
					}
					accountNumber = accounts.getAccount_number(email);
					int choice2 = 0;
					while(choice2!=5) {
						System.out.println("1. Credit Money");
						System.out.println("2. Debit Money");
						System.out.println("3. Transer Money");
						System.out.println("4. Check Balance");
						System.out.println("5. Log out");
						System.out.println("Enter your choice : ");
						choice2 = sc.nextInt();
						switch(choice2) {
						case 1:
							accountManager.creditMoney(accountNumber);
							break;
						case 2:
							accountManager.debitMoney(accountNumber);
							break;
						case 3:
							accountManager.transferMoney(accountNumber);
							break;
						case 4:
							accountManager.checkBalance(accountNumber);
						case 5:
							break;
						default:
							System.out.println("Enter valid choice");
							break;
						}
					}
				}else {
					System.out.println("Incorrect email and password");
				}
			case 3:
				System.out.println("Thank you for Visiting\nHave a Good bye");
				return;
			default:
				System.out.println("Enter Valid choice");
				break;
			}
		}
	}

}
