/*
 * CustomSearchFiles.java
 * 
 * Tim Ward 
 * 09/03/18
 * 
 * This is my setup for the search portion of the task required for 
 * Programming Assignment 1. In my program I have lucene-7.4.0 installed
 * in the same (program home) directory. I am in the process of figuring 
 * out how to run it with Apache Maven, but I have been running it through the 
 * terminal and using the following commands:
 * 
 * javac -cp ./lucene/demo/lucene-demo-7.4.0.jar:./lucene/core/lucene-core-7.4.0.jar:./lucene/queryparser/lucene-queryparser-7.4.0.jar:. CustomSearchFiles.java
 * 
 * java -cp ./lucene/demo/lucene-demo-7.4.0.jar:./lucene/core/lucene-core-7.4.0.jar:./lucene/queryparser/lucene-queryparser-7.4.0.jar:. CustomSearchFiles
 * 
 * This program assumes the index is in a folder named "index" in the 
 * program home directory.
 * 
 */
 
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory; 


import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.search.similarities.BasicStats;

 


public class CustomSearchFiles {
	
	public static void main (String args[]) throws Exception {
		
		
		String index = "index";
		String field = "contents";
		
		// create a reader and Index Searcher to search the query
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		
		//searcher.setSimilarity(new BM25Similarity());
		
		// The custom Similarity as suggested in program specs
		SimilarityBase mySimiliarity = new SimilarityBase (){
			
			@Override
			protected float score ( BasicStats stats , float freq , float docLen ){
				//System.out.println("freq: " + freq);
				return freq;
			}
			
			@Override
			public String toString() {
				return "";
			}
		};
		searcher.setSimilarity( mySimiliarity );
		
		// need an analyzer
		Analyzer analyzer = new StandardAnalyzer();
		
		// The queries specified in program specs
		String queries[] = { 
			"power nap benefits",
			"whale vocalization production of sound",
			"pokemon puzzle league" 
		};
		
		// path containing the document
		String docPath = "./test200/test200-train/train.pages.cbor-paragraphs.cbor";
		
		BufferedReader in = Files.newBufferedReader(Paths.get(docPath), StandardCharsets.UTF_8);
		QueryParser parser = new QueryParser(field, analyzer);
		int hitsPerPage = 10;
		
		// execute (search) each query
		for(int i = 0; i < queries.length; i++) {
			
			String tmpQuery = queries[i];
			Query query = parser.parse(tmpQuery);
			System.out.println("Searching for: " + query.toString(field));
			
			// Collect enough docs to show 5 pages
			TopDocs results = searcher.search(query, 5 * hitsPerPage);
			ScoreDoc[] hits = results.scoreDocs;
			
		}
		
	}
	
}

