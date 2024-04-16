package Components;

import java.sql.Date;

import application.DB_Assets;
import application.DB_Rooms;
import application.DB_Users;
import application.Helper;

public class Action {
	private int action_id;
	private Object related_object;
	private int action_related_obj_id;
	private User action_author;
	private String action_type;
	private Date action_date;
	private String importantInfoForObject;

	public Action(int id,String action_type,int related_obj_id,String related_obj_type,Date date,int action_author_id) {
		this.action_id = id;
		this.action_type = action_type;
		this.action_date = date;
		this.action_author = DB_Users.getUser(action_author_id);
		this.action_related_obj_id = related_obj_id;
		
		
		switch(related_obj_type) {
		case Helper.ASSET:
			this.related_object = DB_Assets.getAsset(related_obj_id);
			break;
		case Helper.USER:
			this.related_object = DB_Users.getUser(related_obj_id);
			break;
		case Helper.ROOM:
			this.related_object = DB_Rooms.getRoom(related_obj_id);
			break;
		}
		
		importantInfoForObject = getObjectStringLabel();
		
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
	
	public User getActionAuthor() {
		return action_author;
	}
	
	public void setActionAuthor(User user) {
		this.action_author = user;
	}
	
	public String getObjectStringLabel() {
		if(related_object instanceof Asset) {
			Asset asset = (Asset) related_object;
			return asset.getAsset_category() + ":" + asset.getAsset_type() + ":" + asset.getAsset_model() + ":serial number = " + asset.getAsset_serial_number();
		}else if(related_object instanceof User) {
			User user = (User) related_object;
			return user.getFull_name() + ":email = " + user.getEmail() + " : " + user.getUser_role();
		}else if(related_object instanceof Room) {
			Room room = (Room) related_object;
			return room.getRoom_name() + " : " + room.getRoom_type();
		}else {			
			return null;
		}
	}
	
	
	@Override
	public String toString() {
		String result=null;
		try {
			result = + this.getAction_id() + ": "+ this.getAction_type() + " : " +action_author.getUsername() + ", component: " + getObjectStringLabel() + " : " + this.action_related_obj_id;
		}catch(NullPointerException e) {
			Helper.displayErrorMessage("Error", e.getMessage());
		}
		return result;
	}
	
}
