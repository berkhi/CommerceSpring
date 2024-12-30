package com.CommerceSpring.entity;

import com.CommerceSpring.utilty.enums.EStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_auths")
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(unique = true)
    private String email;
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EStatus status=EStatus.PENDING;

}