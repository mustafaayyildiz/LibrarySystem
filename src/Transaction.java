import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Transaction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar date;
	private String bookTitle;
	private String type;
	
	public Transaction(String type, String bookTitle) {
		this.type = type;
		this.bookTitle = bookTitle;
		date = new GregorianCalendar();
		date.setTimeInMillis(System.currentTimeMillis());
	}
	
	public boolean onDate(Calendar date) {
		return ((date.get(Calendar.YEAR) == this.date.get(Calendar.YEAR)) &&
				(date.get(Calendar.MONTH) == this.date.get(Calendar.MONTH)) &&
				(date.get(Calendar.DATE) == this.date.get(Calendar.DATE)));
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public String getBookTitle() {
		return bookTitle;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return (type + " " + bookTitle);
	}
}
