package site.yolowa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import site.yolowa.dto.Member;
import site.yolowa.dto.Range;

@Component
public class AdminUtil {
	// �̸��� �߼��� ���� ��ü
	@Autowired
	private JavaMailSender mailSender;
	
	//yyyy-MM-dd
	@Autowired
	@Qualifier("underBarDateFormat")
	private SimpleDateFormat underBarDateFormat;
	
	@Autowired
	@Qualifier("slashDateFormat")
	private SimpleDateFormat slashDateFormat;
	
	
	// ���� �˻� ������ ��¥�� ����
	public Range settingRange(Map<String, Object> filter, int index){
		// ���� ��¥ ���� yyyy-MM-dd
		String startDate = (String)filter.get("rangeStartYear"+index) + "-" + (String)filter.get("rangeStartMonth"+index) + "-" + (String)filter.get("rangeStartDay"+index);
		// ������ ��¥ ���� yyyy-MM-dd
		String endDate = (String)filter.get("rangeEndYear"+index) + "-" + (String)filter.get("rangeEndMonth"+index) + "-" + (String)filter.get("rangeEndDay"+index);
		
		// test�� ���� ���� ��ü ����
		 underBarDateFormat.setLenient(false);
         try {
        	if(startDate.equals("--") || endDate.equals("--")) { 
        		startDate = "0001-01-01";
        		endDate = "2050-12-31";
        	}else {
        		underBarDateFormat.parse(startDate);
        		underBarDateFormat.parse(endDate);
        	}
		} catch (ParseException e) {
			// ��¥ ������ �ƴ� ��� ����ó��
		}
		
		return new Range(startDate, endDate);
	}
	
	// admin claim ���񽺿��� ���
	// ȸ���� ���޹޾� �������� �����Ͽ� ��ȯ
	@SuppressWarnings("deprecation")
	public String setForbiddenDate(Member member, int plus) {
		String forbiddenDate = "";
		if(member.getMemberForbiddenDate() == null) {
			Date date = new Date(System.currentTimeMillis());
			date.setDate(date.getDate()+plus);
			forbiddenDate = slashDateFormat.format(date);
		} else {
			//date ��ü ����
			Date date = new Date();
			System.out.println("abcd"+member.getMemberForbiddenDate());
			if(!member.getMemberForbiddenDate().equals("")) {
				date.setYear(Integer.parseInt(member.getMemberForbiddenDate().substring(0, 4)));
				date.setMonth(Integer.parseInt(member.getMemberForbiddenDate().substring(5, 7))-1);
				date.setDate(Integer.parseInt(member.getMemberForbiddenDate().substring(8, 10)));
			}
			date.setDate(date.getDate()+plus);
			forbiddenDate = slashDateFormat.format(date);
		}
		return forbiddenDate; 
	}
	
	public String sendEmail(String email, String subject, String content) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
			message.setSubject(subject);
			message.setText(content, "utf-8", "html");
			message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email));
			mailSender.send(message);
		return email;
	}

}
