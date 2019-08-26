import java.util.ArrayList;

public class SensorsTrie {
    private TrieNode root;

    public SensorsTrie() {
        root = new SensorTrieNilNode();
    }

    public void add(Sensor sensor) {
        root.add(sensor, 0);
    }

    public void delete(TrieNode node) { root.delete(node); }

    public int size() {
        return root.size();
    }

    public TrieNode find(String id) {
        return root.find(id, 0);
    }

    public ArrayList<TrieNode> getUntouchedCollector() {
        return root.getUntouchedSammelpunkte();
    }
}
