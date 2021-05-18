package com.whale.network;

@FunctionalInterface
public interface Action {
    void apply(Object... object);
}
