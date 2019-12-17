import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by user:sid
 * Date:12/16/19
 */
public class TimingsSplit {

    static int getWeirdSplitKey(AVLTree tree) {
        AVLTree.IAVLNode node = tree.getRoot().getLeft();
        while (node.getRight().isRealNode()) node = node.getRight();
        return node.getKey();
    }

    static void firstTest(boolean isRandomKey) {
        double sumComplexity = 0;
        double maxComplexity = 0;
        for (int i = 1; i <= 10; i++) {
            List<Integer> vals = IntStream.rangeClosed(1, 10000 * i).boxed().collect(Collectors.toList());
            Collections.shuffle(vals);
            AVLTree tree = new AVLTree();
            for (int j = 0; j < 10000 * i; j++) {
                tree.insert(vals.get(j), Integer.toString(vals.get(j)));
            }

            int keyToSplit = isRandomKey ? vals.get(new Random().nextInt(vals.size())) : getWeirdSplitKey(tree);
            List<Integer> complexities = tree.splitAnalysis(keyToSplit);
            maxComplexity = Collections.max(complexities);
            sumComplexity = complexities.stream().mapToInt(Integer::intValue).sum();

            System.out.println(String.format("for %d sized tree, split complexityAvg=%f, complexityMax=%f",
                    tree.size(), sumComplexity / complexities.size(), maxComplexity));
            System.out.println("---------------------------------------------------");
        }
    }

    public static void main(String[] args) {
        System.out.println("split by random key");
        firstTest(true);
        System.out.println("#######################################");
        System.out.println("split by maximum in left subtree");
        firstTest(false);
    }

}
