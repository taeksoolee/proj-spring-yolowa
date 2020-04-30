package site.yolowa.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.yolowa.dto.AdminClaimList;
import site.yolowa.dto.Claim;
import site.yolowa.dto.Range;
import site.yolowa.service.AdminClaimService;
import site.yolowa.service.AdminMemberServiceImpl;
import site.yolowa.utils.AdminUtil;

@Controller
public class AdminClaimController {
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
	private AdminClaimService adminClaimService;
	
	
	@RequestMapping(value = "/admin/claim/member", method = RequestMethod.GET)
	public String adminClaimMember(@RequestParam Map<String, Object> filter, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// ���ͼ���
		// ���� ���Ͱ� ���� ��츦 Ȯ��. ���ͳ� ��ü�� ���� ���
		if(filter.entrySet().size() != 0) {
			List<Range> claimDates = new ArrayList<Range>();
			List<String> reasons = new ArrayList<String>();
			List<Integer> applicantNos = new ArrayList<Integer>();
			List<Integer> suspectNos = new ArrayList<Integer>();
			List<Integer> suspectHostingNos = new ArrayList<Integer>();
			List<Integer> states = new ArrayList<Integer>();
			List<Integer> types = new ArrayList<Integer>();
			
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
		        	claimDates.add(new Range(range.getStart(), range.getEnd()));
		        }
			}
			
			// Ű���� �˻�
			for(int i=1; i<=keywordCount; i++) {
				String keywordFormat = (String)filter.get("keywordFormat"+i);
				// ���õ� ���������� ����Ʈ�� �߰�
				if(keywordFormat != null) {
					int no = 0;
					switch (keywordFormat) {
					case "content":
						reasons.add((String)filter.get("keywordValue"+i));
						break;
					case "applicantNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							applicantNos.add(no);
						break;
					case "suspectNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							suspectNos.add(no);
						break;
					case "suspectHostingNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							suspectHostingNos.add(no);
						break;
					}
				}
			}
			
			// ���� ���� �߰�
			if((String)filter.get("cancelState") != null){
				states.add(((String)filter.get("cancelState")).equals("")?10:Integer.parseInt((String)filter.get("cancelState")));
			}
			if((String)filter.get("watingState") != null){
				states.add(((String)filter.get("watingState")).equals("")?10:Integer.parseInt((String)filter.get("watingState")));
			}
			if((String)filter.get("completeState") != null){
				states.add(((String)filter.get("completeState")).equals("")?10:Integer.parseInt((String)filter.get("completeState")));
			}
			
			// Ÿ�� ���� �߰�
			if((String)filter.get("toGuest") != null){
				types.add(((String)filter.get("toGuest")).equals("")?10:Integer.parseInt((String)filter.get("toGuest")));
			}
			if((String)filter.get("toHost") != null){
				types.add(((String)filter.get("toHost")).equals("")?10:Integer.parseInt((String)filter.get("toHost")));
			}
			
			//
			map.put("claimDates", claimDates);
			map.put("reasons", reasons);
			map.put("applicantNos", applicantNos);
			map.put("suspectNos", suspectNos);
			map.put("suspectHostingNos", suspectHostingNos);
			map.put("states", states);
			map.put("types", types);
			
