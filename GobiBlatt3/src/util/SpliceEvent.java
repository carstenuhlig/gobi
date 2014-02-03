package util;

import data.Exon;
import data.Protein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by carsten on 02.02.14.
 */
public class SpliceEvent {
    Protein proteina, proteinb;
    List<Exon> exons1, exons2;
    StringBuilder vs_nucleotide, vs_aa;

    public SpliceEvent(Protein a, Protein b) {
        vs_nucleotide = new StringBuilder();
        vs_aa = new StringBuilder();
        this.proteina = a;
        this.proteinb = b;
        exons1 = a.getExons();
        exons2 = b.getExons();
    }

    public void main() {
        orderExons();
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
            for (int i = 0; i < exons2.size(); i++) {
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
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(exons1.toString());
        sb.append("\n");
        sb.append(exons2.toString());
        return sb.toString();
    }
}
