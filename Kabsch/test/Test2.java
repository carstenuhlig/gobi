
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
        StringBuilder sb = new StringBuilder();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("#.####",dfs);

        int length = d.getLengthOfId(id);

        for (int i = 0; i < length - 11; i++) {
            for (int j = i + 1; j < length - 10; j++) {
                first = (DenseDoubleMatrix2D) d.getMatrixPiece(id, i, 10);
                second = (DenseDoubleMatrix2D) d.getMatrixPiece(id, j, 10);
                k = new Kabsch(first, second);
                k.main();
                result = f.format(k.getErmsd());
                result2 = f.format(k.getRmsd());
                sb.append(i + "-" + (i + 10) + "\t" + j + "-" + (j + 10) + "\t");
                sb.append(result);
                sb.append("\t");
                sb.append(result2);
                sb.append("(10)\n");
            }
        }

        System.out.println(sb.toString());
    }
}
