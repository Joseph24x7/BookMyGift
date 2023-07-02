package com.bookmygift.reqresp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String token;

}
