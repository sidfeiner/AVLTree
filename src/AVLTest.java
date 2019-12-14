import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class AVLTest {

	private static int[] randomArray(int size, int min, int max)
	{
		int[] arr = new int[size];
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = (int)(Math.random() * (max - min) + min);
		}

		return arr;
	}

	private static TestTree arrayToTree(int[] arr)
	{
		TestTree tree = new TestTree();
		for (int x : arr)
		{
			tree.insert(x, Integer.toString(x));
		}

		return tree;
	}

	void testEmpty()
	{
		AVLTree tree = new TestTree();

		assert(tree.empty()) : "Tree isn't empty";

		tree.insert(1, "1");
		assert(!tree.empty()) : "Tree empty after insert";

		tree.delete(1);
		assert(tree.empty()) : "Tree isn't empty after delete";
		System.out.println("testEmpty Passed!");
	}

	void testSearch()
	{
		AVLTree tree = new TestTree();

		assert(tree.search(1) == null) : "non-existent key found in search";

		tree.insert(1, "1");
		assert(tree.search(1) == "1") : "couldn't find existing key in search";

		tree.delete(1);
		assert(tree.search(1) == null) : "non-existent key found in search after deleted";
		System.out.println("testSearch Passed!");
	}

	void testInsert()
	{
		AVLTree tree = new TestTree();

		assert(tree.insert(1, "1") == 0) : "unexpected insert value";
		assert(tree.insert(1, "1") == -1) : "existing key insterted";
		assert(tree.insert(2, "2") == 1) : "unexpected insert value";
		// RR
		assert(tree.insert(3, "3") == 3) : "unexpected insert value";

		int[] values = randomArray(30000, 0, 100);
		tree = arrayToTree(values);
		List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
		Collections.shuffle(valuesShuffled);
		for (int x : valuesShuffled)
		{

			tree.insert(x,Integer.toString(x));

		}

		System.out.println("testInsert Passed!");
	}

	void testDelete()
	{
		AVLTree tree = new TestTree();

		assert(tree.delete(1) == -1) : "unexpected delete value";

		tree.insert(1, "1");
		assert(tree.delete(1) == 0) : "unexpected insert value";

		int i=0;

		tree.insert(2, "2");
		tree.insert(1, "1");
		tree.insert(3, "3");
		tree.insert(4, "4");



		// should be RR rotation
		assert(tree.delete(1) == 3) : "unexpected delete value";

		int[] values = randomArray(10000, 0, 100);
		tree = arrayToTree(values);
		List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
		Collections.shuffle(valuesShuffled);
		for (int x : valuesShuffled)
		{

			tree.delete(x);

		}
		System.out.println("testDelete Passed!");
	}

	// Simulate random insert and delete operations
	void testInsertAndDelete()
	{
		AVLTree tree = new TestTree();

		for (int tries = 0; tries < 50; tries++)
		{
			int[] values = randomArray(35000, 0, 100);
			int i=0;
			List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
			Collections.shuffle(valuesShuffled);

			List<Integer> valuesShuffled2 = new ArrayList<Integer>(valuesShuffled);
			Collections.shuffle(valuesShuffled2);
			for (int x : valuesShuffled)
			{
				if (x < 0)
					tree.delete(-x);
				else
					tree.insert(x, Integer.toString(x));
			}
		}
		System.out.println("testInsertAndDelete Passed!");
	}

	void testMinMax()
	{
		for (int i = 0; i < 10; i++)
		{
			int[] values = randomArray(100, 0, 100);

			AVLTree tree = arrayToTree(values);
			values = Arrays.stream(values).distinct().toArray();
			Arrays.sort(values);
			assert(tree.max().equals(Integer.toString(values[values.length - 1]))) : "unexpected max value";
			assert(tree.min().equals(Integer.toString(values[0]))) : "unexpected min value";

			tree.delete(Integer.parseInt(tree.max()));

			assert(tree.max().equals(Integer.toString(values[values.length - 2]))) : "unexpected max value";

			tree.delete(Integer.parseInt(tree.min()));
			assert(tree.min().equals(Integer.toString(values[1]))) : "unexpected min value";
		}

		// Empty tree
		AVLTree tree = new TestTree();
		assert(tree.max() == null) : "max should be null on empty tree";
		assert(tree.min() == null) : "min should be null on empty tree";
		System.out.println("testMinMax Passed!");
	}

	void testKeysToArray()
	{
		int[] values = randomArray(100, 0, 100);
		AVLTree tree = arrayToTree(values);
		Arrays.sort(values);
		int[] arr1 = tree.keysToArray(), arr2 = Arrays.stream(values).distinct().toArray();
		assert(arr1.length == arr2.length) : "keys array length different from expected value";
		for (int i = 0; i < arr1.length; i++)
			assert(arr1[i] == arr2[i]) : "key value different from expected value";
		System.out.println("testKeysToArray Passed!");
	}

	void testInfoToArray()
	{
		int[] values = randomArray(100, 0, 100);
		AVLTree tree = arrayToTree(values);
		Arrays.sort(values);
		Object[] arr1 = tree.infoToArray(), arr2 = Arrays.stream(values).distinct().mapToObj(x -> Integer.toString(x)).toArray();
		assert(arr1.length == arr2.length) : "info array length different from expected value";
		for (int i = 0; i < arr1.length; i++)
			assert(arr1[i].equals(arr2[i])) : "info value different from expected value";
		System.out.println("testInfoToArray Passed!");
	}

	void testSize()
	{
		int[] values = randomArray(100, 0, 100);
		AVLTree tree = arrayToTree(values);
		int realSize = (int)Arrays.stream(values).distinct().count();

		assert(tree.size() == realSize) : realSize + " vs " + tree.size();
		System.out.println("testSize Passed!");
	}

	void testGetRoot()
	{
		AVLTree tree = new TestTree();
		assert(null == tree.getRoot()) : "root isn't null on empty tree";

		tree.insert(1, "1");
		assert(1 == tree.getRoot().getKey()) : "unexpected root key";

		tree.insert(2, "2");
		assert(1 == tree.getRoot().getKey()) : "unexpected root key";

		tree.delete(1);
		assert(2 == tree.getRoot().getKey()) : "unexpected root key";

		assert(null == tree.getRoot().getParent()) : "root parent isn't null";
		System.out.println("testGetRoot Passed!");
	}

	void testJoin()
	{
		int[] arr1 = {1,2}, arr2 = {4};
		TestTree tree1 = arrayToTree(arr1), tree2 = arrayToTree(arr2);

		tree1.join(tree1.new AVLNode(3, "3"), tree2);
		tree1.check_if_balanced(tree1.getRoot());
		assert(tree1.sanitizeTree(tree1.getRoot()));
		System.out.println("testJoin Passed!");
	}

	static public class TestTree extends AVLTree {

		static private int get_left_diff(IAVLNode node)
		{
			return node.getHeight() - node.getLeft().getHeight();
		}

		static private int get_right_diff(IAVLNode node)
		{
			return node.getHeight() - node.getRight().getHeight();
		}

		static private int get_child_diff(IAVLNode node)
		{
			return get_left_diff(node) - get_right_diff(node);
		}

		public static void check_if_balanced(IAVLNode current)
		{
			if (current == null || !current.isRealNode())
				return;
			assert(!(get_child_diff(current) < -1 || get_child_diff(current) > 1)): "Unbalanced tree after insert/delete";
			check_if_balanced(current.getRight());
			check_if_balanced(current.getLeft());
		}

		public int insert(int k, String i) {
			int retval = super.insert(k, i);

			check_if_balanced(this.getRoot());
			assert sanitizeTree(this.getRoot());
			return retval;
		}

		public int delete(int k) {
			int retval = super.delete(k);

			check_if_balanced(this.getRoot());
			assert sanitizeTree(this.getRoot());
			return retval;
		}

		public static int calcHeight(IAVLNode node)
		{
			if (node == null || !node.isRealNode())
				return -1;
			return Math.max(calcHeight(node.getLeft()), calcHeight(node.getRight())) + 1;
		}

		public static int calcSize(IAVLNode node)
		{
			if (node == null || !node.isRealNode())
				return 0;
			int sizeLeft = calcSize(node.getLeft());
			int sizeRight = calcSize(node.getRight());
			int size = sizeLeft + sizeRight + 1;
			if (size != node.getSize())
			{
				assert false;
			}
			return size;
		}

		public static int calcSum(IAVLNode node)
		{
			if (node == null || !node.isRealNode())
				return 0;
			return calcSum(node.getLeft()) + calcSum(node.getRight()) + node.getKey();
		}

		public static boolean sanitizeTree(IAVLNode root)
		{
			if (root == null)
				return true;

			if (!root.isRealNode())
				return true;

			if (calcHeight(root) != root.getHeight())
			{
				System.out.format("Height should be %d but is %d%n", calcHeight(root), root.getHeight());
				return false;
			}

			if (calcSize(root) != root.getSize())
			{
				System.out.format("Size should be %d but is %d%n", calcSize(root), root.getSize());
				calcSize(root);
				return false;
			}

			int bf = calcHeight(root.getLeft()) - calcHeight(root.getRight());
			if (Math.abs(bf) >= 2)
			{
				System.out.format("Balance factor should be out of range %d%n", bf, root.getSize());
				return false;
			}

			return sanitizeTree(root.getLeft()) && sanitizeTree(root.getRight());
		}
	}

	public static void main(String[] args)
	{
		AVLTest test = new AVLTest();
		AVLTree tree = new TestTree();

		test.testEmpty();
		test.testSize();
		test.testDelete();
		test.testInsert();
		test.testSearch();
		test.testGetRoot();
		test.testMinMax();
		test.testInfoToArray();
		test.testKeysToArray();
		test.testInsertAndDelete();
		test.testJoin();
	}
}