package com.CommerceSpring.RabbitMQ.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmailVerificationModel {
    private UUID authId;
    private String email;
    private String firstName;
    private String lastName;
}
