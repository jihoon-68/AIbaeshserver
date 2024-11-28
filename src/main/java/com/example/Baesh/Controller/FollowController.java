package com.example.Baesh.Controller;

import com.example.Baesh.DTO.Follow.FollowDTO;
import com.example.Baesh.DTO.Follow.FollowListDTO;
import com.example.Baesh.Interface.FollowRepository;
import com.example.Baesh.Service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping("/{id}")
    public FollowListDTO followList (@PathVariable("id") Long userId){
        return followService.Follows(userId);
    }

    @PostMapping("/follower")
    public void CreateFollower(@RequestBody FollowDTO followDTO){
        followService.follow(followDTO.getFollower().getId(),followDTO.getFollowing().getId());
    }

    @DeleteMapping
    public void deleteFollower(@RequestBody FollowDTO followDTO){
        System.out.println(followDTO);
        followService.deleteFollow(followDTO.getFollower().getId(),followDTO.getFollowing().getId());
    }

}
