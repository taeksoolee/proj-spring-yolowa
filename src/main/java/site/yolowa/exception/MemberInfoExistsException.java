package site.yolowa.exception;

import site.yolowa.dto.Member;

//ȸ����Ͻ� ����ڰ� �Է��� ���̵� �̹� ������ ��� �߻��Ǵ� ����Ŭ���� 
public class MemberInfoExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	
	//����ó�� �޼ҵ�(ExceptionHandler)���� ���� ���� ������ �����ϱ� ���� �ʵ�
	private Member member;
	
	public MemberInfoExistsException() {
		// TODO Auto-generated constructor stub
	}
	
	
	public MemberInfoExistsException(Member member, String message) {
		super(message);
		this.member = member;
	}


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	
	
}
