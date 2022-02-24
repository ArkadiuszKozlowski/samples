package com.github.samples


import org.springframework.boot.SpringApplication
import org.springframework.cache.CacheManager
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.util.concurrent.CyclicBarrier
import java.util.concurrent.atomic.AtomicReference

class GracefulShutdownIT extends Specification {

    def "should use hazelcast cached value after shutdown initiation"() {
        given: "setup application"
        def client = WebTestClient.bindToServer().baseUrl("http://localhost:8080/").build()
        def appContext = SpringApplication.run(GracefulShutdownApplication.class, new String[]{})
        def cacheManager = appContext.getBean(CacheManager)

        and: "setup test specific environment"
        def name = "John"
        def cachedValue = "Hello stranger"
        cacheManager.getCache("greetings").put(name, cachedValue)

        def barrier = new CyclicBarrier(3)
        def result = new AtomicReference<String>()

        when:
        runWithBarrier((Runnable) {
            def response = client.get().uri("/greet?name=${name}")
                    .exchange().expectBody().returnResult().getResponseBody()
            result.set(new String(response))
        }, barrier)

        runWithBarrier((Runnable) {
            Thread.sleep(1000)
            SpringApplication.exit(appContext)
        }, barrier)

        barrier.await()

        then:
        assert result.get() == cachedValue
    }

    void runWithBarrier(Runnable runnable, CyclicBarrier barrier) {
        new Thread((Runnable) {
            try {
                runnable.run()
            } finally {
                barrier.await()

            }
        }).start()
    }
}
