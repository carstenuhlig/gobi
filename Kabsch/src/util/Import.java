/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author uhligc
 */
public class Import {

    final static FileSystem FS = FileSystems.getDefault();

    public static DenseDoubleMatrix2D readSampleFile(String stringpath) throws IOException {
        Path p = Paths.get(stringpath);
        
        List<String> strings = Files.readAllLines(p, Charset.defaultCharset());
        double[][] result = new double[strings.size()][3];

        String regex = "^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("");

        for (String str : strings) {
            if (matcher.reset(str).matches()) {
                int pos = Integer.parseInt(matcher.replaceFirst("$1"));
                double x1 = Double.parseDouble(matcher.replaceAll("$2"));
                double x2 = Double.parseDouble(matcher.replaceAll("$3"));
                double x3 = Double.parseDouble(matcher.replaceAll("$4"));
                
                result[pos][0] = x1;
                result[pos][1] = x2;
                result[pos][2] = x3;
            }
        }
        DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
        resultmatrix.trimToSize();
        return resultmatrix;
    }
}
