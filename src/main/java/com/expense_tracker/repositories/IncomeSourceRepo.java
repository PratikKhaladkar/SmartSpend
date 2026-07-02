package com.expense_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.expense_tracker.entities.IncomeSource;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeSourceRepo extends JpaRepository<IncomeSource, UUID> {
   
	
	
	@Query("""
		    SELECT i FROM IncomeSource i
		    WHERE i.name = :name
		    AND (i.user IS NULL OR i.user.id = :userId)
		""")
		Optional<IncomeSource> findByNameAndBelongsToDefaultOrUser(
		    @Param("name") String name, 
		    @Param("userId") UUID userId
		);
	
	@Query("""
		    SELECT i FROM IncomeSource i
		    WHERE i.name = :name
		    AND  i.user.id = :userId
		""")
		Optional<IncomeSource> findByNameAndBelongsTOUser(
		    @Param("name") String name, 
		    @Param("userId") UUID userId
		);
	
	@Query("""
		    SELECT COUNT(i) > 0 FROM IncomeSource i
		    WHERE i.name = :name
		    AND (i.user IS NULL OR i.user.id = :userId)
		""")
		boolean isExistsByNameAndBelongsToDefaultOrUser(@Param("name") String name,
		                                      @Param("userId") UUID userId);
      
	
	@Query("""
		    SELECT COUNT(i) > 0 FROM IncomeSource i
		    WHERE i.name = :name
		    AND i.user.id = :userId
		""")
	boolean isBelongsToUser(@Param("name") String name,
		                                      @Param("userId") UUID userId);
	
	@Query("""
		    SELECT COUNT(i) > 0 FROM IncomeSource i
		    WHERE i.name = :name
		    AND (i.user IS NULL)
		""")
	boolean isBelongsToSystem(@Param("name") String name);
	
	
	@Query("""
		    SELECT i FROM IncomeSource i
		    WHERE i.user IS NULL OR i.user.id = :userId
		""")
	List<IncomeSource>getAllIncomeSources(@Param("userId") UUID userId);
}
