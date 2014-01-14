package data;

/**
 * Created by uhligc on 14.01.14.
 */
public class Alignment {
    String one, two, id1, id2;

    public Alignment(String one, String two, String id1, String id2) {
        this.one = one;
        this.two = two;
        this.id1 = id1;
        this.id2 = id2;
    }

    public String getOne() {
        return one;
    }

    public String getTwo() {
        return two;
    }

    public boolean isAlignment(String a, String b) {
        if (a == id1 && b == id2)
            return true;
        if (a == id2 && b == id1)
            return true;
        return false;
    }
}
