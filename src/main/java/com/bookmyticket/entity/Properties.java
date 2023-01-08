package com.bookmyticket.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROPERTIES")
@Data
@NoArgsConstructor
public class Properties implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PROP_ID", nullable = false)
	private Long propId;

	@Column(name = "PROP_NAME", nullable = false)
	private String propName;

	@Column(name = "PROP_VALUE", nullable = false)
	private String propValue;

}
