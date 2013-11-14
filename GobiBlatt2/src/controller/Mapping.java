package controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

import util.BLASTPiece;
import util.Data;
import util.Database;
import util.GeneDouble;
import util.ImportFiles;

public class Mapping {
	private static final FileSystem FS = FileSystems.getDefault();
	private static final Path path_to_blast = FS
			.getPath("/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST");
	private static final String path_to_nrdump = "/home/proj/biosoft/PROTEINS/NR/nrdump.fasta";
	private static final String path_to_gids = "/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp";
//	private static final String path_to_nrdump = "/home/u/uhligc/aufgaben_gobi/assignment2/nrdump_testfile.fasta";
//	private static final String path_to_gids = "/home/u/uhligc/aufgaben_gobi/assignment2/gi_taxid_prot_testfile.dmp";
	private static final int taxid = 9606;
	static LinkedList<BLASTPiece> data;
	static Data d;
	static String filename;
	static String to_write;
	static String pdbid;
	static LinkedList<Integer> tmp;

	private static Path path_to_mapping;

	public static void makeMapping(String file) throws IOException {
		path_to_mapping = FS.getPath(file);

		BufferedWriter writer = Files.newBufferedWriter(path_to_mapping,
				StandardCharsets.UTF_8, StandardOpenOption.CREATE);

		// get GIDs
		tmp = new LinkedList<Integer>();
		tmp = ImportFiles.getGiTaxIdList(taxid, path_to_gids);

		System.out.println("finished reading Gids");

		// get NCBI_NR Database From GIDs
		Database database = new Database("Human GIDs");
		ImportFiles.getNCBIObjectsFromGiIds(tmp, path_to_nrdump, database);

		System.out.println("finished reading NCBI_NR database");

		// process BLAST folder
		// variables
		filename = null;
		to_write = null;
		pdbid = null;
		data = null;
		d = null;

		String beginning_line = "PDB-ID\tProtein-ID\t\tBLAST E-Value\t\tRound\n";
		writer.write(beginning_line, 0, beginning_line.length());

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path_to_blast)) {
			for (Path p : ds) {
				filename = p.getFileName().toString();
				pdbid = filename.substring(0, 4);

				data = ImportFiles.getMatchObjectsFromBLASTFile(filename);

				for (BLASTPiece piece : data) {
					d = database.getDataFromProteinID(piece.getProteinid());

					// now look for empty or not empty datapiece -> if found
					// then proteinid is in human database
					if (d != null) {
						// TODO print in file path_to_mapping
						to_write = pdbid + "\t" + piece.getProteinid() + "\t\t" + piece.getEvalue() + "\t\t" + piece.getRound() + "\n";
						writer.write(to_write, 0, to_write.length());
//						System.out.println(to_write);
					}
				}
			}
		}
		//close writer
		writer.flush();
		writer.close();
	}

	public static void makeMapping(String file, boolean genes_flag) throws IOException {
		path_to_mapping = FS.getPath(file);

		BufferedWriter writer = Files.newBufferedWriter(path_to_mapping,
				StandardCharsets.UTF_8, StandardOpenOption.CREATE);

		// get GIDs
		tmp = new LinkedList<Integer>();
		tmp = ImportFiles.getGiTaxIdList(taxid, path_to_gids);

		System.out.println("finished reading Gids");

		// get NCBI_NR Database From GIDs
		Database database = new Database("Human GIDs");
		ImportFiles.getNCBIObjectsFromGiIds(tmp, path_to_nrdump, database);

		System.out.println("integrate geneids");
		ImportFiles.integrateGeneIDs(database);
		System.out.println("finished integrating geneids");

		// process BLAST folder
		// variables
		filename = null;
		to_write = null;
		pdbid = null;
		data = null;
		d = null;

		String beginning_line = "PDB-ID\tProtein-ID\t\tBLAST E-Value\t\tRound\n";
		writer.write(beginning_line, 0, beginning_line.length());

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path_to_blast)) {
			for (Path p : ds) {
				filename = p.getFileName().toString();
				pdbid = filename.substring(0, 4);

				data = ImportFiles.getMatchObjectsFromBLASTFile(filename);

				for (BLASTPiece piece : data) {
					d = database.getDataFromProteinID(piece.getProteinid());

					// now look for empty or not empty datapiece -> if found
					// then proteinid is in human database
					if (d != null) {
						// TODO print in file path_to_mapping
						to_write = pdbid + "\t" + piece.getProteinid() + "\t\t" + piece.getEvalue() + "\t\t" + piece.getRound()  + "\t\t" + d.getGeneid() + "\n";
						writer.write(to_write, 0, to_write.length());
//						System.out.println(to_write);
					}
				}
			}
		}
		//close writer
		writer.flush();
		writer.close();
	}
}
