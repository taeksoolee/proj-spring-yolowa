package site.yolowa.dao;

import java.util.List;
import java.util.Map;

import site.yolowa.dto.AdminCard;
import site.yolowa.dto.AdminChart;
import site.yolowa.dto.AdminClaimReview;
import site.yolowa.dto.Claim;
import site.yolowa.dto.Range;

public interface ClaimDAO {
	List<Claim> selectAdminClaimUserTable(Map<String, Object> map);
	int updateClaimState(Claim claim);
	List<AdminClaimReview> selectAdminClaimReviewTable(Map<String, Object> map);
	List<AdminChart> selectAdminClaimChart(Range range);
	List<AdminCard> selectAdminToGuestClaimCard();
	List<AdminCard> selectAdminToHostClaimCard();
	List<AdminCard> selectAdminReivewClaimCard();
	
	//main ������ ������ �Ű� ���� �޼ҵ�
	int insertClaimDetail(Claim claim);
	
	//mypage �Ű� ���� �޼ҵ�
	int insertClaimGuest(Claim claim);
	
	
	//�Խ��� Ŭ���� ���
	int insertClaimComment(Claim claim);
	Claim selectClaimReviewNo(int reviewNo);
}