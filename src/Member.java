import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.Iterator;

public class Member {
	private String id;
	private String name;
	private String address;
	private String phone;
	private List<Hold> booksOnHold = new LinkedList<Hold>();
	private List<Book> booksBorrowed = new LinkedList<Book>();
	private List<Transaction> transactions = new LinkedList<Transaction>();
	
	public Member (String name, String address, String phone) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
	
	public boolean returnBook(Book book) {
		Iterator<Book> itr = booksBorrowed.iterator();
		while(itr.hasNext()) {
			Book tmpBook = itr.next();
			if (tmpBook.getId().equals(book.getId())) {
				transactions.add(new Transaction("-Book Returned", tmpBook.getTitle())); 
				return booksBorrowed.remove(book);
			}
		}
		return false;
	}
	
	public boolean renew(Book book) {
		if (book == null) {
			return false;
		}
		return issue(book);
	}
	
	public void placeHold(Hold hold) {
		transactions.add(new Transaction("-Hold Placed", hold.getBook().getTitle())); 
		booksOnHold.add(hold);
	}
	
	public boolean issue(Book book) {
		if (booksBorrowed.add(book)){
			transactions.add(new Transaction ("-Book issued ", book.getTitle()));
			return true;
		}
		return false;
	}
	
	public boolean removeHold(String bookId) {
		boolean removed = false;
		ListIterator<Hold> iterator = booksOnHold.listIterator();
		while (iterator.hasNext()) {
			Hold hold = (Hold) iterator.next();
			String id = hold.getBook().getId();
			if (id.equals(bookId)) {
				transactions.add(new Transaction ("-Hold Removed ", hold.getBook().getTitle()));
				removed = true;
				booksOnHold.remove(hold);
			}
		}
		return removed;
	}
	
	public Iterator<Hold> getHolds() {
		if (booksOnHold.isEmpty()) {
			return null;
		}
		return booksOnHold.listIterator();
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Iterator<Transaction> getTransactions(Calendar date) {
		List<Transaction> result = new LinkedList<Transaction>();
		Iterator<Transaction> iterator = transactions.iterator();
		while(iterator.hasNext()) {
			Transaction transaction = (Transaction) iterator.next();
			if (transaction.onDate(date)) {
				result.add(transaction);
			}
		}
		return (result.iterator());
	}
	
	public Iterator<Book> getBooksIssued() {
		return booksBorrowed.iterator();
	}

	@Override
	public String toString() {
	return "Member : \n	id : " + id + "\n	name : " + name + "\n	address : " + address
			+ "\n	phone : " + phone;
	}
	
	
}
