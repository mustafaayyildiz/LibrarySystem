import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Library {
	protected static final int NOT_FOUND = 0;
	protected static final int SUCCESSFULL_AND_HOLD = 1;
	protected static final int SUCCESSFULL = 2;
	protected static final int FAILED = 3;
	
	private static MemberList memberList = new MemberList();
	private static Catalog catalog = new Catalog();
	private static Library library;
	
	private Library() {

	}
	
	public static Library getInstance() {
		if (library == null) {
			library = new Library();
		}
		return library;
	}
	
	public Book addBook(String id, String title, String author) {
		Book book = new Book(id, title, author);
		if (catalog.insertBook(book)) {
			return book;
		}
		return null;
	}
	
	public Member addMember(String id, String name, String address, String phone) {
		Member member = new Member(id, name, address, phone);
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
		if (member.getFine() > 0) {
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
			return NOT_FOUND;
		}
		
		Member member = book.returnBook();
		if (member == null) {
			return NOT_FOUND;
		}
		
		double fine = book.computeFine();
		if (fine > 0) {
			member.addFine(fine, book.getTitle());
		}
		
		boolean hasHold = book.hasHold();
		boolean receiveBookFromMember = member.returnBook(book);
		if (receiveBookFromMember && hasHold) {
			return SUCCESSFULL_AND_HOLD;
		} else if (receiveBookFromMember) {
			return SUCCESSFULL;
		}
		
		return FAILED;
	}
	
	public Member getBorrower(String bookId) {
		Book book = catalog.search(bookId);
		if (book == null) {
			return null;
		}
		return book.getBorrower();
	}
	
	public int removeBook(String bookId) {
		Book book = catalog.search(bookId);
		
		if (book == null) {
			return NOT_FOUND;
		}
		
		boolean hasHold = book.hasHold();
		Member member = book.getBorrower();
		if (member == null && !hasHold) {
			if (catalog.removeBook(bookId));
				return SUCCESSFULL;
		}
		return FAILED;
	}
	
	public int placeHold(String memberId, String bookId, int duration) {
		Member member = memberList.search(memberId);
		if (member == null) {
			return NOT_FOUND;
		}
		Book book = catalog.search(bookId);
		if (book == null) {
			return NOT_FOUND;
		}
		
		Member borrower = book.getBorrower();
		if (borrower != null && !borrower.getId().equals(memberId)) {
			Hold hold = new Hold(member, book, duration);
			book.placeHold(hold);
			member.placeHold(hold);
			return SUCCESSFULL;
		}
		return FAILED;
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
			return NOT_FOUND;
		}
		
		Book book = catalog.search(bookId);
		if (book == null) {
			return NOT_FOUND;
		}
		
		boolean removeHoldFromBook = book.removeHold(memberId);
		boolean removeHoldFromMember = member.removeHold(bookId);

		if (removeHoldFromBook && removeHoldFromMember) {
			return SUCCESSFULL;
		}
		return FAILED;
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
		
		Book book = catalog.search(bookId);
		Member member = memberList.search(memberId);
		
		if (book == null || member == null) {
			return null;
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
	
	public Iterator<Transaction> getTransactions(String memberID, Calendar date) {
		Member member = searchMembership(memberID);
		if (member == null) {
			return null;
		}
		
		return member.getTransactions(date);
	}

	public boolean deleteAllinValidHolds() {
		List<Hold> deletedWillHoldList = new ArrayList<Hold>();
		try {
			Iterator<Member> itrMember = memberList.getMembers();
			while(itrMember.hasNext()) {
				Member member = itrMember.next();
				Iterator<Hold> itrHold = member.getHolds();
				while (itrHold.hasNext()) {
					Hold hold = itrHold.next();
					if (!hold.isValid()) {
						deletedWillHoldList.add(hold);
					}
				}
			}
			
			for(Hold hold : deletedWillHoldList) {
				Member member = hold.getMember();
				Book book = hold.getBook();
				member.removeHold(book.getId());
				book.removeHold(member.getId());
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void retrieve(String fileName) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jsonObject = (JSONObject) obj;

			JSONArray catalogJSON = (JSONArray) jsonObject.get("Catalog");
			catalog.retrieve(catalogJSON.iterator());
			
			JSONArray memberListJSON = (JSONArray) jsonObject.get("MemberList");
			memberList.retrieve(memberListJSON.iterator());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean save(String fileName) {
		PrintWriter writer;
		JSONObject libraryJSON;
		try {
        	writer = new PrintWriter(fileName, "UTF-8");
        	libraryJSON = new JSONObject();
            libraryJSON.put("Catalog", catalog.generateCatalog());
            libraryJSON.put("MemberList", memberList.generateMemberList());
        	writer.println(libraryJSON.toString());
            writer.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return false;
	}
}
