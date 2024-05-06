package Components;

public class User {
	private int user_id;
	private String username;
	private String pass_word;
	private String user_salt;
	private String email;
	private String first_name;
	private String last_name;
	private String user_role;
	
	public User(int id,String username,String pass_word,String email,String first_name,String last_name,String user_role,String user_salt) {
		this.user_id = id;
		this.username = username;
		this.pass_word = pass_word;
		this.user_salt = user_salt;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_role = user_role;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass_word() {
		return pass_word;
	}

	public void setPass_word(String pass_word) {
		this.pass_word = pass_word;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_role() {
		return user_role;
	}

	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public String getUser_salt() {
		return user_salt;
	}
	
	public void setUser_salt(String salt) {
		this.user_salt = salt;
	}
	
	public String getFullName() {
		return capitalize(first_name) + " " + capitalize(last_name);
	}
	
	@Override
	public String toString() {
		return user_id + " : " + username + " : " + user_role + " ";
	}
	
	public static String capitalize(String str) {
		if(str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
}
