public final class Pair<T, U> {
    private T head;
    private U tail;

    private Pair(T head, U tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <V, W> Pair<V, W> of(V head, W tail) {
        return new Pair<>(head, tail);
    }

    public T head() {
        return this.head;
    }

    public U tail() {
        return this.tail;
    }
}
