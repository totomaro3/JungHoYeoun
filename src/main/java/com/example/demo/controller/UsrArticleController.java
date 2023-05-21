package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.ArticleService;
import com.example.demo.service.ReplyService;
import com.example.demo.util.Ut;
import com.example.demo.vo.Article;
import com.example.demo.vo.Reply;
import com.example.demo.vo.ResultData;
import com.example.demo.vo.Rq;

@Controller
public class UsrArticleController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private Rq rq;

	@RequestMapping("/usr/article/list")
	public String showList(Model model, int page,
			@RequestParam(defaultValue = "") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword
			) {
		
		int articlesCount = articleService.getArticlesCount(searchKeywordTypeCode, searchKeyword);
		
		if(articlesCount < 1) {
			return rq.jsHitoryBackOnView("게시물이 없습니다.");
		}
		
		int itemsInAPage = 10;
		int limitFrom = (page - 1) * itemsInAPage;
		int pagesCount = (int) Math.ceil((double) articlesCount / itemsInAPage);	
		
		ResultData<List<Article>> getArticlesRd = articleService.getArticles(limitFrom, itemsInAPage, searchKeywordTypeCode, searchKeyword);
		
		List<Article> articles = getArticlesRd.getData1();
		
		model.addAttribute("articles", articles);
		model.addAttribute("articlesCount", articlesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount",pagesCount);
		model.addAttribute("searchKeywordTypeCode",searchKeywordTypeCode);
		model.addAttribute("searchKeyword",searchKeyword);

		// ResultData.from("S-1", "게시글 목록을 조회합니다.","articles", articles);
		return "usr/article/list";
	}

	@RequestMapping("/usr/article/detail")
	public String showDetail(Model model, int id) {
		
		ResultData<Article> getArticleRd = articleService.getArticle(id);
		Article article = getArticleRd.getData1();
		
		List<Reply> replies = replyService.getReplies(rq.getLoginedMemberId(), "article" , id);
		int repliesCount = replies.size();
		
		if (article == null) {
			return rq.jsHitoryBackOnView(id + "번글은 존재하지 않습니다.");
		}
		
		model.addAttribute("repliesCount", repliesCount);
		model.addAttribute("article", article);
		model.addAttribute("replies", replies);
		
		return "usr/article/detail";
	}

	@RequestMapping("/usr/article/write")
	public String showWrite(Model model) {
		
		int loginedMemberId = rq.getLoginedMemberId();

		model.addAttribute("loginedMemberId", loginedMemberId);

		return "usr/article/write";
	}

	@RequestMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(String title, String body) {

		if (Ut.empty(title)) {
			// ResultData.from("F-1", "제목을 입력해주세요");
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.empty(body)) {
			// ResultData.from("F-2", "내용을 입력해주세요");
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		int loginedMemberId = rq.getLoginedMemberId();

		ResultData<Integer> writeArticleRd = articleService.writeArticle(title, body, loginedMemberId);

		int id = (int) writeArticleRd.getData1();

		ResultData<Article> getArticleRd = articleService.getArticle(id);
		
		Article article = getArticleRd.getData1();

		// ResultData.newData(writeArticleRd, "String", sb.toString());

		return Ut.jsReplace("S-1", id + "번글이 작성되었습니다.", "detail?id="+id);
	}

	@RequestMapping("/usr/article/modify")
	public String showModify(Model model, int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);
		
		Article article = getArticleRd.getData1();

		if (article == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			return rq.jsHitoryBackOnView(id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		
		if (article.getMemberId() != loginedMemberId) {
			// ResultData.from("F-2", Ut.f("해당 글에 대한 권한이 없습니다."));
			return rq.jsHitoryBackOnView("해당 글에 대한 권한이 없습니다.");
		}

		model.addAttribute("article", article);

		return "usr/article/modify";
	}

	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body) {
		
		ResultData<Article> getArticleRd = articleService.getArticle(id);
		
		Article article = getArticleRd.getData1();
		
		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}
		
		articleService.doModifyArticle(id, title, body);
		
		//ResultData.from("S-1", id + "번글이 수정되었습니다.", "article", article);
		return Ut.jsReplace("S-1", id + "번글이 수정되었습니다.", "detail?id="+id);
	}

	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);
		
		Article article = getArticleRd.getData1();

		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}

		articleService.doDeleteArticle(article);

		return Ut.jsReplace("S-1", id + "번글이 삭제되었습니다.", "list?boardId=0&page=1");
	}
	
	@RequestMapping("/usr/article/doIncreaseHitCountRd")
	@ResponseBody
	public ResultData<Integer> doIncreaseHitCountRd(int id) {

		ResultData<Integer> increaseHitCountRd = articleService.increaseHitCount(id);

		if (increaseHitCountRd.isFail()) {
			return increaseHitCountRd;
		}

		return ResultData.newData(increaseHitCountRd, "hitCount", articleService.getArticleHitCount(id));
	}
}