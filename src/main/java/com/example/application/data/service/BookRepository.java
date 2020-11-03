package com.example.application.data.service;

import com.example.application.data.entity.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface BookRepository extends JpaRepository<Book, Integer> {

}