package site.yolowa.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.yolowa.dto.Member;
import site.yolowa.dto.Message;
import site.yolowa.dto.Range;
import site.yolowa.exception.MemberInfoNotFoundException;
import site.yolowa.service.AdminMemberService;
import site.yolowa.utils.AdminUtil;

@Controller
public class AdminMemberController {
	private Logger logger = LoggerFactory.getLogger(AdminMemberController.class);
	private Integer adminTableDefaultRowCount = 300;
	
	@Autowired
	private AdminUtil util;
	
	//yyyy�� MM�� dd�� hh:mm:ss
	@Autowired
	@Qualifier("AdminTableNowDateFormat")
	private SimpleDateFormat adminTableNowDateFormat;
	
	@Autowired
	@Qualifier("jacksonMapper")
	private ObjectMapper jacksonmapper;
	
	@Autowired
	private AdminMemberService adminMemberService;
	
	@RequestMapping(value = "/admin/member/member", method = RequestMethod.GET)
	public String adminMemberMember(@RequestParam Map<String, Object> filter,Model model) {
		// form�� ���� ��ȿ�� �˻�
		
		Map<String, Object> map = new HashMap<String, Object>();
		// ���ͼ���
		// ���� ���Ͱ� ���� ��츦 Ȯ��. ���ͳ� ��ü�� ���� ���
		if(filter.entrySet().size() != 0) {
			// ���͸� ���� �⺻ ����
			List<Range> birthdays = new ArrayList<Range>();
			List<Range> lastLogins = new ArrayList<Range>();
			List<Range> joinDates = new ArrayList<Range>();
			List<Integer> nos = new ArrayList<Integer>();
			List<String> names = new ArrayList<String>();
			List<String> emails = new ArrayList<String>();
			List<String> phones = new ArrayList<String>();
			List<String> addresses = new ArrayList<String>();
			List<Integer> hostStates = new ArrayList<Integer>();
			List<Integer> states = new ArrayList<Integer>();
			
			// ���� �˻��� ���� ��ȯ
			int rangeCount = (String)filter.get("rangeCount")==""?0:Integer.parseInt((String)filter.get("rangeCount"));
			int keywordCount = (String)filter.get("keywordCount")==""?0:Integer.parseInt((String)filter.get("keywordCount"));
			
			// ���� �˻� ����Ʈ ����
			for(int i=1; i<=rangeCount; i++) {
				String rangeFormat = (String)filter.get("rangeFormat"+i);
				
				//���� ������ ���ڿ��� �����Ͽ� Range ��ü�� ��ȯ
				Range range = util.settingRange(filter, i);
		         
				// ���õ� ���������� ����Ʈ�� �߰�
		        if(rangeFormat != null) { 
					switch (rangeFormat) {
					case "birthDay":
						birthdays.add(new Range(range.getStart(), range.getEnd()));
						break;
					case "joinDate":
						joinDates.add(new Range(range.getStart(), range.getEnd()));
						break;
					case "lastLogin":
						lastLogins.add(new Range(range.getStart(), range.getEnd()));
						break;
					}
		        }
			}
			
			// Ű���� �˻�
			for(int i=1; i<=keywordCount; i++) {
				String keywordFormat = (String)filter.get("keywordFormat"+i);
				// ���õ� ���������� ����Ʈ�� �߰�
				if(keywordFormat != null) {
					switch (keywordFormat) {
					case "no":
						int no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							nos.add(no);
						break;
					case "email":
						emails.add((String)filter.get("keywordValue"+i));
						break;
					case "name":
						names.add((String)filter.get("keywordValue"+i));
						break;
					case "phone":
						phones.add((String)filter.get("keywordValue"+i));
						break;
					case "address":
						addresses.add((String)filter.get("keywordValue"+i));
						break;
					}
				}
			}
			
			// ȣ��Ʈ ���� ���� �߰�
			if((String)filter.get("host") != null){
				hostStates.add(((String)filter.get("host")).equals("")?10:Integer.parseInt((String)filter.get("host")));
			}
			if((String)filter.get("superHost") != null){
				hostStates.add(((String)filter.get("superHost")).equals("")?10:Integer.parseInt((String)filter.get("superHost")));
			}
			if((String)filter.get("guest") != null){
				hostStates.add(((String)filter	.get("guest")).equals("")?10:Integer.parseInt((String)filter.get("guest")));
			}
			
			// ���� ���� �߰�
			if((String)filter.get("active") != null){
				states.add(((String)filter.get("active")).equals("")?10:Integer.parseInt((String)filter.get("active")));
			}
			if((String)filter.get("inactive") != null){
				states.add(((String)filter.get("inactive")).equals("")?10:Integer.parseInt((String)filter.get("inactive")));
			}
			if((String)filter.get("pause") != null){
				states.add(((String)filter.get("pause")).equals("")?10:Integer.parseInt((String)filter.get("pause")));
			}
			
			// ���Ϳ��� ���� �˻������� �ʿ� ����
			map.put("birthdays", birthdays);
			map.put("lastLogins", lastLogins);
			map.put("joinDates", joinDates);
			
			map.put("nos", nos);
			map.put("names", names);
			map.put("emails", emails);
			map.put("phones", phones);
			map.put("addresses", addresses);
			map.put("hostStates", hostStates);
			map.put("states", states);
			
			// �ݾ� ���� �߰�
			map.put("totalPriceStart", (String)filter.get("totalPriceStart")==""?0:Integer.parseInt((String)filter.get("totalPriceStart")));
			map.put("totalPriceEnd", (String)filter.get("totalPriceEnd")==""?999999999:Integer.parseInt((String)filter.get("totalPriceEnd")));
			
			map.put("count", Integer.parseInt((String)filter.get("searchRow")));
			
			// ���� �̿��Ͽ� �˻�
			model.addAttribute("memberTableList", adminMemberService.getAdminMemberTable(map));
		}else {
			// filter�� ������� ���� ���
			map.put("filter", "off");
			map.put("count", adminTableDefaultRowCount);
			model.addAttribute("memberTableList", adminMemberService.getAdminMemberTable(map));
		}
		
		
		// ������Ʈ �ð��� ��ȯ
		model.addAttribute("changeDate", adminTableNowDateFormat.format(new Date()));
		// ��û ���������� ��ȯ
		try {
			model.addAttribute("filter", jacksonmapper.writeValueAsString(filter));
		} catch (JsonProcessingException e) {
			// pasting error
			logger.info("!!! jacksonParsing Exception !!!");
		}
		
		return "admin/member/member";
	}
	
