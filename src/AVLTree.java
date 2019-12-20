import java.util.ArrayList;
import java.util.List;

/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

/*submitted by:
Guy Dankovich, 313246811, guydankovich
Sidney Feiner, 328871199, sidneyfeiner
*/

public class AVLTree {
    private IAVLNode root; //root
    private int size; //size of tree
    private IAVLNode virtualNode = new AVLNode(-1, "V", true); //one virtual node for all leafs of tree
    private IAVLNode maxNode; //node with Maximum key
    private IAVLNode minNode; //node with Minimum key

    public AVLTree(IAVLNode root) {
        this.root = root;
        root.setParent(null);
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

    /*searches for a node in the tree recursively*/
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
        IAVLNode node = searchRecNode(this.root, k);
        if (node != null) {
            return node.getValue();
        }
        return null;
    }

    /*inserts a node into the tree, returns true if successfully inserted*/
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
    /*rebalances AVL tree after insertion of node*/
    private int rebalanceTree(IAVLNode node) {
        if (node == this.root) {
            return 0;
        }
        IAVLNode parent = node.getParent();

        // getLeft/getRight always exist because we have virtual nodes
        int leftRankDiff = parent.getHeight() - parent.getLeft().getHeight();
        int rightRankDiff = parent.getHeight() - parent.getRight().getHeight();

        if (leftRankDiff + rightRankDiff == 1) {
            // Case of 1-0 or 0-1
            promoteNode(parent);
            return 1 + rebalanceTree(parent);
        } else if (leftRankDiff + rightRankDiff == 2 && leftRankDiff != 1) {
            // case of 2-0 or 0-2 or 1-3+ or 3+-1

            int ops = 2;
            if (rightRankDiff == 0) {
                // 2-0
                parent = node;
                if (node.getHeight() - node.getLeft().getHeight() == 1 && node.getHeight() - node.getRight().getHeight() == 2) {
                    IAVLNode left = node.getLeft();
                    rotateRight(left);
                    ops += 3;
                    node = left;
                }
                rotateLeft(node);
            } else {
                // 0-2
                parent = node;
                if (node.getHeight() - node.getLeft().getHeight() == 2 && node.getHeight() - node.getRight().getHeight() == 1) {
                    IAVLNode right = node.getRight();
                    rotateLeft(right);
                    ops += 3;
                    node = right;
                }
                rotateRight(node);
            }
            return ops + rebalanceTree(parent);
        }
        return 0;
    }

    /*increases sizes of all nodes from node until root by parameter amount*/
    private int increaseAncestorsSize(IAVLNode node, int increase) {
        if (node == null) return 0;
        node.setSize(node.getSize() + increase);
        return 1 + increaseAncestorsSize(node.getParent(), increase);
    }

    /*increases sizes of all nodes from node until root by 1*/
    private int incrementAncestorsSize(IAVLNode node) {
        return increaseAncestorsSize(node, 1);
    }

    /*decrements sizes of all nodes from node until root by 1*/
    private int decrementAncestorsSize(IAVLNode node) {
        return decreaseAncestorsSize(node, 1);
    }

    /*decrements sizes of all nodes from node until root by enetered amount*/
    private int decreaseAncestorsSize(IAVLNode node, int decrease) {
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
        AVLNode node = new AVLNode(k, i);
        if (this.root == null) {
            this.root = node;
            this.size++;
            this.minNode = node;
            this.maxNode = node;
            return 0;
        }
        boolean inserted = insertRec(this.root, node);
        if (!inserted) return -1;
        this.size++;
        incrementAncestorsSize(node.getParent());
        int rebalanceOpsAmount = rebalanceTree(node);
        if (this.minNode != null && this.maxNode != null) {
            // This if is not reached only when we are on a split method, and theat method
            // will update the min/max at the end
            if (k < this.minNode.getKey()) {
                this.minNode = node;
            }
            if (k > this.maxNode.getKey()) {
                this.maxNode = node;
            }
        }

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
        IAVLNode node = searchForNode(k); //finds node
        if (node == null) {
            return -1;
        }
        if (this.size() == 1) {
            this.minNode = null;
            this.maxNode = null;
        } else {
            if (k == this.minNode.getKey()) {
                this.minNode = getSuccessor(node);
            }
            if (k == this.maxNode.getKey()) {
                this.maxNode = getPredecessor(node);
            }
        }
        this.size--;
        IAVLNode parent = node.getParent();
        if (node.isLeaf()) {
            deleteLeaf(node);
            decrementAncestorsSize(parent);
            return (rebalanceAfterDelete(parent));
        } //deletes if node is leaf
        if (node.isUnaryNode()) {
            deleteUnaryNode(node);
            decrementAncestorsSize(parent);
            return (rebalanceAfterDelete(parent));
        } //deletes if node is unary node
        IAVLNode nodeToDelete = swapNodeWithSuccessor(node);
        parent = nodeToDelete.getParent();
        deleteAfterSuccessorSwap(nodeToDelete); // swaps regular node with successor then deletes
        decrementAncestorsSize(parent);
        int rebalanceOpsAmount = rebalanceAfterDelete(parent);
        return rebalanceOpsAmount;
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (minNode != null) {
            return this.minNode.getValue();
        }
        return null;
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (maxNode != null) {
            return this.maxNode.getValue();
        }
        return null;
    }

