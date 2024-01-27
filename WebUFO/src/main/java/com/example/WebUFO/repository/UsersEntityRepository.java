package com.example.WebUFO.repository;

import com.example.WebUFO.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersEntityRepository extends JpaRepository<Users,Long> {
}
