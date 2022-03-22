package com.example.hystrix.service;

import com.example.service.entity.Book;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BookCollapseCommand extends HystrixCollapser<List<Book>,Book,Integer> {

    private BooksService booksService;
    private Integer id;

    public BookCollapseCommand(BooksService booksService, Integer id) {
        super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("UserCollapseCommand")).andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(200)));
        this.booksService = booksService;
        this.id = id;
    }

    /**
     * 获取请求参数
     * @return
     */
    @Override
    public Integer getRequestArgument() {
        return id;
    }

    /**
     * 合并请求
     * @param collection 框架已经收集好的请求
     * @return
     */
    @Override
    protected HystrixCommand<List<Book>> createCommand(Collection<CollapsedRequest<Book, Integer>> collection) {
        List<Integer> ids = collection.stream().map(r -> r.getArgument()).collect(Collectors.toList());
        return new BookBatchCommand(booksService,ids);
    }

    /**
     * 分发请求结果
     * @param books 这个是调用的结果
     * @param collection 合并起来的请求在这个集合中
     */
    @Override
    protected void mapResponseToRequests(List<Book> books, Collection<CollapsedRequest<Book, Integer>> collection) {
        int num = 0;
        for (CollapsedRequest<Book, Integer> bookIntegerCollapsedRequest : collection) {
            bookIntegerCollapsedRequest.setResponse(books.get(num++));
        }
    }
}