    /*puts all nodes in array sorted by key*/
    private void nodesToArrayRec(IAVLNode node, IAVLNode[] nodes, int offset) {
        if (node.getLeft().isRealNode()) {
            nodesToArrayRec(node.getLeft(), nodes, offset);
        }
        int relIndex = node.getLeft().getSize();
        nodes[offset + relIndex] = node;
        if (node.getRight().isRealNode()) {
            nodesToArrayRec(node.getRight(), nodes, offset + relIndex + 1);
        }
    }

    /*returns array of nodes in order by key*/
    private IAVLNode[] nodesToArray(IAVLNode node) {
        if (node == null) {
            return new IAVLNode[0];
        }
        IAVLNode[] nodes = new IAVLNode[node.getSize()];
        nodesToArrayRec(node, nodes, 0);
        return nodes;
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

    /*performs the split operation recursively*/
    private void splitRecBottomUp(IAVLNode node, AVLTree smaller, AVLTree bigger, List<Integer> complexities) {
        IAVLNode parent = node.getParent();
        if (node != this.root) {
            if (parent.getRight() == node) {
                // this is a right child
                if (parent.getLeft().isRealNode()) {
                    IAVLNode copyNode = new AVLNode(parent.getKey(), parent.getValue(), false, parent.getSize());
                    AVLTree subTree = new AVLTree(parent.getLeft());
                    complexities.add(smaller.join(copyNode, subTree));
                }
            } else if (parent.getLeft() == node) {
                // this is a left child
                if (parent.getRight().isRealNode()) {
                    IAVLNode copyNode = new AVLNode(parent.getKey(), parent.getValue(), false, parent.getSize());
                    AVLTree rightTree = new AVLTree(parent.getRight());
                    complexities.add(bigger.join(copyNode, rightTree));
                }
            }
            splitRecBottomUp(parent, smaller, bigger, complexities);
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
        AVLTree smaller = new AVLTree(), bigger = new AVLTree();
        if (node == this.getRoot()) {
            // recursive function stops at root, so in case we get the root, we split it here
            if (node.getLeft().isRealNode()) {
                smaller = new AVLTree(node.getLeft());
            }
            if (node.getRight().isRealNode()) {
                bigger = new AVLTree(node.getRight());
            }
            return new AVLTree[]{smaller, bigger};
        } else {
            // splitting node is not the root
            if (node.getRight().isRealNode()) {
                bigger = new AVLTree(node.getRight());
            } else {
                bigger = new AVLTree();
            }
            if (node.getLeft().isRealNode()) {
                smaller = new AVLTree(node.getLeft());
            } else {
                smaller = new AVLTree();
            }
            AVLTree[] result = {smaller, bigger};
            splitRecBottomUp(node, smaller, bigger, new ArrayList<>());
            smaller.updateMin();
            smaller.updateMax();
            bigger.updateMin();
            bigger.updateMax();
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
                return (joinBiggerKeyTree(x, t));
            } else {
                if (x.getKey() > this.getRoot().getKey()) {
                    return (joinSmallerKeyTree(x, t));
                } else { // equal height
                    return (joinTreeWithEqualHeight(x, t));
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

    }

    /*performs join between this tree to a tree with bigger keys,returns cost (height difference + 1)*/
    private int joinBiggerKeyTree(IAVLNode x, AVLTree t) {
        int cost = Math.abs(this.getRoot().getHeight() - t.getRoot().getHeight()) + 1;
        this.maxNode = t.maxNode;
        this.size = this.size + t.size() + 1;
        if (t.getRoot().getHeight() > this.getRoot().getHeight()) { //if t is of larger rank
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
            if (parent == null) {
                this.root = x;
            } else {
                this.root = t.getRoot();
                parent.setLeft(x);
                increaseAncestorsSize(x.getParent(),this.getRoot().getSize());
                rebalanceTree(x);
            }
        } else { //if t is of smaller rank
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
            if (parent == null) {
                this.root = x;
            } else {
                parent.setRight(x);
                increaseAncestorsSize(x.getParent(),t.getRoot().getSize());
                rebalanceTree(x);
            }

        }

        return cost;
    }

    /*performs join between this tree to a tree with smaller keys,returns cost (height difference + 1)*/
    private int joinSmallerKeyTree(IAVLNode x, AVLTree t) {
        this.minNode = t.minNode;
        this.size = this.size + t.size() + 1;
        int cost = Math.abs(this.getRoot().getHeight() - t.getRoot().getHeight()) + 1;
        if (t.getRoot().getHeight() < this.getRoot().getHeight()) { //if t is of smaller rank
            IAVLNode joinNode = this.getRoot();
            while (joinNode.getHeight() >= t.getRoot().getHeight()) {
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

                parent.setRight(x);
                increaseAncestorsSize(x.getParent(),t.getRoot().getSize());
                rebalanceTree(x);
            }
        } else { //if t is of larger rank
            IAVLNode joinNode = t.getRoot();
            while (joinNode.getHeight() > this.getRoot().getHeight()) {
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
                this.root = t.getRoot();
                parent.setLeft(x);
                increaseAncestorsSize(x.getParent(),this.getRoot().getSize());
                rebalanceTree(x);
            }
        }

        return cost;
    }

    /*performs join between this tree to a tree with equal height, returns difference in height (1)*/
    private int joinTreeWithEqualHeight(IAVLNode x, AVLTree t) {
        this.size = this.size + t.size() + 1;
        if (x.getKey() > this.getRoot().getKey()) { //if x and t keys are larger than this tree
            this.maxNode = t.maxNode;
            x.setRight(t.getRoot());
            t.getRoot().setParent(x);
            x.setLeft(this.getRoot());
            this.getRoot().setParent(x);
            this.root = x;
            x.setParent(null);

        } else { //x and t keys are smaller than this tree keys
            this.minNode = t.minNode;
            x.setLeft(t.getRoot());
            t.getRoot().setParent(x);
            x.setRight(this.getRoot());
            this.getRoot().setParent(x);
            this.root = x;
            x.setParent(null);
        }
        x.setSize(x.getLeft().getSize() + x.getRight().getSize()+1);
        return 1;
    }

    /*performs left rotation on the edge between the node and his parent and updates heights*/
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


        parent.setSize(parent.getLeft().getSize() + parent.getRight().getSize() + 1);
        node.setSize(node.getLeft().getSize() + node.getRight().getSize() + 1);
    }

    /*performs right rotation on the edge between the node and his parent and updates heights*/
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


        parent.setHeight(1 + Math.max(parent.getRight().getHeight(), parent.getLeft().getHeight()));
        node.setHeight(1 + Math.max(node.getRight().getHeight(), node.getLeft().getHeight()));

        parent.setSize(parent.getLeft().getSize() + parent.getRight().getSize() + 1);
        node.setSize(node.getLeft().getSize() + node.getRight().getSize() + 1);
    }

    /*returns predecessor of the node*/
    private IAVLNode getPredecessor(IAVLNode node) {
        IAVLNode y = node;
        IAVLNode parent;
        if (node.getLeft().isRealNode()) {
            y = node.getLeft();
            while (y.getRight().isRealNode()) {
                y = y.getRight();
            }
            return y;
        } else {
            while (y.getParent().getLeft() == y) {
                y = y.getParent();
            }
            return y.getParent() == null ? y : y.getParent();
        }
    }

    /*returns successor of the node*/
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
            while (y.getParent().getRight() == y) {
                y = y.getParent();
            }
            return y.getParent() == null ? y : y.getParent();
        }
    }

