package reactive.common;

/**
 * simple pair class
 * @param <L>
 * @param <R>
 *
 */
public class Pair <L, R> {

    L left;
    R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public static <L, R> Pair<L,R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
