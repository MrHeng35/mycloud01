package com.example.service.api;

import com.example.service.entity.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@Controller
public interface IBookController {
    @GetMapping("/book/")
    @ResponseBody
    public Book get(@RequestParam("id") Integer id);

    @PostMapping("/book/")
    @ResponseBody
    Book add(@RequestBody Book book);

    @PutMapping("/book/")
    @ResponseBody
    void update(@RequestBody Book book);

    @DeleteMapping("/book/{id}")
    @ResponseBody
    void delete(@PathVariable("id") Integer id);
}
