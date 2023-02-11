package com.bookmygift.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS", schema = "myapp")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@Column(name = "PASSWORD", nullable = false)
	@NotBlank(message = "Password is required")
	private String password;

	@Column(name = "USERNAME", nullable = false)
	@NotBlank(message = "Username is required")
	private String username;

	@NotNull(message = "Email is required")
	@Column(name = "EMAIL", nullable = false)
	private String email;

	@NotNull(message = "Role is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE", nullable = false)
	private Role role;

	@NotBlank(message = "Full Name is required")
	@Column(name = "FULL_NAME", nullable = false)
	private String fullname;

	@NotNull(message = "Account Non Expired is required")
	@Column(name = "ACCOUNT_NON_EXPIRED", nullable = false)
	private Boolean accountNonExpired;

	@NotNull(message = "Account Non Locked is required")
	@Column(name = "ACCOUNT_NON_LOCKED", nullable = false)
	private Boolean accountNonLocked;

	@NotNull(message = "Credentials Non Expired is required")
	@Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false)
	private Boolean credentialsNonExpired;

	@NotNull(message = "Enabled is required")
	@Column(name = "ENABLED", nullable = false)
	private Boolean enabled;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
