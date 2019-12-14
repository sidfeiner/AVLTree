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

    public AVLTree(IAVLNode root) {
        this.root = root;
        this.size = root.getSize();
    }

    public AVLTree() {

    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.root == null;
    }

    private IAVLNode searchRecNode(IAVLNode root, int k) {
        if (root == null) {
            return null;
        }
        if (k == root.getKey()) {
            return root;
        } else if (k < root.getKey()) {
            if (root.getLeft() == null) return null;
            return searchRecNode(root.getLeft(), k);
        } else {
            if (root.getRight() == null) return null;
            return searchRecNode(root.getRight(), k);
        }
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        return searchRecNode(this.root, k).getValue();
    }


    private boolean insertRec(IAVLNode curNode, AVLNode insertNode) {
        if (insertNode.getKey() < curNode.getKey()) {
            if (!curNode.getLeft().isRealNode()) {
                curNode.setLeft(insertNode);
                insertNode.setParent(curNode);
                return true;
            } else {
                return insertRec(curNode.getLeft(), insertNode);
            }
        } else if (insertNode.getKey() > curNode.getKey()) {
            if (!curNode.getRight().isRealNode()) {
                curNode.setRight(insertNode);
                insertNode.setParent(curNode);
                return true;
            } else {
                return insertRec(curNode.getRight(), insertNode);
            }
        } else {
            // Key already exists
            return false;
        }
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
            // case of 2-0 or 0-2 or 1-3+ or 3+-1
            if (rightRankDiff == 0) {
                // 2-0
                logger.finest("we have 2-0 case, rotating left");
                parent = node;
                if (node.getHeight() - node.getLeft().getHeight() == 1 && node.getHeight() - node.getRight().getHeight() == 2) {
                    logger.finest("double rotating needed, first rotate with left child (right-rotate)");
                    IAVLNode left = node.getLeft();
                    rotateRight(left);
                    node = left;
                }
                rotateLeft(node);
            } else {
                // 0-2
                logger.finest("we have 0-2 case, rotating right");
                parent = node;
                if (node.getHeight() - node.getLeft().getHeight() == 2 && node.getHeight() - node.getRight().getHeight() == 1) {
                    logger.finest("double rotating needed, first rotate with right child (left-rotate)");
                    IAVLNode right = node.getRight();
                    rotateLeft(right);
                    node = right;
                }
                rotateRight(node);
            }
            return 1 + rebalanceTree(parent);
        }
        logger.finest("done rebalancing at node " + node.getValue());
        return 0;
    }

    public int increaseAncestorsSize(IAVLNode node, int increase) {
        if (node == null) return 0;
        logger.fine("increasing size of node " + node.getValue() + " by " + increase);
        node.setSize(node.getSize() + increase);
        return 1 + increaseAncestorsSize(node.getParent(), increase);
    }

    public int incrementAncestorsSize(IAVLNode node) {
        return increaseAncestorsSize(node, 1);
    }

    public int decrementAncestorsSize(IAVLNode node) {
        return decreaseAncestorsSize(node, 1);
    }

    public int decreaseAncestorsSize(IAVLNode node, int decrease) {
        return increaseAncestorsSize(node, -decrease);
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
        logger.finest("inserting key: " + k);
        AVLNode node = new AVLNode(k, i);
        if (this.root == null) {
            this.root = node;
            this.size++;
            return 0;
        }
        boolean inserted = insertRec(this.root, node);
        if (!inserted) return -1;
        this.size++;
        incrementAncestorsSize(node.getParent());
        int rebalanceOpsAmount = rebalanceTree(node);
        return rebalanceOpsAmount;
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
        IAVLNode node = searchForNode(k);
        if (node == null) {
            return -1;
        }
        IAVLNode parent = node.getParent();
        if (node.isLeaf()) {
            deleteLeaf(node);
            decrementAncestorsSize(parent);
            return (rebalanceAfterDelete(parent));
        }
        if (node.isUnaryNode()) {
            deleteUnaryNode(node);
            decrementAncestorsSize(parent);
            return (rebalanceAfterDelete(node.getParent()));
        }
        IAVLNode nodeToDelete = swapNodeWithSuccessor(node);
        deleteAfterSuccessorSwap(nodeToDelete);
        decrementAncestorsSize(nodeToDelete.getParent());
        return rebalanceAfterDelete(nodeToDelete.getParent());
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        IAVLNode node = this.root;
        while (node.getLeft().isRealNode()) node = node.getLeft();
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
        while (node.getRight().isRealNode()) node = node.getRight();
        return node.getValue();
    }

    private IAVLNode[] nodesToArray(IAVLNode node) {
        IAVLNode[] left, right, finalArray;
        if (!node.getLeft().isRealNode()) left = new IAVLNode[0];
        else left = nodesToArray(node.getLeft());

        if (!node.getRight().isRealNode()) right = new IAVLNode[0];
        else right = nodesToArray(node.getRight());

        finalArray = new IAVLNode[left.length + right.length + 1];
        for (int i = 0; i < left.length; i++) finalArray[i] = left[i];
        finalArray[left.length] = node;
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
        IAVLNode[] nodes = nodesToArray(this.root);
        int[] keys = new int[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            keys[i] = nodes[i].getKey();
        }
        return keys;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        IAVLNode[] nodes = nodesToArray(this.root);
        String[] keys = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            keys[i] = nodes[i].getValue();
        }
        return keys;
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

    private void splitRecBottomUp(IAVLNode node, AVLTree smaller, AVLTree bigger) {
        IAVLNode parent = node.getParent();
        if (node == this.root) {
            logger.finest("reached root.");
        } else {
            if (parent.getRight() == node) {
                // this is a right child
                if (parent.getLeft().isRealNode()) {
                    logger.finest("joining smaller tree with sibling at left");
                    IAVLNode copyNode = new AVLNode(parent.getKey(), parent.getValue(), false, parent.getSize());
                    smaller.join(copyNode, new AVLTree(parent.getLeft()));
                }
            } else if (parent.getLeft() == node) {
                // this is a left child
                if (parent.getRight().isRealNode()) {
                    logger.finest("joining bigger tree with sibling at right");
                    AVLTree rightTree = new AVLTree(parent.getRight());
                    IAVLNode copyNode = new AVLNode(parent.getKey(), parent.getValue(), false, parent.getSize());
                    bigger.join(copyNode, rightTree);
                }
            }
            splitRecBottomUp(parent, smaller, bigger);
        }

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
        IAVLNode node = searchForNode(x);
        AVLTree smaller, bigger;
        if (node == this.getRoot()) {
            // recursive function stops at root, so in case we get the root, we split it here
            smaller = new AVLTree(node.getLeft());
            bigger = new AVLTree(node.getRight());
            return new AVLTree[]{smaller, bigger};
        } else {
            // splitting node is not the root
            if (node.getRight().isRealNode()) {
                bigger = new AVLTree(node.getRight());
            } else {
                bigger = new AVLTree();
            }
            smaller = new AVLTree();
            AVLTree[] result = {smaller, bigger};
            splitRecBottomUp(node, smaller, bigger);
            return result;
        }
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
        if (t.getRoot() != null && this.getRoot() != null) {
            if (x.getKey() > this.getRoot().getKey()) {
                joinBiggerKeyTree(x, t);
            } else {
                if (x.getKey() > this.getRoot().getKey()) {
                    joinSmallerKeyTree(x, t);
                } else { // equal height
                    joinTreeWithEqualHeight(x, t);
                }
            }
        } else {

            {
                if (this.getRoot() == null && t.getRoot() != null) {
                    t.insert(x.getKey(), x.getValue());
                    this.root = t.getRoot();
                    this.size = t.size();
                    return t.getRoot().getHeight() + 1;
                } else {
                    if (this.getRoot() != null && t.getRoot() == null) {
                        this.insert(x.getKey(), x.getValue());
                        return this.getRoot().getHeight() + 1;
                    } else {
                        this.root = x;
                        x.setLeft(virtualNode);
                        x.setRight(virtualNode);
                        x.setHeight(0);
                        return 1;
                    }
                }
            }

        }
        return Math.abs(this.getRoot().getHeight() - t.getRoot().getHeight()) + 1;
    }

    private void joinBiggerKeyTree(IAVLNode x, AVLTree t) {
        if (t.getRoot().getHeight() > this.getRoot().getHeight()) {
            IAVLNode joinNode = t.getRoot();
            while (joinNode.getHeight() > this.root.getHeight()) {
                joinNode = joinNode.getLeft();
            }
            x.setRight(joinNode);
            x.setLeft(this.getRoot());
            x.setParent(joinNode.getParent());
            this.getRoot().setParent(x);
            IAVLNode parent = joinNode.getParent();
            joinNode.setParent(x);
            x.setHeight(1 + Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
            x.setSize(this.getRoot().getSize() + joinNode.getSize() + 1);
            if (joinNode == t.getRoot()) {
                this.root = x;
            } else {
                this.root = t.getRoot();
                parent.setLeft(x);
                increaseAncestorsSize(parent, 1 + t.getRoot().getSize());
                rebalanceTree(x);
            }
        } else {
            IAVLNode joinNode = this.getRoot();
            while (joinNode.getHeight() > t.getRoot().getHeight()) {
                joinNode = joinNode.getRight();
            }
            x.setLeft(joinNode);
            x.setRight(t.getRoot());
            x.setParent(joinNode.getParent());
            t.getRoot().setParent(x);
            IAVLNode parent = joinNode.getParent();
            joinNode.setParent(x);
            x.setHeight(1 + Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
            x.setSize(t.getRoot().getSize() + joinNode.getSize() + 1);
            if (joinNode == this.getRoot()) {
                this.root = x;
            } else {
                parent.setRight(x);
                increaseAncestorsSize(parent, 1 + this.getRoot().getSize());
                rebalanceTree(x);
            }

        }
        this.size = this.size + t.size() + 1;
    }

    private void joinSmallerKeyTree(IAVLNode x, AVLTree t) {
        if (t.getRoot().getHeight() > this.getRoot().getHeight()) {
            IAVLNode joinNode = this.getRoot();
            while (joinNode.getHeight() >= this.root.getHeight()) {
                joinNode = joinNode.getLeft();
            }
            x.setRight(joinNode);
            x.setLeft(t.getRoot());
            x.setParent(joinNode.getParent());
            t.getRoot().setParent(x);
            IAVLNode parent = joinNode.getParent();
            joinNode.setParent(x);
            x.setHeight(1 + Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
            x.setSize(this.getRoot().getSize() + joinNode.getSize() + 1);
            if (joinNode == this.getRoot()) {
                this.root = x;
            } else {
                this.root = t.getRoot();
                parent.setRight(x);
                increaseAncestorsSize(parent, 1 + this.getRoot().getSize());
                rebalanceTree(x);
            }
        } else {
            IAVLNode joinNode = t.getRoot();
            while (joinNode.getHeight() > t.getRoot().getHeight()) {
                joinNode = joinNode.getRight();
            }
            x.setRight(t.getRoot());
            x.setLeft(joinNode);
            x.setParent(joinNode.getParent());
            t.getRoot().setParent(x);
            IAVLNode parent = joinNode.getParent();
            joinNode.setParent(x);
            x.setHeight(1 + Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
            x.setSize(t.getRoot().getSize() + joinNode.getSize() + 1);
            if (joinNode == t.getRoot()) {
                this.root = x;
            } else {
                parent.setLeft(x);
                increaseAncestorsSize(parent, 1 + t.getRoot().getSize());
                rebalanceTree(x);
            }
        }
        this.size = this.size + t.size() + 1;
    }

    private void joinTreeWithEqualHeight(IAVLNode x, AVLTree t) {
        if (x.getKey() > this.getRoot().getKey()) {
            x.setRight(t.getRoot());
            t.getRoot().setParent(x);
            x.setLeft(this.getRoot());
            this.getRoot().setParent(x);
            this.root = x;
            x.setParent(null);

        } else {
            x.setLeft(t.getRoot());
            t.getRoot().setParent(x);
            x.setRight(this.getRoot());
            this.getRoot().setParent(x);
            this.root = x;
            x.setParent(null);
        }
        x.setSize(x.getLeft().getSize() + x.getRight().getSize());
        this.size = this.size + t.size() + 1;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    private void rotateLeft(IAVLNode node) {
        IAVLNode parent = node.getParent();
        IAVLNode grandParent = parent.getParent();
        boolean parentIsLeftChild = grandParent != null && grandParent.getLeft() == parent;

        parent.setRight(node.getLeft());
        node.getLeft().setParent(parent);
        parent.setParent(node);
        node.setLeft(parent);
        node.setParent(grandParent);
        if (grandParent == null) {
            this.root = node;
        } else {
            if (parentIsLeftChild) {
                grandParent.setLeft(node);
            } else {
                grandParent.setRight(node);
            }
        }


        parent.setHeight(1 + Math.max(parent.getLeft().getHeight(), parent.getRight().getHeight()));
        node.setHeight(1 + Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));


        node.setSize(node.getLeft().getSize() + node.getRight().getSize() + 1);
        parent.setSize(parent.getLeft().getSize() + parent.getRight().getSize() + 1);
    }

    private void rotateRight(IAVLNode node) {
        IAVLNode parent = node.getParent();
        IAVLNode grandParent = parent.getParent();
        IAVLNode tempB = node.getRight();

        node.setParent(grandParent);
        parent.setLeft(tempB);
        tempB.setParent(parent);
        node.setRight(parent);
        parent.setParent(node);
        if (grandParent == null) {
            this.root = node;
        } else {
            if (grandParent.getLeft() == parent) {
                grandParent.setLeft(node);
            } else {
                grandParent.setRight(node);
            }
        }


        node.setHeight(1 + Math.max(node.getRight().getHeight(), node.getLeft().getHeight()));
        parent.setHeight(1 + Math.max(parent.getRight().getHeight(), parent.getLeft().getHeight()));

        node.setSize(node.getLeft().getSize() + node.getRight().getSize() + 1);
        parent.setSize(parent.getLeft().getSize() + parent.getRight().getSize() + 1);
    }

    private IAVLNode getSuccessor(IAVLNode node) {
        IAVLNode y = node;
        IAVLNode parent;
        if (node.getRight().isRealNode()) {
            y = node.getRight();
            while (y.getLeft().isRealNode()) {
                y = y.getLeft();
            }
            return y;
        } else {
                parent = y.getParent();
            while (parent!= null && parent.getRight() == y) {
                y = parent;
                parent = parent.getParent();
            }
            return y;
        }
    }

    private int rankDifferenceLeft(IAVLNode node) {
        return node.getHeight() - node.getLeft().getHeight();
    }

    private int rankDifferenceRight(IAVLNode node) {
        return node.getHeight() - node.getRight().getHeight();
    }

    private boolean isRankDifferenceLegal(IAVLNode node) {
        return (rankDifferenceLeft(node) == 1 && rankDifferenceRight(node) == 1) || (rankDifferenceLeft(node) == 2 && rankDifferenceRight(node) == 1) || (rankDifferenceLeft(node) == 1 && rankDifferenceRight(node) == 2);
    }

    private IAVLNode searchForNode(int k) {
        return searchRecNode(this.root, k);
    }

    private int rebalanceAfterDelete(IAVLNode parent) {
        int sum = 0;
        while (parent != null && !isRankDifferenceLegal(parent)) {
            if (rankDifferenceRight(parent) == 2 && rankDifferenceLeft(parent) == 2) {
                logger.finest("We have 2-2 case");
                logger.finest("Demoting parent: " + parent.getKey());
                parent.setHeight(parent.getHeight() - 1);
                sum += 1;
            }
            if (rankDifferenceRight(parent) == 1 && rankDifferenceLeft(parent) == 3 && rankDifferenceLeft(parent.getRight()) == 1 && rankDifferenceRight(parent.getRight()) == 1) {
                logger.finest("We have 3-1, 1-1 case");
                logger.finest("Performing left rotation on: " + parent.getRight() + " demoting: " + parent.getKey());
                parent.setHeight(parent.getHeight() - 1);
                if (parent.getParent() != null) {
                    logger.finest("Promoting: " + parent.getParent().getKey());
                    sum += 1;
                }
                sum += 2;
            }
            if (rankDifferenceRight(parent) == 3 && rankDifferenceLeft(parent) == 1 && rankDifferenceLeft(parent.getLeft()) == 1 && rankDifferenceRight(parent.getLeft()) == 1) {
                logger.finest("We have 1-3, 1-1 case");
                logger.finest("Performing right rotation on: " + parent.getLeft() + " demoting: " + parent.getKey());
                rotateRight(parent.getLeft());
                if (parent.getParent() != null) {
                    logger.finest("Promoting: " + parent.getParent().getKey());
                    sum += 1;
                }
                sum += 2;
            }

            if (rankDifferenceRight(parent) == 1 && rankDifferenceLeft(parent) == 3 && rankDifferenceLeft(parent.getRight()) == 2 && rankDifferenceRight(parent.getRight()) == 1) {
                logger.finest("We have 3-1, 2-1 case");
                logger.finest("Performing left rotation on: " + parent.getRight().getKey() + "demoting twice: " + parent.getKey());
                rotateLeft(parent.getRight());
                sum += 2;
            }
            if (rankDifferenceRight(parent) == 3 && rankDifferenceLeft(parent) == 1 && rankDifferenceLeft(parent.getLeft()) == 1 && rankDifferenceRight(parent.getLeft()) == 2) {
                logger.finest("We have 1-3, 1-2 case");
                logger.finest("Performing right rotation on: " + parent.getLeft().getKey() + "demoting twice: " + parent.getKey());
                rotateRight(parent.getLeft());
                sum += 2;
            }

            if (rankDifferenceRight(parent) == 1 && rankDifferenceLeft(parent) == 3 && rankDifferenceLeft(parent.getRight()) == 1 && rankDifferenceRight(parent.getRight()) == 2) {
                logger.finest("We have 3-1, 1-2 case");
                rotateRight(parent.getRight().getLeft());
                logger.finest("Performing Double Rotation");
                rotateLeft(parent.getRight());
                sum += 2;
            }

            if (rankDifferenceRight(parent) == 3 && rankDifferenceLeft(parent) == 1 && rankDifferenceLeft(parent.getLeft()) == 2 && rankDifferenceRight(parent.getLeft()) == 1) {
                logger.finest("We have 1-3, 2-1 case");
                rotateLeft(parent.getLeft().getRight());
                logger.finest("Performing Double Rotation");
                rotateRight(parent.getLeft());
                sum += 2;
            }
            parent = parent.getParent();
        }
        if (parent == null) {
            logger.finest("Reached root, Finished rebalancing");
        } else {
            logger.finest("Finished rebalancing before root");
        }
        return sum;
    }

    private void deleteAfterSuccessorSwap(IAVLNode nodeToDelete) {
        if (nodeToDelete.isLeaf()) {
            deleteLeaf(nodeToDelete);
        } else {
            deleteUnaryNode(nodeToDelete);
        }
    }

    private void deleteLeaf(IAVLNode node) {

        if (node == this.root) {
            logger.finest("Deleting leaf which is root from tree");
            this.root = null;
        } else {
            logger.finest("Deleting leaf: " + node.getKey() + " from tree");
            if (node.getParent().getRight() == node) { // Y is right child of parent
                node.getParent().setRight(virtualNode);
            } else {
                node.getParent().setLeft(virtualNode);
            }

        }
        this.size--;

    }

    private void deleteUnaryNode(IAVLNode node) {

        if (node == this.root) {
            logger.finest("Deleting unaryNode  which is root from tree");
            if (node.getRight().isRealNode()) {
                this.root = node.getRight();
                this.root.setParent(null);
            } else {
                this.root = node.getLeft();
                this.root.setParent(null);
            }
        } else {
            IAVLNode parent = node.getParent();
            logger.finest("Deleting unary node with key: " + node.getKey());
            {
                if (parent.getRight() == node) {
                    if (node.getRight().isRealNode()) {
                        parent.setRight(node.getRight());
                        node.getRight().setParent(parent);
                    } else {
                        parent.setRight(node.getLeft());
                        node.getLeft().setParent(parent);
                    }
                } else {
                    if (node.getLeft().isRealNode()) {
                        parent.setLeft(node.getLeft());
                        node.getLeft().setParent(parent);
                    } else {
                        parent.setLeft(node.getRight());
                        node.getRight().setParent(parent);
                    }
                }
            }
        }
        this.size--;

    }

    private IAVLNode swapNodeWithSuccessor(IAVLNode node) {
        logger.finest("Swapping node: " + node.getKey() + " with successor and deleting it");
        IAVLNode successor = getSuccessor(node);
        swapNode(node, successor);
        return successor;
    }

    private void swapNode(IAVLNode node1, IAVLNode node2) {
        int node1key = node1.getKey();
        String node1value = node1.getValue();
        int node1Size = node1.getSize();
        node1.setKey(node2.getKey());
        node1.setValue(node2.getValue());
        node1.setSize(node1Size);
        node2.setKey(node1key);
        node2.setValue(node1value);
        node2.setSize(node1Size);

    }

    public interface IAVLNode {

        public int getSize(); //returns amount of nodes under current node

        public void setSize(int size); //returns amount of nodes under current node

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

        public boolean isLeaf(); //returns True if the node is a leaf

        public boolean isUnaryNode(); //returns True if the node is a unary Node

        public void setKey(int key); //sets key for node (inside use)

        public void setValue(String info); //sets value for node
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

        int key, height, size;
        String info;
        IAVLNode left, right, parent;


        public AVLNode(int key, String info) {
            this(key, info, false, 1);
        }

        public AVLNode(int key, String info, boolean isVirtual) {
            this(key, info, isVirtual, 1);
        }

        public AVLNode(int key, String info, boolean isVirtual, int size) {
            this.key = key;
            this.info = info;
            if (isVirtual) {
                height = -1;
                size = 0;
            } else {
                height = 0;
                this.size = size;
            }
            this.left = virtualNode;
            this.right = virtualNode;
        }

        @Override
        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
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
            return this.height;
        }

        public boolean isLeaf() {
            return this.getLeft().getHeight() == -1 && this.getRight().getHeight() == -1;
        }

        public boolean isUnaryNode() {
            return (this.getRight().isRealNode() && !this.getLeft().isRealNode()) || ((!this.getRight().isRealNode() && this.getLeft().isRealNode()));
        }

        public void setKey(int k) {
            this.key = k;
        }

        public void setValue(String value) {
            this.info = value;
        }
    }

}


