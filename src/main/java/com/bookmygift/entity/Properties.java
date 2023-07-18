package com.bookmygift.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "properties", schema = "myapp")
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

    public Properties(){
    }

    public Long getPropId() {
        return propId;
    }

    public String getPropName() {
        return propName;
    }

    public String getPropValue() {
        return propValue;
    }
}
