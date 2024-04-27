package Components;

import java.io.Serializable;


public class Credentials implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String hashedPassword;
	private String sk;
	private byte[] iv;
	
	public Credentials(String username,String hashedPassword,String sk,byte[] iv) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.sk = sk;
		this.iv = iv;
	}
	
	public String getSk() {
		return sk;
	}
	
	public void setSk(String sk) {
		this.sk = sk;
	}
	
	public String getHashedPassword() {
		return hashedPassword;
	}
	
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
}
