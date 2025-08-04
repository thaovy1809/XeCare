package com.group3.xecare2.user.repositories;

import com.group3.xecare2.user.entities.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findAverageRating();
}
