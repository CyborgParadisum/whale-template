package com.whale.network.demo;

@FunctionalInterface
public interface Action {
    void apply(Object... object);
}
