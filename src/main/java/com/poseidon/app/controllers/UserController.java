package com.poseidon.app.controllers;

import com.poseidon.app.dto.UserDto;
import com.poseidon.app.exceptions.UserAlreadyExistException;
import com.poseidon.app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.getAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(UserDto userDto) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid UserDto userDto, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            UserDto userSave = new UserDto(userDto.id(), 
                                             userDto.username(),
                                             encoder.encode(userDto.password()),
                                             userDto.fullname(),
                                             userDto.role());
            try {
                userService.save(userSave);
                return "redirect:/user/list";
            }catch (UserAlreadyExistException e) {
                model.addAttribute("alreadyExist", "The user already exist");
                return "user/add";
            }
            
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        UserDto userDto = userService.getUserById(id);
        UserDto userDtoSave = new UserDto(userDto.id(),
                userDto.username(),
                "",
                userDto.fullname(),
                userDto.role());
        model.addAttribute("userDto", userDtoSave);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable Integer id, @Valid UserDto userDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDto userDtoSave = new UserDto(id,
                                        userDto.username(),
                                        encoder.encode(userDto.password()),
                                        userDto.fullname(),
                                        userDto.role());
        try {
            userService.save(userDtoSave);
            return "redirect:/user/list";
        }catch (UserAlreadyExistException e) {
            model.addAttribute("alreadyExist", "The user already exist")
                    .addAttribute("id", id);
            return "/user/update";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        userService.deleteUserById(id);
        return "redirect:/user/list";
    }
}
