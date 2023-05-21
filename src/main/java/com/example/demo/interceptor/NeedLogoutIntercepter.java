package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.util.Ut;
import com.example.demo.vo.Rq;

@Component
public class NeedLogoutIntercepter implements HandlerInterceptor {

	@Autowired
	private Rq rq;
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		resp.setContentType("text/html; charset=UTF-8");
		
		if (rq.isLogined()) {
			rq.printJs(Ut.jsHistoryBack("F-B", "로그아웃을 먼저 해주세요."));
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
}