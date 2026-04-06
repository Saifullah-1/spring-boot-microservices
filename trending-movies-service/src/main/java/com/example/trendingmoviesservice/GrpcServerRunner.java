package com.example.trendingmoviesservice;

import com.example.trendingmoviesservice.service.TrendingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerRunner implements CommandLineRunner {

    private final TrendingServiceImpl trendingService;

    public GrpcServerRunner(TrendingServiceImpl trendingService) {
        this.trendingService = trendingService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the gRPC server on port 9090
        Server server = ServerBuilder.forPort(9090)
                .addService(trendingService)
                .build();

        server.start();
        System.out.println("=========================================");
        System.out.println("gRPC Server started successfully on port 9090!");
        System.out.println("=========================================");
        
        // This keeps the gRPC server alive as long as the Spring app is running
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server...");
            server.shutdown();
        }));
    }
}