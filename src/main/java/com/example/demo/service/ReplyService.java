package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.ReplyRepository;
import com.example.demo.util.Ut;
import com.example.demo.vo.Article;
import com.example.demo.vo.Reply;
import com.example.demo.vo.ResultData;

@Service
public class ReplyService {

	@Autowired
	private ReplyRepository replyRepository;

	public ReplyService(ReplyRepository replyRepository) {
		this.replyRepository = replyRepository;
	}

	public ResultData<Integer> writeReply(int actorId, String relTypeCode, int relId, String body) {
		replyRepository.writeReply(actorId, relTypeCode, relId, body);

		int id = replyRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 댓글이 생성되었습니다", id), "id", id);
	}
	
	public List<Reply> getReplies(int memberId, String relTypeCode, int relId) {
		
		return replyRepository.getReplies(memberId, relTypeCode, relId);
	}

	public ResultData<Reply> getReply(int id) {
		
		Reply reply = replyRepository.getReply(id);
		
		return ResultData.from("S-1", Ut.f("%d번 글을 불러왔습니다.", id),"reply", reply);
	}

	public ResultData<Integer> doModifyReply(int id, String body) {
		
		replyRepository.doModifyReply(id,body);
		
		return ResultData.from("S-1", Ut.f("%d번 댓글이 수정되었습니다", id),"id", id);
	}

	public ResultData<Integer> doDeleteReply(int id) {
		
		replyRepository.doDeleteReply(id);
		
		return ResultData.from("S-1", Ut.f("%d번 댓글이 삭제되었습니다", id),"id", id);
	}
}