package com.FirstApp.demo.controller;

import com.FirstApp.demo.model.Car;
import com.FirstApp.demo.model.User;
import com.FirstApp.demo.repository.CarRepository;
import com.FirstApp.demo.service.CarService;
import com.FirstApp.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final CarRepository carRepository;
    private final UserService userService;
    private final CarService carService;

    @Autowired
    public MainController(CarRepository carRepository, UserService userService, CarService carService) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.carService = carService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Начало");

        List<Car> allCars = carRepository.findAll();
        model.addAttribute("featuredCars", allCars.stream().limit(3).toList());
        return "index";
    }

    @GetMapping("/cars")
    public String cars(@RequestParam(value = "brand", required = false) String brand, Model model) {
        List<Car> cars;

        if (brand != null && !brand.isEmpty()) {
            cars = carRepository.findByBrandContainingIgnoreCase(brand);
            model.addAttribute("selectedBrand", brand);
        } else {
            cars = carRepository.findAll();
        }

        model.addAttribute("title", "Каталог с автомобили");
        model.addAttribute("cars", cars);
        return "cars";
    }

    @GetMapping("/cars/search")
    public String searchCars(@RequestParam String query, Model model) {

        Integer year = null;
        try {
            year = Integer.parseInt(query);
        } catch (NumberFormatException ignored) {}

        List<Car> results = carRepository
                .findByBrandContainingIgnoreCaseOrModelContainingIgnoreCaseOrFuelTypeContainingIgnoreCaseOrYear(
                        query, query, query, year
                );
        model.addAttribute("title", "Резултати от търсенето за: " + query);
        model.addAttribute("cars", results);
        model.addAttribute("searchQuery", query);

        return "cars";
    }

    @PostMapping("/cars/rent/{id}")
    public String rentCar(@PathVariable Long id,
                          Principal principal,
                          RedirectAttributes redirectAttributes) {

        String email = principal.getName();

        carService.rentCar(id, email);

        redirectAttributes.addFlashAttribute("message", "Успешно наехте автомобила!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/cars";
    }

    @GetMapping("/my-rentals")
    public String myRentals(Model model, Principal principal) {

        String email = principal.getName();

        model.addAttribute("cars", carService.getCarsRentedBy(email));
        model.addAttribute("title", "Моите наеми");

        return "my-rentals";
    }

    @PostMapping("/cars/return/{id}")
    public String returnCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        carService.returnCar(id);

        redirectAttributes.addFlashAttribute("message", "Успешно върнахте автомобила!");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/my-rentals";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "За нас");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Контакти");
        return "contact";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute User user,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Регистрация");
            return "register";
        }

        try {
            user.setRole("USER");
            userService.createUser(user);

            model.addAttribute("message", "Регистрацията е успешна! Можете да влезете в системата.");
            model.addAttribute("messageType", "success");

            return "login";

        } catch (RuntimeException e) {

            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "error");
            model.addAttribute("title", "Регистрация");

            return "register";
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Вход в системата");
        return "login";
    }
}