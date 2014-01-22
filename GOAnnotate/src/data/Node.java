package data;

import java.util.*;

/**
 * Created by uhligc on 22.01.14.
 */
public class Node {
    //GO-accession number
    private String id;
    //type -> term
    private String name;
    private ArrayList<Node> children;
    private ArrayList<Node> parents;

    public Node(String id) {
        init();
        this.id = id;
    }

    public Node(String id, String name) {
        init();
        this.id = id;
        this.name = name;
    }

    public Node(String id, String name, Node child) {
        init();
        this.id = id;
        this.name = name;
        children.add(child);
    }

    public Node(String id, String name, List<String> parents, List<String> children) {
        init();
        this.id = id;
        this.name = name;
    }

    public Node(String id, String name, List<String> parents) {
        init();
        this.id = id;
        this.name = name;
    }

    //evtl doch gebraucht
    /*public Node(String id, String name, List<String> children) {
        init();
        this.id = id;
        this.name = name;
    }*/

    private void init() {
        children = new ArrayList<>();
        parents = new ArrayList<>();
        name = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public ArrayList<Node> getParents() {
        return parents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addChildren(List<Node> children) {
        this.children.addAll(children);
    }

    public void addParents(List<Node> parents) {
        this.parents.addAll(parents);
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}' + "\n";
    }

    public Set<Node> getAllChildren() {
        //Catch 'em all !!! Pokemon
        HashSet<Node> tmp = new HashSet<>();
        for (Node child : children) {
            tmp.addAll(child.getAllChildren());
        }
        tmp.add(this);
        return tmp;
    }
}
