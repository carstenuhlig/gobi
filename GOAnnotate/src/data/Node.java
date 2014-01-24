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
    private List<Node> children;
    private List<Node> parents;
    //protein ensemble gene id
    private Set<String> proteins;

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

    private void init() {
        children = new ArrayList<>();
        parents = new ArrayList<>();
        name = null;
        proteins = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Node> getParents() {
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

    //kann gar nicht vorkommen da direkt bei id fund dem konstruktor übergeben
    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    @Override
    public String toString() {
        StringBuilder p = new StringBuilder();
        StringBuilder c = new StringBuilder();
        for (Iterator<Node> nodeIterator = parents.iterator(); nodeIterator.hasNext(); ) {
            Node node = nodeIterator.next();
            if (nodeIterator.hasNext())
                p.append(node.getId() + ", ");
            else
                p.append(node.getId());
        }

        for (Iterator<Node> nodeIterator = children.iterator(); nodeIterator.hasNext(); ) {
            Node node = nodeIterator.next();
            if (nodeIterator.hasNext())
                c.append(node.getId() + ", ");
            else
                c.append(node.getId());
        }


//        StringBuilder pr = new StringBuilder();
        /*List<String> pr = new LinkedList<>();
        Iterator proteinit = proteins.iterator();
        while (proteins.iterator().hasNext()) {
            String s = proteins.iterator().next();
            if (proteins.iterator().hasNext())
                pr.add(s + ", ");
            else
                pr.add(s);
        }*/

        return "Node{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ",\n\t\t\t\t\t\tParents='" + p.toString() + '\'' +
                ",\n\t\t\t\t\t\tChildren='" + c.toString() + '\'' +
                ",\n\t\t\t\t\t\tNr of Proteins='" + proteins.toString() + '\'' +
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

    public Set<Node> getAllParents() {
        //Catch 'em all !!! Pokemon
        HashSet<Node> tmp = new HashSet<>();
        for (Node parent : parents) {
            tmp.addAll(parent.getAllParents());
        }
        tmp.add(this);
        return tmp;
    }

    public boolean hasName() {
        return name != null;
    }

    public boolean hasParent(String id) {
        for (Node parent : parents) {
            if (parent.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasChild(String id) {
        for (Node child : children) {
            if (child.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasParent(Node n) {
        for (Node parent : parents) {
            if (parent == n) {
                return true;
            }
        }
        return false;
    }

    public boolean hasChild(Node n) {
        for (Node child : children) {
            if (child == n) {
                return true;
            }
        }
        return false;
    }

    public void printIncompleteConnections() {
        for (Node parent : parents) {
            if (!parent.hasChild(this))
                System.out.println(this.id + " -> Parent?: " + parent.id);
        }

        for (Node child : children) {
            if (!child.hasParent(this))
                System.out.println(this.id + " -> Child?: " + child.id);
        }
    }

    public Set<String> getProteins() {
        return proteins;
    }

    public void addProtein(String proteinid) {
        if (proteins.contains(proteinid))
            return;
        else
            proteins.add(proteinid);
    }

    public boolean hasProteins() {
        if (proteins.isEmpty())
            return false;
        else
            return true;
    }
}
