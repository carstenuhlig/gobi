import java.util.regex.Pattern;

/**
 * Created by carsten on 06.02.14.
 */
public class PatternTest {
    static final Pattern PATTERN_PROSITE_FASTA = Pattern.compile("^>(\\w+)\\/(\\d+)-(\\d+) : (\\w+)\\s+(\\w+).*");

    public static void main(String[] args) {
        String bla = ">ENSP00000346168/191-203 : PS00108 PROTEIN_KINASE_ST ";
        if (PATTERN_PROSITE_FASTA.matcher(bla).matches()) {
            System.out.println("gefunden");
            System.out.println(PATTERN_PROSITE_FASTA.matcher(bla).replaceAll("$1 $2 $3 $4 $5"));
        }
    }
}
