package com.CommerceSpring.RabbitMQ.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SaveUserFromAuthModel {
    private static final long serialVersionUID = 1L;
    private Long authId;
    private String firstName;
    private String lastName;
}
