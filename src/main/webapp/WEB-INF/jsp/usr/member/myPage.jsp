<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER MYPAGE" />
<%@ include file="../common/head.jspf"%>
<%@ page import="com.example.demo.util.Ut"%>
<hr />

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table border="1">
				<colgroup>
					<col width="200" />
				</colgroup>

				<tbody>
					<tr>
						<th>가입일</th>
						<td>${rq.loginedMember.regDate }</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>${rq.loginedMember.email }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td>${rq.loginedMember.name }</td>
					</tr>
					<tr>
						<th><button class="btn-text-link btn btn-active btn-ghost"
								type="button" onclick="history.back();">뒤로가기</button></th>
						<td><a href="../member/checkPw"
							class="btn btn-active btn-ghost">회원정보 수정</a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>