package com.example.storage.controller;

import com.example.service.entity.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @GetMapping("/")
    @ResponseBody
    public Book get(Integer id){
        Book book = new Book();
        book.setId(id);
        return book;
    }

    @PostMapping("/")
    @ResponseBody
    public Book add(Book book){
        return book;
    }

    @PostMapping("/add")
    public String add2(@RequestBody Book book){
        System.out.println("post-location-book = " + book);
        return "redirect:/index";
    }

    @PutMapping("/")
    public void update(@RequestBody Book book){
        System.out.println("update book: "+book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id){
        System.out.println("detele book :"+id);
    }
}
