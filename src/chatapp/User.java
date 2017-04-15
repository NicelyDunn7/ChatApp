package chatapp;

public class User {
	private String UserID;
	private String password;
	private boolean loggedIn;
	
	public User(String UserID, String password, boolean loggedIn){
		 setUserID(UserID);
		 setPassword(password);
		 setLoggedIn(loggedIn);
	}
	
	private void setUserID(String UserID){
		this.UserID = UserID;
	}
	
	private void setPassword(String password){
		this.password = password;
	}
	
	public void setLoggedIn(boolean loggedIn){
		this.loggedIn = loggedIn;
	}
	
	public String getUserID(){
		return this.UserID;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public boolean getLoggedIn(){
		return this.loggedIn;
	}
}
