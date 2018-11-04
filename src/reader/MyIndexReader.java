package reader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MyIndexReader {

	private static final String INDEX_DIR = "indexedFiles";

	/**
	 *
	 * Basic index search with a field value
	 */
	public static ArrayList<String> searchInField(String fieldName, String textToFind) throws IOException, ParseException {
		ArrayList<String> toReturn = new ArrayList<String>();

		// Create lucene searcher. It search over a single IndexReader.
		IndexSearcher searcher = createSearcher();

		QueryParser qp = new QueryParser(fieldName, new StandardAnalyzer());
		Query query = qp.parse(textToFind);

		// search the index
		TopDocs foundDocs = searcher.search(query, 50);

		// Total found documents
		System.out.println("Total Results :: " + foundDocs.totalHits);

		// Let's print out the path of files which have searched term
		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			toReturn.add("ID : " + d.get("ID") + ", Generic_Name : " + d.get("Generic_Name"));
			System.out.println("ID : " + d.get("ID") + ", Generic_Name : " + d.get("Generic_Name") + ", "+ fieldName + " : " + d.get(fieldName));
		}

		return toReturn;
	}

	/**
	 * Index search with a field value and another field value
	 */
	public static void searchInField(String fieldName, String textToFind, String outputField)
			throws IOException, ParseException {
		// Create lucene searcher. It search over a single IndexReader.
		IndexSearcher searcher = createSearcher();

		QueryParser qp = new QueryParser(fieldName, new StandardAnalyzer());
		Query query = qp.parse(textToFind);

		// search the index
		TopDocs foundDocs = searcher.search(query, 50);

		// Total found documents
		System.out.println("Total Results :: " + foundDocs.totalHits);

		// Let's print out the path of files which have searched term
		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(fieldName + " : " + d.get(fieldName) + ", " + outputField + " : " + d.get(outputField));
		}
}


	private static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));

		// It is an interface for accessing a point-in-time view of a lucene
		// index
		IndexReader reader = DirectoryReader.open(dir);

		// Index searcher
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}
