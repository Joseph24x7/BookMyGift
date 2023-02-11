package com.bookmygift.reqresp;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Username is required")
	@Size(min=3, max=50, message = "Username must be between 3 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Username can only contain alphanumeric characters and .(dot) or _(underscore)")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min=8, max=20, message = "Password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character and must not contain any spaces")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in the format of example@domain.com")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min=3, max=20, message = "Full name must be between 3 and 20 characters")
    private String fullname;
    
    @Pattern(regexp = "CUSTOMER|ADMIN", message = "Invalid role. Allowed values: CUSTOMER, ADMIN")
	@Column(name = "ROLE", nullable = false)
	private String role;
    
}
