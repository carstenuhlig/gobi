
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author uhligc
 */
public class PatternTest {

    public static void main(String[] args) {
        String regex = "^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";
        String input = "15	12.3420	7.7750	-14.5200";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("flower");
        
        System.out.println(matcher.reset(input).matches());

        String nix = "babababab abababa";
        String a = nix.replaceAll(nix, "\\w+\\s(\\w+)");
        if ( nix.matches(regex)) {
            System.out.println("geht");
        }
        
        
        int pos = Integer.parseInt(matcher.replaceFirst("$1"));
        double x1 = Double.parseDouble(matcher.replaceAll("$2"));
        double x2 = Double.parseDouble(matcher.replaceAll("$3"));
        double x3 = Double.parseDouble(matcher.replaceAll("$4"));

        System.out.println(pos + " " + x1 + " " + x2 + " " + x3);

        regex = "^(\\S+)\\s+(\\S+).+tmscore:\\s(\\S+).*$";
        input = "1bj4.A 3l49.A p1: 470 p2: 282 ali length: 161 tmscore: 0.40063 rmsd: 5.088(161)";
        System.out.println();
        if (input.matches(regex))
            System.out.println("it worked\n");
        System.out.println(input.replaceFirst(regex, "$1 $2"));
        System.out.println(input.replaceAll("^(\\S+).*$", "$1"));
    }
}
