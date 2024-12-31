package com.dotdot.site.repository;

import com.dotdot.site.model.Board;
import org.springframework.data.jpa.domain.Specification;

public class BoardSpecification {

//    public static Specification<Board> likeUsername(String keyword) {
//        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("username"), "%" + keyword + "%");
//    }

    public static Specification<Board> likeTitle(String keyword) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

    public static Specification<Board> likeContent(String keyword) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("content"), "%" + keyword + "%");
    }

}
