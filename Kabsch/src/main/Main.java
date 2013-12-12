/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import data.Database;
import java.io.IOException;
import util.IO;

/**
 *
 * @author uhligc
 */
public class Main {
    public static void main(String[] args ) throws IOException{
        Database d = new Database();
        String stringcathscop = "/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/cathscop.inpairs";
        IO.importCathScop(stringcathscop, d);
        IO.importListOfPDBIds(d);
//        IO.exportForGotoh(args[0], args[1], d);
        IO.processAlignmentFile(args[0], args[1], d);
    }
}
