/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author uhligc
 */
public class DeleteNonPairs {

    final static FileSystem FS = FileSystems.getDefault();
    
    public static void main(String[] args) throws IOException {
        Path p = FS.getPath(args[0]);
        Path p_seqlibfile_big = FS.getPath(args[0]);
        Path p_pairs_small = FS.getPath(args[1]+ ".txt");
        Path p_seqlibfile_small = FS.getPath(args[0] + ".2");
        
        LinkedList<String> liste = new LinkedList<>();
        
        BufferedReader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8);
//        BufferedWriter writer_big = Files.newBufferedWriter(p, null, options)
        String line = "";
        String[] bla;
        while ((line = reader.readLine()) != null) {
            bla = line.split(":");
            liste.add(bla[0]);
        }
        
        reader.close();
        
        for (Iterator<String> it = liste.iterator(); it.hasNext();) {
            String id = it.next();
            
        }
    }
}
