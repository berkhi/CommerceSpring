package com.CommerceSpring.RabbitMQ.Model;


import com.CommerceSpring.utilty.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateStatusModel {
    private UUID authId;
    private EStatus status;
}
