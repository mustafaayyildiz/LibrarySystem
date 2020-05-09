import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Catalog {
	private static final String FILE_NAME = "catalog.txt";
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
	
	public boolean retrieve() {
		try {
			File file = new File(FILE_NAME);
			if (file.exists() && file.canRead()) {
				BufferedReader br = new BufferedReader(new FileReader(file)); 
				  
				String st; 
				while ((st = br.readLine()) != null) {
					String book[] = st.split(",");
					books.add(new Book(book[0], book[1], book[2]));
				}
				br.close();
				
				Iterator<Book> itr = books.iterator();
				while(itr.hasNext()) {
					System.out.println(itr.next());
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean save(){
        try {
        	PrintWriter writer = new PrintWriter(FILE_NAME, "UTF-8");
            for(Book element : books){
                writer.println(element.getId()+","+element.getTitle()+","+element.getAuthor());
            }
            writer.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
