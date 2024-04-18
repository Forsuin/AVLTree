import javax.swing.text.html.Option;
import java.util.Optional;

public record Node<T extends Comparable<T>>(T datum, BSTree<T> l, BSTree<T> r, int height) implements BSTree<T> {

    // a convenience constructor:
    Node( T _datum ) {
        this( _datum, EmptyTree.getInstance(), EmptyTree.getInstance() );
    }
    Node( T _datum, BSTree<T> l, BSTree<T> r) {
        this(_datum, l, r, Math.max(l.height(), r.height()) + 1);
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
            case Node(T d, BSTree<T> l, BSTree<T> r, int __) when target.compareTo(d) < 0 -> new Node<T>(d, l.delete(target, targetNeedNotOccur), r);
            case Node( T d, BSTree<T> l, BSTree<T> r, int __) when target.compareTo(d) > 0 -> new Node<T>(d, l, r.delete(target, targetNeedNotOccur) );
            // mutating version would be very similar: Just assign the recursive-result, rather than call a constructor with it:
            // case Node( T d, BSTree l, BSTree r ) when target.compareTo(d) < 0 ->  this.l = l.delete(target, targetNeedNotOccur);

            // When target found (and not empty), look at cases:
            case Node( T d, EmptyTree(), EmptyTree(), int __) /* when target.compareTo(d) == 0 */ -> EmptyTree.getInstance();
            case Node( T d, Node<T> l, EmptyTree(), int __) /* when target.compareTo(d) == 0 */ -> l;
            case Node( T d, EmptyTree(), Node<T> r, int __) /* when target.compareTo(d) == 0 */ -> r;

            // "difficult" case: found target; both children non-empty.
            // We'll just move the largest value on left to the root, and delete it from left.
            case Node( T d, Node<T> _l, Node<T> _r, int __) /* when target.compareTo(d) == 0 */ -> {
                BSTree<T> toDelete = r.min().get();

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

    public int height() {
        return this.height;
    }

    @Override
    public boolean isValid() {
        return switch(this) {
            case Node(T d, EmptyTree(), EmptyTree(), int h) when h == 1 -> true;
            case Node(T d, Node<T> l, EmptyTree(), int h) -> l.datum.compareTo(d) < 0 && l.isValid() && l.height() + 1 == h;
            case Node(T d, EmptyTree(), Node<T> r, int h) -> r.datum.compareTo(d) > 0 && r.isValid() && r.height() + 1 == h;
            case Node(T d, Node<T> l, Node<T> r, int h) -> {
                boolean leftValid = l.isValid() && l.datum().compareTo(d) < 0;;
                boolean rightValid = r.isValid() && r.datum().compareTo(d) > 0;
                yield leftValid && rightValid && (Math.max(l.height(), r.height()) + 1 == h);
            }
            default -> throw new IllegalStateException("Unexpected value, invalid BSTree implementor: " + this);
        };
    }

    public Node<T> rotateLeft() {
        if(this.l.equals(EmptyTree.getInstance()) && this.r.equals(EmptyTree.getInstance())) {
            throw new IllegalArgumentException("Both children of node are 'EmptyTree's: " + this);
        }
        else if(this.r.equals(EmptyTree.getInstance())) {
            throw new IllegalArgumentException("Right child is empty, unable to rotateLeft(): " + this);
        }

        Node<T> root = this;
        Node<T> right = (Node<T>) this.r;

        return new Node<>(right.datum, new Node<>(root.datum, root.l, right.l), right.r);
    }

    public Node<T> rotateRight() {
        if(this.l.equals(EmptyTree.getInstance()) && this.r.equals(EmptyTree.getInstance())) {
            throw new IllegalArgumentException("Both children of node are 'EmptyTree's: " + this);
        }
        else if(this.l.equals(EmptyTree.getInstance())) {
            throw new IllegalArgumentException("Left child is empty, unable to rotateRight(): " + this);
        }

        Node<T> root = this;
        Node<T> left = (Node<T>) this.l;

        return new Node<>(left.datum, left.l, new Node<>(root.datum, left.r, root.r));
    }
}