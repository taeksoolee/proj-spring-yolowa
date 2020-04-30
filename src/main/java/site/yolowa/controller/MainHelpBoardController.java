package site.yolowa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.yolowa.dto.Board;
import site.yolowa.dto.MainMessage;
import site.yolowa.dto.MainMessageCount;
import site.yolowa.dto.Member;
import site.yolowa.service.MainHelpBoardService;
import site.yolowa.service.MainMessageService;

@Controller
public class MainHelpBoardController {
	@Autowired
	MainHelpBoardService mainHelpBoardService;
	
	@Autowired
	MainMessageService mainMessageService;
	
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
	
	//���� ���������� �κ�
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String HelpBoard(Board board, Model model){
		model.addAttribute("helpBoardViewList", mainHelpBoardService.getHelpBoardViewList());
		
		return "main/help";
	}
	
	//���� faq.jsp ��ºκ�
	@RequestMapping(value = "/faq", method = RequestMethod.GET)
	public String HelpBoardFAQ(@RequestParam(required = false) Board board, @RequestParam(value="type", required = false) String type, 
			@RequestParam(value = "boardNo", defaultValue = "0", required = false) int boardNo, Model model){
		
		List<Board> faqBoardList1 = new ArrayList<Board>();
		List<Board> faqBoardList2 = new ArrayList<Board>();
		List<Board> faqBoardList3 = new ArrayList<Board>();
		List<Board> faqBoardList4 = new ArrayList<Board>();
		List<Board> faqBoardList5 = new ArrayList<Board>();
		List<Board> faqBoardList6 = new ArrayList<Board>();
		
		for(Board faqBoard : mainHelpBoardService.getHelpBoardList()) {
			if(faqBoard.getBoardCategory()==1) {
				faqBoardList1.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==2) {
				faqBoardList2.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==3) {
				faqBoardList3.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==4) {
				faqBoardList4.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==5) {
				faqBoardList5.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==6) {
				faqBoardList6.add(faqBoard);
			}
		}
		 
		model.addAttribute("boardNo", boardNo);
		model.addAttribute("faqBoardList1", faqBoardList1);
		model.addAttribute("faqBoardList2", faqBoardList2);
		model.addAttribute("faqBoardList3", faqBoardList3);
		model.addAttribute("faqBoardList4", faqBoardList4);
		model.addAttribute("faqBoardList5", faqBoardList5);
		model.addAttribute("faqBoardList6", faqBoardList6);
		model.addAttribute("type", type);
		
		return "main/faq";
	}
	
	//���� �˻����
	@RequestMapping(value = "/help", method = RequestMethod.POST)
	public String HelpBoardSearch(@RequestParam(required = false) String boardContent, Model model){
		Board board = new Board();
		board.setBoardTitle(boardContent);
		board.setBoardContent(boardContent);
		
		List<Board> faqBoardList1 = new ArrayList<Board>();
		List<Board> faqBoardList2 = new ArrayList<Board>();
		List<Board> faqBoardList3 = new ArrayList<Board>();
		List<Board> faqBoardList4 = new ArrayList<Board>();
		List<Board> faqBoardList5 = new ArrayList<Board>();
		List<Board> faqBoardList6 = new ArrayList<Board>();
		
		for(Board faqBoard : mainHelpBoardService.getHelpBoard(board)) {
			if(faqBoard.getBoardCategory()==1) {
				faqBoardList1.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==2) {
				faqBoardList2.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==3) {
				faqBoardList3.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==4) {
				faqBoardList4.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==5) {
				faqBoardList5.add(faqBoard);
			} else if(faqBoard.getBoardCategory()==6) {
				faqBoardList6.add(faqBoard);
			}
		}
		 
		model.addAttribute("faqBoardList1", faqBoardList1);
		model.addAttribute("faqBoardList2", faqBoardList2);
		model.addAttribute("faqBoardList3", faqBoardList3);
		model.addAttribute("faqBoardList4", faqBoardList4);
		model.addAttribute("faqBoardList5", faqBoardList5);
		model.addAttribute("faqBoardList6", faqBoardList6);
		model.addAttribute("searchText", boardContent);
		
		return "main/faq";
	}
	
	//��ȸ�� �������
	@RequestMapping(value = "/faq/viewCount", method = RequestMethod.POST)
	@ResponseBody
	public int updateHelpBoardView(@RequestParam(defaultValue = "1") int boardNo) {
		Board board = new Board();
		board.setBoardNo(boardNo);
		return mainHelpBoardService.modifyHelpBoardView(board);
	}
	
	
}
