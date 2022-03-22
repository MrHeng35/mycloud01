package com.example.openfeign.controller;

import com.example.openfeign.feign.BookService;
import com.example.service.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    BookService bookService;

    @GetMapping("/")
    public void test01(){
        Book book = bookService.get(2);
        System.out.println("getbook = " + book);
        Book add = bookService.add(book);
        System.out.println("add = " + add);
//        String add2 = bookService.add2(book);
//        System.out.println("add2 = " + add2);
        bookService.update(book);
        bookService.delete(2);
    }

}
