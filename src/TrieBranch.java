public class TrieBranch {
    private char value;
    private TrieNode node;
    public TrieNode parentNode;

    public TrieBranch(char value, TrieNode node) {
        this.value = value;
        this.node = node;
        this.node.parentBranch = this;
    }

    public TrieBranch(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public TrieNode getNode() {
        return node;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setNode(TrieNode node) {
        this.node = node;
        this.node.parentBranch = this;
    }

    public void setParent(TrieNode parent) {
        this.parentNode = parent;
    }

    public boolean pointsToNil() {
        return node.isNil();
    }
}
