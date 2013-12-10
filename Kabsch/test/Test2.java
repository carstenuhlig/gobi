
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import data.Database;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;
import kabsch.Kabsch;
import util.Import;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Carsten
 */
public class Test2 {

    public static void main(String[] args) {
        Database d = new Database();
        String id = "1r5ra00";
        try {
            d.addMatrix(id, Import.readSampleFile("res/1r5ra00.backbone"));
        } catch (IOException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(d.viewMatrix("1r5ra00"));
        DenseDoubleMatrix2D first;
        DenseDoubleMatrix2D second;
        Kabsch k;
        String result = "";
        String result2 = "";
        double res = 0;
        double res2 = 0;
        StringBuilder sb = new StringBuilder();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("#.####",dfs);

        double totaldiff = 0;
        
        int length = d.getLengthOfId(id);
        int counter = 0;

        for (int i = 0; i < length - 11; i++) {
            for (int j = i + 1; j < length - 10; j++) {
                first = (DenseDoubleMatrix2D) d.getMatrixPiece(id, i, 10);
                second = (DenseDoubleMatrix2D) d.getMatrixPiece(id, j, 10);
                k = new Kabsch(first, second);
                k.main();
                res = k.getErmsd();
                res2 = k.getRmsd();
                result = f.format(res);
                result2 = f.format(res2);
                sb.append(i + "-" + (i + 10) + "\t" + j + "-" + (j + 10) + "\t");
                sb.append(result);
                sb.append("\t");
                sb.append(result2);
                sb.append("(10) und Diff: ");
                totaldiff+=Math.abs(res-res2);
                sb.append(f.format(Math.abs(res-res2)));
                sb.append("\n");
                counter++;
            }
        }
        sb.append(totaldiff/counter);
        
        System.out.println(sb.toString());
    }
}
