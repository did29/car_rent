package com.FirstApp.demo.service;

import com.FirstApp.demo.model.Car;
import com.FirstApp.demo.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public List<Car> getTop3Cars() {
        return carRepository.findAll()
                .stream()
                .limit(3)
                .toList();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> searchCars(String brand) {
        return carRepository.findByBrandContainingIgnoreCase(brand);
    }

    public Car rentCar(Long id, String email) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобилът не е намерен"));

        if (!car.getAvailable()) {
            throw new RuntimeException("Автомобилът вече е нает");
        }

        car.setAvailable(false);
        car.setRentedBy(email);

        return carRepository.save(car);
    }

    public List<Car> getCarsRentedBy(String email) {
        return carRepository.findByRentedBy(email);
    }

    public Car returnCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобилът не е намерен"));

        car.setAvailable(true);
        car.setRentedBy(null);

        return carRepository.save(car);
    }
}