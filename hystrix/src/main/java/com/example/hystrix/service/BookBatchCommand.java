package com.example.hystrix.service;

import com.example.service.entity.Book;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import java.util.ArrayList;
import java.util.List;

public class BookBatchCommand extends HystrixCommand<List<Book>> {
    private BooksService booksService;
    private List<Integer> ids;

    public BookBatchCommand(BooksService booksService, List<Integer> ids) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("batchCmd")).andCommandKey(HystrixCommandKey.Factory.asKey("batchKey")));
        this.booksService = booksService;
        this.ids = ids;
    }

    @Override
    protected List<Book> run() throws Exception {
        return booksService.getBooksByIds(ids);
    }

    @Override
    protected List<Book> getFallback() {
        List<Book> list = new ArrayList<>();
        Book book = new Book();
        book.setName("服务降级了");
        list.add(book);
        return list;
    }
}
