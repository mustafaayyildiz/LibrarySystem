import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		return books.add(book);
	}
	
	public Iterator<Book> getBooks() {
		if (books.isEmpty()) {
			return null;
		}
		return books.listIterator();
	}
}
