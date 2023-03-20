package com.example.user_rest_service.repository;

import com.example.user_rest_service.entity.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDB, String> {
}
