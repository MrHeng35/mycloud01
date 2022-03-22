package com.example.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;


    /**
     * 远程调用 storage 服务，但是，可能会调用失败
     * <p>
     * 目的：当 storage 调用失败的时候，不影响到当前的方法
     *
     * @HystrixCommand(fallbackMethod = "") 制定了当前方法的断路器，即当前方法如果执行失败，执行抛出异常，会触发 fallbackMethod 中指定的方法，这种方式也叫做服务降级
     */
    @HystrixCommand(fallbackMethod = "error",ignoreExceptions = ArithmeticException.class)
    public String hello(){
        int i = 1 / 0;
        String s = restTemplate.getForObject("http://storage/deduct", String.class);
        return s;
    }


    /**
     * 该方法是 hello 方法的降级方法，error 方法在定义的过程中，返回值需要和 hello 方法保持一致
     * <p>
     * @return
     */
    public String error(Throwable t){
        return "error-----fallback : "+t;
    }
}
