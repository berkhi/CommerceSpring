package com.CommerceSpring.RabbitMQ.Model;

import com.CommerceSpring.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerSaveFromUserModel {
    private Long authId;
    private Long userId;
    private String firstName;
    private String lastName;
    private EStatus status;

}
