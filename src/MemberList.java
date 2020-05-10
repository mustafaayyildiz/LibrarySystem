import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MemberList {
	private List<Member> memberList = new LinkedList<Member>();
	
	public Member search(String memberId) {
		if (memberList.isEmpty()) {
			return null;
		}
		Iterator<Member> iterator = memberList.iterator();
		while(iterator.hasNext()) {
			Member member = iterator.next();
			if (member.getId().equals(memberId)) {
				return member;
			}
		}
		return null;
	}
	
	public boolean insertMember(Member member) {
		if (search(member.getId()) == null) {
			return memberList.add(member);
		}
		return false;
	}
	
	public ListIterator<Member> getMembers() {
		return memberList.listIterator();
	}
	
	public void retrieve(Iterator<JSONObject> itrMember) {
		while(itrMember.hasNext()) {
			JSONObject member = (JSONObject) itrMember.next();
			memberList.add(new Member(member.get("MemberID").toString(), member.get("MemberName").toString(), 
					member.get("MemberAddress").toString(), member.get("MemberPhone").toString()));
		}
		
		Iterator<Member> itr = memberList.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray generateMemberList() {
		JSONArray memberListJSON = new JSONArray();
		Iterator<Member> itr = memberList.iterator();
		while(itr.hasNext()) {
			JSONObject memberJSON = new JSONObject();
			Member member = itr.next();
			memberJSON.put("MemberID", member.getId());
			memberJSON.put("MemberName", member.getName());
			memberJSON.put("MemberAddress", member.getAddress());
			memberJSON.put("MemberPhone", member.getPhone());
			JSONArray holdJSONArray = new JSONArray();
        	Iterator<Hold> holdItr = member.getHolds();
        	while(holdItr.hasNext()) {
        		Hold hold = holdItr.next();
        		JSONObject holdJSONObject = new JSONObject();
        		holdJSONObject.put("BookID", hold.getBook().getId());
        		holdJSONArray.add(holdJSONObject);
        	}
        	memberJSON.put("Holds", holdJSONArray);
        	JSONArray borrowedBooksJSONArray = new JSONArray();
        	Iterator<Book> itrBook = member.getBooksIssued();
        	while(itrBook.hasNext()) {
        		JSONObject borrowedBooksJSONObj = new JSONObject();
        		Book book = itrBook.next();
        		borrowedBooksJSONObj.put("BookID", book.getId());
        		borrowedBooksJSONArray.add(borrowedBooksJSONObj);
        	}
        	memberJSON.put("BorrowedBooks", borrowedBooksJSONArray);
        	memberListJSON.add(memberJSON);
		}
		return memberListJSON;
	}
}
