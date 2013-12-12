
import data.Database;
import java.io.IOException;
import kabsch.Kabsch;
import util.IO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author uhligc
 */
public class KabschTest {

    public static void main(String[] args) throws IOException {
        Database d = new Database();
        d.addMatrix("1r5ra00", IO.readSampleFile("res/1r5ra00.backbone"));
//        System.out.println(d.viewMatrix("1r5ra00"));
        Kabsch k = new Kabsch(d.getRandomMatrix(),d.getRandomMatrix());
        System.out.println(k.getP().toString());
        k.main();
        System.out.println(k.getP().toString());
//        System.out.println(k);
    }

}
