import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Iterator;

public class Library {
	public static final int BOOK_NOT_FOUND = 1;
	public static final int BOOK_NOT_ISSUED = 2;
	private static MemberList memberList = new MemberList();
	private static Catalog catalog = new Catalog();
	private static Library libraryInstance;
	
	private Library() {

	}
	
	public static Library getInstance() {
		if (libraryInstance == null) {
			libraryInstance = new Library();
		}
		return libraryInstance;
	}
	
	public Book addBook(String title, String author, String id) {
		Book book = new Book(title, author, id);
		if (catalog.insertBook(book)) {
			return book;
		}
		return null;
	}
	
	public Member addMember(String name, String address, String phone) {
		Member member = new Member(name, address, phone);
		if (memberList.insertMember(member)) {
			return member;
		}
		return null;
	}
	
	public Book issueBook(String memberId, String bookId) {
		Book book = catalog.search(bookId);
		if (book == null) {
			return null;
		}
		if (book.getBorrower() != null) {
			return null;
		}
		Member member = memberList.search(memberId);
		if (member == null) {
			return null;
		}
		if (!(book.issue(member) && member.issue(book))) {
			return null;
		}
		return(book);
	}
	
	public int returnBook(String bookId) {
		Book book = catalog.search(bookId);
		if (book == null) {
			return 1;
		}
		
		Member member = book.returnBook();
		if (member == null) {
			return -1;
		}
		boolean hasHold = book.hasHold();
		boolean receiveBookFromMember = member.returnBook(book);
		if (receiveBookFromMember && hasHold) {
			return 3;
		} else if (receiveBookFromMember) {
			return 2;
		}
		
		return -1;
	}
	
	public int removeBook(String bookId) {
		Book book = catalog.search(bookId);
		
		if (book == null) {
			return -1;
		}
		
		boolean hasHold = book.hasHold();
		Member member = book.getBorrower();
		if (member == null && !hasHold) {
			if (catalog.removeBook(bookId));
				return 1;
		}
		return 0;
	}
	
	public int placeHold(String memberId, String bookId, int duration) {
		Member member = memberList.search(memberId);
		if (member == null) {
			return -1;
		}
		Book book = catalog.search(bookId);
		if (book == null) {
			return -1;
		}
		
		Member borrower = book.getBorrower();
		if (borrower != null) {
			Hold hold = new Hold(member, book, duration);
			book.placeHold(hold);
			member.placeHold(hold);
			return 1;
		}
		return 0;
	}
	
	public Member processHold(String bookId) {
		Book book = catalog.search(bookId);
		if (book == null) {
			return null;
		}
		Hold hold = book.getNextHold();
		if (hold == null) {
			return null;
		}
		Member member = hold.getMember();
		boolean removeHoldFromBook = book.removeHold(member.getId());
		boolean removeHoldFromMember = member.removeHold(bookId);
		if (removeHoldFromBook && removeHoldFromMember) {
			member.issue(book);
			book.issue(member);
			return member;
		}
		return null;
	}
	
	public int removeHold(String memberId, String bookId) {
		Member member = memberList.search(memberId);
		if (member == null) {
			return -1;
		}
		
		Book book = catalog.search(bookId);
		if (book == null) {
			return -1;
		}
		
		boolean removeHoldFromBook = book.removeHold(memberId);
		boolean removeHoldFromMember = member.removeHold(bookId);

		if (removeHoldFromBook && removeHoldFromMember) {
			return 1;
		}
		return 0;
	}
	
	public Member searchMembership(String memberId) {
		return memberList.search(memberId);
	}
	
	public Book renewBook(String memberId, String bookId) {
		if (memberId == null || memberId.equals("")) {
			return null;
		}
		if (bookId == null || bookId.equals("")) {
			return null;
		}
		
		Iterator<Book> iterator = catalog.getBooks();
		Book book = null;
		while (iterator.hasNext()) {
			Book tmp = iterator.next();
			if (tmp.getId().equals(bookId)) {
				book = tmp;
				break;
			}
		}
		
		Member member = null;
		Iterator<Member> itr = memberList.getMembers();
		while (itr.hasNext()) {
			Member tmp = itr.next();
			if (tmp.getId().equals(memberId)) {
				member = tmp;
				break;
			}
		}
		
		boolean renewToMember = member.renew(book);
		boolean renewToBook = book.renew(member);
		if (renewToMember && renewToBook) {
			return book;
		}
		return null;
	}
	
	public Iterator<Book> getBooks(String memberId) {
		Iterator<Member> itrMember = memberList.getMembers();
		while(itrMember.hasNext()) {
			Member member = itrMember.next();
			if (member.getId().equals(memberId)) {
				return member.getBooksIssued();
			}
		}
		return null;
	}
	
	public static boolean save() {
		try {
			FileOutputStream file = new FileOutputStream("LibraryData");
			@SuppressWarnings("resource")
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(libraryInstance);
			output.writeObject(MemberIdServer.instance());
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}
	
	public static Library retrieve() {
		try {
			FileInputStream file = new FileInputStream("LibraryData");
			ObjectInputStream input = new ObjectInputStream(file);
			input.readObject();
			MemberIdServer.retrieve(input);
			return libraryInstance;
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			return null;
		}
	}

	public Iterator<Transaction> getTransactions(String memberID, Calendar date) {
		Member member = searchMembership(memberID);
		if (member == null) {
			return null;
		}
		
		return member.getTransactions(date);
	}

	public void deleteAllinValidHolds() {
		Iterator<Member> itrMember = memberList.getMembers();
		Iterator<Book> itrBook = catalog.getBooks();
		while(itrMember.hasNext()) {
			Member member = itrMember.next();
			//Hold hold = 
		}
	}
}
