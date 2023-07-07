package com.bookmygift.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "properties", schema = "myapp")
@Getter
@NoArgsConstructor
public class Properties implements Serializable {

    @Serial
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
