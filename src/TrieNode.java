import java.util.ArrayList;
import java.util.Arrays;

public class TrieNode implements TrieNodeInterface {
    public Sensor sensor;
    public TrieBranch parentBranch;
    public ArrayList<TrieBranch> children;
    private int count;

    private TrieNode(Sensor sensor, TrieBranch parent) {
        this.sensor = sensor;
        this.parentBranch = parent;
        parent.setNode(this);
    }

    // The default one (used for NIL nodes)
    public TrieNode() {
    }

    @Override
    public boolean isNil() {
        return sensor == null;
    }

    @Override
    public void add(Sensor sensor, int idx) {
        TrieNode childWithAppropriateKey = null;

        for (TrieBranch child : children) {
            if (child.getValue() == sensor.getId().charAt(idx)) {
                childWithAppropriateKey = child.getNode();
                break;
            }
        }

        TrieBranch newBranch = new TrieBranch(sensor.getId().charAt(idx));
        new TrieNode(sensor, newBranch);

        if (childWithAppropriateKey == null) {
            // If there is no such branch
            this.addChildren(newBranch);
        } else {
            // If there is a branch with appropriate key, check if it's NIL or not
            if (childWithAppropriateKey.isNil()) {
                childWithAppropriateKey.add(sensor, ++idx);
            } else {
                if (sensor.getId().compareTo(childWithAppropriateKey.sensor.getId()) == 0) {
                    // Do nothing if the same sensor wants to be added again
                    return;
                }

                // Update branches values, create NIL node, replace old node with NIL and add both nodes as NIL children
                newBranch.setValue(sensor.getId().charAt(idx + 1));
                SensorTrieNilNode newNilNode = new SensorTrieNilNode();
                childWithAppropriateKey.parentBranch.setNode(newNilNode);
                TrieBranch newBranchForChildWithAppropriateKey = new TrieBranch(childWithAppropriateKey.sensor.getId().charAt(idx + 1), childWithAppropriateKey);
                newNilNode.addChildren(newBranchForChildWithAppropriateKey, newBranch);
            }
        }
    }

    @Override
    public TrieNode getParentNode() {
        if (parentBranch != null) {
            return parentBranch.parentNode;
        }

        return null;
    }

    @Override
    public void delete(TrieNode node) {
        if (node == null || node.isNil() || node.getParentNode() == null) {
            return;
        }

        if (node.getParentNode().getParentNode() == null || node.getParentNode().children.size() > 2) {
            // node.parentNode == root OR there are more than 1 node left after we delete this one
            removeNodeReferenceFromParent(node);
            return;
        }

        if (node.getParentNode().children.size() == 2) {
            removeNodeReferenceFromParent(node);

            TrieNode brother = node.getParentNode().children.get(0).getNode();

            if (!node.isNil()) {
                TrieNode parentNode = node.getParentNode();

                if (node.getParentNode().isTheOnlyChild()) {
                    ;
                    while (parentNode.getParentNode().isTheOnlyChild()) {
                        parentNode = parentNode.getParentNode();
                    }
                }

                parentNode.parentBranch.setNode(brother);
            }
        }
    }

    private void removeNodeReferenceFromParent(TrieNode node) {
        node.getParentNode().children.remove(node.parentBranch);
    }

    private boolean isTheOnlyChild() {
        return getParentNode() != null && getParentNode().children.size() == 1;
    }

    private boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    @Override
    public void addChildren(TrieBranch... children) {
        for (TrieBranch branch : children) {
            branch.setParent(this);
        }

        this.children.addAll(new ArrayList<>(Arrays.asList(children)));
    }

    @Override
    public int size() {
        if (children != null) {
            int childrenSize = 0;
            int noNilChildrenAmount = 0;

            for (TrieBranch child : children) {
                // We don't count NIL nodes
                if (!child.pointsToNil()) {
                    noNilChildrenAmount++;
                }
            }

            for (TrieBranch child : children) {
                childrenSize += child.getNode().size();
            }

            return noNilChildrenAmount + childrenSize;
        }

        return 0;
    }

    @Override
    public TrieNode find(String id, int idx) {
        if (hasChildren()) {
            for (TrieBranch child : children) {
                if (child.getValue() == id.charAt(idx)) {
                    if (!child.pointsToNil() && child.getNode().sensor.getId().compareTo(id) == 0) {
                        // If exactly the one we are looking for
                        count++;
                        return child.getNode();
                    }

                    // If prefix is right, but the node is NIL
                    count++;
                    return child.getNode().find(id, ++idx);
                }
            }
        }

        if (this.isNil()) {
            return findSmallest();
        }

        return this;
    }

    @Override
    public TrieNode findSmallest() {
        TrieNode tmpNode = this;

        while (tmpNode.hasChildren()) {
            TrieBranch smallestValueBranch = tmpNode.children.get(0);

            for (TrieBranch child : tmpNode.children) {
                if (smallestValueBranch.getValue() > child.getValue()) {
                    smallestValueBranch = child;
                }
            }

            tmpNode = smallestValueBranch.getNode();
        }

        return tmpNode;
    }

    public ArrayList<TrieNode> getUntouchedSammelpunkte() {
        ArrayList<TrieNode> arr = new ArrayList<>();

        if (!isNil() && sensor.didNotGetAnyRequests()) {
            arr.add(this);
        }

        if (isNil()) {
            for (TrieBranch child : children) {
                arr.addAll(child.getNode().getUntouchedSammelpunkte());
            }
        }

        return arr;
    }
}
