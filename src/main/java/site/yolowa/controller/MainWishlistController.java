package site.yolowa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.yolowa.dto.MainMessage;
import site.yolowa.dto.MainMessageCount;
import site.yolowa.dto.Member;
import site.yolowa.service.MainMessageService;
import site.yolowa.service.WishlistService;

@Controller
public class MainWishlistController {

	@Autowired
	private WishlistService wishlistService;
	
	@Autowired
	private MainMessageService mainMessageService;
	
	/*
	 * ���� �������� ��� �������� ����� ��ȯ�ϱ� ���� �޼ҵ�
	 */
	@ModelAttribute("headerNotice")
	public List<MainMessage> getNoticeHeaderList(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		return mainMessageService.getMessageNoticeList(member.getMemberNo());
	}
	
	//���� �������� ��� �޼��� ����� ��ȯ�ϱ� ���� �޼ҵ�
	@ModelAttribute("headerMessage")
	public List<MainMessage> getMessageHeaderList(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		return mainMessageService.getMessageHeaderList(member.getMemberNo());
	}
	
	//���� �������� ���� ������ �޼��� ī����
	@ModelAttribute("headerMessageCount")
	public MainMessageCount getMessageHeaderCount(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		return mainMessageService.getCountingMessageHeader(member.getMemberNo());
	}
	
	
	//���
	@RequestMapping(value = "/wishlist", method = RequestMethod.GET)
	public String wishlist(Model model, HttpSession session) {
		Member member = (Member)session.getAttribute("loginMember");
		if(member != null) {
			model.addAttribute("wishList", wishlistService.getSelectMainWishlist(member.getMemberNo()));			
			return "main/wishlist";
		} else {
			return "redirect:/loginPage";
		}
			
		

	}
	
	//����
	@RequestMapping(value = "/wishlist", method = RequestMethod.DELETE)
	@ResponseBody
	public String removeWishlist(@RequestParam int hostingNo, HttpSession session) {
		Member member = (Member)session.getAttribute("loginMember");
		Map<String, Object> queryParam = new HashMap<>();
		queryParam.put("member_no", member.getMemberNo());
		queryParam.put("hosting_no", hostingNo);
		wishlistService.removeWishlist(queryParam);
		return String.valueOf(hostingNo);
	}

	//�߰�
	@RequestMapping(value = "/addWishlist", method = RequestMethod.GET)
	@ResponseBody
	public String addWishlist(@RequestParam int hostingNo, HttpSession session) {
		Member member=(Member)session.getAttribute("loginMember");
		Map<String, Object> queryValue = new HashMap<>();
		queryValue.put("member_no", member.getMemberNo());
		queryValue.put("hosting_no", hostingNo);
		wishlistService.addWishlist(queryValue);
		return "success";
	}

	}
	 
	 
	 