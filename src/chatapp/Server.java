package chatapp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Server {
	private static String userFile;
	
	public Server(String userFile){
		setUserFile(userFile);
	}
	
	public void run() throws JSONException{
		String clientMessage;
		try {
			ServerSocket serverSocket = new ServerSocket(10221);
			Socket connectSocket = serverSocket.accept();
			BufferedReader inbound = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
			DataOutputStream outbound = new DataOutputStream(connectSocket.getOutputStream());
			List<User> userList = loadUsers();
			String response;
			boolean go = true;
			while (go){
				clientMessage = inbound.readLine();
				JSONObject message = new JSONObject(clientMessage);
				response = "";
				
				if (message.getString("Type").equals("message")){
					for(User user : userList){
						if(user.getUserID().equals(message.getString("UserID"))){
							if(user.getLoggedIn() == true){
								response = message.getString("UserID") + ": " + message.getString("message");
								break;
							}
						}
					}
					if(response.equals("")){
						response = "Server: Denied. Please login first.";
					}
					System.out.println(response);
					outbound.writeBytes(response + "\n");
				} else if (message.getString("Type").equals("newuser")){
					for(User user : userList){
						if(user.getUserID().equals(message.getString("UserID"))){
							response = "Server: Denied. Account already exists.";
							outbound.writeBytes(response + '\n');
							System.out.println(response);
							break;
						}
					}
					if(response.equals("")){
						if (message.getString("UserID").length() > 32 || message.getString("UserID").length() < 1){
							response = "Server: Denied. UserID should be between 1 and 32 characters.";
						} else if (message.getString("Password").length() > 8 || message.getString("Password").length() < 4){
							response = "Server: Denied. Password should be between 4 and 8 characters.";
						} else {
							User newUser = new User(message.getString("UserID"), message.getString("Password"), true);
							userList.add(newUser);
							response = "Server: " + newUser.getUserID() + " created account";
						}
						outbound.writeBytes(response + '\n');
						System.out.println(response);
					}
				} else if (message.getString("Type").equals("login")){
					for(User user : userList){
						if(user.getUserID().equals(message.getString("UserID"))){
							if(user.getPassword().equals(message.getString("Password"))){
								user.setLoggedIn(true);
								response = "Server: " + user.getUserID() + " joins";
								outbound.writeBytes(response + '\n');
								System.out.println(user.getUserID() + " login.");
								break;
							}
						}
					}
					if(response.equals("")){
						response = "Server: Denied. Incorrect Login Credentials.";
						outbound.writeBytes(response + '\n');
					}
				} else if (message.getString("Type").equals("logout")){
					for(User user : userList){
						if(user.getUserID().equals(message.getString("UserID"))){
							user.setLoggedIn(false);
							response = "Server: " + user.getUserID() + " left.";
							outbound.writeBytes(response + '\n');
							System.out.println(user.getUserID() + " logout.");
						}
					}
					break;
				} else {
					System.out.println("Error: Invalid message format.");
					continue;
				}
			}
			serverSocket.close();
			saveUsers(userList);
		} catch (IOException e) {
			System.out.println("Error Opening Socket!");
			e.printStackTrace();
		}
	}
	
	private List<User> loadUsers() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(this.getUserFile()));
		List<User> userList = new ArrayList<User>();
		String[] credentials;
		String line;
		
		while((line = reader.readLine()) != null) {
			credentials = line.split(" ", 2);
			User user = new User(credentials[0], credentials[1], false);
			userList.add(user);
		}
		reader.close();

		return userList;
	}
	
	private void saveUsers(List<User> userList) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new FileOutputStream(this.getUserFile()));
		for(User user : userList){
			pw.println(user.getUserID() + " " + user.getPassword());
		}
		pw.close();
	}
	
	private void setUserFile(String userFile){
		Server.userFile = userFile;
	}
	
	private String getUserFile(){
		return Server.userFile;
	}
}
