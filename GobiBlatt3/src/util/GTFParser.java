package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.Genes;

public class GTFParser {
	private static final FileSystem FS = FileSystems.getDefault();

	public static void readFile(String p, Genes genes) throws IOException {
		Path path = FS.getPath(p);

		BufferedReader reader = Files.newBufferedReader(path,
				StandardCharsets.UTF_8);

		String regexdelimit = "\\s+|;\\s*";

		String line;
		String[] pieces;
		String seqname;
		String source;
		String feature;
		int start;
		int end;
		int score;
		String strand;
		int frame;
		List<String> gene_id = new ArrayList<String>();
		List<String> transcript_id = new ArrayList<String>();
		List<Integer> exonnr = new ArrayList<Integer>();

		while ((line = reader.readLine()) != null) {
			// split
			pieces = line.split(regexdelimit);

			// save pieces
			seqname = pieces[0];
			source = pieces[1];
			feature = pieces[2];
			start = Integer.parseInt(pieces[3]);
			end = Integer.parseInt(pieces[4]);
			score = Integer.parseInt(pieces[5]);
			// TODO check for available strand inputs -> evtl als Character oder
			// Integer oder boolean
			strand = pieces[6];
			try {
				frame = Integer.parseInt(pieces[7]);
			} catch (NumberFormatException e) {
				frame = -1000;
			}
			for (int i = 8; i < pieces.length; i += 2) {
				switch (pieces[i]) {
				case "gene_id":
					gene_id.add(pieces[i + 1]);
					break;
				case "transcript_id":
					transcript_id.add(pieces[i + 1]);
					break;
				case "exon_number":
					exonnr.add(Integer.parseInt(pieces[i + 1]));
					break;
				default:
					System.err
							.println("fehlende Eigenschaft gefunden. Bitte beheben!");
					break;
				}
			}

			// save in database
			// TODO

			// reset values
			// TODO
			genes.addGene(String gene_id);
		}
	}
}
