/**
 * Test
 */

public class Test {
	
	public static void main(String[] args) {
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
	}

	public static void printTree(AVLTree.IAVLNode node, int level){ //call with root,level=0
	    if(node==null)
	         return;
	    printTree(node.getRight(), level+1);
	    if(level != 0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	        	if(node.getParent() != null) {
	        		System.out.println("|------- " + node.getKey()+ " " + getBF(node) + " " + node.getParent().getKey()); 
	        	}
	        	else{
	        		System.out.println("|------- " + node.getKey()+ " " + getBF(node)); //+ node.getHeight() + " " + node.getSubtreeSize());
	        	}
	    }
	    else
	    	if(node.getParent() != null) {
        		System.out.println(node.getKey() + " " + getBF(node) + " " + node.getParent().getKey()); 
        	}
	    	else {
	        System.out.println(node.getKey() + " " + getBF(node));//node.getHeight()+" " + node.getSubtreeSize());
	    	}
	    printTree(node.getLeft(), level+1);
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


