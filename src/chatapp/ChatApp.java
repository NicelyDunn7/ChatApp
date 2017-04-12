package chatapp;

import java.util.Scanner;

import org.json.JSONException;

public class ChatApp {

	public static void main(String[] args) throws JSONException {
		// Print Welcome Menu and get user choice for server or client
		System.out.print("Welcome to ChatApp!\n\n");
		System.out.print("What would you like to launch as?\n");
		System.out.print("1. Client\n");
		System.out.print("2. Server\n");
		System.out.print("Please enter 1 or 2: ");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		
		// If user chose 1, create and launch client
		// If user chose 2, create and launch server
		// If user chose something else, error and exit
		if (choice == 1){
			Client client = new Client();
			client.run();
		} else if (choice == 2){
			Server server = new Server();
			server.run();
		} else {
			System.out.print("Error: Please Enter 1 or 2. Exiting Program.\n");
		}
		scanner.close();
	}
}
