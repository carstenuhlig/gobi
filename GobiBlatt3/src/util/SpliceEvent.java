package util;

import data.Exon;
import data.Protein;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by carsten on 02.02.14.
 */
public class SpliceEvent {
    Protein proteina, proteinb;
    List<Exon> exons1, exons2;
    StringBuilder vs_nucleotide, vs_aa, vs_aa_space;

    public SpliceEvent(Protein a, Protein b) {
        vs_nucleotide = new StringBuilder();
        vs_aa = new StringBuilder();
        vs_aa_space = new StringBuilder();
        this.proteina = a;
        this.proteinb = b;
        exons1 = a.getExons();
        exons2 = b.getExons();
    }

    public void main() {
        orderExons();
        easyAASplice();
        makeNucleotideSeq();
    }

    private void orderExons() {
        Collections.sort(exons1);
        Collections.sort(exons2);
    }

    /**
     * Checks positional relation between 2 exons.
     * when a < b and a U b = 0 then -2
     * when a > b and a U b = 0 then 2
     * when a < b and a U b > 0 then -1
     * when a > b and a U b > 0 then 1
     * when a = b and a U b > 0 then 0
     * when a << b and a U b > 0 then -3
     * when a >> b and a U b > 0 then 3
     *
     * @param a first exon
     * @param b second exon
     * @return goes from -3 to 3
     */
    private int checkCut(Exon a, Exon b) {
        long starta = a.getCDS().getStart();
        long enda = a.getCDS().getStop();
        long startb = b.getCDS().getStart();
        long endb = b.getCDS().getStop();
        if (a.equals(b)) {
            return 0;
        } else if (Math.min(enda, endb) - Math.max(starta, startb) < 0) {
            if (starta < startb)
                return -2;
            else
                return 2;
        } else {
            if (starta < startb) {
                if (enda > endb)
                    return -3;
                else {
                    return -1;
                }
            } else {
                if (enda < endb) {
                    return 3;
                } else {
                    return 1;
                }
            }
        }
    }

    private void makeNucleotideSeq() {
        TreeSet<Long> transcripta = new TreeSet<>();
        TreeSet<Long> transcriptb = new TreeSet<>();
        for (Exon exon : exons1) {
            transcripta.addAll(makeSetFromRange(exon.getCDS().getStart(), exon.getCDS().getStop()));
        }
        for (Exon exon : exons2) {
            transcriptb.addAll(makeSetFromRange(exon.getCDS().getStart(), exon.getCDS().getStop()));
        }
        TreeSet<Long> all = new TreeSet<>();
        all.addAll(transcripta);
        all.addAll(transcriptb);

        long start = all.first();
        long end = all.last();

        char[] chars = new char[(int) (end - start + 1)];

        TreeSet<Long> combined = new TreeSet<>();
        combined.addAll(transcripta);
        combined.retainAll(transcriptb);
        for (Long aLong : combined) {
            chars[(int) (aLong - start)] = '*';
        }

        TreeSet<Long> onlya = new TreeSet<>();
        onlya.addAll(all);
        onlya.removeAll(transcriptb);
        for (Long aLong : onlya) {
            chars[(int) (aLong - start)] = 'D';
        }

        TreeSet<Long> onlyb = new TreeSet<>();
        onlyb.addAll(all);
        onlyb.removeAll(transcripta);
        for (Long aLong : onlyb) {
            chars[(int) (aLong - start)] = 'I';
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '\u0000')
                sb.append(chars[i]);
        }
        String output = sb.toString();
        output = output.replace(" ", "");

        String regex_di = "^(.*)D(R*)I(.*)$";
        String regex_id = "^(.*)I(R*)D(.*)$";
        Pattern pattern_di = Pattern.compile(regex_di);
        Pattern pattern_id = Pattern.compile(regex_id);

        while (pattern_di.matcher(output).matches()) {
            output = pattern_di.matcher(output).replaceAll("$1R$2$3");
        }

        while (pattern_id.matcher(output).matches()) {
            output = pattern_id.matcher(output).replaceAll("$1R$2$3");
        }
        //TODO frame wird nicht beachtet

