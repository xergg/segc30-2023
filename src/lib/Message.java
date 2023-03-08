package lib;

public class Message {
	private Commands type;
	private String message;
	
	public Message(Commands t, String message) {
		this.type = t;
		this.message = message;
	}	
	
	public Commands getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
