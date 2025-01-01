package com.dotdot.site.repository;

import com.dotdot.site.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, JpaSpecificationExecutor<Board> {

    @Modifying
    @Query("update Board p set p.viewCnt = p.viewCnt + 1 where p.id = :id")
    int updateViewCnt(int id);
}
