package com.example.edutech.Model;


import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class ControlStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
