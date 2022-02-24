package com.github.samples.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class GreetingRepository {

    @Cacheable("greetings")
    public String getGreeting(String name) {
        return "Hello " + name + "!";
    }


}
