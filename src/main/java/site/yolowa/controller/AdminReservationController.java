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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.yolowa.dao.ReservationDAOImpl;
import site.yolowa.dto.Range;
import site.yolowa.service.AdminReservationService;
import site.yolowa.utils.AdminUtil;

@Controller
public class AdminReservationController {
	private Logger logger = LoggerFactory.getLogger(AdminReservationController.class);
	private Integer adminTableDefaultRowCount = 300;
	
	@Autowired
	private AdminUtil util;
	
	@Autowired
	private AdminReservationService adminReservationService;
	
	//yyyy�� MM�� dd�� hh:mm:ss
	@Autowired
	@Qualifier("AdminTableNowDateFormat")
	private SimpleDateFormat adminTableNowDateFormat;
	
	@Autowired
	@Qualifier("jacksonMapper")
	private ObjectMapper jacksonmapper;
	
	@RequestMapping(value = "/admin/reservation/reservation", method = RequestMethod.GET)
	public String adminReservationReservation(@RequestParam Map<String, Object> filter, Model model) {
		
		//
		Map<String, Object> map = new HashMap<String, Object>();
		// ���ͼ���
		// ���� ���Ͱ� ���� ��츦 Ȯ��. ���ͳ� ��ü�� ���� ���
		if(filter.entrySet().size() != 0) {
			// ���͸� ���� �⺻ ����
			List<Range> checkins = new ArrayList<Range>();
			List<Range> checkouts = new ArrayList<Range>();
			
			List<Integer> nos = new ArrayList<Integer>();
			List<String> memberNos = new ArrayList<String>();
			List<String> hostingNos = new ArrayList<String>();
			List<String> payers = new ArrayList<String>();
			
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
					case "checkIn":
						checkins.add(new Range(range.getStart(), range.getEnd()));
						break;
					case "checkOut":
						checkouts.add(new Range(range.getStart(), range.getEnd()));
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
					case "reservationNo":
						int no = (String)filter.get("keywordValue"+i)==""?0:Integer.parseInt((String)filter.get("keywordValue"+i));
						// ���Ϳ� 0�� ��� �� ��� ���͸� ���� �ʵ��� ��.
						if(no != 0)
							nos.add(no);
						break;
					case "memberNo":
						memberNos.add((String)filter.get("keywordValue"+i));
						break;
					case "hostingNo":
						hostingNos.add((String)filter.get("keywordValue"+i));
						break;
					case "payer":
						payers.add((String)filter.get("keywordValue"+i));
						break;
					}
				}
			}
			
			// ȣ��Ʈ ���� ���� �߰�
			if((String)filter.get("stateWating") != null){
				states.add(((String)filter.get("stateWating")).equals("")?10:Integer.parseInt((String)filter.get("stateWating")));
			}
			if((String)filter.get("stateAccess") != null){
				states.add(((String)filter.get("stateAccess")).equals("")?10:Integer.parseInt((String)filter.get("stateAccess")));
			}
			if((String)filter.get("stateComplete") != null){
				states.add(((String)filter	.get("stateComplete")).equals("")?10:Integer.parseInt((String)filter.get("stateComplete")));
			}
			if((String)filter.get("stateCancel") != null){
				states.add(((String)filter	.get("stateCancel")).equals("")?10:Integer.parseInt((String)filter.get("stateCancel")));
			}
			
			// ���Ϳ��� ���� �˻������� �ʿ� ����
			map.put("checkins", checkins);
			map.put("checkouts", checkouts);
			
			map.put("nos", nos);
			map.put("memberNos", memberNos);
			map.put("hostingNos", hostingNos);
			map.put("payers", payers);
			
			map.put("states", states);
			
			map.put("count", Integer.parseInt((String)filter.get("searchRow")));
			
			// ���� �̿��Ͽ� �˻�
			model.addAttribute("reservationTableList", adminReservationService.getAdminReservationTable(map));
		}else {
			// filter�� ������� ���� ���
			map.put("filter", "off");
			map.put("count", adminTableDefaultRowCount);
			model.addAttribute("reservationTableList", adminReservationService.getAdminReservationTable(map));
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
		//
		
		return "admin/reservation/reservation";
	}
}
