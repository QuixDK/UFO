package com.quixdk.UFOBot.repository;

import com.quixdk.UFOBot.model.UsersBills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersBillsEntityRepository extends JpaRepository<UsersBills, Integer> {
}
