public interface TrieNodeInterface {
    public boolean isNil();
    public void add(Sensor sensor, int idx);
    public void delete(TrieNode node);
    public void addChildren(TrieBranch... children);
    public TrieNode getParentNode();
    public int size();
    public TrieNode find(String id, int idx);
    public TrieNode findSmallest();
}
