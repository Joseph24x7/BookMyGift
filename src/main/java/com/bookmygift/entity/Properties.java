package com.bookmygift.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "properties", schema = "myapp")
@Getter
@NoArgsConstructor
public class Properties implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "prop_id", nullable = false)
	private Long propId;

	@Column(name = "prop_name", nullable = false)
	private String propName;

	@Column(name = "prop_value", nullable = false)
	private String propValue;

}
