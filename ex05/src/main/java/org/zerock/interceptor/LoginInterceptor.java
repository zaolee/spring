package org.zerock.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

  private static final String LOGIN = "login";
  private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

  @Override // 컨트롤러에서 post방식으로 온거 여기서부터 실행됨 (3)
  public void postHandle(HttpServletRequest request, 
      HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

    HttpSession session = request.getSession();

    ModelMap modelMap = modelAndView.getModelMap();
    Object userVO = modelMap.get("userVO");

    if (userVO != null) {

      logger.info("new login success");
      session.setAttribute(LOGIN, userVO);

      if (request.getParameter("useCookie") != null) { // 여기서 유즈쿠키(리맴버미) 체크하면 널이 아니겠징,,

        logger.info("remember me................");
        Cookie loginCookie = new Cookie("loginCookie", session.getId()); // 쿠키생성됨 이름 = 로그인쿠키, session.getId() = jsession
        loginCookie.setPath("/"); // 모든 루트에 지정할게~
        loginCookie.setMaxAge(60 * 60 * 24 * 7); // 초 분 시 일 시간 설정하는거 (그럼 브라우저를 죽여도 로그인이 되겠징..)
        response.addCookie(loginCookie); // 없쪙 -> 근데 리맴버미 체크하면 있쪙
      }
      // response.sendRedirect("/");
      Object dest = session.getAttribute("dest"); // null

      //response.sendRedirect(dest != null ? (String) dest : "/");
      response.sendRedirect(dest != null ? (String) dest : "/sboard/list"); // 최종으로 맞으면 lsit로 ㄱㄱ
    } // 여기서 의문 dest가 널인 경우는 언제인거?
  }

  // @Override
  // public void postHandle(HttpServletRequest request,
  // HttpServletResponse response, Object handler,
  // ModelAndView modelAndView) throws Exception {
  //
  // HttpSession session = request.getSession();
  //
  // ModelMap modelMap = modelAndView.getModelMap();
  // Object userVO = modelMap.get("userVO");
  //
  // if(userVO != null){
  //
  // logger.info("new login success");
  // session.setAttribute(LOGIN, userVO);
  // //response.sendRedirect("/");
  //
  // Object dest = session.getAttribute("dest");
  //
  // response.sendRedirect(dest != null ? (String)dest:"/");
  // }
  // }

  @Override // sign in 하고 들어옴 이거 실행됨 -> 끝나면 다시 컨트롤러로 감(1)
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    HttpSession session = request.getSession();

    if (session.getAttribute(LOGIN) != null) {
      logger.info("clear login data before");
      session.removeAttribute(LOGIN);
    }

    return true;
  }
}
