package site.yolowa.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import site.yolowa.dao.HostingDAO;
import site.yolowa.dao.MemberDAO;
import site.yolowa.dto.AdminHostingMemberDetail;
import site.yolowa.dto.AdminHostingReservationDetail;
import site.yolowa.dto.AdminMemberDetail;
import site.yolowa.dto.Member;
import site.yolowa.utils.AdminUtil;

@Service
public class AdminComponentServiceImpl implements AdminComponentService {
	
	@Autowired
	private MemberDAO memberDao;
	
	@Autowired
	private HostingDAO hostingDao;
	
	@Autowired
	private AdminUtil adminUtil;
	//yyyy-MM-dd
	@Autowired
	@Qualifier("underBarDateFormat")
	private SimpleDateFormat underBarDateFormat;
	
	
	@Override
	public AdminMemberDetail getAdminMemberComponent(int memberNo){
		return memberDao.selectAdminMemberDetail(memberNo);
	}
	@Override
	public AdminHostingMemberDetail getAdminHostingMemberHComponent(int hostingNo) {
		return memberDao.selectAdminMemberHostingDetail(memberDao.AdminHostingMemberDetailNo(hostingNo));
	}
	@Override
	public AdminHostingMemberDetail getAdminHostingMemberMComponent(int memberNo) {
		return memberDao.selectAdminMemberHostingDetail(memberNo);
	}
	@Override
	public AdminHostingReservationDetail getAdminHostingComponent(int hostingNo) {
		return hostingDao.selectAdminHostingDetail(hostingNo);
	}
	@Override
	public int modifyMemberMemo(Member member) {
		/* ���߿� Ʈ������ ó��
		if(memberDao.selectNoMember(member.getMemberNo())== null){
			throw memberNoSearchException("��û�Ͻ� ȸ���� �������� �ʽ��ϴ�.");
		}
		*/
		
		return memberDao.updateMemberMemo(member);
	}
	@Override
	public String modifyMemberForbiddenDate(Member member, int plus) {
		/*
		if(memberDao.selectNoMember(member.getMemberNo()) == null) throw memberNoSearchException("�Ű� ��� ȸ���� �������� �ʽ��ϴ�.");
		*/
		if(plus>30) {
			member.setMemberForbiddenDate("2050-01-01");
		}else {
			if(member.getMemberForbiddenDate() == null) {
				member.setMemberForbiddenDate(underBarDateFormat.format(new Date()));
			}
			member.setMemberForbiddenDate(adminUtil.setForbiddenDate(member, plus));
		}
		if(memberDao.updateMemberForbiddenDate(member) == 1) {
			return member.getMemberForbiddenDate();
		}
		return "";
	}
	@Override
	public String modifyMemberForbiddenDateReset(int memberNo) {
		/*
		if(memberDao.selectNoMember(member.getMemberNo()) == null) throw memberNoSearchException("�Ű� ��� ȸ���� �������� �ʽ��ϴ�.");
		 */
		// ����
		memberDao.updateMemberForbiddenDateReset(memberNo);
		return "";
	}
	
}
