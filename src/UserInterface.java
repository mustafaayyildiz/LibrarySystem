import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class UserInterface {
	
	private static final String FILE_NAME = "LibraryData.txt";
	private static UserInterface userInterface;
	private static Library library;
	private static Scanner scan;
	private static SimpleDateFormat formatter;
	
	private UserInterface() {
		File file = new File(FILE_NAME);
		if (file.exists() && file.canRead()) {
			
		}
		library = Library.getInstance();
	}
	
	public static UserInterface getInstance() {
		if (userInterface == null) {
			return userInterface = new UserInterface();
		}
		return userInterface;
	}
	
	public void process() {
		do {
			help();	
			System.out.print("Process : ");
			Requests command = Requests.getRequestFromValue(getCommand());	
			if (command == Requests.EXIT) {
				break;
			}
			switch (command) {
				case ADD_MEMBER:
					addMember();
					break;
				case ADD_BOOK:
					addBook();
					break;
				case ISSUE_BOOK:
					issueBooks();
					break;
				case RETURN_BOOK:
					returnBook();
					break;
				case REMOVE_BOOK:
					removeBook();
					break;
				case MEMBER_TRANSACTION:
					getTransactions();
					break;
				case PLACE_HOLD:
					placeHold();
					break;
				case PROCESS_HOLD:
					processHold();
					break;
				case REMOVE_HOLD:
					removeHold();
					break;
				case RENEW_BOOK:
					renewBook();
					break;
				case DELETE_INVALID_HOLDS:
					deleteAllinValidHolds();
					break;
				default:
					help();
					break;
			}
		} while(true);
	}
	
	private int getCommand() {
		scan = new Scanner(System.in);
		return scan.nextInt();
	}
	
	public void addMember() {
		Member result;
		scan = new Scanner(System.in);
		do {
			System.out.print("Enter name : ");
			String name = scan.nextLine();
			System.out.print("Enter address : ");
			String address = scan.nextLine();
			System.out.print("Enter phone : ");
			String phone = scan.nextLine();
			result = library.addMember(name, address, phone);
			if (result != null) {
				System.out.println(result);
			} else {
				System.out.println("Member could not be added");
			}
			if (!yesOrNo("Add more members?")) {
				break;
			}
		} while (true);
	}

	public void addBook() {
		Book result;
		scan = new Scanner(System.in);
		do {
			System.out.print("Enter id : ");
			String bookID = scan.nextLine();
			System.out.print("Enter book title : ");
			String title = scan.nextLine();
			System.out.print("Enter author : ");
			String author = scan.nextLine();
			result = library.addBook(title, author, bookID);
			if (result != null) {
				System.out.println(result);
			} else {
				System.out.println("Book could not be added");
			}
			if (!yesOrNo("Add more books?")) {
				break;
			}
		} while (true);
	}
	
	public void issueBooks() {
		Book result;
		scan = new Scanner(System.in);
		formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
		System.out.print("Enter member id : ");
		String memberID = scan.nextLine();
		if (library.searchMembership(memberID) == null) {
			System.out.println("No such member");
			return;
		}
		do {
			System.out.print("Enter book id : ");
			String bookID = scan.nextLine();
			result = library.issueBook(memberID, bookID);
			if (result != null){
				System.out.println(result.getTitle() + " -> " + formatter.format(result.getDueDate()));
			} else {
				System.out.println("Book could not be issued");
			}
			if (!yesOrNo("Issue more books?")) {
				break;
			}
		} while (true);
	}
	
	public void returnBook() {
		scan = new Scanner(System.in);
		int result;
		do {
			System.out.print("Enter book id : ");
			String bookID = scan.nextLine();
			result = library.returnBook(bookID);
			if (result == 1) {
				System.out.println("Invalid Book ID");
			} else if (result == 2) {
				System.out.println("The operation was successful");
			} else {
				System.out.println("The operation was successful and there is a hold on the book.");
			}
		} while (yesOrNo("Return more books?"));
	}
	
	public void removeBook() {
		scan = new Scanner(System.in);
		int result;
		do {
			System.out.print("Enter book id : ");
			String bookID = scan.nextLine();
			result = library.removeBook(bookID);
			if (result == -1) {
				System.out.println("Invalid Book ID");
			} else if (result == 0) { 
				System.out.println("The operation was failed.");
			} else {
				System.out.println("The operation was successful.");
			}
		} while (yesOrNo("Delete more books?"));
	}
	
	public void getTransactions(){
		Calendar calendar = Calendar.getInstance();
		scan = new Scanner(System.in);
		Iterator<Transaction> result;
		System.out.print("Enter member id : ");
		String memberID = scan.nextLine();
		System.out.print("Please enter the date for which you want records as dd/mm/yyyy : ");
		String dateString = scan.nextLine();
		Date date;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result = library.getTransactions(memberID, calendar);
		if (result == null) {
			System.out.println("Invalid Member ID");
		} else {
			while(result.hasNext()) {
				Transaction transaction = (Transaction) result.next();
				System.out.println(transaction.getType() + " " + transaction.getBookTitle() + "\n");
			}
			System.out.println("\n There are no more transactions \n" );
		}
	}
	
	public void placeHold() {
		scan = new Scanner(System.in);
		int result;
		System.out.print("Enter user ID : ");
		String memberID = scan.nextLine();
		System.out.print("Enter book ID : ");
		String bookID = scan.nextLine();
		System.out.print("Enter duration : ");
		int duration = scan.nextInt();
		result = library.placeHold(memberID, bookID, duration);
		if (result == -1) {
			System.out.println("Invalid UserID or BookID");
		} else if (result == 1) {
			System.out.println("The operation was successful.");
		} else {
			System.out.println("The operation was failed.");
		}
	}
	
	public void processHold() {
		scan = new Scanner(System.in);
		do {
			System.out.print("Enter book ID : ");
			String bookID = scan.nextLine();
			Member result = library.processHold(bookID);
			if (result == null) {
				System.out.println("The operation was failed.");
			} else {
				System.out.println("The operation was successful.");
			}
		} while(yesOrNo("Process more holds?"));
	}
	
	public void removeHold() {
		int result;
		scan = new Scanner(System.in);
		System.out.print("Enter member ID : ");
		String memberID = scan.nextLine();
		System.out.print("Enter book ID : ");
		String bookID = scan.nextLine();
		result = library.removeHold(memberID, bookID);
		if (result == -1) {
			System.out.println("Invalid memberID or bookID");
		} else if (result == 1) {
			System.out.println("The operation was successful.");
		} else {
			System.out.println("The operation was failed.");
		}
	}
	
	public void renewBook() {
		Book result = null;
		scan = new Scanner(System.in);
		System.out.print("Enter member ID : ");
		String memberID = scan.nextLine();
		Member member = library.searchMembership(memberID);
		if (member == null) {
			return;
		}
		Iterator<Book> issuedBooks = library.getBooks(memberID);
		if (issuedBooks == null) {
			return;
		}
		while(issuedBooks.hasNext()) {
			Book book = issuedBooks.next();
			System.out.println(book);
			if (yesOrNo("Would you like to renew the duration of the book?")) {
				result = library.renewBook(memberID, book.getId());
			}
			if (result == null) {
				System.out.println("The operation was failed.");
			} else {
				System.out.println("The operation was successful.");
			}
		}
	}
	
	public void deleteAllinValidHolds() {
		/*boolean isDeleted = library.deleteAllinValidHolds();
		if (isDeleted) {
			System.out.println("The operation was successful.");
		} else {
			System.out.println("The operation was failed.");
		}*/
	}
	
	public boolean yesOrNo(String question) {
		System.out.print(question + " [yes / no] : ");
		String yesOrNo = scan.nextLine();
		return yesOrNo.equalsIgnoreCase("yes") ? true:false;
	}
	
	/* TODO: Not Implemented */
	private void save() {
		if (library.save()) {
			System.out.println("The library has been successfully saved" );
		} else {
			System.out.println("There has been an error in saving \n" );
		}
	}
	
	private void help() {
		System.out.println("--------------------------");
		System.out.println("Welcome to Library System!");
		System.out.println("--------------------------");
		System.out.println("please select a transaction from the following below");
		for (Requests request : Requests.values()) {
			System.out.println(request + " : " + request.getValue());
		}
		System.out.println("--------------------------");
	}
	
	public static void main(String[] args) {
		UserInterface.getInstance().process();
	}
}
