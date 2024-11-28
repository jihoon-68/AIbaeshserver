package com.example.Baesh.Controller;

import com.example.Baesh.DTO.Oauth.AccountDTO;
import com.example.Baesh.Entity.UserEntity;
import com.example.Baesh.Service.UserServcie;
import io.micrometer.core.ipc.http.HttpSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/oauth")
public class AccountController {

    @Autowired
    UserServcie userServcie;

    @GetMapping("/user/info")
    public ResponseEntity getUserInfo(Principal principal){
        if(principal == null){
            System.out.println("No user is authenticated.");
            return ResponseEntity.status(401).body("No user authenticated");
        }

        UserEntity user = userServcie.getUser(Long.valueOf(principal.getName()));

        if(user == null){
            System.out.println("User not found.");
            return ResponseEntity.status(404).body("User not found");
        }

        System.out.println("User Info: " + user.getEmail());

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUser(userServcie.userEntityToDTO(user));
        accountDTO.setEmail(user.getEmail());
        accountDTO.setFirstName(user.getFirstName());
        accountDTO.setLastName(user.getLastName());
        accountDTO.setPictureUrl(user.getPictureUrl());


        return ResponseEntity.ok().body(accountDTO);
    }
}
