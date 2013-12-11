
import data.Database;
import util.Import;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author uhligc
 */
public class Test3 {
    public static void main(String[] args) {
        Database d = new Database();
        Import.readPDBFile("/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/1jb7B00.pdb", "1jb7B00", d);
        System.out.println(d.getMatrix("1jb7B00"));
    }
}