	@RequestMapping(value = "/admin/member/hosting", method = RequestMethod.GET)
	public String adminMemberHosting(@RequestParam Map<String, Object> filter, Model model) {
		// ���͸� ���� �⺻ ����
		Map<String, Object> map = new HashMap<String, Object>();
		
		// ���ͼ���
		// ���� ���Ͱ� ���� ��츦 Ȯ��. ���Ͱ� null�� ��� Ȥ�� ���ͳ� ��ü�� ���� ���
		if(filter.entrySet().size() != 0) {
			List<Range> hostingDates = new ArrayList<Range>();
			List<Integer> nos = new ArrayList<Integer>();
			List<String> names = new ArrayList<String>();
			List<String> memberNos = new ArrayList<String>();
			List<Integer> states = new ArrayList<Integer>();
			
			// ���� �˻��� ���� ��ȯ
			int rangeCount = (String)filter.get("rangeCount")==""?0:Integer.parseInt((String)filter.get("rangeCount"));
			int keywordCount = (String)filter.get("keywordCount")==""?0:Integer.parseInt((String)filter.get("keywordCount"));
			
			
			// ���� �˻� ����Ʈ ����
			for(int i=1; i<=rangeCount; i++) {
				String rangeFormat = (String)filter.get("rangeFormat"+i);
				
				//���� ������ ���ڿ��� �����Ͽ� Range ��ü�� ��ȯ
				Range range = util.settingRange(filter, i);
		         
				// ���õ� ���������� ����Ʈ�� �߰�
		        if(rangeFormat != null) {
		        	hostingDates.add(new Range(range.getStart(), range.getEnd()));
		        }
			}
			
			// Ű���� �˻�
			for(int i=1; i<=keywordCount; i++) {
				String keywordFormat = (String)filter.get("keywordFormat"+i);
				// ���õ� ���������� ����Ʈ�� �߰�
				if(keywordFormat != null) {
					switch (keywordFormat) {
					case "hostingNo":
						int no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							nos.add(no);
						break;
					case "title":
						names.add((String)filter.get("keywordValue"+i));
						break;
					case "hostNo":
						memberNos.add((String)filter.get("keywordValue"+i));
						break;
					}
				}
			}
			
			// ���� ���� �߰�
			if((String)filter.get("active") != null){
				states.add(((String)filter.get("active")).equals("")?10:Integer.parseInt((String)filter.get("active")));
			}
			if((String)filter.get("inactive") != null){
				states.add(((String)filter.get("inactive")).equals("")?10:Integer.parseInt((String)filter.get("inactive")));
			}
			if((String)filter.get("pause") != null){
				states.add(((String)filter.get("pause")).equals("")?10:Integer.parseInt((String)filter.get("pause")));
			}
			
			map.put("hostingDates", hostingDates);
			
			map.put("nos", nos);
			map.put("names", names);
			map.put("memberNos", memberNos);
			map.put("states", states);
			
			
			// �̿��� �� ���� �߰�
			map.put("usedCountStart", (String)filter.get("usedCountStart")==""?0:Integer.parseInt((String)filter.get("usedCountStart")));
			map.put("usedCountEnd", (String)filter.get("usedCountEnd")==""?999999999:Integer.parseInt((String)filter.get("usedCountEnd")));
			map.put("reservationCountStart", (String)filter.get("reservationCountStart")==""?0:Integer.parseInt((String)filter.get("reservationCountStart")));
			map.put("reservationCountEnd", (String)filter.get("reservationCountEnd")==""?999999999:Integer.parseInt((String)filter.get("reservationCountEnd")));
			
			map.put("count", Integer.parseInt((String)filter.get("searchRow")));
			/**/
			// ���� �̿��Ͽ� �˻�
			model.addAttribute("hostingTableList", adminMemberService.getAdminHostingTable(map));
		}else {
			// filter�� ������� ���� ���
			map.put("filter", "off");
			map.put("count", adminTableDefaultRowCount);
			model.addAttribute("hostingTableList", adminMemberService.getAdminHostingTable(map));
		}
		
		// ������Ʈ �ð��� ��ȯ
		model.addAttribute("changeDate", adminTableNowDateFormat.format(new Date()));
		// ��û ���������� ��ȯ
		try {
			model.addAttribute("filter", jacksonmapper.writeValueAsString(filter));
		} catch (JsonProcessingException e) {
			// pasting error
			logger.info("!!! jacksonParsing Exception !!!");
		}
		
		return "admin/member/hosting";
	}
	
