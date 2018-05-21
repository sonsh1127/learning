package reactive.ch3;

/**
 * simple pair class
 * @param <L>
 * @param <R>
 *
 */
class Pair <L, R> {

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

    static <L, R> Pair<L,R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
