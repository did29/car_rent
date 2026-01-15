package com.FirstApp.demo.controller;

import com.FirstApp.demo.model.Car;
import com.FirstApp.demo.model.User;
import com.FirstApp.demo.repository.CarRepository;
import com.FirstApp.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    private final CarRepository carRepository;
    private final UserService userService;

    @Autowired
    public UserController(CarRepository carRepository, UserService userService) {
        this.carRepository = carRepository;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Потребител не е намерен");
        }

        model.addAttribute("title", "Моят профил");
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/rent")
    public String rentCars(Model model) {
        model.addAttribute("title", "Наемане на автомобили");
        model.addAttribute("cars", carRepository.findByAvailableTrue());
        return "user/rent";
    }

    @PostMapping("/rent/{id}")
    public String rentCar(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобилът не е намерен"));

        if (!car.getAvailable()) {
            model.addAttribute("message", "Автомобилът вече е нает!");
            model.addAttribute("messageType", "error");
        } else {
            car.setAvailable(false);
            carRepository.save(car);
            model.addAttribute("message", "Автомобилът е нает успешно!");
            model.addAttribute("messageType", "success");
        }

        model.addAttribute("cars", carRepository.findByAvailableTrue());
        return "user/rent";
    }

    @PostMapping("/return/{id}")
    public String returnCar(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобилът не е намерен"));

        car.setAvailable(true);
        carRepository.save(car);

        model.addAttribute("message", "Автомобилът е върнат успешно!");
        model.addAttribute("messageType", "success");
        model.addAttribute("cars", carRepository.findByAvailableTrue());
        return "user/rent";
    }

    @GetMapping("/search")
    public String searchCars(@RequestParam String query, Model model) {
        model.addAttribute("title", "Търсене на автомобили");
        model.addAttribute("cars", carRepository.findByBrandContainingIgnoreCase(query));
        model.addAttribute("searchQuery", query);
        return "user/rent";
    }
}