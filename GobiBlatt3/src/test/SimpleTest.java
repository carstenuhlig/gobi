/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import util.GenomicUtils;

/**
 *
 * @author uhligc
 */
public class SimpleTest {

    static String seq = "CATATATAGC";
    static int frame = 1;
    static String strand = "+";

    public static void main(String[] args) {
        System.out.println(GenomicUtils.convertToAA(seq, strand, frame));

    }
}
