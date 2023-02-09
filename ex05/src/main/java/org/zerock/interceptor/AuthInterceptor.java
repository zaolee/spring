package org.zerock.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.zerock.domain.UserVO;
import org.zerock.service.UserService;

public class AuthInterceptor extends HandlerInterceptorAdapter { 

	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	@Inject
	private UserService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception { // 로그인인터셉터에서 옴(4) sboard 가 여기 인터셉터로 걸림
		// 이거 끝나고 서치보드 컨드롤러 실행됨(아직 sboard/list 가려면 멀었음)
		// 여기서 서치보드컨트롤러는 listpaging 보여주는거임

		HttpSession session = request.getSession();

		if (session.getAttribute("login") == null) { // else는 걍 통과함

			logger.info("current user is not logined");

			saveDest(request); // 원래 가려고 했던 url 저장함 (session.setAttribute("dest","/sboard/list");)

			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie"); // 21DD407441E6542618221BAA9CBF9B56
			if (loginCookie != null) {

				UserVO userVO = service.checkLoginBefore(loginCookie.getValue()); // 데이타에서 가져옴, UserServiceImpl

				logger.info("USERVO: " + userVO);

				if (userVO != null) {
					session.setAttribute("login", userVO);
					return true;
				}

			}

			response.sendRedirect("/user/login");
			return false;
		}
		return true;
	}

	private void saveDest(HttpServletRequest req) {

		String uri = req.getRequestURI(); // sboard/list?

		String query = req.getQueryString(); // page=1

		if (query == null || query.equals("null")) {
			query = "";
		} else {
			query = "?" + query;
		}

		if (req.getMethod().equals("GET")) {
			logger.info("dest: " + (uri + query));
			req.getSession().setAttribute("dest", uri + query); // board/list?page=1 dest라는걸 정의
			// "dest" : /sboard/register
		}
	}

	// @Override
	// public boolean preHandle(HttpServletRequest request, HttpServletResponse
	// response, Object handler) throws Exception {
	//
	// HttpSession session = request.getSession();
	//
	// if (session.getAttribute("login") == null) {
	//
	// logger.info("current user is not logined");
	//
	// saveDest(request);
	//
	// response.sendRedirect("/user/login");
	// return false;
	// }
	// return true;
	// }
}

// if(session.getAttribute("login") == null){
//
// logger.info("current user is not logined");
// response.sendRedirect("/user/login");
// return false;
// }
