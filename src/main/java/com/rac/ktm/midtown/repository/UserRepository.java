package com.rac.ktm.midtown.repository;

import com.rac.ktm.midtown.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM test u WHERE (u.userName = :identifier OR u.email = :identifier) AND u.password = :password")
    User findByUserNameOrEmail(@Param("identifier") String identifier, @Param("password") String password);

    @Query("SELECT u FROM test u WHERE (u.userName = :identifier OR u.email = :identifier)")
    User findByIdentifier(@Param("identifier") String identifier);

    User findByUserName(String userName);

    @Modifying
    @Query("UPDATE test t set t.email= :email, t.name= :name,t.phoneNumber= :phoneNumber WHERE t.userName= :userName")
    void updateProfile(@Param("name") String name, @Param("email") String email,@Param("phoneNumber") String phoneNumber,@Param("userName") String userName);
}
