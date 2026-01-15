package com.FirstApp.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Марка не може да бъде празна")
    @Column(nullable = false)
    private String brand;

    @NotEmpty(message = "Моделът не може да бъде празен")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Годината е задължителна")
    @Min(value = 2000, message = "Годината трябва да бъде след 2000")
    @Max(value = 2030, message = "Годината не може да бъде в бъдещето")
    @Column(name = "car_year", nullable = false)
    private Integer year;

    @NotEmpty(message = "Горивото не може да бъде празно")
    @Column(nullable = false)
    private String fuelType;

    @NotNull(message = "Цената на ден е задължителна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цената трябва да бъде положителна")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private Boolean available = true;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "rented_by")
    private String rentedBy;

    public Car() {
        this.createdAt = LocalDate.now();
    }

    public Car(String brand, String model, Integer year, String fuelType,
               BigDecimal pricePerDay, Boolean available) {
        this();
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.pricePerDay = pricePerDay;
        this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public String getRentedBy() { return rentedBy; }
    public void setRentedBy(String rentedBy) { this.rentedBy = rentedBy; }

    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ")";
    }
}
