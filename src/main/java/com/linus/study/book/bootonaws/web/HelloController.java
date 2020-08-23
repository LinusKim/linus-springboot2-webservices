package com.linus.study.book.bootonaws.web;

import com.linus.study.book.bootonaws.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final String helloMessage = "hello";

    @GetMapping("/hello")
    public String hello() {
        return helloMessage;
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam String name, @RequestParam int amount) {
        return new HelloResponseDto(name, amount);
    }

}
