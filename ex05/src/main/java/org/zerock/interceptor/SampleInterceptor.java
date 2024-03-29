package org.zerock.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SampleInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println("pre handle.........................."); // doA 객체.. 실행되면

		HandlerMethod method = (HandlerMethod) handler;
		Method methodObj = method.getMethod();

		System.out.println("Bean: " + method.getBean());
		System.out.println("Method: " + methodObj);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception { // ModelAndView 모델과 화면을 같이

		System.out.println("post handle........................");

		Object result = modelAndView.getModel().get("result"); // DOB RESULT

		if (result != null) {
			System.out.println("result exists");
			request.getSession().setAttribute("result", result);
			response.sendRedirect("/doA");
		}
	}
}

// @Override
// public void postHandle(HttpServletRequest request, HttpServletResponse
// response, Object handler,
// ModelAndView modelAndView) throws Exception {
//
// System.out.println("post handle.........");
//
// }

// @Override
// public boolean preHandle(HttpServletRequest request,
// HttpServletResponse response, Object handler) throws Exception {
//
// System.out.println("pre handle..........");
//
// return true;
// }