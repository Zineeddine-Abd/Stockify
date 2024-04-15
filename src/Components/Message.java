package Components;

import java.sql.Date;

import application.DB_Users;

public class Message {
	private int messageid;
	private String message;
	private int cor_asset_id;
	private int message_author;
	private Date message_date;
	
	public Message(int messageid,String message,int cor_asset_id,int message_author,Date message_date) {
		this.messageid = messageid;
		this.message = message;
		this.cor_asset_id = cor_asset_id;
		this.message_author = message_author;
		this.message_date = message_date;
	}
	
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCor_asset_id() {
		return cor_asset_id;
	}
	public void setCor_asset_id(int cor_asset_id) {
		this.cor_asset_id = cor_asset_id;
	}
	public int getMessage_author() {
		return message_author;
	}
	public void setMessage_author(int message_author) {
		this.message_author = message_author;
	}
	public Date getMessage_date() {
		return message_date;
	}
	public void setMessage_date(Date message_date) {
		this.message_date = message_date;
	}
	
	@Override
	public String toString() {
		User author=null;
		try {
			author = DB_Users.getUser(this.message_author);
			return "[" + author.getFull_name() +"] On "+ this.getMessage_date() + " : Message " + this.getMessageid();
		}catch(NullPointerException e) {
			//
		}
		return "[" + author.getFull_name() +"] On "+ this.getMessage_date() + " : Message " + this.getMessageid();
	}
}
