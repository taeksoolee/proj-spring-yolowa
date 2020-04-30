package site.yolowa.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import site.yolowa.dao.ClaimDAO;
import site.yolowa.dao.MemberDAO;
import site.yolowa.dao.ReviewDAOImpl;
import site.yolowa.dto.AdminClaimReview;
import site.yolowa.dto.Claim;
import site.yolowa.dto.Member;
import site.yolowa.dto.Review;
import site.yolowa.utils.AdminUtil;

@Service
public class AdminClaimServiceImpl implements AdminClaimService {
	@Autowired
	private MemberDAO memberDao;
	@Autowired
	private ClaimDAO claimDao;
	@Autowired
	private ReviewDAOImpl reviewDao;
	@Autowired
	private AdminUtil adminUtil;
	
	
	@Override
	public List<Claim> getAdminClaimUserTable(Map<String, Object> map) {
		return claimDao.selectAdminClaimUserTable(map);
	}
	
	@Override
	public List<AdminClaimReview> getAdminClaimReviewTable(Map<String, Object> map){
		return claimDao.selectAdminClaimReviewTable(map);
	}
	
	@Override
	public int modifyClaimMemberState (Claim claim, int plus) {
		Member member = memberDao.selectNoMember(claim.getClaimSuspectNo());
		/*
		if(member == null) throw memberNoSearchException("�Ű� ��� ȸ���� �������� �ʽ��ϴ�.");
		*/
		// ó���Ϸ���
		if(claim.getClaimState() == 2) {
			member.setMemberForbiddenDate(adminUtil.setForbiddenDate(member, plus));
			// 7�� ����
			memberDao.updateMemberForbiddenDate(member);
		}
		return claimDao.updateClaimState(claim);
	}
	
	@Override
	public int modifyClaimMemberState (List<Claim> claimList, int plus) {
		int rows = 0;
		for(Claim claim : claimList) {
			Member member = memberDao.selectNoMember(claim.getClaimSuspectNo());
			/*
			if(member == null) throw memberNoSearchException("�Ű� ��� ȸ���� �������� �ʽ��ϴ�.");
			*/
			// ó���Ϸ���
			if(claim.getClaimState() == 2) {
				member.setMemberForbiddenDate(adminUtil.setForbiddenDate(member, plus));
				// 7�� ����
				memberDao.updateMemberForbiddenDate(member);
			}
			rows += claimDao.updateClaimState(claim);
		}
		return rows;
	}
	
	@Override
	public int modifyClaimReviewState (Claim claim) {
		Review review = reviewDao.selectReviewNo(claim.getClaimSuspectReviewNo());
		/*
		if(review == null) throw new ReviewNoSearchException("�Ű� ��� ���䰡 �������� �ʽ��ϴ�.");
		*/
		// ��û�� ó���Ϸ���
		if(claim.getClaimState() == 2) {
			// ���� ����
			reviewDao.updateReviewStateStop(review);
		}else if(claim.getClaimState() == 1){
			// ���䰻��
			reviewDao.updateReviewStatePost(review);
		}
		return claimDao.updateClaimState(claim);
	}
	
	@Override
	public int modifyClaimReviewState (List<Claim> claimList) {
		int rows = 0;
		for(Claim claim : claimList) {
			Review review = reviewDao.selectReviewNo(claim.getClaimSuspectReviewNo());
			/*
			if(review == null) throw new ReviewNoSearchException("�Ű� ��� ���䰡 �������� �ʽ��ϴ�.");
			*/
			// ��û�� ó���Ϸ���
			if(claim.getClaimState() == 2) {
				// ���� ����
				reviewDao.updateReviewStateStop(review);
			}
			rows += claimDao.updateClaimState(claim);
		}
		return rows;
	}
	
}
