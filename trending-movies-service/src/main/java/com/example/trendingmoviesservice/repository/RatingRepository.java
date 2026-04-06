package com.example.trendingmoviesservice.repository;

import com.example.trendingmoviesservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query(value = "SELECT movie_id, AVG(rating) as avg_rate " +
            "FROM rating GROUP BY movie_id " +
            "ORDER BY avg_rate DESC LIMIT 10",
            nativeQuery = true)
    List<Object[]> findTopMovies();
}
