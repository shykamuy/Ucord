package com.example.ucord_personal_account_service.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private Long id;

    private LocalDateTime timestamp;

    private String message;

    private UUID userId;

    private Long appealId;

}