    /*returns the difference in rank between the node and his left child*/
    private int rankDifferenceLeft(IAVLNode node) {
        return node.getHeight() - node.getLeft().getHeight();
    }

    /*returns the difference in rank between the node and his right child*/
    private int rankDifferenceRight(IAVLNode node) {
        return node.getHeight() - node.getRight().getHeight();
    }

    /*returns true if the node is balanced (1-1,2-1,1-2 node)*/
    private boolean isRankDifferenceLegal(IAVLNode node) {
        return (rankDifferenceLeft(node) == 1 && rankDifferenceRight(node) == 1) || (rankDifferenceLeft(node) == 2 && rankDifferenceRight(node) == 1) || (rankDifferenceLeft(node) == 1 && rankDifferenceRight(node) == 2);
    }

    /*searches for a node in tree and returns it if found*/
    private IAVLNode searchForNode(int k) {
        return searchRecNode(this.root, k);
    }

    /*rebalances AVL tree after deletion of a node, returns cost (number of rebalance operations)*/
    private int rebalanceAfterDelete(IAVLNode parent) {
        int sum = 0; //sum of rebalance operations
        while (parent != null && !isRankDifferenceLegal(parent)) {
            IAVLNode originalParent = parent.getParent();
            if (rankDifferenceRight(parent) == 2 && rankDifferenceLeft(parent) == 2) {
                parent.setHeight(parent.getHeight() - 1);
                sum += 1;
            } //2-2 case
            if (rankDifferenceRight(parent) == 1 && rankDifferenceLeft(parent) == 3 && rankDifferenceLeft(parent.getRight()) == 1 && rankDifferenceRight(parent.getRight()) == 1) {
                rotateLeft(parent.getRight());
                sum += 3;
            } //3-1,1-1 case
            if (rankDifferenceRight(parent) == 3 && rankDifferenceLeft(parent) == 1 && rankDifferenceLeft(parent.getLeft()) == 1 && rankDifferenceRight(parent.getLeft()) == 1) {
                rotateRight(parent.getLeft());
                sum += 3;
            }//1-3,1-1 case

            if (rankDifferenceRight(parent) == 1 && rankDifferenceLeft(parent) == 3 && rankDifferenceLeft(parent.getRight()) == 2 && rankDifferenceRight(parent.getRight()) == 1) {
                rotateLeft(parent.getRight());
                sum += 3;
            }//3-1,1-2 case
            if (rankDifferenceRight(parent) == 3 && rankDifferenceLeft(parent) == 1 && rankDifferenceLeft(parent.getLeft()) == 1 && rankDifferenceRight(parent.getLeft()) == 2) {
                rotateRight(parent.getLeft());
                sum += 3;
            }//1-3,2-1 case

            if (rankDifferenceRight(parent) == 1 && rankDifferenceLeft(parent) == 3 && rankDifferenceLeft(parent.getRight()) == 1 && rankDifferenceRight(parent.getRight()) == 2) {
                rotateRight(parent.getRight().getLeft());
                rotateLeft(parent.getRight());
                sum += 6;
            }//3-1,2-1 case

            if (rankDifferenceRight(parent) == 3 && rankDifferenceLeft(parent) == 1 && rankDifferenceLeft(parent.getLeft()) == 2 && rankDifferenceRight(parent.getLeft()) == 1) {
                rotateLeft(parent.getLeft().getRight());
                rotateRight(parent.getLeft());
                sum += 6;
            }//1-3,1-2 case
            parent = originalParent;
        }
        return sum;
    }

