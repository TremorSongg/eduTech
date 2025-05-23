package com.example.edutech.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edutech.Model.CarritoItem;

public interface CarritoRepository extends JpaRepository<CarritoItem, Integer> { 

}

