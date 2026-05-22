package com.expense_tracker.repositories;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.expense_tracker.entities.User;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
       
	
	public Optional<User>findByEmail(String email);
	public boolean existsByEmail(String email);
	
}
