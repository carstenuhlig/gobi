package data;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by uhligc on 22.01.14.
 */
public class Database {
    HashMap<String, Node> knoedel;

    public Database() {
        knoedel = new HashMap<>();
    }

    public Node getNode(String id) {
        Node n = knoedel.get(id);
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
}
