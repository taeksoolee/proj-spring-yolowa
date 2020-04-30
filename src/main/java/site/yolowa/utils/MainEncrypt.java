package site.yolowa.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//���α׷� �ۼ��� �ʿ��� ����� �����ϴ� Ŭ����
public class MainEncrypt {	
	//���ڿ��� ���޹޾� ��ȣȭ �˰����� �̿��Ͽ� ��ȣȭ ��ȯ�Ͽ� ��ȯ�ϴ� �޼ҵ�
	public static String encrypt(String source) {
		//��ȣȭ�� ���ڿ��� �����ϱ� ���� ����
		String password="";
		
		try {
			//MessageDigest : ��ȣȭ �˰����� �̿��Ͽ� ��ȣȭ ó���ϴ� ����� �����ϴ� Ŭ����
			//MessageDigest.getInstance(String algorithm) : ��ȣȭ �˰�����
			//���޹޾� MessageDigest �ν��Ͻ��� ��ȯ�ϴ� �޼ҵ�
			// => NoSuchAlgorithmException ���� �߻� - ����ó��
			//��ȣȭ �˰���(�ܹ���) : MD5, SHA-1, SHA-256(����), SHA-512 �� 
			MessageDigest md=MessageDigest.getInstance("SHA-256");
			
			//MessageDigest.update(byte[] input) : MessageDigest �ν��Ͻ���
			//��ȣȭ �ϰ��� ���ڿ��� byte �迭�� �����Ͽ� �����ϴ� �޼ҵ�
			//String.getBytes() : ���ڿ��� byte �迭�� ��ȯ�Ͽ� ��ȯ�ϴ� �޼ҵ�
			md.update(source.getBytes());
			
			//MessageDigest.digest() : MessageDigest �ν��Ͻ��� ����� byte
			//�迭�� ��ȣȭ �˰����� �̿��Ͽ� ��ȣȭ ó�� �� byte �迭��
			//��ȯ�ϴ� �޼ҵ�
			byte[] digest=md.digest();
			
			//byte �迭�� ��Ұ��� ���ڿ�(String �ν��Ͻ�)�� ��ȯ�Ͽ� ����
			// => byte �迭�� ��Ұ����� ���ʿ��� �� ���� �� 16������ ���ڿ��� ��ȯ�Ͽ� ����
			for(int i=0;i<digest.length;i++) {
				password+=Integer.toHexString(digest[i]&0xff);
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println("[����]�߸��� ��ȣȭ �˰����� ��� �Ͽ����ϴ�.");
		}
		
		return password;
	}
	
	
}






