package data;

import java.nio.file.NoSuchFileException;
import java.util.*;

/**
 * Created by uhligc on 22.01.14.
 */
public class Database {
    HashMap<String, Node> knoedel;

    public Database() {
        knoedel = new HashMap<>();
    }

    public Node getNode(String id) {
        return knoedel.get(id);
    }

    public void addNode(String id, String name) {
        knoedel.put(id, new Node(id, name));
    }

    public void addNode(Node node) {
        knoedel.put(node.getId(), node);
    }

    public void addChild(String parentid, Node thisnode) {
        Node parentnode = knoedel.get(parentid);
        parentnode.addChild(thisnode);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : knoedel.values()) {
            sb.append(node);
        }
        return "Database{" +
                "knoedel=" + sb.toString() +
                '}';
    }

    public Set<Node> getAllChildrenFromID(String id) {
        return knoedel.get(id).getAllChildren();
    }

    public boolean hasNoNames() {
        for (Node node : knoedel.values()) {
            if (!node.hasName())
                return true;
        }
        return false;
    }

    public int getBalanceChildrenParents() {
        int children, parents;
        children = 0;
        parents = 0;
        for (Node node : knoedel.values()) {
            children += node.getChildren().size();
            parents += node.getParents().size();
        }

        return parents - children; //would be neutral if 0 so it should be 0
    }

    /**
     * IncompleteNode means e.g. Link to Parent Node but that node doesnt have any links to this child (or vice versa)
     *
     * @return Set<Node> with all incomplete nodes
     */
    public Set<Node> getIncompleteNodes() {
        HashSet<Node> missingparentlinks = new HashSet<>();
        HashSet<Node> missingchildlinks = new HashSet<>();
        for (Node node : knoedel.values()) {
            for (Node parent : node.getParents()) {
                if (!knoedel.get(parent.getId()).hasChild(node.getId())) {
                    missingparentlinks.add(node);
                }
            }

            for (Node child : node.getChildren()) {
                if (!knoedel.get(child.getId()).hasChild(node.getId())) {
                    missingchildlinks.add(node);
                }
            }
        }

        HashSet<Node> alllinks = new HashSet<>();
        alllinks.addAll(missingchildlinks);
        alllinks.addAll(missingparentlinks);
        return alllinks;
    }

    public void printAllIncompleteNodes() {
        for (Node node : knoedel.values()) {
            node.printIncompleteConnections();
        }
    }

    public void addNodeInformation(String id, String proteinid) {
        if (knoedel.containsKey(id))
            knoedel.get(id).addProtein(proteinid);
    }

    public void addNodeInformation(String id, List<String> proteinids) {
        Node n = knoedel.get(id);
        for (String proteinid : proteinids) {
            n.addProtein(proteinid);
        }
    }
}
