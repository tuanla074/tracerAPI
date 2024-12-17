package com.example.jaegerapi;

import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerConfig {

    @Bean
    public OpenTelemetry openTelemetry() {
        // Configure Jaeger Exporter
        JaegerGrpcSpanExporter jaegerExporter = JaegerGrpcSpanExporter.builder()
                .setEndpoint("http://localhost:14250") // Jaeger gRPC endpoint
                .build();

        // Set up Batch Span Processor and Tracer Provider
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .build();
    }

    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        // Return OpenTelemetry Tracer
        return openTelemetry.getTracer("com.example.jaegerapi");
    }
}