	@RequestMapping(value = "/admin/member/notice", method = RequestMethod.GET)
	public String adminMemberNotice(@RequestParam Map<String, Object> filter, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		// ���ͼ���
		// ���� ���Ͱ� ���� ��츦 Ȯ��. ���ͳ� ��ü�� ���� ���
		if(filter.entrySet().size() != 0 && filter.get("writeSuccess") == null) {
			List<Range> messageDates = new ArrayList<Range>();
			List<String> contents = new ArrayList<String>();
			List<Integer> senderNos = new ArrayList<Integer>();
			List<Integer> receiverNos = new ArrayList<Integer>();
			List<Integer> states = new ArrayList<Integer>();
			List<Integer> selects = new ArrayList<Integer>();
			
			// ���� �˻��� ���� ��ȯ
			int rangeCount = (String)filter.get("rangeCount")==""?0:Integer.parseInt((String)filter.get("rangeCount"));
			int keywordCount = (String)filter.get("keywordCount")==""?0:Integer.parseInt((String)filter.get("keywordCount"));
			
			// ���� �˻� ����Ʈ ����
			for(int i=1; i<=rangeCount; i++) {
				String rangeFormat = (String)filter.get("rangeFormat"+i);
				
				//���� ������ ���ڿ��� �����Ͽ� Range ��ü�� ��ȯ
				Range range = util.settingRange(filter, i);
		         
				// ���õ� ���������� ����Ʈ�� �߰�
		        if(rangeFormat != null) {
		        	messageDates.add(new Range(range.getStart(), range.getEnd()));
		        }
			}
			
			// Ű���� �˻�
			for(int i=1; i<=keywordCount; i++) {
				String keywordFormat = (String)filter.get("keywordFormat"+i);
				// ���õ� ���������� ����Ʈ�� �߰�
				if(keywordFormat != null) {
					int no = 0;
					switch (keywordFormat) {
					case "message":
						contents.add((String)filter.get("keywordValue"+i));
						break;
					case "memberNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							receiverNos.add(no);
						break;
					case "managerNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							senderNos.add(no);
						break;
					}
				}
			}
			
			// ���� ���� �߰�
			if((String)filter.get("statusSend") != null){
				states.add(((String)filter.get("statusSend")).equals("")?10:Integer.parseInt((String)filter.get("statusSend")));
			}
			if((String)filter.get("statusCancel") != null){
				states.add(((String)filter.get("statusCancel")).equals("")?10:Integer.parseInt((String)filter.get("statusCancel")));
			}
			
			// ȸ�� ���� ���� �߰�
			if((String)filter.get("divSelect") != null){
				selects.add(((String)filter.get("divSelect")).equals("")?10:Integer.parseInt((String)filter.get("divSelect")));
			}
			if((String)filter.get("divEvery") != null){
				selects.add(((String)filter.get("divEvery")).equals("")?10:Integer.parseInt((String)filter.get("divEvery")));
			}
			
			//
			map.put("messageDates", messageDates);
			
			map.put("contents", contents);
			map.put("senderNos", senderNos);
			map.put("receiverNos", receiverNos);
			
			map.put("states", states);
			map.put("selects", selects);
			
			// ī��Ʈ�� ���� ��� ī��Ʈ ����
			map.put("count", Integer.parseInt((String)filter.get("searchRow")));
			/**/
			// ���� �̿��Ͽ� �˻�
			model.addAttribute("messageTableList", adminMemberService.getAdminMessageTable(map));
		}else {
			// filter�� ������� ���� ���
			map.put("filter", "off");
			map.put("count", adminTableDefaultRowCount);
			model.addAttribute("messageTableList", adminMemberService.getAdminMessageTable(map));
		}

		// ������Ʈ �ð��� ��ȯ
		model.addAttribute("changeDate", adminTableNowDateFormat.format(new Date()));
		// ��û ���������� ��ȯ
		try {
			model.addAttribute("filter", jacksonmapper.writeValueAsString(filter));
		} catch (JsonProcessingException e) {
			// pasting error
		}
		
		return "admin/member/notice";
	}
	
