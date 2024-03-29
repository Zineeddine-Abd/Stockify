package Components;

public class User {
	private int user_id;
	private String username;
	private String pass_word;
	private String email;
	private String full_name;
	private String user_role;
	
	public User(int id,String username,String pass_word,String email,String full_name,String user_role) {
		this.user_id = id;
		this.username = username;
		this.pass_word = pass_word;
		this.email = email;
		this.full_name = full_name;
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

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getUser_role() {
		return user_role;
	}

	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	
}
