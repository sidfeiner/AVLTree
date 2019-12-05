import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test
 */

public class Test {
    static Logger logger = Logger.getLogger("Tester");

    public static void testForDango() throws InterruptedException {
        AVLTree tree = new AVLTree();
        int[] keys = {7, 6, 5};

        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
            BTreePrinter.printNode(tree.getRoot());
            Thread.sleep(200L);
            System.out.println("------");
        }


        tree.delete(7);
        BTreePrinter.printNode(tree.getRoot());

    }

    public static AVLTree testForSid() throws InterruptedException {
        AVLTree tree = new AVLTree();
        int[] keys = {36,91,55};

        for (int k : keys) {
            tree.insert(k, Integer.toString(k));
            BTreePrinter.printNode(tree.getRoot());
            Thread.sleep(200L);
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

    public static void main(String[] args) throws InterruptedException {
        testForDango();
        AVLTree tree = randomTest();
        for(int i: tree.keysToArray()){
            System.out.println(i);
        }
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


