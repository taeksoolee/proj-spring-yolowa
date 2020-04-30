package site.yolowa.dao;

import java.util.List;
import java.util.Map;

import site.yolowa.dto.AdminCard;
import site.yolowa.dto.AdminChart;
import site.yolowa.dto.AdminHostingReservationDetail;
import site.yolowa.dto.AdminHostingTable;
import site.yolowa.dto.Hosting;
import site.yolowa.dto.MainHostingDetail;
import site.yolowa.dto.MainHostingSearch;
import site.yolowa.dto.MainHostingSearchCounting;
import site.yolowa.dto.MypageHeaderCount;
import site.yolowa.dto.Range;

public interface HostingDAO {
	List<AdminHostingTable> selectAdminHostingTable(Map<String, Object> map);
	List<AdminChart> selectAdminHostingChart(Range range);
	List<AdminCard> selectAdminApplyHostingCard();
	List<AdminCard> selectAdminStateHostingCard();
	AdminHostingReservationDetail selectAdminHostingDetail(int hostingNo);
	//main hosting DAO
	//index ������ �ֱ� ��� ���÷���
	List<MainHostingSearch> HostingSearchIndexLately();

	//index ������ ���� ���� ȣ��Ʈ ���÷���
	List<MainHostingSearch> HostingSearchFamous();

	//index ������ ���� ���� ȣ��Ʈ ���÷���
	List<MainHostingSearch> HostingSearchSuperhost();

	//index ������ ��ϵ� ȣ���� ���� ī����
	MainHostingSearchCounting HostingSearchCounting();

	//������ ������ ���
	MainHostingDetail selectMainHostingDetail(int hostingNo);

	//mypage hosting DAO
	int insertHosting(Hosting hosting);
	
	//mypage ��� ī����
	MypageHeaderCount selectMypageHeaderCount(int loginMemberNo);
	
	//mypage ȣ���� ���� ���÷���
	int selectMypageHostingCount(Map<String, Object>map);
	List<Hosting> selectMypageHostingList(Map<String, Object>map);
	
	//mypage ȣ���� ���� ���� ����
	int updateMypageHosting(Hosting hosting);
	Hosting selectMypageHosting(Map<String, Object>map);
	int updateMypageHostingList(Hosting hosting);
	
	//main ������ �˻�
	List<MainHostingSearch> selectMainHostingList(Map<String, Object> map);
}