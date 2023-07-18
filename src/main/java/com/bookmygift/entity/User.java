package com.bookmygift.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	private String password;

	@Column(name = "EMAIL", nullable = false)
	@NotNull(message = "Email is required")
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
    private String twoFaCode;

    @NotNull(message = "twoFaExpiry is required")
	@Column(name = "TWO_FA_EXPIRY")
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

	public User() {

	}
	public User(Long userId, String username, String password, String email, String fullName, RoleEnum role, String twoFaCode, String twoFaExpiry, boolean isVerified) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
		this.role = role;
		this.twoFaCode = twoFaCode;
		this.twoFaExpiry = twoFaExpiry;
		this.isVerified = isVerified;
	}

	public static UserBuilder builder() {
		return new UserBuilder();
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

	public UserBuilder toBuilder() {
		return new UserBuilder().userId(this.userId).username(this.username).password(this.password).email(this.email).fullName(this.fullName).role(this.role).twoFaCode(this.twoFaCode).twoFaExpiry(this.twoFaExpiry).isVerified(this.isVerified);
	}

	public Long getUserId() {
		return this.userId;
	}

	public @NotBlank(message = "Username is required") String getUsername() {
		return this.username;
	}

	public @NotBlank(message = "Password is required") String getPassword() {
		return this.password;
	}

	public @NotNull(message = "Email is required") String getEmail() {
		return this.email;
	}

	public @NotBlank(message = "Full name is required") String getFullName() {
		return this.fullName;
	}

	public @NotNull(message = "RoleEnum is required") RoleEnum getRole() {
		return this.role;
	}

	public @NotNull(message = "twoFaCode is required") String getTwoFaCode() {
		return this.twoFaCode;
	}

	public @NotNull(message = "twoFaExpiry is required") String getTwoFaExpiry() {
		return this.twoFaExpiry;
	}

	public boolean isVerified() {
		return this.isVerified;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUsername(@NotBlank(message = "Username is required") String username) {
		this.username = username;
	}

	@JsonIgnore
	public void setPassword(@NotBlank(message = "Password is required") String password) {
		this.password = password;
	}

	public void setEmail(@NotNull(message = "Email is required") String email) {
		this.email = email;
	}

	public void setFullName(@NotBlank(message = "Full name is required") String fullName) {
		this.fullName = fullName;
	}

	public void setRole(@NotNull(message = "RoleEnum is required") RoleEnum role) {
		this.role = role;
	}

	public void setTwoFaCode(@NotNull(message = "twoFaCode is required") String twoFaCode) {
		this.twoFaCode = twoFaCode;
	}

	public void setTwoFaExpiry(@NotNull(message = "twoFaExpiry is required") String twoFaExpiry) {
		this.twoFaExpiry = twoFaExpiry;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String toString() {
		return "User(userId=" + this.getUserId() + ", username=" + this.getUsername() + ", fullName=" + this.getFullName() + ", role=" + this.getRole() + ", isVerified=" + this.isVerified() + ")";
	}

	public static class UserBuilder {
		private Long userId;
		private @NotBlank(message = "Username is required") String username;
		private @NotBlank(message = "Password is required") String password;
		private @NotNull(message = "Email is required") String email;
		private @NotBlank(message = "Full name is required") String fullName;
		private @NotNull(message = "RoleEnum is required") RoleEnum role;
		private @NotNull(message = "twoFaCode is required") String twoFaCode;
		private @NotNull(message = "twoFaExpiry is required") String twoFaExpiry;
		private boolean isVerified;

		UserBuilder() {
		}

		public UserBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public UserBuilder username(@NotBlank(message = "Username is required") String username) {
			this.username = username;
			return this;
		}

		@JsonIgnore
		public UserBuilder password(@NotBlank(message = "Password is required") String password) {
			this.password = password;
			return this;
		}

		public UserBuilder email(@NotNull(message = "Email is required") String email) {
			this.email = email;
			return this;
		}

		public UserBuilder fullName(@NotBlank(message = "Full name is required") String fullName) {
			this.fullName = fullName;
			return this;
		}

		public UserBuilder role(@NotNull(message = "RoleEnum is required") RoleEnum role) {
			this.role = role;
			return this;
		}

		public UserBuilder twoFaCode(@NotNull(message = "twoFaCode is required") String twoFaCode) {
			this.twoFaCode = twoFaCode;
			return this;
		}

		public UserBuilder twoFaExpiry(@NotNull(message = "twoFaExpiry is required") String twoFaExpiry) {
			this.twoFaExpiry = twoFaExpiry;
			return this;
		}

		public UserBuilder isVerified(boolean isVerified) {
			this.isVerified = isVerified;
			return this;
		}

		public User build() {
			return new User(this.userId, this.username, this.password, this.email, this.fullName, this.role, this.twoFaCode, this.twoFaExpiry, this.isVerified);
		}

		public String toString() {
			return "User.UserBuilder(userId=" + this.userId + ", username=" + this.username + ", password=" + this.password + ", email=" + this.email + ", fullName=" + this.fullName + ", role=" + this.role + ", twoFaCode=" + this.twoFaCode + ", twoFaExpiry=" + this.twoFaExpiry + ", isVerified=" + this.isVerified + ")";
		}
	}
}
