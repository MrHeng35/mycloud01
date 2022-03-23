package com.example.business.controller;

import com.example.service.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class OrderController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;

    AtomicInteger count = new AtomicInteger(1);

    @GetMapping("/test")
    public void test() throws IOException {
        List<ServiceInstance> instanceList = discoveryClient.getInstances("storage");
        ServiceInstance storage = instanceList.get(count.getAndAdd(1)%instanceList.size());
        String storageHost = storage.getHost();
        int storagePort = storage.getPort();
        URL storageURL = new URL("http://" + storageHost + ":" + storagePort + "/deduct");
        HttpURLConnection connection = (HttpURLConnection) storageURL.openConnection();
        connection.connect();
        if (connection.getResponseCode()==200){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String s = bufferedReader.readLine();
            System.out.println("s = " + s);
            bufferedReader.close();
        }
    }

	//hello mrh
	//hello f1
    //new adasd
    // add login
	//hello master
    @GetMapping("/test2")
    public void test2(){
        System.out.println("===================deduct==================");
        String deduct = restTemplate.getForObject("http://storage/deduct", String.class);
        System.out.println("deduct = " + deduct);
        System.out.println("===================get==================");
        ResponseEntity<Book> entity = restTemplate.getForEntity("http://storage/book/?id={1}", Book.class, 1);

        Map<String, Object> param = new HashMap<>();
        param.put("id",1);
        ResponseEntity<Book> entity2 = restTemplate.getForEntity("http://storage/book/?id={id}", Book.class, param);
        Book getBody = entity2.getBody();
        System.out.println("getBody = " + getBody);
        int statusCodeValue = entity2.getStatusCodeValue();
        System.out.println("statusCodeValue = " + statusCodeValue);
        HttpStatus statusCode = entity2.getStatusCode();
        System.out.println("statusCode = " + statusCode);
        HttpHeaders headers = entity2.getHeaders();
        System.out.println("headers = " + headers);
        Set<String> sets = headers.keySet();
        for (String set : sets) {
            List<String> strings = headers.get(set);
            System.out.println(set+" : "+ strings);
        }
        System.out.println("===================post==================");
        MultiValueMap<String,Object> p = new LinkedMultiValueMap<>();
        p.add("id",2);
        p.add("name","三国演绎");
        p.add("author","罗贯中");
        Book addbook = restTemplate.postForObject("http://storage/book/", p, Book.class);
        System.out.println("addbook = " + addbook);
        System.out.println("===================post-location==================");
        URI uri = restTemplate.postForLocation("http://storage/book/add", addbook);
        System.out.println("uri = " + uri);
        System.out.println("===================put==================");
        restTemplate.put("http://storage/book/",addbook);
        System.out.println("===================delete==================");
        restTemplate.delete("http://storage/book/{1}",3);
    }
}
