package com.expense_tracker.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expense_tracker.entities.RefreshToken;
import java.util.List;


@Repository
public interface RefreshTokenRepo  extends JpaRepository<RefreshToken, UUID>{
    
	
	public Optional<RefreshToken>findByToken(String token);
}
