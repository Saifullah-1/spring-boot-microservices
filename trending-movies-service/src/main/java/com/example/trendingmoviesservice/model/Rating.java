package com.example.trendingmoviesservice.model;

import javax.persistence.*;

@Entity
@Table(name = "rating", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "movieId"})
})
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userId;
    private String movieId;
    private int rating;

    public Rating() {
    }

    public Rating(String movieId, int rating) {
        this.movieId = movieId;
        this.rating = rating;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}