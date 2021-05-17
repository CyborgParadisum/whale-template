package network;

@FunctionalInterface
public interface Action {
    void apply(Object... object);
}
