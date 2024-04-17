package Components;

public class Room {
	private String room_type,room_name;
	
	public String getRoom_type() {
		return room_type;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	private int room_id;
	
	public Room(int room_id,String room_type,String room_name) {
		this.room_id = room_id;
		this.room_type = room_type;
		this.room_name = room_name;
	}
	
	@Override
	public String toString() {
		return room_id + " : " + room_type + " : " + room_name + " :";
	}
}
