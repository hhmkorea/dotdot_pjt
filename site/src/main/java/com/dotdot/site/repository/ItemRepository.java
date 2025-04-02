package com.dotdot.site.repository;

import com.dotdot.site.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {

    @Query(value = "select auto_increment as newId from information_schema.tables where table_schema = 'dotdot' and table_name = 'item'", nativeQuery = true)
    int nextId();
}
