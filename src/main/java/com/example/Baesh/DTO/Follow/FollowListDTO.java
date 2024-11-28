package com.example.Baesh.DTO.Follow;

import com.example.Baesh.DTO.User.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowListDTO {
    private List<UserDTO> followerList;
    private List<UserDTO> followingList;

}
