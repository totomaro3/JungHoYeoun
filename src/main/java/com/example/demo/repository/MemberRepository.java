package com.example.demo.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Member;

@Mapper
public interface MemberRepository {

	@Insert("""
			INSERT INTO `member`
			set regDate = NOW(),
			updateDate = NOW(),
			email = #{email},
			loginPw = #{loginPw},
			`name` = #{name}
			""")
	public void doJoinMember(String email, String loginPw, String name);

	@Select("""
			SELECT *
			FROM `member`
			WHERE id = #{id}
			""")
	public Member getMemberById(int id);

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE email = #{email}
			""")
	public boolean isDupEmail(String email);
	
	@Select("""
			SELECT *
			FROM `member`
			WHERE email = #{email}
			""")
	public Member getMemberByLoginId(String email);
	
	@Select("""
			SELECT LAST_INSERT_ID()
			""")
	public int getLastInsertId();

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public boolean isDupNameAndEmail(String name, String email);
	
	@Select("""
			SELECT *
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public Member getMemberByNameAndEmail(String name, String email);
	
	@Select("""
			SELECT loginPw
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public String getLoginPwByNameAndEmail(String name, String email);
	
	@Update("""
			<script>
			UPDATE `member`
			<set>
				<if test="loginPw !=''">
					loginPw = #{loginPw},
				</if>
				<if test="name !=''">
					name = #{name},
				</if>
				updateDate= NOW()
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doModifyMember(int id, String loginPw, String name);

	@Update("""
			<script>
			UPDATE member
			<set>
			delStatus = 1,
			delDate = NOW()
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doDeleteMember(int id);

	
}