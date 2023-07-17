package com.bookmygift.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USERS", schema = "myapp")
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID", nullable = false)
	private Long userId;

	@Column(name = "USERNAME", nullable = false)
	@NotBlank(message = "Username is required")
	private String username;

	@Column(name = "PASSWORD", nullable = false)
	@NotBlank(message = "Password is required")
	@JsonIgnore
	@ToString.Exclude
	private String password;

	@Column(name = "EMAIL", nullable = false)
	@NotNull(message = "Email is required")
	@ToString.Exclude
	private String email;

	@Column(name = "FULL_NAME", nullable = false)
	@NotBlank(message = "Full name is required")
	private String fullName;

	@Column(name = "ROLE", nullable = false)
	@NotNull(message = "RoleEnum is required")
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

    @NotNull(message = "twoFaCode is required")
	@Column(name = "TWO_FA_CODE")
	@ToString.Exclude
    private String twoFaCode;

    @NotNull(message = "twoFaExpiry is required")
	@Column(name = "TWO_FA_EXPIRY")
	@ToString.Exclude
    private String twoFaExpiry;

    @Column(name = "IS_VERIFIED")
    private boolean isVerified;

	@JsonCreator
	public User(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(json, User.class);
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.fullName = user.getFullName();
		this.role = user.getRole();
		this.twoFaCode = user.getTwoFaCode();
		this.twoFaExpiry = user.getTwoFaExpiry();
		this.isVerified = user.isVerified();
	}

	@JsonCreator
	public User(@JsonProperty("userId") Long userId, @JsonProperty("username") String username,
				@JsonProperty("email") @NotNull String email, @JsonProperty("fullName") String fullName,
				@JsonProperty("role") @NotNull RoleEnum role, @JsonProperty("twoFaCode") @NotNull String twoFaCode,
				@JsonProperty("twoFaExpiry") @NotNull String twoFaExpiry, @JsonProperty("isVerified") boolean isVerified) {

		this.userId = userId;
		this.username = username;
		this.email = email;
		this.fullName = fullName;
		this.role = role;
		this.twoFaCode = twoFaCode;
		this.twoFaExpiry = twoFaExpiry;
		this.isVerified = isVerified;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

}
