package edu.unh.cs753.indexing;

import edu.unh.cs753.utils.SearchUtils;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

import java.io.IOException;

public class LuceneSearcher {
    final IndexSearcher searcher;

    LuceneSearcher(String indexLoc) {
        searcher = SearchUtils.createIndexSearcher(indexLoc);
    }

    /**
     * Function: query
     * Desc: Queries Lucene paragraph corpus using a standard similarity function.
     *       Note that this uses the StandardAnalyzer.
     * @param queryString: The query string that will be turned into a boolean query.
     * @param nResults: How many search results should be returned
     * @return TopDocs (ranked results matching query)
     */
    public TopDocs query(String queryString, Integer nResults) {
        Query q = SearchUtils.createStandardBooleanQuery(queryString, "text");
        try {
            return searcher.search(q, nResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Function: queryWithCustomScore
     * Desc: Queries Lucene paragraph corpus using a custom similarity function.
     *       Note that this uses the StandardAnalyzer.
     * @param queryString: The query string that will be turned into a boolean query.
     * @param nResults: How many search results should be returned
     * @return TopDocs (ranked results matching query)
     */
    public TopDocs queryWithCustomScore(String queryString, Integer nResults) {
        Query q = SearchUtils.createStandardBooleanQuery(queryString, "text");
        IndexSearcher customSearcher = new IndexSearcher(searcher.getIndexReader());

        // Declares a custom similarity function for use with a new IndexSearcher
        SimilarityBase similarity = new SimilarityBase() {
            @Override
            protected float score(BasicStats basicStats, float freq, float docLen) {

                // Needs to be filled out
                return 0;
            }

            @Override
            public String toString() {
                return null;
            }
        };

        customSearcher.setSimilarity(similarity);

        try {
            return customSearcher.search(q, nResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}