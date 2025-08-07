// Hoang
package com.group3.xecare2.garage.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group3.xecare2.enums.GarageStatus;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.user.entities.User;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    
    // Tìm garage theo chủ sở hữu
    Optional<Garage> findByOwner(User owner);

    long countByIsVerifiedTrue();
    // Tìm garage theo email
    Optional<Garage> findByEmail(String email);
    
    // Tìm garage theo số điện thoại
    Optional<Garage> findByPhone(String phone);

    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // Tìm garage theo trạng thái
    List<Garage> findByStatus(GarageStatus status);
    
    // Tìm garage đã được xác thực
    List<Garage> findByIsVerifiedTrue();
    
    // Tìm garage theo tên (tìm kiếm mờ)
    List<Garage> findByNameContainingIgnoreCase(String name);
    
    // Tìm garage theo địa chỉ (tìm kiếm mờ)
    List<Garage> findByAddressContainingIgnoreCase(String address);
    
    // Tìm garage trong phạm vi bán kính (sử dụng công thức Haversine)
    @Query(value = "SELECT g.*, " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * " +
           "cos(radians(g.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(g.latitude)))) AS distance " +
           "FROM Garages g " +
           "WHERE g.status = 'ACTIVE' AND g.is_verified = true " +
           "HAVING distance <= :radius " +
           "ORDER BY distance", nativeQuery = true)
    List<Object[]> findNearbyGarages(@Param("latitude") Double latitude, 
                                    @Param("longitude") Double longitude, 
                                    @Param("radius") Double radius);
    
    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);
    
    // Kiểm tra số điện thoại đã tồn tại chưa
    boolean existsByPhone(String phone);
    
    // Đếm số garage theo trạng thái
    long countByStatus(GarageStatus status);
} 