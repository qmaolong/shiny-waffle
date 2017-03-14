package com.nick.repository;

import com.nick.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * Created by qmaolong on 2017/2/27.
 */
@org.springframework.stereotype.Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

    Page<Test> findAll(Pageable pageable);

    Test findByNameAndCountryAllIgnoringCase(String name, String country);

    @Query("select r from Test r where name = ?1")
    Test FindByName(String name);

}