import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class Book {
	private String id;
	private String title;
	private String author;
	private Member borrowedBy;
	private List<Hold> holds = new LinkedList<Hold>();
	private Calendar calendar = Calendar.getInstance();
	private Date dueDate;
	private Date acquisitionDate;
	
	public Book(String id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.acquisitionDate = new Date();
	}
	
	public Book(String id, String title, String author, String acquisitionDate) {
		this.id = id;
		this.title = title;
		this.author = author;
		Instant instant = Instant.parse(acquisitionDate);
		this.acquisitionDate = Date.from(instant);
	}
	
	public boolean matches(String bookId) {
		return this.id.equals(bookId);
	}
	
	public boolean issue(Member member) {
		borrowedBy = member;
		dueDate = new Date();
		calendar.setTime(dueDate);
		calendar.add(Calendar.SECOND, 1);
		dueDate = calendar.getTime();
		return true;
	}
	
	public boolean removeHold(String memberId) {
		boolean removed = false;
		ListIterator<Hold> iterator = holds.listIterator();
		while (iterator.hasNext()) {
			Hold hold = iterator.next();
			String id = hold.getMember().getId();
			if (id.equals(memberId)) {
				removed = true;
				iterator.remove();
			}
		}
		return removed;
	}
	
	public Member returnBook() {
		Member member = borrowedBy;
		borrowedBy = null;
		return member;
	}
	
	public boolean renew(Member member) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 1);
			Date date = calendar.getTime();
			this.setDueDate(date);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public void placeHold(Hold hold) {
		holds.add(hold);
	}
	
	public Hold getNextHold() {
		ListIterator<Hold> iterator = holds.listIterator();
		while(iterator.hasNext()) {
			Hold hold = (Hold) iterator.next();
			if (hold.isValid()) {
				return hold;
			}
		}
		return null;
	}
	
	public ListIterator<Hold> getHolds() {
		return holds.listIterator();
	}
	
	public boolean hasHold() {
		if (holds.isEmpty()) {
			return false;
		}
		
		Iterator<Hold> itr = holds.iterator();
		while (itr.hasNext()) {
			Hold hold = (Hold) itr.next();
			if (hold.getBook().getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate ;
	}
	
	public Member getBorrower() {
		return borrowedBy;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getId() {
		return id;
	}
	
	public Date getAcquisitionDate() {
		return acquisitionDate;
	}
	
	public double computeFine() {
		double fine = 0.0;
		if (System.currentTimeMillis() > dueDate.getTime()) {
			if (yearApart(acquisitionDate, dueDate)) {
				fine = 0.15 + 0.05 * daysElapsedSince(dueDate);
			} else {
				fine = 0.25 + 0.1 * daysElapsedSince(dueDate);
			}
			if (hasHold()) {
				fine *= 2;
			}
		}
		return fine;
	}
	
	private boolean yearApart(Date acquisitionDate, Date dueDate) {
		return ((dueDate.getTime() - acquisitionDate.getTime()) / TimeUnit.DAYS.toMillis(1)) > 365;
	}
	
	private int daysElapsedSince(Date date) {
		return (int) ((System.currentTimeMillis() - date.getTime()) / TimeUnit.SECONDS.toMillis(1));
	}

	public String toString() {
		return "Book :\n	id : " + id + "\n	title : " + title + "\n	author : " + author +"\n	acquisitionDate : " + acquisitionDate.toInstant();
	}
}
