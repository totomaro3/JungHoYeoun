<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER CHECKPW" />
<%@ include file="../common/head.jspf"%>
<hr />

<script>
	let submitJoinFormDone = false;
	function submitJoinForm(form) {
		form.loginPw.value = form.loginPw.value.trim();
		if (form.loginPw.value == 0) {
			alert('비밀번호를 입력해주세요');
			return;
		}
		submitJoinFormDone = true;
		form.submit();
	}
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<form action="../member/doCheckPw" name="form1" method="POST" onsubmit="submitJoinForm(this); return false;">
				<input value="${rq.loginedMember.email }" type="hidden" name="email" />
				<table border="1">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>아이디</th>
							<td>${rq.loginedMember.email }</td>
						</tr>
						<tr>
							<th>비밀번호</th>
							<td>
								<input class="input input-bordered w-full max-w-xs" autocomplete="off" type="text"
									placeholder="비밀번호를 입력해주세요" name="loginPw" />
							</td>
						</tr>
						<tr>
							<th><button class="btn-text-link btn btn-active btn-ghost" type="button" onclick="history.back();">뒤로가기</button></th>
							<td>
								<button class="btn-text-link btn btn-active btn-ghost" type="submit">확인</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>