	@RequestMapping(value = "/admin/member/notice", method = RequestMethod.POST)
	public String adminMemberUpdateNotice(@RequestParam Map<String, Object> writeContent, Model model, HttpServletRequest request) throws MemberInfoNotFoundException {
		//Rest
		HttpSession session = request.getSession();
		Message message = new Message();
		message.setMessageContent((String)writeContent.get("message"));
		message.setMessageReceiverNo(((String)writeContent.get("receiver")).equals("")?0:Integer.parseInt((String)writeContent.get("receiver")));
		Member loginMember = ((Member)session.getAttribute("loginMember"));
		if(loginMember == null) throw new MemberInfoNotFoundException("�α��� �� �̿� �ٶ��ϴ�.");
		message.setMessageSenderNo(loginMember.getMemberNo());
		// session ȸ���̸� ����
		//message.setMessageSenderNo(((Member)request.getSession().getAttribute("login?")).getMemberNo());
		
		return "redirect: notice?writeSuccess=" + adminMemberService.addAdminMessage(message)+","+message.getMessageContent()
					+ (writeContent.get("queryString").equals("")?"":"&"+writeContent.get("queryString").equals(""));
	} 
	
	@RequestMapping(value = "/admin/member/notice/update", method = RequestMethod.POST)
	@ResponseBody
	public String adminMemberUpdateNoticeState(@RequestParam int messageNo) {
		int rows = adminMemberService.modifyAdminMessageState(messageNo);
		try {
			return jacksonmapper.writeValueAsString(messageNo+"");
		} catch (JsonProcessingException e) {
			return "";
		}
	}
	
	
}

