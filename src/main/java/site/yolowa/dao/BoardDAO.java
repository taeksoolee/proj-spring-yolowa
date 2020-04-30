package site.yolowa.dao;

import java.util.List;
import java.util.Map;

import site.yolowa.dto.Board;
import site.yolowa.dto.BoardComment;

public interface BoardDAO{
	List<Board> selectAllBoard (Map<String, Object> map);
	
	//�Խ���(��������)
	List<BoardComment> selectCommentList(Map<String, Object>map);
	List<BoardComment> selectSearchCommentList(Map<String, Object>map);
	List<BoardComment> selectCategoryCommentList(Map<String, Object>map);
	int selectCommentCount(Map<String, Object> map);
	List<Board> selectComment();
	int selectCommentCategoryCount(Map<String, Object> map);
	BoardComment selectDetailComment(int boardNo);
	List<Board> selectCommentOrderDate4();
	
	//�Խ���
	int insertAdminBoardNotice(Board board);
	List<Board> selectAdminEtcNotice(Map<String, Object> map);
	//int updateAdminBoardNoticeState(Board board);
	int updateAdminBoardNoticeContent(Board board);
	
	Board selectBoardCategoryTitleEqual(Board board);
	List<Board> selectBoardCategoryTitleLike(Board board);
	
	//����
	int insertAdminBoardHelp(Board board);
	List<Board> selectAdminEtcHelp(Map<String, Object> map);
	//int updateAdminBoardHelpState(Board board);
	int updateAdminBoardHelpContent(Board board);
	
	Board selectBoardHelpCategoryTitleEqual(Board board);
	List<Board> selectBoardHelpCategoryTitleLike(Board board);
	
	//���
	int insertAdminBoardTerms(Board board);
	int updateAdminBoardTermsContent(Board board);
	Board selectBoardTermsCategoryEqual(Board board);
	
	//�Խ���, ���� ����
	Board selectBoardNo(int boardNo);
	int updateAdminBoardState(Board board);
	
	//���� ���� ������
	List<Board> selectHelpBoardList();
	List<Board> selectHelpBoard(Board board);
	int updateHelpBoardView(Board board);
	List<Board> selectHelpBoardViewList();
	
	//���� ������ ���
	Board selectMainServiceTerms(Board board);
	
}
