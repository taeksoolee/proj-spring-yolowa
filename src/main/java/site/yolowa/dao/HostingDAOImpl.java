package site.yolowa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
import site.yolowa.mapper.AdminMapper;
import site.yolowa.mapper.MainHostingMapper;
import site.yolowa.mapper.MainHostingMypageMapper;


@Repository
public class HostingDAOImpl implements HostingDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<AdminHostingTable> selectAdminHostingTable(Map<String, Object> map) {
		return sqlSession.getMapper(AdminMapper.class).selectAdminHostingTable(map);
	}
	
	@Override
	public List<AdminChart> selectAdminHostingChart(Range range) {
		return sqlSession.getMapper(AdminMapper.class).selectAdminHostingChart(range);
	}
	
	@Override
	public List<AdminCard> selectAdminApplyHostingCard(){
		return sqlSession.getMapper(AdminMapper.class).selectAdminApplyHostingCard();
	}
	
	@Override
	public List<AdminCard> selectAdminStateHostingCard(){
		return sqlSession.getMapper(AdminMapper.class).selectAdminStateHostingCard();
	}
	
	@Override
	public AdminHostingReservationDetail selectAdminHostingDetail(int hostingNo){
		return sqlSession.getMapper(AdminMapper.class).selectAdminHostingDetail(hostingNo);
	}
	
	//main hosting DAO
	//index ������ �ֱ� ��� ���÷���
	@Override
	public List<MainHostingSearch> HostingSearchIndexLately() {
		return sqlSession.getMapper(MainHostingMapper.class).HostingSearchIndexLately();
	}
	
	//index ������ ���� ���� ȣ��Ʈ ���÷���
	@Override
	public List<MainHostingSearch> HostingSearchFamous() {
		return sqlSession.getMapper(MainHostingMapper.class).HostingSearchFamous();
	}
	
	//index ������ ���� ���� ȣ��Ʈ ���÷���
	@Override
	public List<MainHostingSearch> HostingSearchSuperhost() {
		return sqlSession.getMapper(MainHostingMapper.class).HostingSearchSuperhost();
	}
	
	//index ������ ��ϵ� ȣ���� ���� ī����
	@Override
	public MainHostingSearchCounting HostingSearchCounting() {
		return sqlSession.getMapper(MainHostingMapper.class).HostingSearchCounting();
	}
	
	//������ ������ ���
	@Override
	public MainHostingDetail selectMainHostingDetail(int hostingNo) {
		return sqlSession.getMapper(MainHostingMapper.class).selectMainHostingDetail(hostingNo);
	}
	
	//mypage hosting DAO
	@Override
	public int insertHosting(Hosting hosting) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).insertHosting(hosting);
	}
	
	//mypage header count
	@Override
	public MypageHeaderCount selectMypageHeaderCount(int loginMemberNo) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).selectMypageHeaderCount(loginMemberNo);
	}

	@Override
	public int selectMypageHostingCount(Map<String, Object> map) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).selectMypageHostingCount(map);
	}

	@Override
	public List<Hosting> selectMypageHostingList(Map<String, Object> map) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).selectMypageHostingList(map);
	}

	@Override
	public int updateMypageHosting(Hosting hosting) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).updateMypageHosting(hosting);
	}

	@Override
	public Hosting selectMypageHosting(Map<String, Object> map) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).selectMypageHosting(map);
	}

	@Override
	public int updateMypageHostingList(Hosting hosting) {
		return sqlSession.getMapper(MainHostingMypageMapper.class).updateMypageHostingList(hosting);
	}

	@Override
	public List<MainHostingSearch> selectMainHostingList(Map<String, Object> map) {
		return sqlSession.getMapper(MainHostingMapper.class).selectMainHostingList(map);
	}
}
