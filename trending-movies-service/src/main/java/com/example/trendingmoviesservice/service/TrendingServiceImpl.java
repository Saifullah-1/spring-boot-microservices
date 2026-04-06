package com.example.trendingmoviesservice.service;

import com.example.trendingmoviesservice.grpc.EmptyRequest;
import com.example.trendingmoviesservice.grpc.TrendingMovie;
import com.example.trendingmoviesservice.grpc.TrendingMoviesResponse;
import com.example.trendingmoviesservice.grpc.TrendingServiceGrpc;
import com.example.trendingmoviesservice.repository.RatingRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendingServiceImpl extends TrendingServiceGrpc.TrendingServiceImplBase {
    private final RatingRepository ratingRepository;

    public TrendingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void getTopMovies(EmptyRequest request, StreamObserver<TrendingMoviesResponse> responseObserver) {

        System.out.println("Received gRPC request for Top Movies from MySQL!");

        List<Object[]> topMovies = ratingRepository.findTopMovies();

        TrendingMoviesResponse.Builder responseBuilder = TrendingMoviesResponse.newBuilder();

        for (Object[] topMovie : topMovies) {
            TrendingMovie movie = TrendingMovie.newBuilder()
                    .setMovieId(topMovie[0].toString())
                    .setAverageRating(Double.parseDouble(topMovie[1].toString()))
                    .build();

            responseBuilder.addMovies(movie);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}