import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Hold implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Member member;
	private Book book;
	private Calendar calendar = Calendar.getInstance();
	private Date date;
	
	public Hold(Member member, Book book, int duration) {
		this.member = member;
		this.book = book;
		date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, duration);
		date = calendar.getTime();
	}

	public Member getMember() {
		return member;
	}

	public Book getBook() {
		return book;
	}

	public Calendar getDate() {
		return calendar;
	}
	
	public boolean isValid() {
		return (System.currentTimeMillis() < calendar.getTimeInMillis());
	}
}
