package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Article;

@Mapper
public interface ArticleRepository {


	@Select("""
			<script>
			SELECT A.*, M.name AS extra__writer
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			WHERE A.id = #{id}
			GROUP BY A.id
			</script>
			""")
	public Article getArticle(int id);

	@Select("""
			<script>
			SELECT A.*, M.name AS extra__writer
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			WHERE 1
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'" >
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<when test="searchKeywordTypeCode == 'body'" >
						AND A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<otherwise>
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
						OR A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</otherwise>
				</choose>
			</if>
			GROUP BY A.id
			ORDER BY A.id DESC
			LIMIT #{limitFrom}, #{itemsInAPage};
			</script>
			""")
	public List<Article> getArticles(int limitFrom,int itemsInAPage, String searchKeywordTypeCode, String searchKeyword);
	
	@Select("""
			<script>
			SELECT Count(*) AS cnt
			FROM article AS A
			WHERE 1
				<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'" >
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<when test="searchKeywordTypeCode == 'body'" >
						AND A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<otherwise>
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
						OR A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</otherwise>
				</choose>
			</if>
			</script>
			""")
	public int getArticlesCount(String searchKeywordTypeCode, String searchKeyword );
	
	@Select("""
			<script>
			SELECT LAST_INSERT_ID()
			</script>
			""")
	public int getLastInsertId();

	@Insert("""
			<script>
		INSERT INTO article
		SET regDate = NOW(),
		updateDate=
		NOW(),
		title =#{title},
		`body`= #{body},
		memberId = #{memberId}
			</script>
			""")
	public void writeArticle(String title, String body, int memberId);

	@Delete("""
			<script>
			DELETE FROM
			article
			WHERE id = #{id}
			</script>
			""")
	public void doDeleteArticle(Article article);

	@Update("""
			<script>
			UPDATE article
			<set>
			<if test="title != null and title != ''">title = #{title},</if>
			<if test="body != null and title != ''">`body` = #{body},</if>
			updateDate= NOW(),
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doModifyArticle(int id, String title, String body);
	
	@Select("""
			<script>
			SELECT hitCount
			FROM article
			WHERE id = #{id}
			</script>
			""")
	public int getArticleHitCount(int id);
	
	@Update("""
			<script>
			UPDATE article
			SET hitCount = hitCount + 1
			WHERE id = #{id}
			</script>
			""")
	public int increaseHitCount(int id);

	@Update("""
			<script>
			UPDATE article
			SET goodReactionPoint = goodReactionPoint + 1
			WHERE id = #{relId}
			</script>
			""")
	public int increaseGoodReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET badReactionPoint = badReactionPoint + 1
			WHERE id = #{relId}
			</script>
			""")
	public int increaseBadReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET goodReactionPoint = goodReactionPoint - 1
			WHERE id = #{relId}
			</script>
			""")
	public int decreaseGoodReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET badReactionPoint = badReactionPoint - 1
			WHERE id = #{relId}
			</script>
			""")
	public int decreaseBadReactionPoint(int relId);
}