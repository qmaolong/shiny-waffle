package com.nick.repository.jpa;

import com.nick.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by qmaolong on 2017/2/27.
 */
@org.springframework.stereotype.Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    @Query("select r from Test r where name = ?1")
    Test findByName(String name);

}