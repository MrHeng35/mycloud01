package com.example.hystrix.controller;

import com.example.hystrix.service.*;
import com.example.service.entity.Book;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BookService bookService;
    @Autowired
    BooksService booksService;

    @GetMapping("/hello")
    public String hello(){
        //注解方式调用
        return helloService.hello();
        //继承方式调用,一个 helloCommand 对象只能执行一次
//        HelloCommand helloCommand = new HelloCommand(restTemplate);
        //同步执行请求方法，这个会发生阻塞
//        String execute = helloCommand.execute();
        //异步调用，不会阻塞
//        Future<String> future = helloCommand.queue();
//        return execute;

    }

    @GetMapping("/test01")
    public void test01() {
        //缓存有一个范围
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        //初始化上下文之后，缓存就开始了
        //注解方式调用
//        Book b1 = bookService.getBookById(99,"三国");
//        Book b2 = bookService.getBookById(99,"水浒");
        //继承方式调用
        BookCommand command1 = new BookCommand(restTemplate, 1, "sanguo");
        Book b1 = command1.execute();
        BookCommand command2 = new BookCommand(restTemplate, 1, "shuihu");
        Book b2 = command2.execute();
        //到上下文关闭，缓存结束
        ctx.close();
        //虽然 b3 的 id 也是 99，但是由于上下文已经关闭，之前的缓存已经失效，
        //凡是带有 @CacheResult 注解的方法，都应该在 ctx 未关闭之前调用
//        Book b3 = bookService.getBookById(99);
        System.out.println("b1 = " + b1);
        System.out.println("b2 = " + b2);
//        System.out.println("b3 = " + b3);
    }

    @GetMapping("/books")
    public void testBooks() throws ExecutionException, InterruptedException {
        //请求合并必须在 HystrixRequest 的上下文范围内执行才会进行合并
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        BookCollapseCommand command0 = new BookCollapseCommand(booksService,1);
        BookCollapseCommand command1 = new BookCollapseCommand( booksService,2);
        BookCollapseCommand command2 = new BookCollapseCommand(booksService,3);
        Future<Book> f0 = command0.queue();
        Future<Book> f1 = command1.queue();
        Future<Book> f2 = command2.queue();
        Book b0 = f0.get();
        Book b1 = f1.get();
        Book b2 = f2.get();
        System.out.println("b0="+b0);
        System.out.println("b1="+b1);
        System.out.println("b2="+b2);
        Thread.sleep(1000);
        BookCollapseCommand command3 = new BookCollapseCommand(booksService,4);
        Future<Book> f3 = command3.queue();
        Book b3 = f3.get();
        System.out.println("b3="+b3);
        ctx.close();
    }
 @GetMapping("/books2")
    public void testBooks2() throws ExecutionException, InterruptedException {
        //请求合并必须在 HystrixRequest 的上下文范围内执行才会进行合并
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        Future<Book> f0 = booksService.getBookById(1);
        Future<Book> f1 = booksService.getBookById(2);
        Future<Book> f2 = booksService.getBookById(3);
        Book b0 = f0.get();
        Book b1 = f1.get();
        Book b2 = f2.get();
        System.out.println("b0="+b0);
        System.out.println("b1="+b1);
        System.out.println("b2="+b2);
        Thread.sleep(1000);
        Future<Book> f3 = booksService.getBookById(4);
        Book b3 = f3.get();
        System.out.println("b3="+b3);
        ctx.close();
    }

}
