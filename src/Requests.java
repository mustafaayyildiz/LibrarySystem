
public enum Requests {
	EXIT(0, "EXIT"),
	HELP(1, "HELP"),
	ADD_MEMBER(2, "ADD MEMBER"),
	ADD_BOOK(3, "ADD BOOK"),
	ISSUE_BOOK(4, "ISSUE BOOK"),
	RETURN_BOOK(5, "RETURN BOOK"),
	REMOVE_BOOK(6, "REMOVE BOOK"),
	MEMBER_TRANSACTION(7, "MEMBER TRANSACTION"),
	PLACE_HOLD(8, "PLACE HOLD"),
	PROCESS_HOLD(9, "PROCESS HOLD"),
	REMOVE_HOLD(10, "REMOVE HOLD"),
	RENEW_BOOK(11, "RENEW BOOK"),
	DELETE_INVALID_HOLDS(12, "DELETE INVALID HOLDS");
	
	private int value;
	private String name;
	Requests(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return name;
	}
	
	public static Requests getRequestFromValue(int value) {
		for (Requests request : Requests.values()) {
			if (request.value == value) {
				return request;
			}
		}
		return null;
	}
}
