package com.example.hystrix.service;

import com.example.service.entity.Book;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookService {
    @Autowired
    RestTemplate restTemplate;

    /**
     * Hystrix 中自带有请求缓存功能，getBookById 方法执行的结果会被缓存起来，默认情况下，方法的返回值是缓存的 value，方法的参数是缓存的 key。
     *
     * @CacheResult 表示启用缓存
     * @CacheKey 表示该字段是缓存的 key，name 字段不算。
     * @CacheRemove 表示移除缓存中的数据
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "error")
    @CacheResult
    public Book getBookById(@CacheKey Integer id, String name){
        Book book = restTemplate.getForObject("http://storage/book/?id={1}", Book.class, id);
        System.out.println("book = " + book);
        return book;
    }


    /**
     * 该方法是 hello 方法的降级方法，error 方法在定义的过程中，返回值需要和 hello 方法保持一致
     * <p>
     * @return
     */
    // 此时加上 Throwable t, 会报错？异常处理和缓存不能同时用吗
    public Book error(Integer id,String name){
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor("服务降级");
        return book;
    }
}
