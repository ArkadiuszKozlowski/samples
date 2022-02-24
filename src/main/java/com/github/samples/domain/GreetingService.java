package com.github.samples.domain;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class GreetingService {

    private final GreetingRepository repository;

    @Async
    public CompletableFuture<String> greeting(String name) {
        log.info("Greeting needed for: {}!", name);
        timeConsumingOperations();
        return CompletableFuture.completedFuture(repository.getGreeting(name));
    }

    @SneakyThrows
    private void timeConsumingOperations() {
        Thread.sleep(4000);
    }
}
