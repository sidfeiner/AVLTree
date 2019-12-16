import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by user:sid
 * Date:12/16/19
 */
public class TimingsGeneral {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            double rebalanceInsertsSum = 0;
            double rebalanceDeletesSum = 0;
            double rebalanceInsertMax = 0;
            double rebalanceDeleteMax = 0;
            List<Integer> vals = IntStream.rangeClosed(1, 10000 * i).boxed().collect(Collectors.toList());
            List<Integer> valsOrdered = IntStream.rangeClosed(1, 10000 * i).boxed().collect(Collectors.toList());
            Collections.sort(valsOrdered);
            Collections.shuffle(vals);
            AVLTree tree = new AVLTree();
            int rebalanceAmount;
            for (int j = 0; j < 10000 * i; j++) {
                rebalanceAmount = tree.insert(vals.get(j), Integer.toString(vals.get(j)));
                rebalanceInsertsSum += rebalanceAmount;
                if (rebalanceAmount > rebalanceInsertMax) {
                    rebalanceInsertMax = rebalanceAmount;
                }
            }
            for (int j = 0; j < 10000 * i; j++) {
                rebalanceAmount = tree.delete(valsOrdered.get(j));
                rebalanceDeletesSum += rebalanceAmount;
                if (rebalanceAmount > rebalanceDeleteMax) {
                    rebalanceDeleteMax = rebalanceAmount;
                }
            }

            System.out.println(String.format("for %d inserts and deletes, we have the following:", 10000 * i));
            System.out.println(String.format(
                    "rebalanceInsertsAvg: %f, rebalanceInsertMax: %f, rebalanceDeletesAvg: %f, rebalanceDeletesMax: %f"
                    , rebalanceInsertsSum / (10000 * i), rebalanceInsertMax, rebalanceDeletesSum / (10000 * i), rebalanceDeleteMax));
            System.out.println("---------------------------------------------------");
        }
    }

}