			// ī��Ʈ�� ���� ��� ī��Ʈ ����
			map.put("count", Integer.parseInt((String)filter.get("searchRow")));
			/**/
			// ���� �̿��Ͽ� �˻�
			model.addAttribute("userClaimTableList", adminClaimService.getAdminClaimUserTable(map));
		}else {
			// filter�� ������� ���� ���
			map.put("filter", "off");
			map.put("count", adminTableDefaultRowCount);
			model.addAttribute("userClaimTableList", adminClaimService.getAdminClaimUserTable(map));
		}
	
		// ������Ʈ �ð��� ��ȯ
		model.addAttribute("changeDate", adminTableNowDateFormat.format(new Date()));
		// ��û ���������� ��ȯ
		try {
			model.addAttribute("filter", jacksonmapper.writeValueAsString(filter));
		} catch (JsonProcessingException e) {
			// pasting error
		}
		return "admin/claim/member";
	}
	
	@RequestMapping(value = "/admin/claim/review", method = RequestMethod.GET)
	public String adminClaimReview(@RequestParam Map<String, Object> filter, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// ���ͼ���
		// ���� ���Ͱ� ���� ��츦 Ȯ��. ���ͳ� ��ü�� ���� ���
		if(filter.entrySet().size() != 0) {
			List<Range> claimDates = new ArrayList<Range>();
			List<String> reasons = new ArrayList<String>();
			List<Integer> applicantNos = new ArrayList<Integer>();
			List<Integer> suspectReviewNos = new ArrayList<Integer>();
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
		        	claimDates.add(new Range(range.getStart(), range.getEnd()));
		        }
			}
			
			// Ű���� �˻�
			for(int i=1; i<=keywordCount; i++) {
				String keywordFormat = (String)filter.get("keywordFormat"+i);
				// ���õ� ���������� ����Ʈ�� �߰�
				if(keywordFormat != null) {
					int no = 0;
					switch (keywordFormat) {
					case "content":
						reasons.add((String)filter.get("keywordValue"+i));
						break;
					case "applicantNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							applicantNos.add(no);
						break;
					case "suspectReviewNo":
						no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							suspectReviewNos.add(no);
						break;
					}
				}
			}
			
			// ���� ���� �߰�
			if((String)filter.get("cancelState") != null){
				states.add(((String)filter.get("cancelState")).equals("")?10:Integer.parseInt((String)filter.get("cancelState")));
			}
			if((String)filter.get("watingState") != null){
				states.add(((String)filter.get("watingState")).equals("")?10:Integer.parseInt((String)filter.get("watingState")));
			}
			if((String)filter.get("completeState") != null){
				states.add(((String)filter.get("completeState")).equals("")?10:Integer.parseInt((String)filter.get("completeState")));
			}
			//
			map.put("claimDates", claimDates);
			map.put("reasons", reasons);
			map.put("applicantNos", applicantNos);
			map.put("suspectReviewNos", suspectReviewNos);
			map.put("states", states);
			
			// ī��Ʈ�� ���� ��� ī��Ʈ ����
			map.put("count", Integer.parseInt((String)filter.get("searchRow")));
			/**/
			// ���� �̿��Ͽ� �˻�
			model.addAttribute("reviewClaimTableList", adminClaimService.getAdminClaimReviewTable(map));
		}else {
			// filter�� ������� ���� ���
			map.put("filter", "off");
			map.put("count", adminTableDefaultRowCount);
			System.out.println(adminClaimService.getAdminClaimReviewTable(map));
			model.addAttribute("reviewClaimTableList", adminClaimService.getAdminClaimReviewTable(map));
		}
	
		// ������Ʈ �ð��� ��ȯ
		model.addAttribute("changeDate", adminTableNowDateFormat.format(new Date()));
		// ��û ���������� ��ȯ
		try {
			model.addAttribute("filter", jacksonmapper.writeValueAsString(filter));
		} catch (JsonProcessingException e) {
			// pasting error
		}
		
		return "admin/claim/review";
	}
	
	@RequestMapping(value = "/rest/admin/claim/member/state/all/{plus}", method = RequestMethod.POST)
	@ResponseBody
	public int adminClaimMemberUpdateState(@RequestBody List<Claim> claimList, @PathVariable int plus) {
		System.out.println(claimList);
		int rows = adminClaimService.modifyClaimMemberState(claimList, plus);
		System.out.println(rows);
		return rows;
	}
	
	@RequestMapping(value = "/rest/admin/claim/member/state/{plus}", method = RequestMethod.POST)
	@ResponseBody
	public int adminClaimMemberUpdateState(@RequestBody Claim claim, @PathVariable int plus) {
		int rows = adminClaimService.modifyClaimMemberState(claim, plus);
		return rows;
	}
	
	@RequestMapping(value = "/rest/admin/claim/review/state", method = RequestMethod.POST)
	@ResponseBody
	public int adminClaimReviewUpdateState(@RequestBody Claim claim) {
		int rows = adminClaimService.modifyClaimReviewState(claim);
		return rows;
	}
	
	@RequestMapping(value = "/rest/admin/claim/review/state/all", method = RequestMethod.POST)
	@ResponseBody
	public int adminClaimReviewUpdateState(@RequestBody List<Claim> claimList) {
		int rows = adminClaimService.modifyClaimReviewState(claimList);
		return rows;
	}
	
	@RequestMapping(value = "/rest/admin/claim/review/state/post", method = RequestMethod.POST)
	@ResponseBody
	public int adminClaimReviewUpdateStatePost(@RequestBody Claim claim) {
		int rows = adminClaimService.modifyClaimReviewState(claim);
		return rows;
	}
	
}
