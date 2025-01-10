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
public class SaveUserFromAuthModel {
    private static final long serialVersionUID = 1L;
    private UUID authId;
    private String firstName;
    private String lastName;
}
