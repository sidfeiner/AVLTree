import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {
    Logger logger = Logger.getLogger(getClass().getSimpleName());
    private IAVLNode root;
    private int size;
    private IAVLNode virtualNode = new AVLNode(-1, "V", true);

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.root == null;
    }

    private String searchRec(IAVLNode root, int k) {
        if (k == root.getKey()) {
            return root.getValue();
        } else if (k < root.getKey()) {
            if (root.getLeft() == null) return null;
            return searchRec(root.getLeft(), k);
        } else {
            if (root.getRight() == null) return null;
            return searchRec(root.getRight(), k);
        }
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        return searchRec(this.root, k);
    }

    public boolean insertRec(IAVLNode curNode, AVLNode insertNode) {
        if (insertNode.getKey() < curNode.getKey()) {
            if (curNode.getLeft() == virtualNode) {
                curNode.setLeft(insertNode);
                insertNode.setParent(curNode);
            } else {
                insertRec(curNode.getLeft(), insertNode);
            }
        } else if (insertNode.getKey() > curNode.getKey()) {
            if (curNode.getRight() == virtualNode) {
                curNode.setRight(insertNode);
                insertNode.setParent(curNode);
            } else {
                insertRec(curNode.getRight(), insertNode);
            }
        } else {
            return false;
        }
        return true;
    }

    private void promoteNode(IAVLNode node) {
        node.setHeight(node.getHeight() + 1);
    }

    /**
     * @param node Node that was inserted
     * @return Amount of rebalance operations needed to rebalance the tree starting from where node was inserted
     */
    private int rebalanceTree(IAVLNode node) {
        logger.finest("rebalancing tree starting from node " + node.getValue());
        if (node == this.root) {
            logger.finest("reached root, done rebalancing");
            return 0;
        }
        IAVLNode parent = node.getParent();

        // getLeft/getRight always exist because we have virtual nodes
        int leftRankDiff = parent.getHeight() - parent.getLeft().getHeight();
        int rightRankDiff = parent.getHeight() - parent.getRight().getHeight();

        if (leftRankDiff + rightRankDiff == 1) {
            logger.finest("we have 0-1/1-0 case, promoting parent");
            // Case of 1-0 or 0-1
            promoteNode(parent);
            return 1 + rebalanceTree(parent);
        } else if (leftRankDiff + rightRankDiff == 2 && leftRankDiff != 1) {
            // case of 2-0 or 0-2
            if (rightRankDiff == 0) {
                logger.finest("we have 2-0 case, rotating left");
                rotateLeft(node);
            } else {
                logger.finest("we have 0-2 case, rotating right");
                rotateRight(node);
            }
            return 1 + rebalanceTree(parent);
        }
        logger.finest("done rebalancing at node " + node.getValue());
        return 0;
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        AVLNode node = new AVLNode(k, i);
        if (this.root == null) {
            this.root = node;
            return 0;
        }
        boolean inserted = insertRec(this.root, node);
        if (!inserted) return -1;
        return rebalanceTree(node);
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        IAVLNode node = this.root;
        while (node.getLeft() != null) node = node.getLeft();
        return node.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        IAVLNode node = this.root;
        while (node.getRight() != null) node = node.getRight();
        return node.getValue();
    }

    private int[] keysToArray(IAVLNode node) {
        int[] left, right, finalArray;
        if (node.getLeft() == null) left = new int[0];
        else left = keysToArray(node.getLeft());

        if (node.getRight() == null) right = new int[0];
        else right = keysToArray(node.getRight());

        finalArray = new int[left.length + right.length + 1];
        for (int i = 0; i < left.length; i++) finalArray[i] = left[i];
        finalArray[left.length] = node.getKey();
        for (int i = 0; i < right.length; i++) finalArray[i + left.length + 1] = right[i];
        return finalArray;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[42]; // to be replaced by student code
        return arr;              // to be replaced by student code
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        String[] arr = new String[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     */
    public IAVLNode getRoot() {
        return this.root;
    }

    /**
     * public string split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * precondition: search(x) != null
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        return null;
    }

    /**
     * public join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (rank difference between the tree and t)
     * precondition: keys(x,t) < keys() or keys(x,t) > keys()
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        return 0;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    private void rotateLeft(IAVLNode node) {
        IAVLNode parent = node.getParent();
        IAVLNode grandParent = parent.getParent();

        parent.setRight(node.getLeft());
        parent.setParent(node);
        node.setLeft(parent);
        node.setParent(grandParent);
        if (grandParent == null) {
            this.root = node;
        } else {
            grandParent.setLeft(node);
        }

        parent.setHeight(1 + Math.max(parent.getLeft().getHeight(), parent.getRight().getHeight()));
        node.setHeight(1 + Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));
    }

    private void rotateRight(IAVLNode node) {
        IAVLNode tempB = node.getRight();
        IAVLNode tempY = node.getParent();

        tempY.setLeft(tempB);
        node.setParent(tempY.getParent());
        node.setRight(tempY);

        tempY.setHeight(1 + Math.max(tempY.getRight().getHeight(), tempY.getLeft().getHeight()));
        node.setHeight(1 + Math.max(node.getRight().getHeight(), node.getLeft().getHeight()));

    }

    private IAVLNode treePosition(int k) {
        IAVLNode x = root;
        IAVLNode y = null;
        while (x != null) {
            y = x;
            if (k == x.getKey()) {
                return y;
            } else {
                if (k < x.getKey()) {
                    x = x.getLeft();
                } else {
                    x = x.getRight();
                }
            }
        }
        return y;
    }

    private void treeInsert(int k, IAVLNode z) {
        IAVLNode y = treePosition(k);
        z.setParent(y);
        if (z.getKey() < y.getKey()) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }

    }

    public interface IAVLNode {
        public int getKey(); //returns node's key (for virtuval node return -1)

        public String getValue(); //returns node's value [info] (for virtuval node return null)

        public void setLeft(IAVLNode node); //sets left child

        public IAVLNode getLeft(); //returns left child (if there is no left child return null)

        public void setRight(IAVLNode node); //sets right child

        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)
     */
    public class AVLNode implements IAVLNode {

        int key, height;
        String info;
        IAVLNode left, right, parent;


        public AVLNode(int key, String info) {
            this(key, info, false);
        }

        public AVLNode(int key, String info, boolean isVirtual) {
            this.key = key;
            this.info = info;
            if (isVirtual) {
                height = -1;
            } else {
                height = 0;
            }
            this.left = virtualNode;
            this.right = virtualNode;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return info;
        }

        public void setLeft(IAVLNode node) {
            left = node;

        }

        public IAVLNode getLeft() {
            return left;
        }

        public void setRight(IAVLNode node) {
            right = node;

        }

        public IAVLNode getRight() {
            return right;
        }

        public void setParent(IAVLNode node) {
            parent = node;
        }

        public IAVLNode getParent() {
            return parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return (getHeight() != -1);
        }

        public void setHeight(int height) {
            if (this.getHeight() != -1) {
                this.height = height;
            }
        }

        public int getHeight() {
            return height;
        }
    }

}
  

