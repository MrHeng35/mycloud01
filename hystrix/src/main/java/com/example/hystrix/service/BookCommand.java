package com.example.hystrix.service;

import com.example.service.entity.Book;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

public class BookCommand extends HystrixCommand<Book> {

    RestTemplate restTemplate;

    Integer id;
    String name;

    public BookCommand(RestTemplate restTemplate,Integer id,String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("zhangsan")));
        this.restTemplate = restTemplate;
        this.id=id;
        this.name=name;
    }

    /**
     * 发起请求的地方
     *
     * @return
     * @throws Exception
     */
    @Override
    protected Book run() throws Exception {
        Book book = restTemplate.getForObject("http://storage/book/?id={1}", Book.class, id);
        System.out.println("book = " + book);
        return book;
    }

    /**
     * 这是服务降级的方法，即 run 方法执行失败的时候，会自动触发该方法的执行
     *
     * @return
     */
    @Override
    protected Book getFallback() {
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor("服务降级");
        return book;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }
}
