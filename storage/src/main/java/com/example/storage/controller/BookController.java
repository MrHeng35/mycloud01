package com.example.storage.controller;

import com.example.service.api.IBookController;
import com.example.service.entity.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController implements IBookController {

    @Override
    public Book get(Integer id){
        Book book = new Book();
        book.setId(id);
        System.out.println("book = " + book);
        return book;
    }

    @Override
    public Book add(Book book){
        return book;
    }

    @PostMapping("/add")
    public String add2(@RequestBody Book book){
        System.out.println("post-location-book = " + book);
        return "redirect:/index";
    }

    @Override
    public void update(Book book){
        System.out.println("update book: "+book);
    }

    @Override
    public void delete(Integer id){
        System.out.println("detele book :"+id);
    }

    @GetMapping("/books")
    @ResponseBody
    public List<Book> getBooksByIds(String ids){
        List<Integer> list = Arrays.stream(ids.split(",")).map(id -> Integer.parseInt(id)).collect(Collectors.toList());
        List<Book> books = new ArrayList<>();
        for (Integer id : list) {
            Book book = new Book();
            book.setId(id);
            books.add(book);
        }
        System.out.println("ids = " + ids);
        return books;
    }
}
