package site.yolowa.exception;

//���°� ������(state == 9)�� �ƴ� ��� �߻��Ǵ� ���� Ŭ����
public class MemberAdminNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private int memberState;
	
	public MemberAdminNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public MemberAdminNotFoundException(int memberState, String message) {
		super(message);
		this.memberState = memberState;
	}


	public int getMemberState() {
		return memberState;
	}

	public void setMemberState(int memberState) {
		this.memberState = memberState;
	}
	
	
	
	
}
