package Components;

public class Session {
	private int logged_user_id;
	private String user_session_UUID;
	
	public Session(int logged_user_id,String user_session_UUID) {
		this.logged_user_id = logged_user_id;
		this.user_session_UUID = user_session_UUID;
	}
	
	public int getLogged_user_id() {
		return logged_user_id;
	}

	public void setLogged_user_id(int logged_user_id) {
		this.logged_user_id = logged_user_id;
	}

	public String getUser_session_UUID() {
		return user_session_UUID;
	}

	public void setUser_session_UUID(String user_session_UUID) {
		this.user_session_UUID = user_session_UUID;
	}
	
}
