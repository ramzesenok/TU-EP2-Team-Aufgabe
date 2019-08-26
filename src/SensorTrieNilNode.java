import java.util.ArrayList;
import java.util.Arrays;

public class SensorTrieNilNode extends TrieNode {
    public SensorTrieNilNode(TrieBranch... children) {
        this.sensor = null;
        this.children = new ArrayList<>(Arrays.asList(children));
    }
}
