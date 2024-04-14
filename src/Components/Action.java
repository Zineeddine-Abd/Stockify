package Components;

public class Action {
	private int action_id;
	private int action_related_obj_id;
	private String action_type;

	public Action(int id,int related_obj_id,String action_type) {
		this.action_id = id;
		this.action_related_obj_id = related_obj_id;
		this.action_type = action_type;
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
	
}
