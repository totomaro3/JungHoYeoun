<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER JOIN" />
<%@ include file="../common/head.jspf"%>

<script>
	let submitJoinFormDone = false;
	function submitJoinForm(form) {
		if (submitJoinFormDone) {
			alert('처리중입니다');
			return;
		}
		form.email.value = form.email.value.trim();
		if (form.email.value == 0) {
			alert('이메일을 입력해주세요');
			return;
		}
		if (form.email.value != validEmail) {
			alert('사용할 수 없는 이메일입니다');
			form.email.focus();
			return;
		}
		form.loginPw.value = form.loginPw.value.trim();
		if (form.loginPw.value == 0) {
			alert('비밀번호를 입력해주세요');
			return;
		}
		form.loginPwConfirm.value = form.loginPwConfirm.value.trim();
		if (form.loginPwConfirm.value == 0) {
			alert('비밀번호 확인을 입력해주세요');
			return;
		}
		if (form.loginPwConfirm.value != form.loginPw.value) {
			alert('비밀번호가 일치하지 않습니다');
			form.loginPw.focus();
			return;
		}
		form.name.value = form.name.value.trim();
		if (form.name.value == 0) {
			alert('이름을 입력해주세요');
			return;
		}
		submitJoinFormDone = true;
		form.submit();
	}
</script>

<script>
	function checkEmailDup(el) {
		const form = $(el).closest('form').get(0);

		if (form.email.value.length == 0) {
			validEmail = '';
		}

		const emailRegex = /\S+@\S+\.\S+/; // 이메일 유효성 검사용 정규표현식
		if (emailRegex.test(form.email.value)) {
			validEmail = '';
		}

		var email = form.email.value;
		var action = './getEmailDup';

		$.get(action, {
			isAjax : 'Y',
			email : email,
		}, function(data) {
			$('.checkDup-msg').text(data.msg);
			if (data.success) {
				$('.checkDup-msg').removeClass('text-red-500');
				validEmail = data.data1;
			} else {
				$('.checkDup-msg').addClass('text-red-500');
				validEmail = '';
			}

		}, 'json');
	}

	function checkLoginPwConfirm(el) {
		const form = $(el).closest('form').get(0);

		var loginPw = form.loginPw.value;
		var loginPwConfirm = form.loginPwConfirm.value;
		var action = './getLoginPwConfirm';

		$.get(action, {
			isAjax : 'Y',
			loginPw : loginPw,
			loginPwConfirm : loginPwConfirm,
		}, function(data) {
			$('.checkConfirm-msg').text(data.msg);
			if (data.success) {
				$('.checkConfirm-msg').removeClass('text-red-500')
			} else {
				$('.checkConfirm-msg').addClass('text-red-500');
			}

		}, 'json');
	}
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form class="table-box-type-1" name="form1" method="POST"
			action="../member/doJoin"
			onsubmit="submitJoinForm(this); return false;">
			<input type="hidden" name="afterLoginUri"
				value="${param.afterLoginUri}" />
			<table class="table table-zebra w-full">
				<colgroup>
					<col width="200" />
				</colgroup>

				<tbody>
					<tr>
						<th>이메일</th>
						<td><input onblur="checkEmailDup(this);" name="email"
							class="w-full input input-bordered  max-w-xs"
							placeholder="이메일을 입력해주세요" />
							<div class="checkDup-msg mt-2">&nbsp</div></td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td><input onblur="checkLoginPwConfirm(this);" name="loginPw"
							class="w-full password-input input input-bordered  max-w-xs"
							type="password" placeholder="비밀번호 입력" /></td>
					</tr>
					<tr>
						<th>비밀번호 확인</th>
						<td><input onblur="checkLoginPwConfirm(this);"
							name="loginPwConfirm"
							class="w-full password-input input input-bordered  max-w-xs"
							type="password" placeholder="비밀번호 확인" />
							<div class="checkConfirm-msg mt-2">&nbsp</div></td>
					</tr>
					<tr>
						<th>이름</th>
						<td><input name="name"
							class="w-full input input-bordered  max-w-xs"
							placeholder="이름을 입력해주세요" /></td>
					</tr>
					<tr>
						<th><button class="btn-text-link btn btn-active btn-ghost"
								type="button" onclick="history.back();">뒤로가기</button></th>
						<td>
							<button class="btn btn-active btn-ghost" type="submit"
								value="회원가입">회원가입</button>
						</td>
					</tr>
				</tbody>
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