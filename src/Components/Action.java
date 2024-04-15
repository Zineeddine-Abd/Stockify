package Components;

import java.sql.Date;

import application.DB_Users;
import application.Helper;

public class Action {
	private int action_id;
	private int action_related_obj_id;
	private Object related_object;
	private String action_type;
	private Date action_date;
	private int action_author;

	public Action(int id,int related_obj_id,String action_type,Date date,Object obj,int author) {
		this.action_id = id;
		this.action_related_obj_id = related_obj_id;
		this.action_type = action_type;
		this.action_date = date;
		this.related_object = obj;
		this.action_author = author;
	}
	
	public int getAction_id() {
		return action_id;
	}

	public void setAction_id(int action_id) {
		this.action_id = action_id;
	}

	public int getAction_related_obj_id() {
		return action_related_obj_id;
	}

	public void setAction_related_obj_id(int action_related_obj_id) {
		this.action_related_obj_id = action_related_obj_id;
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
	
	public Object getRelated_object() {
		return related_object;
	}
	
	public void setRelated_object(Object obj) {
		this.related_object = obj;
	}
	
	public int getAction_author() {
		return action_author;
	}
	
	public void setAction_author(int author) {
		this.action_author = author;
	}
	
	public String getAffectedComp() {
		if(related_object instanceof Asset) {
			return "Asset";
		}else if(related_object instanceof User) {
			return "User";
		}else if(related_object instanceof Room) {
			return "Room";
		}
		return null;
	}
	
	@Override
	public String toString() {
		String result=null;
		User actual_author = DB_Users.getUser(action_author);
		try {
			result = + this.getAction_id() + ":" + "[" +actual_author.getUsername() + "] : " + actual_author.getFull_name() +": "+ this.getAction_type() + " : component: " + this.getAffectedComp() + " with id: " + this.action_related_obj_id;
		}catch(NullPointerException e) {
			Helper.displayErrorMessage("Error", e.getMessage());
		}
		return result;
	}
	
}
