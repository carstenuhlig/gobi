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
        IO.importCathScop(args[0], d);
        IO.importListOfPDBIds(d);
        IO.exportForGotoh(args[1], args[2], d); 
    }
}
