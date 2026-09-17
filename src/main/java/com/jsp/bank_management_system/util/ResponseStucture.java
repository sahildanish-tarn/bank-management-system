package com.jsp.bank_management_system.util;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"message","localDateTime","statuscode","data"})
public class ResponseStucture<T> {
    private LocalDateTime localDateTime;
    private String message;
    private T data;
    private int statuscode;

}

