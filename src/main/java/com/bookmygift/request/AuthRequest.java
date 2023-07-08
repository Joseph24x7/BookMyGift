package com.bookmygift.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class AuthRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in the format of example@domain.com")
    @ToString.Exclude
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character and must not contain any spaces")
    @ToString.Exclude
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 250, message = "Full name must be less than 250 characters")
    @ToString.Exclude
    private String fullName;

}
