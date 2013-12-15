
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import data.Database;
import kabsch.Kabsch;
import util.IO;
import util.Matrix;

/**
 *
 * @author uhligc
 */
public class Test5 {

    public static void main(String[] args) {
        Database d = new Database();
        String first = "1tfxC00";
        String second = "1ca0I00";
        String third = "1jhnA02";
        String fourth = "1k9cA00";
        String firstali = "KPDFCFLEEDPGICRGYITRYFYNNQTKQCERFKYGGCLGNMNNFETLEECKNICEDG";
        String secondali = "--EVCSEQAETGPCRAMISRWYFDVTEGKCAPFFYGGCGGNRNNFDTEEYCMAVCG--";
        IO.readPDBFile(first, d);
        IO.readPDBFile(second, d);
        IO.readPDBFileWhole(first, d);
        IO.readPDBFileWhole(second, d);

        DenseDoubleMatrix2D[] dada = Matrix.processMatrices(d.getMatrix(first), d.getMatrix(second), firstali, secondali);
        Kabsch k = new Kabsch(dada[0], dada[1]);
        k.main();
        DenseDoubleMatrix2D secondprotein = k.processWholeStructure(d.getBigMatrix(second));
        dada = Matrix.processMatrices(d.getMatrix(first), d.getMatrix(second), firstali, secondali);
        k = new Kabsch(dada[0], dada[1]);
        k.main();
        DenseDoubleMatrix2D fourthprotein = k.processWholeStructure(d.getBigMatrix(fourth));
    }
}
