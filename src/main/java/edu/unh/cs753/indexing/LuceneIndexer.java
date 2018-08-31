package edu.unh.cs753.indexing;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs753.utils.IndexUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

public class LuceneIndexer {
    private final IndexWriter writer;

    LuceneIndexer(String indexLoc) {
        writer = IndexUtils.createIndexWriter(indexLoc);
    }

    public void doIndex(String cborLoc) throws IOException {
        int stupidCounter = 0;
        for (Data.Paragraph p : IndexUtils.createParagraphIterator(cborLoc)) {
            Document doc = new Document();
            doc.add(new StringField("id", p.getParaId(), Field.Store.YES));
            doc.add(new TextField("text", p.getTextOnly(), Field.Store.YES));
            writer.addDocument(doc);
            stupidCounter++;
            if (stupidCounter % 20 == 0) {
                System.out.println("Commited");
                writer.commit();
            }
        }

        writer.close();
    }


    public static void main(String[] args) {
       String cborLoc = "/home/hcgs/data_science/data/test200/test200-train/train.pages.cbor-paragraphs.cbor";
    }



}
