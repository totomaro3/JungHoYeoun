package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.repository.MemberRepository;
import com.example.demo.util.Ut;
import com.example.demo.vo.Member;
import com.example.demo.vo.ResultData;

@Service
public class MemberService {
	
	@Value("${custom.siteMainUri}")
	private String siteMainUri;
	@Value("${custom.siteName}")
	private String siteName;
	
	@Autowired
	private MemberRepository memberRepository;
	
	public ResultData<Integer> join(String email, String loginPw, String name) {
		
		if(memberRepository.isDupEmail(email)) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", email));
		}
		
		memberRepository.doJoinMember(email, loginPw, name);
		
		int id =  memberRepository.getLastInsertId();
		
		return ResultData.from("S-1", name+"회원이 가입되었습니다.","id", id);
		
	}

	public ResultData<Member> getMemberById(int id) {
		
		Member member = memberRepository.getMemberById(id);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","member", member);
	}

	public ResultData<Member> login(String email, String loginPw) {
		
		Member member = memberRepository.getMemberByLoginId(email);
		
		return ResultData.from("S-1", Ut.f("%s님 환영합니다.", member.getName()),"member", member);
	}

	public ResultData<String> doModifyMember(int id, String loginPw, String name) {
		
		memberRepository.doModifyMember(id, loginPw, name);
		
		return ResultData.from("S-1", "회원이 수정되었습니다.","name", name);
	}

	public ResultData<Integer> doDeleteMember(int id) {
		memberRepository.doDeleteMember(id);
		
		return ResultData.from("S-1", "회원이 삭제되었습니다.","id", id);
	}

	public ResultData<Boolean> isDupEmail(String loginId) {
		
		if(memberRepository.isDupEmail(loginId)) {
			return ResultData.from("F-2", "중복된 이메일 입니다.","isDupEmail", true);
		}
		else {
			return ResultData.from("S-1", "사용 가능한 이메일 입니다.","isDupEmail", false);
		}
	}
	
	public ResultData<Member> getMemberByLoginId(String loginId) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","Member", member);
	}
}
