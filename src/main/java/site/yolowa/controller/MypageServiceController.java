package site.yolowa.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import site.yolowa.dto.Claim;
import site.yolowa.dto.Hosting;
import site.yolowa.dto.Member;
import site.yolowa.dto.MypageHeaderCount;
import site.yolowa.dto.Reservation;
import site.yolowa.dto.Review;
import site.yolowa.service.MainMemberService;
import site.yolowa.service.MainMessageService;
import site.yolowa.service.MypageHostingService;
import site.yolowa.utils.Pager;

//���������� ȣ���� ���� ���� ��Ʈ�ѷ�

@Controller
public class MypageServiceController implements ApplicationContextAware {
	@Autowired
	private MypageHostingService mainHostingMypageService;
	
	@Autowired
	private MainMessageService MainMessageService;
	
	@Autowired
	private MainMemberService mainMemberService;
	
	//WebApplicationContext : SpringMVC�� Front Controller���� ����ϱ� ���� Spring Bean�� �����ϴ� Spring Container ���� �ν��Ͻ�
	private WebApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//ApplicationContext �ν��Ͻ��� WebApplicationContext �ν��Ͻ��� �θ� �ν��Ͻ��̹Ƿ� ��ü ����ȯ�� �ؾ߸� �ʵ忡 ���� ����
		context = (WebApplicationContext)applicationContext;
	}
	
	/*
	//ȣ���� ��� get ����
	*/
	@RequestMapping(value = "/mypage/addlisting", method = RequestMethod.GET)
	public String addListing(HttpSession session) {
		//�α��� ���� ó��
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		
		return "mypage/add-listing";
	}
	
	//ȣ���� ���
	@Transactional
	@RequestMapping(value = "/mypage/addlisting", method = RequestMethod.POST)
	public String addListing(@ModelAttribute Hosting hosting, HttpSession session) throws IOException {
		//�α��� ���� ó��
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		Member member = (Member)session.getAttribute("loginMember");
		//��� ����
		String uploadDirPath = context.getServletContext().getRealPath("/resources/img/hosting");
		//�̹��� �̸� ����
		String dbImageName = "";
		
		//���ε� ó�� �� �̹��� �߰�
		if (!hosting.getFile().isEmpty()) {
			for (MultipartFile file : hosting.getFile()) {
				String upload = System.currentTimeMillis()+"";
				file.transferTo(new File(uploadDirPath, upload));
				
				dbImageName += upload+"|";
			}
			hosting.setHostingImage(dbImageName);
		}
		
		mainHostingMypageService.addHosting(hosting);
		mainMemberService.updateMemberHostState(member.getMemberNo());
		
		return "redirect:/mypage/listings?hostingState=1";
	}
	
	//ȣ���� ������Ʈ �� ����
	@RequestMapping(value = "/mypage/modifylisting", method = RequestMethod.GET)
	public String modifyListing(HttpSession session, @RequestParam int hostingNo, Model model) {
		//�α��� ���� ó��
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		Member member = (Member)session.getAttribute("loginMember");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("hostingMemberNo", member.getMemberNo());
		searchMap.put("hostingNo", hostingNo);
		
		model.addAttribute("hosting", mainHostingMypageService.getMypageHosting(searchMap));
		
		return "mypage/add-listing";
	}
	
	//ȣ���� ������Ʈ
	@Transactional
	@RequestMapping(value = "/mypage/modifylisting", method = RequestMethod.POST)
	public String modifyListing(@ModelAttribute Hosting hosting, HttpSession session) throws IOException {
		//�α��� ���� ó��
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		
		//��� ����
		String uploadDirPath = context.getServletContext().getRealPath("/resources/img/hosting");
		//�̹��� �̸� ����
		String dbImageName = "";
		
		//���ε� ó�� �� �̹��� �߰�
		if (!hosting.getFile().isEmpty()) {
			for (MultipartFile file : hosting.getFile()) {
				String upload = System.currentTimeMillis()+"";
				file.transferTo(new File(uploadDirPath, upload));
				
				dbImageName += upload+"|";
			}
			hosting.setHostingImage(dbImageName);
		}
		
		mainHostingMypageService.modifyMypageHostingList(hosting);
		
		return "redirect:/mypage/dashboard";
	}
	
	
	
	/*
	//�������
	*/
	@RequestMapping(value = "/mypage/bookings", method = RequestMethod.GET)
	public String bookings(HttpSession session) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		
		return "mypage/bookings";
	}
	
	//������ ���÷��� �ϱ� ���� �޼ҵ�
	@RequestMapping(value = "mypage/bookingList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> bookingList(@RequestParam(defaultValue = "1") int pageNum, HttpSession session, @RequestParam int reservationState) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("reservationState", reservationState);
		countMap.put("hostingMemberNo", member.getMemberNo());
		
		int totalbooking = mainHostingMypageService.getBookingCount(countMap);
		
		int pageSize = 5; 
		int blockSize = 5; 
		
		Pager pager = new Pager(pageNum, totalbooking, pageSize, blockSize);
		
		//System.out.println(pager.getEndRow());
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("startRow", pager.getStartRow());
		pagerMap.put("endRow", pager.getEndRow());
		pagerMap.put("reservationState", reservationState);
		pagerMap.put("LoginMemberNo", member.getMemberNo());
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("bookingList", mainHostingMypageService.getBookingList(pagerMap));
		returnMap.put("pager", pager);
		
		return returnMap;
	}
	
	//���������� ���࿡�� ���� �ۼ�
	@Transactional
	@RequestMapping(value = "/writeGuestReview", method = RequestMethod.POST)
	@ResponseBody
	public String writeGuestReview(@RequestBody Review review) {
		MainMessageService.addReviewGuest(review);
		return "success";
	}
	
	
	
	/*
	//ȣ���� �뽬����
	 */
	@RequestMapping(value = "/mypage/dashboard", method = RequestMethod.GET)
	public String dashBoard(HttpSession session, Model model) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		Member member = (Member)session.getAttribute("loginMember");
		model.addAttribute("dashboardCount", mainHostingMypageService.getMypageHeaderCount(member.getMemberNo()));
		return "mypage/dashboard";
	}
	
	//�׺� �޼��� ī��Ʈ
	@ModelAttribute("newMessage")
	public MypageHeaderCount newMessage(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		return mainHostingMypageService.getMypageHeaderCount(member.getMemberNo());
	}

	
	
	/*
	//���Ұ���
	 */
	@RequestMapping(value = "/mypage/listings", method = RequestMethod.GET)
	public String listings(@RequestParam int hostingState, Model model, HttpSession session) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		model.addAttribute("hostingState", hostingState);
		return "mypage/listings";
	}
	
	//���Ұ����� ���� ���÷���
	@RequestMapping(value = "mypage/hostingList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> hostingList(@RequestParam(defaultValue = "1") int pageNum, HttpSession session, @RequestParam String orderby, @RequestParam int hostingState) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("hostingState", hostingState);
		countMap.put("hostingMemberNo", member.getMemberNo());
		
		int totalhosting = mainHostingMypageService.getMypageHostingCount(countMap);
		
		int pageSize = 5; 
		int blockSize = 5; 
		
		Pager pager = new Pager(pageNum, totalhosting, pageSize, blockSize);
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("startRow", pager.getStartRow());
		pagerMap.put("endRow", pager.getEndRow());
		pagerMap.put("hostingState", hostingState);
		pagerMap.put("hostingMemberNo", member.getMemberNo());
		pagerMap.put("order", orderby);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("hostingList", mainHostingMypageService.getMypageHostingList(pagerMap));
		returnMap.put("pager", pager);
		
		return returnMap;
	}
	
	//ȣ���� ���°� ����
	@Transactional
	@RequestMapping(value = "mypage/modifyHosting", method = {RequestMethod.PUT, RequestMethod.PATCH})
	@ResponseBody
	public String modifyHosting(@RequestBody Hosting hosting) {
		mainHostingMypageService.modifyMypageHosting(hosting);
		return "success";
	}
	
	
	
	/*
	//�ϸ�ũ
	 */
	@RequestMapping(value = "/mypage/bookmarks", method = RequestMethod.GET)
	public String bookmarks(HttpSession session) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		/* �׽�Ʈ
		Member member = (Member)session.getAttribute("loginMember");
		System.out.println(mainHostingMypageService.getBookmarkCount(member.getMemberNo()));
		 */
		return "mypage/bookmarks";
	}
	
	//�ϸ�ũ�� ���÷��� �ϱ� ���� �޼ҵ�
	@RequestMapping(value = "mypage/bookmarkList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> bookmarkList(@RequestParam(defaultValue = "1") int pageNum, HttpSession session, String orderby) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		int totalBookmark = mainHostingMypageService.getBookmarkCount(member.getMemberNo());
		
		int pageSize = 5; 
		int blockSize = 5; 
		
		Pager pager = new Pager(pageNum, totalBookmark, pageSize, blockSize);
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("startRow", pager.getStartRow());
		pagerMap.put("endRow", pager.getEndRow());
		pagerMap.put("orderby", orderby);
		pagerMap.put("LoginMemberNo", member.getMemberNo());
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("bookmarkList", mainHostingMypageService.getBookmarkList(pagerMap));
		returnMap.put("pager", pager);
		
		return returnMap;
	}
	
	//�������̼� ���°� ����
	@Transactional
	@RequestMapping(value = "mypage/modifyReservationState", method = {RequestMethod.PUT, RequestMethod.PATCH})
	@ResponseBody
	public String cansel(@RequestBody Reservation reservation) {
		mainHostingMypageService.modifyReservationState(reservation);
		return "success";
	}
	
	@RequestMapping(value = "/invoice", method = RequestMethod.GET)
	public String invoice(@RequestParam int reservationNo, Model model, HttpSession session) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		model.addAttribute("invoice", mainHostingMypageService.getInvoice(reservationNo));
		return "invoice";
	}
	
	
	
	/*
	//���������� �޼��� ������
	 */
	@RequestMapping(value = "/mypage/messages", method = RequestMethod.GET)
	public String messages(HttpSession session) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		return "mypage/messages";
	}
	
	//���������� �޼��� ���÷���
	@RequestMapping(value = "mypage/messageList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> messageList(@RequestParam(defaultValue = "1") int pageNum, HttpSession session, @RequestParam int orderby) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		Member member = (Member)session.getAttribute("loginMember");
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("messageReceiverNo", member.getMemberNo());
		countMap.put("messageState", orderby);
		
		int totalMessage = MainMessageService.getMessageMypageCount(countMap);
		int pageSize = 5; 
		int blockSize = 5; 
		
		Pager pager = new Pager(pageNum, totalMessage, pageSize, blockSize);
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("startRow", pager.getStartRow());
		pagerMap.put("endRow", pager.getEndRow());
		pagerMap.put("messageState", orderby);
		pagerMap.put("messageReceiverNo", member.getMemberNo());
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("messageList", MainMessageService.getMessageMypageList(pagerMap));
		returnMap.put("pager", pager);
		
		MainMessageService.updateMessageList(member.getMemberNo());
		MainMessageService.updateNoticeList(member.getMemberNo());
		
		return returnMap;
	}
	
	
	
	/*
	//�Խ�Ʈ ���� ������
	 */
	@RequestMapping(value = "/mypage/reviews", method = RequestMethod.GET)
	public String ReviewProfile(HttpSession session, Model model, @RequestParam int memberNo) {
		if (session.getAttribute("loginMember") == null) {
			return "main/loginPage";
		}
		
		model.addAttribute("member", mainHostingMypageService.getMypageMember(memberNo));
	
		return "mypage/reviews";
	}
	
	//���������� �Խ�Ʈ �Ű� ����
	@Transactional
	@RequestMapping(value = "/sendGuestClaim", method = RequestMethod.POST)
	@ResponseBody
	public String sendGuestClaim(@RequestBody Claim claim) {
		MainMessageService.addClaimGuest(claim);
		return "success";
	}
	
	
	//�Խ�Ʈ ���並 ���÷��� �ϱ� ���� �޼ҵ�
	@RequestMapping(value = "mypage/guestReviewList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> guestReviewList(@RequestParam(defaultValue = "1") int pageNum, HttpSession session, @RequestParam int memberNo) {
		if(session.getAttribute("loginMember") == null) {
			return null;
		}
		int totalGuestReview = mainHostingMypageService.getReviewGuestCount(memberNo);
		
		int pageSize = 5; 
		int blockSize = 5; 
		
		Pager pager = new Pager(pageNum, totalGuestReview, pageSize, blockSize);
		
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put("startRow", pager.getStartRow());
		pagerMap.put("endRow", pager.getEndRow());
		pagerMap.put("reviewMemberNo", memberNo);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("reviewList", mainHostingMypageService.getReviewGuestList(pagerMap));
		returnMap.put("pager", pager);
		
		return returnMap;
	}
	
	//���������� �Խ�Ʈ ���信�� ��۸��� �ۼ�
	@Transactional
	@RequestMapping(value = "/reWriteGuestReview", method = RequestMethod.POST)
	@ResponseBody
	public String reWriteGuestReview(@RequestBody Review review) {
		MainMessageService.addReviewReGuest(review);
		return "success";
	}
}
