package com.FirstApp.demo.controller;

import com.FirstApp.demo.model.Car;
import com.FirstApp.demo.repository.CarRepository;
import com.FirstApp.demo.model.User;
import com.FirstApp.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CarRepository carRepository;
    private final UserService userService;

    @Autowired
    public AdminController(CarRepository carRepository, UserService userService) {
        this.carRepository = carRepository;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Администраторски панел");
        model.addAttribute("totalCars", carRepository.count());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("availableCars", carRepository.countByAvailableTrue());
        model.addAttribute("rentedCars", carRepository.countByAvailableFalse());
        return "admin/dashboard";
    }

    @GetMapping("/cars")
    public String manageCars(Model model) {
        model.addAttribute("title", "Управление на автомобили");
        model.addAttribute("cars", carRepository.findAll());
        return "admin/cars";
    }

    @GetMapping("/cars/new")
    public String addCarForm(Model model) {
        model.addAttribute("title", "Добавяне на нов автомобил");
        model.addAttribute("car", new Car());
        return "admin/car-form";
    }

    @GetMapping("/cars/edit/{id}")
    public String editCarForm(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобилът не е намерен"));

        model.addAttribute("title", "Редактиране на автомобил");
        model.addAttribute("car", car);
        return "admin/car-form";
    }

    @PostMapping("/cars/save")
    public String saveCar(@Valid @ModelAttribute Car car, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", car.getId() == null ? "Добавяне на нов автомобил" : "Редактиране на автомобил");
            return "admin/car-form";
        }

        carRepository.save(car);
        return "redirect:/admin/cars";
    }

    @GetMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/admin/cars";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("title", "Управление на потребители");
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        var user = userService.getUserById(id);
        if (user == null) throw new RuntimeException("Потребителят не е намерен");

        model.addAttribute("title", "Редактиране на потребител");
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("title", "Добавяне на потребител");
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getId() == null) { userService.createUser(user); }
        else { userService.updateUser(user); }
        return "redirect:/admin/users";
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

}