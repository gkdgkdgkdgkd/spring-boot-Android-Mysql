package com.test;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>
{
    @Query(value = "select * from user where name = ?1",nativeQuery = true)
    public List<User> findByName(String name);

    @Modifying
    @Query(value = "delete from user where name = ?1",nativeQuery = true)
    public int deleteByName(String name);
}
