
import data.Database;
import util.IO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Carsten
 */
public class Test4 {
    
    public static void main (String[] args) {
        Database d = new Database();
        String cathscop = "res\\cathscop.inpairs";
        IO.importCathScop(cathscop, d);
        System.out.println("FINITO");
    }
}
