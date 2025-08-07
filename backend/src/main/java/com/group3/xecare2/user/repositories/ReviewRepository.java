package com.group3.xecare2.user.repositories;

import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.user.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findAverageRating();

    List<Review> findByGarage(Garage garage);
    List<Review> findByGarageId(Long id);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.createdAt BETWEEN :from AND :to")
    Double getAverageRatingBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
