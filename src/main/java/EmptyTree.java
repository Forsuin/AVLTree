import java.util.Optional;

record EmptyTree<T extends Comparable<T>>() implements BSTree<T> {

    private static final EmptyTree _theOneAndOnlySingletonInstance = new EmptyTree();

    @SuppressWarnings("unchecked")
    static <T extends Comparable<T>> EmptyTree<T> getInstance() {
        return (EmptyTree<T>) _theOneAndOnlySingletonInstance;
    }

    public long size( /* EmptyTree this, */ ) { return 0L; }

    public Node<T> insert( /* Node this, */ T newItem ) { return new Node<T>(newItem); }

    public BSTree<T> delete( /* EmptyTree this, */ T target, boolean targetNeedNotOccur  ) {
        if (targetNeedNotOccur) {
            return EmptyTree.getInstance();
        }
        else {
            throw new RuntimeException( "Tried deleting " + target + " but it wasn't in tree.\n"
                    + "  If deleting a non-existent-item is expected,\n"
                    + "  call delete with targetNeedNotOccur==true." );
        }
    }

    @Override
    public Optional<BSTree<T>> min() {
        return Optional.empty();
    }
}