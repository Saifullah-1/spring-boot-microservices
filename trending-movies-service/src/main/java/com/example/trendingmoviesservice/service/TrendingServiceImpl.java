package com.example.trendingmoviesservice.service;

import com.example.trendingmoviesservice.grpc.EmptyRequest;
import com.example.trendingmoviesservice.grpc.TrendingMovie;
import com.example.trendingmoviesservice.grpc.TrendingMoviesResponse;
import com.example.trendingmoviesservice.grpc.TrendingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class TrendingServiceImpl extends TrendingServiceGrpc.TrendingServiceImplBase {

    @Override
    public void getTopMovies(EmptyRequest request, StreamObserver<TrendingMoviesResponse> responseObserver) {
        
        System.out.println("Received gRPC request for Top Movies!");

        // In the final version, your teammate will query MySQL here. 
        // For now, we return mock data to prove the gRPC connection works.
        TrendingMovie movie1 = TrendingMovie.newBuilder().setMovieId("100").setAverageRating(4.9).build();
        TrendingMovie movie2 = TrendingMovie.newBuilder().setMovieId("200").setAverageRating(4.8).build();
        TrendingMovie movie3 = TrendingMovie.newBuilder().setMovieId("300").setAverageRating(4.7).build();

        // Build the final response list
        TrendingMoviesResponse response = TrendingMoviesResponse.newBuilder()
                .addMovies(movie1)
                .addMovies(movie2)
                .addMovies(movie3)
                .build();

        // Send the response back to the client (Catalog Service)
        responseObserver.onNext(response);
        
        // Tell the client we are done sending data
        responseObserver.onCompleted();
    }
}