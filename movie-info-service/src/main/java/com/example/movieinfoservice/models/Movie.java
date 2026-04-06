package com.example.movieinfoservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "movie_cache")
public class Movie {
    @Id
    private String movieId;

    private String name;
    private String description;
    private LocalDateTime cachedAt;

    public Movie() {
    }

    public Movie(String movieId, String name, String description, LocalDateTime cachedAt) {
        this.movieId = movieId;
        this.name = name;
        this.description = description;
        this.cachedAt = cachedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCachedAt() {
        return cachedAt;
    }

    public void setCachedAt(LocalDateTime cachedAt) {
        this.cachedAt = cachedAt;
    }
}