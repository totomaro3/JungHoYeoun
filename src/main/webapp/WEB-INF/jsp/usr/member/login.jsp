<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER LOGIN" />
<%@ include file="../common/head.jspf"%>
<hr />

<section class="mt-5 text-xl">
	<div class="container mx-auto px-3">
<form method="post" action="doLogin">
	<input value="${param.afterLoginUri }" type="hidden" name="afterLoginUri" />
	<table>
		<tr>
			<th>아이디</th>
			<td><input class="input input-bordered w-full max-w-xs" type="text" name="email" placeholder="아이디를 입력해주세요" /></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input class="password-input input input-bordered w-full max-w-xs" type="password" name="loginPw" placeholder="비밀번호를 입력해주세요" /></td>
		</tr>
		<tr>
			<th><button class="button btn btn-active btn-ghost text-xl" type="button" onclick="history.back();">뒤로가기</button></th>
			<td><button class="btn btn-active btn-ghost text-xl" type="submit">로그인</button></td>
		</tr>
	</table>
</form>
</div>
</section>

<%@ include file="../common/foot.jspf"%>

<style>
.password-input {
  font-family: monospace; /* 모노스페이스 폰트 사용 */
  text-security: disc; /* 별표(*) 대신 디스크 모양으로 표시 */
}
</style>