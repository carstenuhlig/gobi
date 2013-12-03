
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

        int pos = Integer.parseInt(matcher.replaceFirst("$1"));
        double x1 = Double.parseDouble(matcher.replaceAll("$2"));
        double x2 = Double.parseDouble(matcher.replaceAll("$3"));
        double x3 = Double.parseDouble(matcher.replaceAll("$4"));

        System.out.println(pos + " " + x1 + " " + x2 + " " + x3);
    }
}
