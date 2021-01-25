package com.example.androiddemo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RestResponse {
    private int code;
    private Object data;
}
