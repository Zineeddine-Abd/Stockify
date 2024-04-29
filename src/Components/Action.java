package Components;

import java.sql.Date;

import LoginUi.LoginController;

public class Action {
	private int action_id;
	private String action_type;
	private Date action_date;
	private String action_info;
	private String cor_obj_type;

	public Action(int id,String action_type,String action_info,String cor_obj_type,Date date) {
		this.action_id = id;
		this.action_type = action_type;
		this.action_date = date;
		this.action_info = action_info ;
		this.cor_obj_type = cor_obj_type;
	}
	
	public int getAction_id() {
		return action_id;
	}

	public void setAction_id(int action_id) {
		this.action_id = action_id;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}
	
	public Date getAction_date() {
		return action_date;
	}
	
	public void setAction_date(Date date) {
		this.action_date = date;
	}
	
	public String getAction_info() {
		return this.action_info;
	}
	
	public void setAction_info(String action_info) {
		this.action_info = action_info;
	}
	
	public String getCor_obj_type() {
		return cor_obj_type;
	}
	
	public void setCor_obj_type(String str) {
		this.cor_obj_type = str;
	}
	
	@Override
	public String toString() {
		return  this.getAction_id() + ":"+ this.action_type + " : " + this.cor_obj_type + " : " +  this.action_info + " On: " + this.getAction_date() + "\nBy: " +LoginController.getLoggedUser().toString();
	}
	
}
