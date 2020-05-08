import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		return memberList.add(member);
	}
	
	public Iterator<Member> getMembers() {
		if (memberList.isEmpty()) {
			return null;
		}
		return memberList.listIterator();
	}
}
