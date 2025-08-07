package com.group3.xecare2.garage.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "garage_service_types") // đặt rõ tên bảng
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//bảng này dùng để phân loại các loại cate của dịch vụ của garage, ví dụ: bảo trì, sửa chữa, cứu hộ v.v.
public class GarageServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "garageServiceType") // trỏ ngược lại field trong GarageService
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private List<GarageService> garageServices;
}
