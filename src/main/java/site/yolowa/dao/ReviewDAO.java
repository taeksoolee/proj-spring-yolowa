package site.yolowa.dao;

import java.util.List;
import java.util.Map;

import site.yolowa.dto.BoardCommentReview;
import site.yolowa.dto.MainReview;
import site.yolowa.dto.MainReviewStarCount;
import site.yolowa.dto.Member;
import site.yolowa.dto.Review;

public interface ReviewDAO {
	int updateReviewStateStop(Review review);
	int updateReviewStatePost(Review review);
	List<Review> selectReview();
	Review selectReviewNo(int reviewNo);
	
	int insertReviewDtail(Review review);
	List<MainReview> selectReviewDetailList(Map<String, Object> map);
	MainReviewStarCount selectReviewStarCount(int hostingNo);
	
	//���������� ȣ��Ʈ -> �Խ�Ʈ ���� �ۼ�
	int insertReviewGuest(Review review);
	int insertReviewReGuest(Review review);
	
	//���������� �Խ�Ʈ���� ���
	List<MainReview> selectReviewGuestList(Map<String, Object> map);
	int selectReviewGuestCount(int reviewMemberNo);
	
	//���������� �Խ�Ʈ ���� ���
	Member selectMypageMember(int memberNo);
	
	//�Խ��� ���� �ۼ� �޼ҵ�
	int insertCommentReview(Review review);
	List<BoardCommentReview> selectDetailCommentReview(int boardNo);
}