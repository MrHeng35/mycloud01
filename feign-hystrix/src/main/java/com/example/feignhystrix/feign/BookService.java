package com.example.feignhystrix.feign;

import com.example.service.entity.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "storage",fallback = BookServiceFalBack.class)
public interface BookService {

    @GetMapping("/book/")
    Book get(@RequestParam("id") Integer id);
}
