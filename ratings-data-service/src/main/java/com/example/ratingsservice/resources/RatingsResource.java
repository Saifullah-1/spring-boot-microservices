package com.example.ratingsservice.resources;

import com.example.ratingsservice.models.UserRating;
import com.example.ratingsservice.repositories.RatingRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {
    private final RatingRepository ratingRepository;

    public RatingsResource(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @RequestMapping("/{userId}")
    public UserRating getRatingsOfUser(@PathVariable String userId) {
        return new UserRating(ratingRepository.findAllByUserId(userId));
    }
}