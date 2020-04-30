package site.yolowa.exception;

//�α��� �� �� ȸ�� ���� ���н� �߻��Ǵ� ����Ŭ����
public class LoginAuthFailException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private String memberEmail;
	
	public LoginAuthFailException() {
		// TODO Auto-generated constructor stub
	}

	public LoginAuthFailException(String memberEmail, String message) {
		super(message);
		this.memberEmail = memberEmail;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	
	
	
}
