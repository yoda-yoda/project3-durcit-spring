package org.durcit.be.search.repository;

import org.durcit.be.post.domain.Post;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.search.domain.Search;
import org.durcit.be.security.domian.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {



    // 검색으로 Post를 찾아올건데, 해당하는 제목, 내용, 태그, 닉네임이 검색에 포함되는 Post를 찾는다.
    // 태그와 Post가, delete false인 것만 찾는다.
    @Query("SELECT p FROM Post as p " +
            "LEFT JOIN p.postsTagList as pt " +
            "LEFT JOIN p.member as m " +
            "WHERE (p.title LIKE %:search% OR p.content LIKE %:search% OR pt.contents LIKE %:search% OR m.nickname LIKE %:search%) " +
            "AND p.deleted = false AND pt.deleted = false" )
    public List<Post> findCheckedAllPostWithNonDeleted(@Param("search") String search);




}
