package com.FirstApp.demo.repository;
import java.util.List;
import com.FirstApp.demo.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    long countByAvailableTrue();
    long countByAvailableFalse();

    List<Car> findByAvailableTrue();

    List<Car> findByBrandContainingIgnoreCase(String brand);

    List<Car> findByRentedBy(String username);

    List<Car> findByFuelTypeContainingIgnoreCase(String fuelType);

    List<Car> findByBrandContainingIgnoreCaseOrModelContainingIgnoreCaseOrFuelTypeContainingIgnoreCaseOrYear(
            String brand, String model, String fuelType, Integer year
    );
}

