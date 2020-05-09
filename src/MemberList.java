import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MemberList {
	private static final String FILE_NAME = "memberList.txt";
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
	
	public Iterator<Member> getMembers() {
		return memberList.listIterator();
	}
	
	public boolean retrieve() {
		try {
			File file = new File(FILE_NAME);
			if (file.exists() && file.canRead()) {
				BufferedReader br = new BufferedReader(new FileReader(file)); 
				  
				String st; 
				while ((st = br.readLine()) != null) {
					String member[] = st.split(",");
					memberList.add(new Member(member[0], member[1], member[2], member[3]));
				}
				br.close();
				
				Iterator<Member> itr = memberList.iterator();
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
            for(Member element : memberList){
            	writer.println(element.getId()+","+element.getName()+","+element.getAddress()+","+element.getPhone());
            }
            writer.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
