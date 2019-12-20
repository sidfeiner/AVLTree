import java.util.Arrays;

class moreTests {
    public static void main(String[] args) {
//------------run this part always-------------
        AVLTree tree = new AVLTree();
        AVLTree tree2 = new AVLTree();
        AVLTree emptytree = new AVLTree();
        AVLTree emptytree2 = new AVLTree();
        int operation;
//------------run this part always-------------


//---------------insert------------------------
	 operation = tree.insert(10, "a");
	 System.out.println("insert a with key 10 :"+operation); // empty
         BTreePrinter.printNode(tree.getRoot());
	 operation =tree.insert(15, "b");
	 System.out.println("insert b with key 15 :"+operation); //case1
        BTreePrinter.printNode(tree.getRoot());
	 operation =tree.insert(17, "c");
	 System.out.println("insert c with key 17 :"+operation); //case1 -> case2 right
        BTreePrinter.printNode(tree.getRoot());
	 operation =tree.insert(13,"d");
	 System.out.println("insert d with key 13 :"+operation);  //case1 -> case1
        BTreePrinter.printNode(tree.getRoot());
	 operation =tree.insert(3,"e");
	 System.out.println("insert e with key 3 :"+operation);   //case0
        BTreePrinter.printNode(tree.getRoot());
	 operation =tree.insert(5,"f");
	 System.out.println("insert f with key 5 :"+operation);    //case1 -> case1 -> case2 left
        BTreePrinter.printNode(tree.getRoot());


	 operation = tree.insert(20, "a");
	 operation = tree.insert(15, "a");
	 operation = tree.insert(10, "a");
	 operation = tree.insert(25, "a");
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.insert(18, "a");
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.insert(17, "a");
	 System.out.println("insert key 17 :"+operation);  //case1 -> case1 -> case3 right
        BTreePrinter.printNode(tree.getRoot());

	 operation = tree.insert(20, "a");
	 operation = tree.insert(25, "a");
	 operation = tree.insert(30, "a");
	 operation = tree.insert(15, "a");
	 operation = tree.insert(22, "a");
//	 drawTree(tree.root,30);
	 operation = tree.insert(23, "a");
	 System.out.println("insert key 28 :"+operation);  //case1 -> case1 -> case3 left
        BTreePrinter.printNode(tree.getRoot());


	 //test Search
	 System.out.println("is 22 value is "+tree.search(22));
	 System.out.println("is 8 value is "+tree.search(8));
	 System.out.println("is 15 value is "+tree.search(15));
	 System.out.println("empty tree - is 15 value is "+emptytree.search(15));

	 //test empty
	 System.out.println("is tree empty? "+tree.empty());
	 System.out.println("is tree empty? "+emptytree.empty());

	 //test getSize
	 System.out.println("tree size "+tree.size());
	 System.out.println("empty tree size "+emptytree.size());

	 //test getRoot
	 System.out.println("tree root is  ("+tree.getRoot().getKey()+")");
	 System.out.println("empty tree root is "+ emptytree.getRoot());



//	---------- delete ----------------
	 operation = tree.insert(10, "a");
	 operation = tree.insert(15, "b");
	 operation = tree.insert(17, "c");
	 operation = tree.insert(13,"d");
	 operation = tree.insert(3,"e");;
	 operation = tree.insert(5,"f");
        BTreePrinter.printNode(tree.getRoot());

	 operation = tree.delete(17);
	 System.out.println("delete key 17 :"+operation);  // node is leaf. case0
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.delete(15);
	 System.out.println("delete key 15 :"+operation);  // node is unary. case1
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.delete(10);
	 System.out.println("delete key 10 :"+operation);  // node is root with 2 children. Case4 right
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.delete(5);
	System.out.println("delete key 5 :"+operation);  // node is root with 2 children. Case0
        BTreePrinter.printNode(tree.getRoot());


//	//---------- delete ----------------
	 operation = tree.insert(20, "c");
	 operation = tree.insert(25, "c");
	 operation = tree.insert(16, "c");
	 operation = tree.insert(19, "c");
	 operation = tree.insert(1, "c");
        BTreePrinter.printNode(tree.getRoot());

//	 operation = tree.delete(100);
//	 System.out.println("delete key 100 :"+operation);  // exspected -1
//	 operation = tree.delete(1);
//	 System.out.println("delete key 1:"+operation);  // Case0
//	 drawTree(tree.root,30);
//	 operation = tree.delete(3);
//	 System.out.println("delete key 3 :"+operation);  // Case0
//	 drawTree(tree.root,30);
//	 operation = tree.delete(13);
//	 System.out.println("delete key 13 :"+operation);  // Case2 left
//	 drawTree(tree.root,30);


	 operation = tree.delete(25);
	 System.out.println("delete key 25:"+operation);  // Case0
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.delete(20);
	 System.out.println("delete key 20 :"+operation);  // Case0
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.delete(19);
	 System.out.println("delete key 19 :"+operation);  // Case2 right
        BTreePrinter.printNode(tree.getRoot());

	//---------- delete ----------------
	 operation = tree.insert(15, "a");
	 operation = tree.insert(5, "b");
	 operation = tree.insert(17, "c");
	 operation = tree.insert(18,"e");;
        BTreePrinter.printNode(tree.getRoot());

	 operation = tree.delete(5);
	 System.out.println("delete key 5:"+operation);  // Case3 left
        BTreePrinter.printNode(tree.getRoot());
	//---------- delete ----------------
	 operation = tree.insert(15, "a");
	 operation = tree.insert(5, "b");
	 operation = tree.insert(17, "c");
	 operation = tree.insert(2,"e");;
        BTreePrinter.printNode(tree.getRoot());

	 operation = tree.delete(17);
	 System.out.println("delete key 17:"+operation);  // Case3 right
        BTreePrinter.printNode(tree.getRoot());

	 //	---------- delete ----------------
	 operation = tree.insert(10, "a");
	 operation = tree.insert(15, "b");
	 operation = tree.insert(5,"d");
	 operation = tree.insert(13,"f");
        BTreePrinter.printNode(tree.getRoot());
	 operation = tree.delete(5);
	 System.out.println("delete key 5 :"+operation);  // node is root with 2 children. Case4 right
        BTreePrinter.printNode(tree.getRoot());

//        	---------- delete ----------------
	 operation = tree.insert(10, "10");
	 operation = tree.insert(15, "15");
	 operation = tree.insert(5,"5");
	 operation = tree.insert(13,"13");
	 operation = tree.insert(7, "7");
	 operation = tree.insert(1, "1");
	 operation = tree.insert(9,"9");
	 operation = tree.insert(35,"35");
	 operation = tree.insert(22, "22");
	 operation = tree.insert(11, "11");
	 operation = tree.insert(79,"79");
	 operation = tree.insert(12,"12");
	 operation = tree.insert(2,"2");
	 operation = tree.insert(4,"4");
        BTreePrinter.printNode(tree.getRoot());

	 operation = tree.delete(7);
	 System.out.println("delete key 7 :"+operation);  // node is root with 2 children. Case4 right
        BTreePrinter.printNode(tree.getRoot());
	 System.out.println("min is "+tree.min());
	 System.out.println("max is "+tree.max());
	 System.out.println("empty - min is "+ emptytree.min());
	 System.out.println("empty - max is "+ emptytree.max());


// ------------------ Array part---------------------
	 System.out.println(Arrays.toString(tree.keysToArray()));
	 System.out.println(Arrays.toString(emptytree.keysToArray()));
	 System.out.println(Arrays.toString(tree.infoToArray()));
	 System.out.println(Arrays.toString(emptytree.infoToArray()));


// ------------------ join & Split---------------------

        operation = tree.insert(10, "10");
        operation = tree.insert(15, "15");
        operation = tree.insert(5, "5");
        operation = tree.insert(13, "13");
        operation = tree.insert(7, "7");
        operation = tree.insert(1, "1");
        operation = tree.insert(9, "9");
        operation = tree.insert(35, "35");
        System.out.println("tree:");
        BTreePrinter.printNode(tree.getRoot());

        operation = tree2.insert(110, "10");
        operation = tree2.insert(115, "15");
        operation = tree2.insert(120, "5");
        operation = tree2.insert(113, "13");
        operation = tree2.insert(170, "7");
        operation = tree2.insert(111, "1");
        operation = tree2.insert(190, "9");
        operation = tree2.insert(135, "35");
        System.out.println("tree2:");
        BTreePrinter.printNode(tree2.getRoot());

        AVLTree tree3 = new AVLTree();
        tree3.insert(100, "100");
        System.out.println("join comlexity is: " + tree.join(tree3.getRoot(), tree2));
        System.out.println("tree + 2:");
        BTreePrinter.printNode(tree.getRoot());


        AVLTree[] afterSplit = tree.split(100);
        System.out.println("-------------------After Split t1:------------------");
        BTreePrinter.printNode(afterSplit[0].getRoot());

        System.out.println("------------------After Split t2:-------------------");
        BTreePrinter.printNode(afterSplit[1].getRoot());


    }
}