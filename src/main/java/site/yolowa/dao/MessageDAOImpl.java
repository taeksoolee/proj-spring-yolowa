package site.yolowa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import site.yolowa.dto.MainMessage;
import site.yolowa.dto.MainMessageCount;
import site.yolowa.dto.Message;
import site.yolowa.mapper.AdminMapper;
import site.yolowa.mapper.MainMessageMapper;

@Repository
public class MessageDAOImpl implements MessageDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<Message> selectAdminMessageTable(Map<String, Object> map) {
		return sqlSession.getMapper(AdminMapper.class).selectAdminMessageTable(map);
	}
	
	@Override
	public int insertAdminMessage(Message message) {
		return sqlSession.getMapper(AdminMapper.class).insertAdminMessage(message);
	}
	
	@Override
	public int updateMessageState(int messageNo) {
		return sqlSession.getMapper(AdminMapper.class).updateAdminMessageState(messageNo);
	}
	
	//���� ������ ��� ����(�ý���) �޼��� ���
	@Override
	public List<MainMessage> selectMessageNoticeList(int memberNo) {
		return sqlSession.getMapper(MainMessageMapper.class).selectMessageNoticeList(memberNo);
	}
	
	//���� ������ ��� �޼��� ���
	@Override
	public List<MainMessage> selectMessageHeaderList(int memberNo) {
		return sqlSession.getMapper(MainMessageMapper.class).selectMessageHeaderList(memberNo);
	}
	
	//���� ������ ���� ������ �޼���, �˸� ī��Ʈ
	@Override
	public MainMessageCount countingMessageHeader(int memberNo) {
		return sqlSession.getMapper(MainMessageMapper.class).countingMessageHeader(memberNo);
	}
	
	//���� �޼��� ����
	@Override
	public int insertMessage(Message message) {
		return sqlSession.getMapper(MainMessageMapper.class).insertMessage(message);
	}

	@Override
	public List<MainMessage> selectMessageMypageList(Map<String, Object> map) {
		return sqlSession.getMapper(MainMessageMapper.class).selectMessageMypageList(map);
	}

	@Override
	public int selectMessageMypageCount(Map<String, Object> map) {
		return sqlSession.getMapper(MainMessageMapper.class).selectMessageMypageCount(map);
	}

	@Override
	public int updateMessageList(int memberNo) {
		return sqlSession.getMapper(MainMessageMapper.class).updateMessageList(memberNo);
	}

	@Override
	public int updateNoticeList(int memberNo) {
		return sqlSession.getMapper(MainMessageMapper.class).updateNoticeList(memberNo);
	}
}