        vs_nucleotide.append(output);
    }

    private Set<Long> makeSetFromRange(long start, long end) {
        Set<Long> tmp = new TreeSet<>();
        for (long i = start; i < end; i++) {
            tmp.add(i);
        }
        return tmp;
    }

    /**
     * Method goes for Exons (incomplete)
     */
    private void easyAASplice() {
        Iterator<Exon> ita = exons1.iterator();
        Iterator<Exon> itb = exons2.iterator();

        int typenext = 0;
        Exon a = ita.next(), b = itb.next();

        StringBuilder sb = new StringBuilder();

        while (ita.hasNext() && itb.hasNext()) {
            if (a.equals(b)) {
                long len = (a.getCDS().getStop() - a.getCDS().getStart() + 1) / 3;
                for (int i = 0; i < len; i++) {
                    sb.append("*");
                }
                a = ita.next();
                b = itb.next();
            } else if (a.compareTo(b) < 0) {
                long len = (a.getCDS().getStop() - a.getCDS().getStart() + 1) / 3;
                for (int i = 0; i < len; i++) {
                    sb.append("D");
                }
                a = ita.next();
            } else {
                long len = (b.getCDS().getStop() - b.getCDS().getStart() + 1) / 3;
                for (int i = 0; i < len; i++) {
                    sb.append("I");
                }
                b = itb.next();
            }
        }
        if (a != null) {
            long len = (a.getCDS().getStop() - a.getCDS().getStart() + 1) / 3;
            for (int i = 0; i < len; i++) {
                sb.append("D");
            }
        }
        if (b != null) {
            long len = (b.getCDS().getStop() - b.getCDS().getStart() + 1) / 3;
            for (int i = 0; i < len; i++) {
                sb.append("I");
            }
        }
        String output = sb.toString();
        String regex_di = "^(.*)D(R*)I(.*)$";
        String regex_id = "^(.*)I(R*)D(.*)$";
        Pattern pattern_di = Pattern.compile(regex_di);
        Pattern pattern_id = Pattern.compile(regex_id);

        while (pattern_di.matcher(output).matches()) {
            output = pattern_di.matcher(output).replaceAll("$1R$2$3");
        }

        while (pattern_id.matcher(output).matches()) {
            output = pattern_id.matcher(output).replaceAll("$1R$2$3");
        }

        vs_aa_space.append(output);

        output = output.replace("*", "*  ");
        output = output.replace("R", "R  ");
        output = output.replace("D", "D  ");
        output = output.replace("I", "I  ");

        vs_aa.append(output);
    }

    private void makeAASplice() {
        int counter_counterpart = 0;
        int checkvar = 0;
        long len = 0;
        //exons1 bigger than exons2
        if (exons1.size() >= exons2.size()) {
            for (int i = 0; i < exons1.size(); i++) {
                Exon exona = exons1.get(i);
                Exon exonb = exons2.get(counter_counterpart);

                long starta = exona.getCDS().getStart();
                long startb = exonb.getCDS().getStart();
                long enda = exona.getCDS().getStop();
                long endb = exonb.getCDS().getStop();

                checkvar = checkCut(exona, exonb);
                switch (checkvar) {
                    case 0: {
                        len = (enda - starta + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('*');
                        }
                    }
                    break;
                    case -1: {
                        len = (startb - starta + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('D');
                        }

                        len = (Math.min(enda, endb) - Math.max(starta, startb));

                        for (int j = 0; j < len; j++) {
                            vs_aa.append('*');
                        }
                        counter_counterpart++;
                        break;
                    }
                    case 1: {
                        len = (starta - startb + 1) / 3;

                        for (int j = 0; j < len; j++) {
                            vs_aa.append('I');
                        }

                        len = (Math.min(enda, endb) - Math.max(starta, startb));

                        for (int j = 0; j < len; j++) {
                            vs_aa.append('*');
                        }
                        counter_counterpart++;
                        break;
                    }
                    case -2: {
                        len = (endb - startb + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('D');
                        }
                        i--;
                        counter_counterpart++;
                        break;
                    }
                    case 2: {
                        len = (enda - starta + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('I');
                        }
                        break;
                    }
                    default:
                        System.err.println("case -3 oder 3 sind vorgekommen.");
                }
            }
        } else {
            int i = 0;
            for (counter_counterpart = 0; counter_counterpart < exons2.size(); counter_counterpart++) {
                Exon exona = exons1.get(i);
                Exon exonb = exons2.get(i);

                long starta = exona.getCDS().getStart();
                long startb = exonb.getCDS().getStart();
                long enda = exona.getCDS().getStop();
                long endb = exonb.getCDS().getStop();

                checkvar = checkCut(exona, exonb);
                switch (checkvar) {
                    case 0: {
                        len = (enda - starta + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('*');
                        }
                    }
                    break;
                    case -1: {
                        len = (startb - starta + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('D');
                        }

                        len = (Math.min(enda, endb) - Math.max(starta, startb));

                        for (int j = 0; j < len; j++) {
                            vs_aa.append('*');
                        }
                        counter_counterpart++;
                        break;
                    }
                    case 1: {
                        len = (starta - startb + 1) / 3;

                        for (int j = 0; j < len; j++) {
                            vs_aa.append('I');
                        }

                        len = (Math.min(enda, endb) - Math.max(starta, startb));

                        for (int j = 0; j < len; j++) {
                            vs_aa.append('*');
                        }
                        counter_counterpart++;
                        break;
                    }
                    case -2: {
                        len = (endb - startb + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('D');
                        }
                        i--;
                        counter_counterpart++;
                        break;
                    }
                    case 2: {
                        len = (enda - starta + 1) / 3;
                        for (int j = 0; j < len; j++) {
                            vs_aa.append('I');
                        }
                        break;
                    }
                    default:
                        System.err.println("case -3 oder 3 sind vorgekommen.");
                }
            }
        }
    }

    public String getVarSplic() {
        return vs_aa.toString();
    }

    public String getNucleotideSplice() {
        return vs_nucleotide.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (vs_aa_space.length() > 0) {
            sb.append(vs_aa_space.toString());
        }
        return sb.toString();
    }
}
