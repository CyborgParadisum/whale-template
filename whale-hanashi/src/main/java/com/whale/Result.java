package com.whale;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Result implements Serializable {
    Object result;
}
