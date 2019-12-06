import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test
 */

public class Test {
    static Logger logger = Logger.getLogger("Tester");

    public static void testForDango() throws InterruptedException {
        AVLTree tree = new AVLTree();
        int[] keys = {13, 15, 2, 7, 9, 18, 11, 20};

        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
            BTreePrinter.printNode(tree.getRoot());
            Thread.sleep(200L);
            System.out.println("------");
        }

        int[] delkeys = {7, 20, 9, 13, 15, 2, 11, 18};
        for (int k : delkeys) {
            tree.delete(k);
            BTreePrinter.printNode(tree.getRoot());
            Thread.sleep(200L);
            System.out.println("------");
        }
    }


    public static AVLTree testForSid() throws InterruptedException {
        AVLTree tree = new AVLTree();
        int[] keys = {7, 6, 5, 1, 2, 3, 4};

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

    public static AVLTree randomTest() throws InterruptedException {
        AVLTree tree = new AVLTree();
        double key;
        int intKey;
        for (int i = 1; i < 8; i++) {
            key = ((Math.random() * ((100 - 1) + 1)) + 1);
            intKey = (int) key;
            logger.info("inserting " + intKey);
            tree.insert(intKey, Integer.toString(intKey));
            BTreePrinter.printNode(tree.getRoot());
            System.out.println("-----------------");
            Thread.sleep(500L);
        }
        return tree;
    }

    public static boolean testParents(AVLTree.IAVLNode node) {
        if (node.getHeight() == 0) return true;
        else if (node.getRight().getHeight() == -1) {
            boolean res = node.getLeft().getParent() == node;
            if (!res) System.out.println("node " + node.getValue() + "'s left child has wrong parent");
            return res&& testParents(node.getLeft());
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

    public static void main(String[] args) throws InterruptedException {
        testForDango();
        AVLTree tree = testForSid();
        System.out.println(tree);
        logger.finest("testing parents...");
        boolean isOk = testParents(tree.getRoot());
        System.out.println(isOk);
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


