package com.example.WebUFO.repository;

import com.example.WebUFO.model.UsersData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDataEntityRepository extends JpaRepository<UsersData, Integer> {
}
