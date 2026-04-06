// package com.moviecatalogservice.resources;

// import com.moviecatalogservice.models.CatalogItem;
// import com.moviecatalogservice.models.Movie;
// import com.moviecatalogservice.models.Rating;
// import com.moviecatalogservice.models.UserRating;
// import com.moviecatalogservice.services.MovieInfoService;
// import com.moviecatalogservice.services.UserRatingService;
// import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

// import java.util.Collections;
// import java.util.List;
// import java.util.Objects;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/catalog")
// public class MovieCatalogResource {

//     private final RestTemplate restTemplate;

//     private final MovieInfoService movieInfoService;

//     private final UserRatingService userRatingService;

//     public MovieCatalogResource(RestTemplate restTemplate,
//                                 MovieInfoService movieInfoService,
//                                 UserRatingService userRatingService) {

//         this.restTemplate = restTemplate;
//         this.movieInfoService = movieInfoService;
//         this.userRatingService = userRatingService;
//     }

//     /**
//      * Makes a call to MovieInfoService to get movieId, name and description,
//      * Makes a call to RatingsService to get ratings
//      * Accumulates both data to create a MovieCatalog
//      * @param userId
//      * @return CatalogItem that contains name, description and rating
//      */
//     @RequestMapping("/{userId}")
//     public List<CatalogItem> getCatalog(@PathVariable String userId) {
//         List<Rating> ratings = userRatingService.getUserRating(userId).getRatings();
//         return ratings.stream().map(movieInfoService::getCatalogItem).collect(Collectors.toList());
//     }
// }

package com.moviecatalogservice.resources;

import com.moviecatalogservice.grpc.EmptyRequest;
import com.moviecatalogservice.grpc.TrendingMoviesResponse;
import com.moviecatalogservice.grpc.TrendingServiceGrpc;
import com.moviecatalogservice.models.CatalogItem;
import com.moviecatalogservice.models.Rating;
import com.moviecatalogservice.services.MovieInfoService;
import com.moviecatalogservice.services.UserRatingService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;
    private final MovieInfoService movieInfoService;
    private final UserRatingService userRatingService;

    // gRPC Stub for communicating with your Trending Service
    private TrendingServiceGrpc.TrendingServiceBlockingStub trendingStub;

    public MovieCatalogResource(RestTemplate restTemplate,
                                MovieInfoService movieInfoService,
                                UserRatingService userRatingService) {
        this.restTemplate = restTemplate;
        this.movieInfoService = movieInfoService;
        this.userRatingService = userRatingService;
    }

    /**
     * Initializes the gRPC connection after the bean is constructed.
     */
    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        this.trendingStub = TrendingServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Original REST flow: Gets catalog for a specific user
     */
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        List<Rating> ratings = userRatingService.getUserRating(userId).getRatings();
        return ratings.stream().map(movieInfoService::getCatalogItem).collect(Collectors.toList());
    }

    /**
     * NEW gRPC flow: Gets top 10 movies from the Trending Service
     */
    @RequestMapping("/trending")
    public List<CatalogItem> getTrending() {
        // 1. Call the gRPC server (Trending Service)
        TrendingMoviesResponse response = trendingStub.getTopMovies(EmptyRequest.newBuilder().build());

        // 2. Map the gRPC response objects to CatalogItems
        return response.getMoviesList().stream()
                .map(movie -> new CatalogItem(
                        "Movie ID: " + movie.getMovieId(),
                        "Trending Score: " + movie.getAverageRating(),
                        0))
                .collect(Collectors.toList());
    }
}
