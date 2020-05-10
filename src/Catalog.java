import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Catalog {
	private List<Book> books = new LinkedList<Book>();
	
	public Book search(String bookId) {
		if (books.isEmpty()) {
			return null;
		}
		
		Iterator<Book> itr = books.iterator();
		while (itr.hasNext()) {
			Book book = itr.next();
			if (book.getId().equals(bookId)) {
				return book;
			}
		}
		return null;
	}
	
	public boolean removeBook(String bookId) {
		if (books.isEmpty()) {
			return false;
		}
		Iterator<Book> itr = books.iterator();
		while(itr.hasNext()) {
			Book book = (Book) itr.next();
			if (book.getId().equals(bookId)) {
				books.remove(book);
			}
		}
		return false;
	}
	
	public boolean insertBook(Book book) {
		if (search(book.getId()) == null) {
			return books.add(book);
		}
		return false;
	}
	
	public Iterator<Book> getBooks() {
		return books.listIterator();
	}
	
	public void retrieve(Iterator<JSONObject> itrCatalog) {
		while(itrCatalog.hasNext()) {
			JSONObject book = (JSONObject) itrCatalog.next();
			books.add(new Book(book.get("BookID").toString(), book.get("BookTitle").toString(), book.get("BookAuthor").toString()));
		}
		
		Iterator<Book> itr = books.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray generateCatalog() {
		JSONArray catalogJSON = new JSONArray();
        Iterator<Book> itr = books.iterator();
        while(itr.hasNext()) {
        	JSONObject bookJSON = new JSONObject();
        	Book book = itr.next();
        	bookJSON.put("BookID", book.getId());
        	bookJSON.put("BookTitle", book.getTitle());
        	bookJSON.put("BookAuthor", book.getAuthor());
        	Member borrowedBy = book.getBorrower();
        	bookJSON.put("BorrowedBy", borrowedBy != null ? borrowedBy.getId() : "");
        	JSONArray holdJSONArray = new JSONArray();
        	Iterator<Hold> holdItr = book.getHolds();
        	while(holdItr.hasNext()) {
        		Hold hold = holdItr.next();
        		JSONObject holdJSONObject = new JSONObject();
        		holdJSONObject.put("MemberID", hold.getMember().getId());
        		holdJSONArray.add(holdJSONObject);
        	}
        	bookJSON.put("Holds", holdJSONArray);
        	Date dueDate = book.getDueDate();
        	bookJSON.put("DueDate", dueDate != null ? dueDate.getTime() : "");
        	catalogJSON.add(bookJSON);
        }
        return catalogJSON;
	}
}
