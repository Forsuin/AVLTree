import java.util.Optional;

public sealed interface BSTree<T extends Comparable<T>> permits EmptyTree, Node {
    long size( /* BSTree this */ );
    Node<T> insert( /* BSTree this, */ T newItem );

    // Default argument: targetNeedNotOccur = false (for safety)
    default BSTree<T> delete( /* BSTree this, */ T target ) { return this.delete(target, false); }
    BSTree<T> delete( T target, boolean targetNeedNotOccur  );

    public Optional<BSTree<T>> min();
}
