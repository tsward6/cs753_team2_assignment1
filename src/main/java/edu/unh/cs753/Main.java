package edu.unh.cs753;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import kotlin.jvm.functions.Function3;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class Main {

	// Used as a wrapper around a static method: will call method and pass
	// argument parser's parameters to it
	private static class Exec {
		private Consumer<Namespace> func;

		Exec(Consumer<Namespace> funcArg) {
			func = funcArg;
		}

		void run(Namespace params) {
			func.accept(params);
		}
	}


	public static ArgumentParser createArgParser() {
		ArgumentParser parser = ArgumentParsers.newFor("program").build();
		Subparsers subparsers = parser.addSubparsers(); // Subparsers is used to
														// create subcommands

		// Add subcommand for running index program
		Subparser indexParser = subparsers.addParser("index") // index is the name of the subcommand
				.setDefault("func", new Exec(Main::runIndexer)).help("Indexes paragraph corpus using Lucene.");
		indexParser.addArgument("corpus").required(true).help("Location to paragraph corpus file (.cbor)");
		indexParser.addArgument("--spotlight_folder").setDefault("")
				.help("Directory containing spotlight jar file and model."
						+ "If the directory doesn't exist, the required files are downloaded automatically."
						+ "If no folder is specified, entity annotation is skipped.");
		indexParser.addArgument("--out").setDefault("index")
				.help("Directory name to create for Lucene index (default: index)");


		return parser;
	}

	private static void runIndexer(Namespace params) {
		String indexLocation = params.getString("out");
		String corpusFile = params.getString("corpus");
		String spotlight_location = params.getString("spotlight_folder");

	}


	// Main class for project
	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");
		ArgumentParser parser = createArgParser();

		try {
			// This parses the arguments (based on createArgParser) and returns
			// the results
			Namespace params = parser.parseArgs(args);

			// We store the function that handles using these parameters in the
			// "func" field
			// In this example, we retrieve the parameter and cast it as Exec,
			// which is used to run the method reference
			// That was passed to it when the Exec was created.
			((Exec) params.get("func")).run(params);
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			System.exit(1);
		}
	}

}
