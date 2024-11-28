package com.example.Baesh.DTO;

import com.example.Baesh.DTO.User.PostUserDTO;
import com.example.Baesh.DTO.User.UserDTO;
import com.example.Baesh.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private PostUserDTO userDTO;
    private LocalDateTime createdTime;
}
