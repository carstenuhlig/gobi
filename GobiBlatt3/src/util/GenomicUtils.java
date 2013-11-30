/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author uhligc
 */
public class GenomicUtils {

    public static String convertToAA(String seq, String strand, int frame) {
        String sequence;
        if (frame > 0) {
            sequence = seq.substring(frame);
        } else {
            sequence = seq;
        }

        if (strand.equals("-")) {
            sequence = convertToStrandPlus(sequence);
        } else {
            sequence = cleanString(sequence);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sequence.length() / 3; i++) {
            sb.append(AminoAcidType.get(sequence.substring(i * 3, i * 3 + 3)));
        }

        return sb.toString();
    }

    public static String convertToStrandPlus(String seq) {
        char[] chars = seq.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (char c : chars) {
            switch (c) {
                case 'A':
                    sb.append('T');
                    break;
                case 'T':
                    sb.append('A');
                    break;
                case 'C':
                    sb.append('G');
                    break;
                case 'G':
                    sb.append('C');
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

    public static String cleanString(String seq) {
        char[] chars = seq.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (c == 'A' || c == 'C' || c == 'T' || c == 'G') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
