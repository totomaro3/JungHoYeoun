package com.example.demo.vo;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.example.demo.util.Ut;

import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	@Getter
	private boolean isLogined;
	@Getter
	private int loginedMemberId;
	@Getter
	private String loginedMemberNickname;
	@Getter
	private Member loginedMember;
	
	HttpServletRequest req;
	HttpServletResponse resp;
	HttpSession session;
	Map<String, String> paramMap;
	
	public Rq(HttpServletRequest req,HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		this.session = req.getSession();
		this.paramMap = Ut.getParamMap(req);
		
		boolean isLogined = false;
		int loginedMemberId = 0;
		String loginedMemberNickname = "";
		Member loginedMember = null;
		

		if (session.getAttribute("loginedMember") != null) {
			isLogined = true;
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			loginedMember = (Member) session.getAttribute("loginedMember");
		}

		this.isLogined = isLogined;
		this.loginedMemberId = loginedMemberId;
		this.loginedMemberNickname = loginedMemberNickname;
		this.loginedMember = loginedMember;
		
		this.req.setAttribute("rq", this);

	}
	
	public String jsHistoryBack(String resultCode, String msg) {
		return Ut.jsHistoryBack(resultCode, msg);
	}

	public String jsReplace(String msg, String uri) {
		return Ut.jsReplace(msg, uri);
	}
	
	public String jsReplace(String resultCode ,String msg, String uri) {
		return Ut.jsReplace(resultCode, msg, uri);
	}

	public void printJs(String str) throws IOException {
		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().append(str);
	}
	
	public String jsHitoryBackOnView(String msg) {
		req.setAttribute("msg", msg);
		req.setAttribute("historyBack", true);
		return "usr/common/js";
	}
	
	public String getCurrentUri() {
		String currentUri = req.getRequestURI();
		String queryString = req.getQueryString();
		
		currentUri += "?" + queryString;
		
		return currentUri;
	}
	
	public String getEncodedCurrentUri() {
		return Ut.getEncodedCurrentUri(getCurrentUri());
	}
	
	
	public void login(Member member) {
		session.setAttribute("loginedMemberId", member.getId());
		session.setAttribute("loginedMember", member);
	}
	
	public void logout() {
		session.removeAttribute("loginedMemberId");
		session.removeAttribute("loginedMember");
	}
	
	public String getLoginUri() {
		return "../member/login?afterLoginUri=" + getAfterLoginUri();
	}

	private String getAfterLoginUri() {
//		로그인 후 접근 불가 페이지

		String requestUri = req.getRequestURI();

		switch (requestUri) {
		case "/usr/member/login":
		case "/usr/member/join":
			return Ut.getEncodedUri(Ut.getAttr(paramMap, "afterLoginUri", ""));

		}

		return getEncodedCurrentUri();
	}
	
	public String getLogoutUri() {
		String requestUri = req.getRequestURI();

		switch (requestUri) {
		case "/usr/article/write":
			return "../member/doLogout?afterLogoutUri=" + "/";
		}

		return "../member/doLogout?afterLogoutUri=" + getAfterLogoutUri();
	}

	public String getAfterLogoutUri() {
		return getEncodedCurrentUri();
	}
	
	public String getFindLoginIdUri() {
		return "../member/findLoginId?afterFindLoginIdUri=" + getAfterFindLoginIdUri();
	}

	private String getAfterFindLoginIdUri() {
		return getEncodedCurrentUri();
	}

	public String getFindLoginPwUri() {
		return "../member/findLoginPw?afterFindLoginPwUri=" + getAfterFindLoginPwUri();
	}

	private String getAfterFindLoginPwUri() {
		return getEncodedCurrentUri();
	}
	
	// 삭제 금지
	public void initOnBeforeActionInterceptor() {

	}

}