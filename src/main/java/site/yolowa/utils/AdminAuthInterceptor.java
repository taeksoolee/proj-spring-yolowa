package site.yolowa.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import site.yolowa.dto.Member;
import site.yolowa.exception.MemberAdminNotFoundException;
import site.yolowa.exception.MemberInfoNotFoundException;

//������ ���� ���� ó���� ���� ���ͼ��� Ŭ����
//=> �����ڰ� �ƴ� ������� ��� ���� �������� �̵�
public class AdminAuthInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session=request.getSession();
		String destURI = "";
		if(request.getQueryString() == null) {
			destURI = request.getServletPath();
		}else {
			destURI = request.getServletPath()+"?"+request.getQueryString();
		}
		System.out.println("abc"+destURI);
		session.setAttribute("destURI", destURI);
		
		Member loginMember=(Member)session.getAttribute("loginMember");
		if(loginMember==null) {
			throw new MemberInfoNotFoundException("�α��� ������ �����ϴ�.");
		}
		if(loginMember.getMemberState()!=9) {
			throw new MemberAdminNotFoundException(loginMember.getMemberState() ,"������ ���̵�� �α��� ���ּ���.");
		}
		
		
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}
	
	

}
