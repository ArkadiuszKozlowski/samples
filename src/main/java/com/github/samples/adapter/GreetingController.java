package com.github.samples.adapter;

import com.github.samples.domain.GreetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingService greetingService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/greet")
    public CompletableFuture<String> greet(@RequestParam("name") String name) {
        return greetingService.greeting(name);
    }

}
