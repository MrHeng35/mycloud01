package com.example.openfeign.feign;

import com.example.service.api.IBookController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("storage")
public interface BookService extends IBookController {
}
