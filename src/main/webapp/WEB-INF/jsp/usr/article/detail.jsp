<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE DETAIL" />
<%@ include file="../common/head.jspf"%>

<!-- <iframe src="http://localhost:8081/usr/article/doIncreaseHitCountRd?id=2" frameborder="0"></iframe> -->
<script>
	const params = {}
	params.id = parseInt('${param.id}');
</script>

<script>
	function ArticleDetail__increaseHitCount() {
		const localStorageKey = 'article__' + params.id + '__alreadyView';
		if (localStorage.getItem(localStorageKey)) {
			return;
		}
		localStorage.setItem(localStorageKey, true);
		$.get('../article/doIncreaseHitCountRd', {
			id : params.id,
			ajaxMode : 'Y'
		}, function(data) {
			$('.article-detail__hit-count').empty().html(data.data1);
		}, 'json');
	}

	function ArticleDetail__increaseReaction() {
		$.get('../reaction/doIncreaseGoodReaction', {
			id : params.id,
			ajaxMode : 'Y'
		}, function(data) {
			$('.article-detail__reaction-point').empty().html(data.data1);
		}, 'json');
	}

	$(function() {
		// 실전코드
		// 		ArticleDetail__increaseHitCount();
		// 연습코드
		setTimeout(ArticleDetail__increaseHitCount, 2000);
	})

	$(function() {
		$('#increment-button').click(function() {
			$.ajax({
				url : '/../reaction/doIncreaseGoodReactionRd', // 함수를 실행할 엔드포인트 URL
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					$('.article-detail__reaction-point').text(data.data1); // 응답 데이터의 result 속성을 페이지에 적용
				},
				error : function() {
					alert('서버와 통신하는 중에 오류가 발생하였습니다.');
				}
			});
		});
	});
</script>

<script type="text/javascript">
	let ReplyWrite__submitFormDone = false;
	function ReplyWrite__submitForm(form) {
		if (ReplyWrite__submitFormDone) {
			return;
		}
		form.body.value = form.body.value.trim();
		if (form.body.value.length < 3) {
			alert('3글자 이상 입력하세요');
			form.body.focus();
			return;
		}
		ReplyWrite__submitFormDone = true;
		form.submit();
	}
</script>

<section class="mt-3 text-xl">
	<div class="container mx-auto px-3">
		<div class="button">
			<button class="btn btn-active btn-ghost text-xl" type="button"
				onclick="history.back();">뒤로가기</button>
			<c:if test="${article.memberId eq loginedMemberId}">
				<a class="btn btn-active btn-ghost text-xl"
					href="../article/modify?id=${article.id }">수정</a>
			</c:if>
			<c:if test="${article.memberId eq loginedMemberId}">
				<a class="btn btn-active btn-ghost text-xl"
					onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;"
					href="../article/doDelete?id=${article.id }">삭제</a>
			</c:if>
		</div>
	</div>
</section>

<section class="mt-3 text-xl">
	<div class="container mx-auto px-3">
		<table>
			<tr>
				<th>번호</th>
				<td>
					<div class="badge badge-lg text-xl">${article.id }</div>
				</td>
			</tr>
			<tr>
				<th>작성날짜</th>
				<td>${article.regDate }</td>
			</tr>
			<tr>
				<th>수정날짜</th>
				<td>${article.updateDate }</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td>${article.extra__writer }</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>${article.title }</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>${article.body }</td>
			</tr>
		</table>
	</div>
</section>

<section class="mt-3 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<c:if test="${rq.isLogined() }">
				<form action="../reply/doWrite" method="POST"
					onsubmit="ReplyWrite__submitForm(this); return false;">
					<input type="hidden" name="relTypeCode" value="article" /> <input
						type="hidden" name="relId" value="${article.id }" />
					<table>
						<colgroup>
							<col width="200" />
						</colgroup>

						<tbody>
							<tr>
								<th>댓글</th>
								<td><textarea class="input input-bordered w-full max-w-xs"
										type="text" name="body" placeholder="내용을 입력해주세요" /></textarea></td>
								<td>
									<button type="submit" value="작성" /> 댓글 작성
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</c:if>
			<c:if test="${!rq.isLogined() }">
				댓글을 작성하려면 <a class="btn-text-link btn btn-active btn-ghost"
					href="/usr/member/login?afterLoginUri=${rq.getEncodedCurrentUri()}">로그인</a> 후 이용해주세요.
			</c:if>
		</div>
	</div>
</section>

<section class="mt-3 text-xl">
	<div class="container mx-auto px-3">
		<h1 class="text-3xl">댓글 리스트(${repliesCount })</h1>
		<table class="table table-zebra">
		
			<colgroup>
				<col width="70" />
				<col width="100" />
				<col width="100" />
				<col width="140" />
				<col width="100" />
				<col width="100" />
			</colgroup>
			
			<thead>
				<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>작성자</th>
					<th>내용</th>
					<th>수정</th>
					<th>삭제</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach var="reply" items="${replies }">
					<tr class="hover">
						<td>
							<div class="badge">${reply.id}</div>
						</td>
						<td>${reply.regDate.substring(2,16)}</td>
						<td>${reply.extra__writer}</td>
						<td align="left">${reply.body}</td>
						<td><c:if test="${reply.memberId eq loginedMemberId}">
								<a class="btn btn-active btn-ghost text-xl"
									href="../reply/modify?id=${reply.id }">수정</a>
							</c:if></td>
						<td><c:if test="${reply.memberId eq loginedMemberId}">
								<a class="btn btn-active btn-ghost text-xl"
									onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;"
									href="../reply/doDelete?id=${reply.id }">삭제</a>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>