import java.io.Serializable;
import java.text.SimpleDateFormat;
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
	private SimpleDateFormat formatter;
	
	public Hold(Member member, Book book, int duration) {
		this.member = member;
		this.book = book;
		date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, duration);
		date = calendar.getTime();
		formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
		System.out.println("Hold -> " + formatter.format(date));
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
		long current = System.currentTimeMillis();
		long cal = calendar.getTimeInMillis();
		System.out.println("Current < Hold Date : " + (current < cal));
		return (current < cal);
	}
}
