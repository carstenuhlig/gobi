/**
 * Created by carsten on 03.02.14.
 */
public class PatternTest {
    public static void main(String[] args) {
        String raw = "IIIIIIIIIIIDDDDDDDDDDDDDDDDDDDD";
        String regex = "^(.*)I(R*)D(.*)$";
        while (raw.matches(regex)) {
            raw = raw.replaceAll(regex, "$1R$2$3");
        }
        System.out.println(raw.matches(regex));
        System.out.println(raw);
    }
}
