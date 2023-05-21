package com.example.demo.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.MemberService;
import com.example.demo.util.Ut;
import com.example.demo.vo.Member;
import com.example.demo.vo.ResultData;
import com.example.demo.vo.Rq;

@Controller
public class UsrMemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private Rq rq;

	@RequestMapping("/usr/member/join")
	public String join() {
		return "usr/member/join";
	}
	
	@RequestMapping("/usr/member/getEmailDup")
	@ResponseBody
	public ResultData<String> getEmailDup(String email) {
		
		if(Ut.empty(email)) {
			return ResultData.from("F-1", "이메일을 입력해주세요.","email",email);
		}
		
	    String emailRegex = "\\S+@\\S+\\.\\S+"; // 이메일 유효성 검사용 정규표현식
	    
	    Pattern pattern = Pattern.compile(emailRegex);
	    Matcher matcher = pattern.matcher(email);

	    if (!matcher.matches()) {
	    	return ResultData.from("F-2", "올바른 이메일 형식이 아닙니다.");
	    }
		
		ResultData<Boolean> isDupEmailRd = memberService.isDupEmail(email);
		
		if(isDupEmailRd.getData1()) {
			return ResultData.from("F-2", isDupEmailRd.getMsg(),"email",email);
		}
		
		return ResultData.from("S-1", isDupEmailRd.getMsg(),"email",email);
	}
	
	@RequestMapping("/usr/member/getLoginPwConfirm")
	@ResponseBody
	public ResultData<String> getLoginPwConfirm(String loginPw, String loginPwConfirm) {
		
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-1", "비밀번호를 입력해주세요");
		}
		
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-2", "비밀번호확인을 입력해주세요");
		}
		
		if(!loginPw.equals(loginPwConfirm)) {
			return ResultData.from("F-3", "비밀번호가 일치하지 않습니다.");
		}
		
		return ResultData.from("S-1", "비밀번호가 일치합니다.");
	}
	
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
			String email, @RequestParam(defaultValue = "/") String afterLoginUri) {

		if (Ut.empty(email)) {
			return rq.jsHistoryBack("F-6", "이메일을 입력해주세요");
		}
		
		if (Ut.empty(loginPw)) {
			return rq.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
		}
		if (Ut.empty(name)) {
			return rq.jsHistoryBack("F-3", "이름을 입력해주세요");
		}

		loginPw = Ut.sha256(loginPw);
		
		ResultData<Integer> joinRd = memberService.join(email, loginPw, name);

		if (joinRd.isFail()) {
			return rq.jsHistoryBack(joinRd.getResultCode(), joinRd.getMsg());
		}

		ResultData<Member> getMemberByIdRd = memberService.getMemberById(joinRd.getData1());

		String afterJoinUri = "../member/login?afterLoginUri=" + Ut.getEncodedUri(afterLoginUri);

		return Ut.jsReplace("S-1", Ut.f("회원가입이 완료되었습니다"), afterJoinUri);
	}

	@RequestMapping("/usr/member/login")
	public String login(String email, String loginPw, String replaceUri) {
		
		return "usr/member/login";
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String email, String loginPw, @RequestParam(defaultValue = "/") String afterLoginUri) {

		if (Ut.empty(email)) {
			return Ut.jsHistoryBack("F-1", "아이디를 입력해주세요");
		}
		if (Ut.empty(loginPw)) {
			return Ut.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
		}
		
		loginPw = Ut.sha256(loginPw);
		
		ResultData<Member> loginRd = memberService.login(email, loginPw);

		if (loginRd.getData1() == null) {
			return Ut.jsHistoryBack("F-3", Ut.f("존재하지 않는 이메일입니다.", email));
		}
		
		if (!loginRd.getData1().getLoginPw().equals(loginPw)) {
			return Ut.jsHistoryBack("F-4", Ut.f("비밀번호가 일치하지 않습니다."));
		}
		
		rq.login(loginRd.getData1());
		
		return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다.", loginRd.getData1().getName()), afterLoginUri);
	}

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(String afterLogoutUri) {
		
		rq.logout();
		
		return Ut.jsReplace("S-1", Ut.f("로그아웃 되었습니다."), afterLogoutUri);
	}
	
	@RequestMapping("/usr/member/myPage")
	public String showMyPage() {
		
		return "usr/member/myPage";
	}
	
	@RequestMapping("/usr/member/checkPw")
	public String showCheckPw() {
		
		return "usr/member/checkPw";
	}
	
	@RequestMapping("/usr/member/doCheckPw")
	@ResponseBody
	public String doCheckPw(String email, String loginPw) {
		
		if (Ut.empty(loginPw)) {
			return rq.jsHistoryBack("F-1","비밀번호를 입력해주세요");
		}
		
		loginPw = Ut.sha256(loginPw);

		ResultData<Member> loginRd = memberService.login(email, loginPw);

		if (!rq.getLoginedMember().getLoginPw().equals(loginPw)) {
			return rq.jsHistoryBack("F-1","비밀번호가 일치하지 않습니다.");
		}
		
		return rq.jsReplace("", "modify");
	}
	
	@RequestMapping("/usr/member/modify")
	public String modify() {
		return "/usr/member/modify";
	}

	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(int id, String email, String loginPw, String name) {
		
		if (Ut.empty(loginPw)) {
			
		} else {
			loginPw = Ut.sha256(loginPw);
		}
		
		ResultData<String> doModifyMemberRd = memberService.doModifyMember(id, loginPw, name);
		
		ResultData<Member> loginRd = memberService.login(email, loginPw);
		
		rq.login(loginRd.getData1());

		return rq.jsReplace(doModifyMemberRd.getResultCode(), doModifyMemberRd.getMsg(), "../member/myPage");
	}
}

//http://localhost:8081/usr/member/doJoin?loginId=1&loginPw=1&name=abc&nickname=toto&cellphoneNum=1&email=abc@gmail.com
//ResultData -> 표준 보고서 양식
//성공, 실패 쉽게 판단이 가능하도록 / 관련 데이터를 같이 주고 받을 수 있도록