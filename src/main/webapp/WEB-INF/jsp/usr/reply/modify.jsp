<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="REPLY MODIFY" />
<%@ include file="../common/head.jspf"%>

<script type="text/javascript">
	let ReplyModify__submitFormDone = false;
	function ReplyModify__submitFormDone(form) {
		if (ReplyModify__submitFormDone) {
			return;
		}
		form.body.value = form.body.value.trim();
		if (form.body.value.length == 0) {
			alert('내용을 입력해주세요.');
			form.body.focus();
			return;
		}
		ReplyWrite__submitFormDone = true;
		form.submit();
	}
</script>

<section class="text-xl">
	<div class="container mx-auto px-3">
<form method="post" action="doModify">
	<input value="${reply.id }" type="hidden" name="id" />
	<input value="${reply.relId }" type="hidden" name="relId" />
	<table>
		<tr>
			<th>번호</th>
			<td>${reply.id }</td>
		</tr>
		<tr>
			<th>작성날짜</th>
			<td>${reply.regDate }</td>
		</tr>
		<tr>
			<th>수정날짜</th>
			<td>${reply.updateDate }</td>
		</tr>
		<tr>
			<th>작성자</th>
			<td>${reply.extra__writer }</td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea class="input input-bordered w-full max-w-xs" name="body" placeholder="내용을 입력해주세요" />${reply.body }</textarea></td>
		</tr>
		<tr>
			<th><button class="btn-text-link btn btn-active btn-ghost" type="button" onclick="history.back();">뒤로가기</button></th>
			<td><button class="btn-text-link btn btn-active btn-ghost" type="submit">수정</button></td>
		</tr>
	</table>
</form>
</div>
</section>

<%@ include file="../common/foot.jspf"%>
