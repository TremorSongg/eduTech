package com.example.edutech.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.edutech.Model.ControlStock;

@Repository
public interface ControlStockRepository extends JpaRepository<ControlStock,Integer> {

}