    /*deletes a node after it was swapped by a successor*/
    private void deleteAfterSuccessorSwap(IAVLNode nodeToDelete) {
        if (nodeToDelete.isLeaf()) {
            deleteLeaf(nodeToDelete);
        } else {
            deleteUnaryNode(nodeToDelete);
        }
    }

    /*deletes the node which is a leaf from the tree*/
    private void deleteLeaf(IAVLNode node) {

        if (node == this.root) {
            this.root = null;
        } else {
            if (node.getParent().getRight() == node) { // Y is right child of parent
                node.getParent().setRight(virtualNode);
            } else {
                node.getParent().setLeft(virtualNode);
            }

        }
    }

    /*deletes the node which is a unary node from the tree*/
    private void deleteUnaryNode(IAVLNode node) {

        if (node == this.root) {
            if (node.getRight().isRealNode()) {
                this.root = node.getRight();
                this.root.setParent(null);
            } else {
                this.root = node.getLeft();
                this.root.setParent(null);
            }
        } else {
            IAVLNode parent = node.getParent();
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
    }

    /*swaps between key and value of node with its successor*/
    private IAVLNode swapNodeWithSuccessor(IAVLNode node) {
        IAVLNode successor = getSuccessor(node);
        swapNode(node, successor);
        return successor;
    }

    /*swaps between the key and values of two nodes*/
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
        if(node1 == minNode){
            node2 = minNode;
        }
        if(node2 == maxNode){
            node1 = maxNode;
        }

    }

    /*sets minKey,minValue to min of tree*/
    private void updateMin() {
        if (this.size == 0) minNode = null;
        else if (this.size == 1) minNode = this.root;
        else {
            IAVLNode y = this.root;
            while (y.getLeft().isRealNode()) {
                y = y.getLeft();
            }
            this.minNode = y;
        }
    }

    /*sets minKey,minValue to min of tree*/
    private void updateMax() {
        if (this.size == 0) maxNode = null;
        else if (this.size == 1) maxNode = this.root;
        else {
            IAVLNode y = this.root;
            while (y.getRight().isRealNode()) {
                y = y.getRight();
            }
            this.maxNode = y;
        }
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */

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
            return getHeight() != -1;
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


