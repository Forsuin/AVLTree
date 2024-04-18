import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BSTreeTest {

    @Test
    void testSize() {
        EmptyTree mt = EmptyTree.getInstance();
        assertEquals(0, mt.size());
        assertEquals(1, new Node(27).size());
        assertEquals(2, new Node(27, new Node(11), mt).size());
        assertEquals(2, new Node(27, mt, new Node(83)).size());
        assertEquals(3, new Node(27, new Node(11), new Node(83)).size());

       /*     27
             /  \
            11   83
           /  \   /\
          x   20  x x       */
        assertEquals(4, new Node(27,
                new Node(11, mt, new Node(20)),
                new Node(83))
                .size());
        assertEquals(5, new Node(27,
                new Node(11, mt, new Node(20, new Node(18), mt)),
                new Node(83))
                .size());
    }


    @Test
    void testInsert() {
        EmptyTree mt = EmptyTree.getInstance();
        assertEquals(new Node(27), mt.insert(27));

        assertEquals(new Node(27,
                        new Node(18, new Node<>(11), new Node<>(20)),
                        new Node(83)),
                new Node(27,
                        new Node(11, mt, new Node(20)),
                        new Node(83))
                        .insert(18));


               /*
       27
      /  \
     11   83
    /  \   /\
   x   20  x x
               */
        assertEquals(new Node(27, mt, new Node(83, mt, mt)),
                new Node(27, mt, mt).insert(83));
    }

    @Test
    void testMin() {
        EmptyTree mt = EmptyTree.getInstance();

        Node<Integer> t1 = new Node<>(27);
        assertEquals(Optional.of(new Node<>(27)), t1.min());

        Node<Integer> t3 = new Node<>(27, new Node<>(11), new Node<>(83));
        assertEquals(Optional.of(new Node<>(11)), t3.min());

        assertEquals(Optional.of(new Node<>(11)),
                new Node<>(27,
                        new Node<Integer>(11, mt, new Node<Integer>(20, new Node<>(18), mt)),
                        new Node<>(83))
                        .min());

        assertEquals(Optional.of(new Node<>(5)),
                new Node<>(27,
                        new Node<>(11, new Node<>(5), new Node<Integer>(20, new Node<>(18), mt)),
                        new Node<>(83))
                        .min());

        assertEquals(Optional.of(new Node<>(1)),
                new Node<>(27,
                        new Node<>(11, new Node<Integer>(5, new Node<>(1), mt), new Node<Integer>(20, new Node<>(18), mt)),
                        new Node<>(83))
                        .min());

        assertEquals(Optional.empty(), mt.min());
    }

    @Test
    void testValid() {
        EmptyTree mt = EmptyTree.getInstance();

        Node<Integer> t1 = new Node<>(27);
        assertTrue(t1.isValid());

        Node<Integer> t3 = new Node<>(27, new Node<>(11), new Node<>(83));
        assertTrue(t3.isValid());

        assertFalse(new Node<>(27,
                new Node<Integer>(83, mt, new Node<Integer>(20, new Node<>(18), mt)),
                new Node<>(11))
                .isValid());

        assertTrue(new Node<>(27,
                new Node<Integer>(11, mt, new Node<Integer>(20, new Node<>(18), mt)),
                new Node<>(83))
                .isValid());

        assertTrue(new Node<>(27,
                new Node<>(11, new Node<>(5), new Node<Integer>(20, new Node<>(18), mt)),
                new Node<>(83))
                .isValid());

        assertFalse(new Node<>(27,
                new Node<>(11, new Node<>(50), new Node<Integer>(20, new Node<>(18), mt)),
                new Node<>(83))
                .isValid());

        assertTrue(new Node<>(27,
                new Node<>(11, new Node<Integer>(5, new Node<>(1, mt, new Node<>(3)), mt), new Node<Integer>(20, new Node<>(18), mt)),
                new Node<>(83))
                .isValid());

        assertTrue(mt.isValid());
    }

    @Test
    void testRotateLeft() {
        EmptyTree mt = EmptyTree.getInstance();

        assertEquals(new Node<>(20, new Node<>(30), new Node<>(10)),
                new Node<Integer>(30, mt, new Node<Integer>(20, mt, new Node<>(10))).rotateLeft()
        );

        assertEquals(new Node<>(20, new Node<>(30), new Node<>(10, mt, new Node<>(5))),
                new Node<>(30, mt, new Node<>(20, mt,  new Node<>(10, mt, new Node<>(5)))).rotateLeft()
        );

        assertEquals(new Node<>(30, new Node<>(20), new Node<>(50, new Node<>(40), new Node<>(60))),
                new Node<>(30, new Node<>(20), new Node<>(40, mt, new Node<>(50, mt, new Node<>(60))).rotateLeft())
        );
    }

    @Test
    void testRotateRight() {
        EmptyTree mt = EmptyTree.getInstance();

        assertEquals(new Node<>(20, new Node<>(10), new Node<>(30)),
                new Node<>(30, new Node<>(20, new Node<>(10), mt), mt).rotateRight()
        );

        assertEquals(new Node<>(20, new Node<>(10, new Node<>(5), mt), new Node<>(30)),
                new Node<>(30, new Node<>(20, new Node<>(10, new Node<>(5), mt), mt), mt).rotateRight()
        );

        assertEquals(new Node<>(30, new Node<>(10, new Node<>(5), new Node<>(20)), new Node<>(40)),
                new Node<>(30, new Node<>(20, new Node<>(10, new Node<>(5), mt), mt).rotateRight(), new Node<>(40))
        );
    }

    @Test
    void testRebalance() {
        EmptyTree mt = EmptyTree.getInstance();

        // rebalancing happens on insertion
        assertEquals(new Node<>(30, new Node<>(20), new Node<>(50, new Node<>(40), new Node<>(60))),
                new Node<>(30, new Node<>(20), new Node<>(40, mt, new Node<>(50, mt, new Node<>(60))).rebalance())
        );

        assertEquals(new Node<>(30, new Node<>(10, new Node<>(0), new Node<>(20)), new Node<>(40)),
                new Node<>(30, new Node<>(20, new Node<>(10, new Node<>(0), mt), mt).rebalance(), new Node<>(40))
        );

        assertEquals(new Node<>(30, new Node<>(15, new Node<>(10), new Node<>(20)), new Node<>(40)),
                new Node<>(30, new Node<>(20, new Node<>(10, mt, new Node<>(15)), mt).rebalance(), new Node<>(40))
        );

        assertEquals(new Node<>(30, new Node<>(20), new Node<>(45, new Node<>(40), new Node<>(50))),
                new Node<>(30, new Node<>(20), new Node<>(40, mt, new Node<>(50, new Node<>(45), mt)).rebalance())
                );
    }



    @Test
    void testDelete() {
        assertEquals(new EmptyTree(),
                new EmptyTree().delete(17, true));
        //assertEquals( new EmptyTree(),
        //            new EmptyTree().delete(17) );
        //assertThrows( "does not exist",
        //              new EmptyTree().delete(17,false) );


        assertEquals(new EmptyTree(),
                new Node(17).delete(17));

        assertEquals(new Node(17, new Node(10), new EmptyTree()),
                new Node(17, new Node(10), new Node(20)).delete(20));

        assertEquals(new Node(17, new EmptyTree(), new Node(20)),
                new Node(17, new Node(10), new Node(20)).delete(10));

        //assertEquals( new Node(...)
        //              new Node(17, new Node(10), new Node(20)).delete(17) );


        assertEquals(new Node(50,
                        new Node(25,
                                new Node(18),
                                new Node(37)),
                        new Node(75,
                                new Node(62),
                                new EmptyTree())),

                new Node(50,
                        new Node(25,
                                new Node(12,
                                        new EmptyTree(),
                                        new Node(18)),
                                new Node(37)),
                        new Node(75,
                                new Node(62),
                                new EmptyTree()))
                        .delete(12));


        // delete at root of a singleton tree:
        EmptyTree mt = EmptyTree.getInstance();
        assertEquals(mt, new Node(27).delete(27));

        // delete at root of a v.small tree:
        BSTree no27 = new Node(27, new Node(11), new Node(83))
                .delete(27);
        assert no27.equals(new Node(11, mt, new Node(83, mt, mt)))
                || no27.equals(new Node(83, new Node(11), mt));

/*
       27
      /  \
     11   83
    /  \   /\
   x    x  x x
*/

        // Add 18 at a leaf, then delete it:
        assertEquals(new Node(27,
                        new Node(11, mt, new Node(20)),
                        new Node(83)),
                new Node(27,
                        new Node(11, mt, new Node(20, new Node(18), mt)),
                        new Node(83))
                        .delete(18));
/*
       27
      /  \
     11   83
    /  \   /\
   5   20  x x
  /\   /\
 x x  18 x
      /\
     x  x

     To delete at root:
         - at leaf (both children Empty) -- just return new Empty().
         - if just one Child EmptyTree -- just return the other (non-empty) subtree
         - else:
             find the largest element in the left (or, smallest from right);
             delete THAT value [It can't have a right-child -- else wouldn't be biggest in left)
             and return a tree with that value at the root


       27
      /  \
     11   83
    /  \   /\
   5   20  x x
  /\   /\
 x x  18 x
      /\
     x  x

removing  27 might give:
   (delete the 20, and then replace it at root)
       20
      /  \
     11   83
    /  \   /\
   5   18  x x
  /\   /\
 x x  x  x



       27
      /  \
     11   83
    /  \   /\
   5   20  x x
  /\   /\
 x x  18 x

       27
      /  \
     11   83
    /  \   /\
   5   20  x x
  /\   /\
 x x  18 x
*/


        // Delete root of a complicated tree with several layers:
        Node orig = new Node(27,
                new Node(11, new Node(5), new Node(20, new Node(18), mt)),
                new Node(83));
        BSTree del27 = orig.delete(27);
        // `delete` may replace root with *either* the item next-bigger OR next-smaller
        // than 27;  our test allows either:
        assert del27.equals(new Node(83,
                orig.l(),
                mt))
                || del27.equals(new Node(20,
                new Node(11, new Node(5), new Node(18)),
                orig.r()));


        // Btw:  If we were testing `delete`,
        //  and relying not on our knowledge of how the algorithm "should" achieve its goal
        //  but instead relying on its declared interface (Cf. "declarative programming"),
        //  we might instead try asserting:
        //assert del27.isBST() && del27.size() == orig.size()-1 && del27.containsAll( orig less 27 );
        //  You can also consider this white-box testing (uncommented), vs black-box testing (commented).
    }

    @Test
    void testHeight() {
        EmptyTree mt = EmptyTree.getInstance();

        Node<Integer> t1 = new Node<>(27);
        assertEquals(1, t1.height());

        Node<Integer> t3 = new Node<>(27, new Node<>(11), new Node<>(83));
        assertEquals(2, t3.height());

        assertEquals(4,
                new Node<>(27,
                        new Node<Integer>(11, mt, new Node<Integer>(20, new Node<>(18), mt)),
                        new Node<>(83))
                        .height());

        assertEquals(4,
                new Node<>(27,
                        new Node<>(11, new Node<>(5), new Node<Integer>(20, new Node<>(18), mt)),
                        new Node<>(83))
                        .height());

        assertEquals(5,
                new Node<>(27,
                        new Node<>(11, new Node<Integer>(5, new Node<>(1, mt, new Node<>(3)), mt), new Node<Integer>(20, new Node<>(18), mt)),
                        new Node<>(83))
                        .height());

        assertEquals(0, mt.height());
    }


    @Test
    void testConstructor() {
        assert EmptyTree.getInstance() == EmptyTree.getInstance();  // check that we get identically-same object returned.

        BSTree mt = EmptyTree.getInstance();
        assertEquals(mt, EmptyTree.getInstance());
        //assertNotEquals( new Node(47, mt, mt) , new Empty());

        // check equality-testing for singleton trees:
        assertEquals(new Node(47, mt, mt), new Node(47, mt, mt));

        // Check our convenience-constructor for singleton-trees:
        assertEquals(new Node(47), new Node(47, mt, mt));

        // An ever-so-slightly-bigger BSTree:
        BSTree x = new Node(27,
                new Node(11,
                        mt,
                        new Node(20)),
                new Node(83));

        // Check equality-test for slightly-bigger BSTree:
        assertEquals(x,
                new Node(27,
                        new Node(11,
                                mt,
                                new Node(20)),
                        new Node(83)));


        // Optional: Printing out some trees.
        // We won't enforce (unit-test) an exact format of `toString` though.
//        System.out.println("An empty tree: " + EmptyTree.getInstance().toString());
//        System.out.println("A singleton tree: " + new Node(47, mt, mt));
//        System.out.println("A slightly bigger tree: " + x);
    }
}

/*
@author ibarland
@version 2024-Mar-29

@license CC-BY -- share/adapt this file freely, but include attribution, thx.
    https://creativecommons.org/licenses/by/4.0/
    https://creativecommons.org/licenses/by/4.0/legalcode
Including a link to the original satisifies "appropriate attribution":
    https://www.radford.edu/itec360/Lectures/BSTree/BSTree-no-balancing.java
*/