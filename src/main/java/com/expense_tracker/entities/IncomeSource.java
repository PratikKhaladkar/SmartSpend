package com.expense_tracker.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name", "user_id" }))
public class IncomeSource {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank
	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	public IncomeSource() {
		super();

	}

	public IncomeSource(@NotBlank String name, User user) {
		super();
		this.name = name.toUpperCase();
		this.user = user;
	}

	public IncomeSource(@NotBlank String name) {
		super();
		this.name = name.toUpperCase();
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}
