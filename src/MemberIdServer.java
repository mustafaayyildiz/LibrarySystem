import java.io.ObjectInputStream;
import java.io.Serializable;

public class MemberIdServer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idCounter;
	private static MemberIdServer server;
	
	private MemberIdServer() {
		idCounter = 1;
	}
	public static MemberIdServer getInstance() {
		if (server == null) {
			return (server = new MemberIdServer());
		} else {
			return server;
		}
	}
	
	public int getId() {
		return idCounter++;
	}
	
	public static void retrieve(ObjectInputStream input) {
		// TODO Auto-generated method stub
	}
}
