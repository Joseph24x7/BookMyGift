package com.bookmygift.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Entity
@Table(name = "USERS", schema = "myapp")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     *
     */
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
    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull(message = "twoFaCode is required")
    @Column(name = "TWO_FA_CODE")
    private String twoFaCode;

    @NotNull(message = "twoFaExpiry is required")
    @Column(name = "TWO_FA_EXPIRY")
    private String twoFaExpiry;

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
    }

    @JsonCreator
    public User(@JsonProperty("userId") Long userId, @JsonProperty("username") String username,
                @JsonProperty("email") String email, @JsonProperty("fullName") String fullName,
                @JsonProperty("role") Role role, @JsonProperty("twoFaCode") String twoFaCode,
                @JsonProperty("twoFaExpiry") String twoFaExpiry) {

        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.twoFaCode = twoFaCode;
        this.twoFaExpiry = twoFaExpiry;
    }

}