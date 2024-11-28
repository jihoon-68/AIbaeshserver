package com.example.Baesh.Controller;

import com.example.Baesh.DTO.User.UserDTO;
import com.example.Baesh.Service.UserServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServcie userServcie;

    @GetMapping
    private List<UserDTO> userList(){
        return userServcie.getUserList();
    }
}
