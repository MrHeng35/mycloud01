package com.example.hystrix.service;

import com.example.service.entity.Book;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class BooksService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand
    public List<Book> getBooksByIds(List<Integer> ids){
        Book[] books = restTemplate.getForObject("http://storage/books?ids={1}", Book[].class, StringUtils.join(ids,","));
        List<Book> bookList = Arrays.asList(books);
        return bookList;
    }

    @HystrixCollapser(batchMethod = "getBooksByIds",collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds",value = "200")})
    public Future<Book> getBookById(Integer id){
        return null;
    }


}
