import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test
 */

public class Test {
    static Logger logger = Logger.getLogger("Tester");

    public static void testForDango() throws InterruptedException {
        AVLTree tree = new AVLTree();
        AVLTree tree2 = new AVLTree();
        AVLTree tree3 = new AVLTree();

        int[] smallKey = {1, 2, 3, 4, 5};
        int[] bigKey = {7, 8, 9, 10};

        for (int k : smallKey) {
            tree.insert(k, Integer.toString(k));

            Thread.sleep(200L);

            System.out.println("------");
        }

        for (int k : bigKey) {
            tree2.insert(k, Integer.toString(k));

            Thread.sleep(200L);

            System.out.println("------");
        }

        BTreePrinter.printNode(tree.getRoot());
        BTreePrinter.printNode(tree2.getRoot());
        tree3.insert(6, "6");

        tree.join(tree3.getRoot(), tree2);
        BTreePrinter.printNode(tree.getRoot());
    }

    public static boolean testTreeSize() {
        int[] keys = {8, 4, 2, 1, 3, 6, 5, 7, 12, 10, 9, 11, 14, 13, 15};
        AVLTree tree = new AVLTree();
        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
        }
        return testTreeSize(tree);
    }

    public static boolean testTreeSize(AVLTree tree) {
        boolean res = testSizes(tree.getRoot());
        return res && tree.getRoot().getSize() == tree.size();
    }

    public static boolean testSizes(AVLTree.IAVLNode node) {
        if (!node.isRealNode()) return true;
        return node.getSize() == node.getLeft().getSize() + node.getRight().getSize()
                && testSizes(node.getLeft())
                && testSizes(node.getRight());
    }

    public static void testSplit() {
        AVLTree tree = new AVLTree();
        int[] keys = {8, 4, 2, 1, 3, 6, 5, 7, 12, 10, 9, 11, 14, 13, 15};
        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
            testParents(tree.getRoot());
        }
        BTreePrinter.printNode(tree.getRoot());

        AVLTree[] res = tree.split(5);
        System.out.println(String.format("smaller (size=%d):", res[0].size()));
        BTreePrinter.printNode(res[0].getRoot());
        System.out.println(String.format("bigger (size=%d):", res[1].size()));
        BTreePrinter.printNode(res[1].getRoot());
    }


    public static AVLTree testInsert() throws InterruptedException {
        AVLTree tree = new AVLTree();
        //int[] keys = {8, 4, 2, 1, 3, 6, 5, 7, 12, 10, 9, 11, 14, 13, 15};
        int[] keys = new int[50];
        for (int i = 1; i <= 50; i++) keys[i-1] = i;

        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
            BTreePrinter.printNode(tree.getRoot());
            Thread.sleep(200L);
            if (!testParents(tree.getRoot())) {
                System.out.println("error with parents after inserting " + k);
            }
            System.out.println("------");
        }
        return tree;
    }

    public static boolean testParents() {
        int[] keys = {8, 4, 2, 1, 3, 6, 5, 7, 12, 10, 9, 11, 14, 13, 15};
        AVLTree tree = new AVLTree();
        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
        }
        return testParents(tree.getRoot());
    }

    public static boolean testHeights(AVLTree.IAVLNode node) {
        if (!node.isRealNode()) return true;
        return node.getHeight() == 1 + Math.max(node.getLeft().getHeight(), node.getRight().getHeight())
                && testHeights(node.getLeft()) && testHeights(node.getRight());
    }

    public static boolean testParents(AVLTree.IAVLNode node) {
        if (node.getHeight() == 0) return true;
        else if (node.getRight().getHeight() == -1) {
            boolean res = node.getLeft().getParent() == node;
            if (!res) System.out.println("node " + node.getValue() + "'s left child has wrong parent");
            return res && testParents(node.getLeft());
        } else if (node.getLeft().getHeight() == -1) {
            boolean res = node.getRight().getParent() == node;
            if (!res) System.out.println("node " + node.getValue() + "'s right child has wrong parent");
            return res &&
                    testParents(node.getRight());
        }
        boolean res =
                node.getLeft().getParent() == node &&
                        node.getRight().getParent() == node &&
                        testParents(node.getRight()) &&
                        testParents(node.getLeft());
        if (!res) System.out.println("node " + node.getValue() + "'s children have wrong parent");
        return res;
    }

    private static boolean testTreeToArray(AVLTree tree) {
        int[] keys = tree.keysToArray();
        int[] sortedKeys = keys.clone();
        Arrays.sort(sortedKeys);
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != sortedKeys[i]) return false;
        }
        return true;
    }

    public static boolean testTreeToArray() {
        int[] keys = {8, 4, 2, 1, 3, 6, 5, 7, 12, 10, 9, 11, 14, 13, 15};
        AVLTree tree = new AVLTree();
        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
        }
        return testTreeToArray(tree);
    }

    public static void main(String[] args) throws InterruptedException {
        AVLTree tree = testInsert();
        System.out.println("testing sizes...");
        if (!testTreeSize(tree)) {
            System.out.println("tree sizes are wrong");
        }
        System.out.println("testing treeToArray...");
        if (!testTreeToArray(tree)) {
            System.out.println("tree to array is wrong");
        }

        System.out.println("testing parents...");
        if (!testParents(tree.getRoot())) {
            System.out.println("parents are wrong");
        }
        ;
        System.out.println("testing heights before delete...");
        BTreePrinter.printNode(tree.getRoot(), true);
        if (!testHeights(tree.getRoot())) {
            System.out.println("heights in tree are wrong");
        }


        System.out.println("testing split, make sure from the prints that the trees are correct and that their SIZES are correct as well");
        testSplit();
    }
	/*public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		
		for(int i=1;i<30;i=i+2)
			tree.insert(i, Integer.toString(i));
		printTree(tree.getRoot(),0);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		tree.delete(15); // prob
		tree.delete(13);
		tree.delete(7);
		tree.delete(27);
		tree.delete(11);
		tree.delete(23);
		printTree(tree.getRoot(),0);
		//tree.delete(7); // prob
		//tree.rotateLeft(tree.root.getRight().getRight());
		//System.out.println(tree.delete(5)); 
		
		//printTree(tree.getRoot(),0);
	}*/

    public static void printTree(AVLTree.IAVLNode node, int level) { //call with root,level=0
        if (node == null)
            return;
        printTree(node.getRight(), level + 1);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++)
                System.out.print("|\t");
            if (node.getParent() != null) {
                System.out.println("|------- " + node.getKey() + " " + getBF(node) + " " + node.getParent().getKey());
            } else {
                System.out.println("|------- " + node.getKey() + " " + getBF(node)); //+ node.getHeight() + " " + node.getSubtreeSize());
            }
        } else if (node.getParent() != null) {
            System.out.println(node.getKey() + " " + getBF(node) + " " + node.getParent().getKey());
        } else {
            System.out.println(node.getKey() + " " + getBF(node));//node.getHeight()+" " + node.getSubtreeSize());
        }
        printTree(node.getLeft(), level + 1);
    }

    public static int getBF(AVLTree.IAVLNode node) {
        //if(node == null || !node.isRealNode())
        //return 0;
        //int leftSubTreeHight = node.getLeft() == null ? 0 : node.getLeft().getHeight() + 1;
        // int rightSubTreeHight = node.getRight() == null ? 0 : node.getRight().getHeight() + 1;
        // return leftSubTreeHight - rightSubTreeHight;
        return node.getHeight();
    }


}


