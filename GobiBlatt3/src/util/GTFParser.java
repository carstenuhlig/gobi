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
		String regexchr = "^.*chromosome\\.(\\d+).*$";
		
		int chromosome = Integer.parseInt(path.getFileName().toString().replaceFirst(regexchr, "$1"));

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
			try {
				score = Integer.parseInt(pieces[5]);
			} catch (NumberFormatException e) {
				score = -Integer.MAX_VALUE;
			}
			// TODO check for available strand inputs -> evtl als Character oder
			// Integer oder boolean
			strand = pieces[6];
			try {
				frame = Integer.parseInt(pieces[7]);
			} catch (NumberFormatException e) {
				frame = -Integer.MAX_VALUE;
			}
			for (int i = 8; i < pieces.length; i += 2) {
				switch (pieces[i]) {
				case "gene_name":
				case "gene_id":
					gene_id.add(pieces[i + 1].replace("\"", ""));
					break;
				case "protein_id":
				case "transcript_name":
				case "transcript_id":
					transcript_id.add(pieces[i + 1].replace("\"", ""));
					break;
				case "exon_number":
					exonnr.add(Integer.parseInt(pieces[i + 1].replace("\"", "")));
					break;
				default:
					System.err
							.println("fehlende Eigenschaft gefunden. Bitte beheben!:" + pieces[i] + " " + pieces[i+1]);
					break;
				}
			}

			// save in database
			// TODO
			if (!gene_id.isEmpty() && !transcript_id.isEmpty()) {
				genes.addGene(gene_id,transcript_id,start,end,chromosome,strand);
			}

			System.out.print("");

			// reset values
			frame = -Integer.MAX_VALUE;
			score = -Integer.MAX_VALUE;
			gene_id.clear();
			transcript_id.clear();
			exonnr.clear();

		}
	}
}
