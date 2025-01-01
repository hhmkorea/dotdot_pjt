package com.dotdot.site.repository;

import com.dotdot.site.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {

    @Modifying
    @Query("update Board p set p.viewCnt = p.viewCnt + 1 where p.id = :id")
    void updateViewCnt(@Param("id") int id);

    @Query(value = "select b.* from Board b join Member m on b.memberId = m.id where m.username like concat('%',:keyword,'%')", nativeQuery = true)
    Page<Board> findLikeUsername(String keyword, Pageable pageable);

    @Query(value = "select b.* from Board b join Member m on b.memberId = m.id where m.username like concat('%',:keyword,'%') or b.title like concat('%',:keyword,'%') or b.content like concat('%',:keyword,'%')", nativeQuery = true)
    Page<Board> findLikeAll(String keyword, Pageable pageable);
}
