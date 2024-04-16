import javax.swing.text.html.Option;
import java.util.Optional;

public record Node<T extends Comparable<T>>(T datum, BSTree<T> l, BSTree<T> r ) implements BSTree<T> {

    // a convenience constructor:
    Node( T _datum ) {
        this( _datum, EmptyTree.getInstance(), EmptyTree.getInstance() );
    }

    public long size( /* Node this, */ ) {
        return this.l.size() + 1L + this.r.size();
    }


    public Node<T> insert( /* Node this, */ T newItem ) {
        return (newItem.compareTo( this.datum ) < 0)
                ? new Node<T>(  this.datum(),  this.l().insert(newItem),  this.r() )
                : new Node<T>(  this.datum(),  this.l(),  this.r().insert(newItem) );
        // mutating version: this.r = this.r().insert(newItem);
    }


    public BSTree<T> delete( /* Node this, */ T target, boolean targetNeedNotOccur ) {

        return switch(this) {
            // First, navigate down to target:
            case Node( T d, BSTree<T> l, BSTree<T> r ) when target.compareTo(d) < 0 -> new Node<T>(d, l.delete(target, targetNeedNotOccur), r);
            case Node( T d, BSTree<T> l, BSTree<T> r ) when target.compareTo(d) > 0 -> new Node<T>(d, l, r.delete(target, targetNeedNotOccur) );
            // mutating version would be very similar: Just assign the recursive-result, rather than call a constructor with it:
            // case Node( T d, BSTree l, BSTree r ) when target.compareTo(d) < 0 ->  this.l = l.delete(target, targetNeedNotOccur);

            // When target found (and not empty), look at cases:
            case Node( T d, EmptyTree(), EmptyTree() ) /* when target.compareTo(d) == 0 */ -> EmptyTree.getInstance();
            case Node( T d, Node<T> l, EmptyTree() ) /* when target.compareTo(d) == 0 */ -> l;
            case Node( T d, EmptyTree(), Node<T> r) /* when target.compareTo(d) == 0 */ -> r;

            // "difficult" case: found target; both children non-empty.
            // We'll just move the largest value on left to the root, and delete it from left.
            case Node( T d, Node<T> _l, Node<T> _r ) /* when target.compareTo(d) == 0 */ -> {
                BSTree<T> toDelete = r.min().orElse(EmptyTree.getInstance());

                BSTree<T> newRight;

                if(toDelete.equals(EmptyTree.getInstance())) {
                    newRight = toDelete;
                }
                else {
                    newRight = r.delete(((Node<T>)toDelete).datum());
                }

                // get() without checking is fine because this case is only when this node has two Node children
                yield new Node<>(((Node<T>)r.min().get()).datum,
                            l,
                            newRight);
            }
        };
    }

    public Optional<BSTree<T>> min() {
        var leftMin = this.l.min();

        // make a new copy of just this node, don't want to give the entire subtree, just the minimum value
        if(leftMin.isEmpty()) {
            return Optional.of(new Node<T>(this.datum));
        }

        return leftMin;
    }
}