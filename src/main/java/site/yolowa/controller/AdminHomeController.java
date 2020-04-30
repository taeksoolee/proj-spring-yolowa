package site.yolowa.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.yolowa.dto.AdminDashBoardTable;
import site.yolowa.dto.Range;
import site.yolowa.service.AdminHomeService;
import site.yolowa.service.AdminReservationService;

@Controller
public class AdminHomeController {
	
	@Autowired
	private AdminHomeService adminHomeService;
	
	@Autowired
	private AdminReservationService adminReservationService;
	
	@Autowired
	@Qualifier("jacksonMapper")
	private ObjectMapper jacksonmapper;
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminDashBoard() {
		return "redirect: admin/home/dashboard";
	}
	
	//yy/MM/dd
	@Autowired
	@Qualifier("slashDateFormat")
	private SimpleDateFormat slashDateFormat;
	
	//yy/MM
	@Autowired
	@Qualifier("slashYearMonthFormat")
	SimpleDateFormat slashYearMonthFormat;
	
	
	@RequestMapping(value = "/admin/home/dashboard", method = RequestMethod.GET)
	public String adminHomeDashBoard(Model model) {
		Map<String, Object> cards = adminHomeService.getAdminHomeCard();
		
		model.addAttribute("joinMemberCard", cards.get("joinMemberCard"));
		model.addAttribute("stateMemberCard", cards.get("stateMemberCard"));
		model.addAttribute("applyHostingCard", cards.get("applyHostingCard"));
		model.addAttribute("stateHostingCard", cards.get("stateHostingCard"));
		model.addAttribute("reviewClaimCard", cards.get("reviewClaimCard"));
		model.addAttribute("guestClaimCard", cards.get("guestClaimCard"));
		model.addAttribute("hostClaimCard", cards.get("hostClaimCard"));
		
		return "admin/home/dashboard";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/admin/home/dashboard/table", method = RequestMethod.POST)
	@ResponseBody
	public String adminHomeDashBoard(@RequestParam int count, @RequestParam String end) {
		// ����¥�� �Ⱓ�� �޾Ƽ� ��ȯ �� 
		String[] ends = end.split("/");
		String[] dates = new String[count];
		Date endDate = new Date();
		
		endDate.setMonth(Integer.parseInt(ends[1]));
		endDate.setYear(Integer.parseInt(ends[0]));
		
		for(int i = 0; i<count; i++) {
			endDate.setMonth(endDate.getMonth()-1);
			dates[i] = slashYearMonthFormat.format(endDate);
		}
		
		// query �˻�
		List<AdminDashBoardTable> adbtList = adminReservationService.getDashboardTable(new Range(dates[count-1], dates[0]));
		// ����� ��ȯ�� ����Ʈ��ü ����
		List<AdminDashBoardTable> result = new ArrayList<AdminDashBoardTable>();
		for(int i = 0; i<count; i++) {
			AdminDashBoardTable table = new AdminDashBoardTable();
			table.setDateMonth(dates[i]);
			for(AdminDashBoardTable adbt : adbtList) {
				if(adbt.getDateMonth().equals(dates[i])) {
					table = adbt;
				}
			}
			result.add(table);
		}
		
		try {
			return jacksonmapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/admin/home/dashboard/chart/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> adminHomeDashBoard(@PathVariable("type") String type, @RequestParam String end, @RequestParam int count, HttpServletRequest request) {
		try {
			return adminHomeService.getAdminHomeChart(type, end, count, request);
		} catch (IOException e) {
			// ������ ���� ������ ��� ���� ó��
			return null;
		}
	}
}