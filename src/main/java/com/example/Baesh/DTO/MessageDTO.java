package com.example.Baesh.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private Long ChatRoomId;
    private Long userId;
    private String content;
    private LocalDateTime sentTime;
}
