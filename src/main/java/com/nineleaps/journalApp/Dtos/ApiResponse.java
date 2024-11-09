package com.nineleaps.journalApp.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;
    private boolean success;


    public ApiResponse(String message, boolean success){
        this.message=message;
        this.success=success;
    }

}
