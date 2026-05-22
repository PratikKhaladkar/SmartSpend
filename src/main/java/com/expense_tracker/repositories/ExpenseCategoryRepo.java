package com.expense_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expense_tracker.entities.ExpenseCategory;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepo extends JpaRepository<ExpenseCategory, UUID> {
   
	
	
	@Query("""
		    SELECT c FROM ExpenseCategory c
		    WHERE c.name = :name
		    AND (c.user IS NULL OR c.user.id = :userId)
		""")
		Optional<ExpenseCategory> findByNameAndBelongsToDefaultOrUser(
		    @Param("name") String name, 
		    @Param("userId") UUID userId
		);
	 
	@Query("""
		    SELECT COUNT(c) > 0 FROM ExpenseCategory c
		    WHERE c.name = :name
		    AND (c.user IS NULL OR c.user.id = :userId)
		""")
		boolean isExistsByNameAndBelongsToDefaultOrUser(@Param("name") String name,
		                                     @Param("userId") UUID userId);
	
	@Query("""
		    SELECT c FROM ExpenseCategory c
		    WHERE c.user IS NULL OR c.user.id = :userId
		""")
	List<ExpenseCategory>getAllExpesExpenseCategories(@Param("userId") UUID userId);
}
