package com.jsp.bank_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsp.bank_management_system.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator =  "user_seq")
    @SequenceGenerator(allocationSize = 1,initialValue = 1,name = "user_seq")
    private Long  id;

    @Column( nullable = false, unique = true)
    private String username;

    @Column( nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status=UserStatus.Active;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Customer customer;

}
