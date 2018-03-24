package calculator.util;

public class Pair<T> {
    private final T a;
    private final T b;

    private Pair(T a, T b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Pair c = (Pair) o;
        return this.a.equals(c.a) && this.b.equals(c.b);
    }

    @Override
    public int hashCode() {
        return this.a.hashCode() + this.b.hashCode();
    }

    /**
     * Creates an ordered pair.
     * @param a An item.
     * @param b An item.
     * @param <T> Type of elements.
     * @return A pair containing supplied items.
     */
    public static <T> Pair<T> of(T a, T b) {
        return new Pair<>(a, b);
    }
}
