package com.example.feignhystrix.feign;

import com.example.service.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookServiceFalBack implements BookService{
    @Override
    public Book get(Integer id) {
        Book book = new Book();
        book.setId(id);
        book.setName("服务降级了");
        return book;
    }
}
