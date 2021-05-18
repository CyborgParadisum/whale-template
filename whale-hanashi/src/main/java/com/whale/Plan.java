package com.whale;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.function.Function;

@Builder
@Getter
public class Plan implements Serializable {
    public interface MyFunc<T, R> extends Serializable, Function<T, R> {
    }

    int start, end;
    public MyFunc func;

    static class PlanBuilder {
        public PlanBuilder setRange(int start, int end) {
            this.start = start;
            this.end = end;
            return this;
        }
    }
}
