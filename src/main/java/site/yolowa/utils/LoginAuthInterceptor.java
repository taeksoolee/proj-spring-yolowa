package site.yolowa.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//�α��� ���� ���� ó���� ���� ���ͼ��� Ŭ����
//=> �α��� ����ڰ� �ƴ� ��� �α����� �ϱ����� ���� ������(localhost:8000/yolowa)�� �̵�
//=> �α��� ���� �� ���� ��û �������� ����ǵ��� ���� 
public class LoginAuthInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session=request.getSession();
		
		if(session.getAttribute("loginMember")==null) {
			String url=request.getRequestURI().substring(request.getContextPath().length());
			String query=request.getQueryString();
			if(query==null) {
				query="";
			} else {
				query="?"+query;
			}
			
			if(request.getMethod().equals("GET")) {
				session.setAttribute("destURI", url+query);
			}
			
			response.sendRedirect(request.getContextPath()+"/");
			return false;
			
		}
		return true;
	}
}
