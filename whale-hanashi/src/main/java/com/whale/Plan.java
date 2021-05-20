package com.whale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.function.Function;

@Builder
@Getter
@AllArgsConstructor
public class Plan implements Serializable {
    public interface MyFunc<T, R> extends Serializable, Function<T, R> {
    }

    public int start, end;
    public MyFunc func;

    static class PlanBuilder {
        public PlanBuilder setRange(int start, int end) {
            this.start = start;
            this.end = end;
            return this;
        }
    }
}
