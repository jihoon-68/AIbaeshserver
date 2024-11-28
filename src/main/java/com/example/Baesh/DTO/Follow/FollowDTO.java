package com.example.Baesh.DTO.Follow;

import com.example.Baesh.DTO.User.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO {
    private UserDTO follower;
    private UserDTO following;
    private LocalDateTime followTime;
}
