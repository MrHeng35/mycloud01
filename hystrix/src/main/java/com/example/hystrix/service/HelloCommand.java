package com.example.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

public class HelloCommand extends HystrixCommand<String> {

    RestTemplate restTemplate;

    public HelloCommand(RestTemplate restTemplate) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("zhangsan")));
        this.restTemplate = restTemplate;
    }

    /**
     * 发起请求的地方
     *
     * @return
     * @throws Exception
     */
    @Override
    protected String run() throws Exception {
        int i = 1 / 0;
        return restTemplate.getForObject("http://storage/deduct", String.class);
    }

    /**
     * 这是服务降级的方法，即 run 方法执行失败的时候，会自动触发该方法的执行
     *
     * @return
     */
    @Override
    protected String getFallback() {
        Throwable executionException = getExecutionException();
        return "error---->hello hystrix command : "+executionException;
    }
}
