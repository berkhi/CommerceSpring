package com.CommerceSpring.RabbitMQ.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQNotification implements Serializable {
    private List<UUID> authIds;
    private String message;
    private String title;
}

