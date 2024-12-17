package com.example.jaegerapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

@RestController
public class DemoController {

    private final Tracer tracer;

    public DemoController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/api/hello")
    public String sayHello() {
        Span span = tracer.spanBuilder("GET /api/hello").startSpan();
        try {
            span.addEvent("Processing request to /api/hello");
            Thread.sleep(500); // Simulating latency
            return "Hello, Jaeger is monitoring this API!";
        } catch (InterruptedException e) {
            span.recordException(e);
            return "Error occurred!";
        } finally {
            span.end();
        }
    }
}
