package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManageRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u FROM test u WHERE u.role<> 'admin'")
    List<User> findAllNonAdminUsers();

}
