package com.example.feignhystrix.controller;

import com.example.feignhystrix.feign.BookService;
import com.example.service.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/test")
    public Book get(){
        Book book = bookService.get(1);
        System.out.println("book = " + book);
        return book;
    }
}
