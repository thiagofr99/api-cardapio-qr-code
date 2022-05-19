package com.devthiagofurtado.cardapioqrcode.repository;

import com.devthiagofurtado.cardapioqrcode.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    User findByUsername(@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.userName LIKE CONCAT('%',:userName,'%')")
    Page<User> findAllByUserName(String userName, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT*FROM users INNER JOIN user_permission ON user_permission.id_user = users.id INNER JOIN permission ON permission.id = user_permission.id_permission WHERE user_permission.id_permission = 2 ")
    List<User> findAllManagers();



}
