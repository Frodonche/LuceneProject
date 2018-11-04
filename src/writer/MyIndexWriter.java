package writer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import treatment.StringTreatment;

public class MyIndexWriter {

	public MyIndexWriter(String docPathArg, String indexPath) {
		Date debut = new Date();
		final Path docPath = Paths.get(docPathArg);

		try {
			// org.apache.lucene.store.Directory instance
			Directory dir = FSDirectory.open(Paths.get(indexPath));

			// analyzer with the default stop words
			Analyzer analyzer = new StandardAnalyzer();

			// IndexWriter Configuration
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// IndexWriter writes new index files to the directory
			IndexWriter writer = new IndexWriter(dir, iwc);

			indexDoc(writer, docPath, Files.getLastModifiedTime(docPath).toMillis());
			writer.close();

			Date fin = new Date();
			System.out.println("Indexed in : "+(fin.getTime()-debut.getTime())+"ms");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {
			/*
			 * Ici il faut recup la String du Files.readAllBytes, et la decouper
			 * en Strings, medoc par medoc. Ensuite faire les textfield
			 * demandes, et updateDocument DONC un doc par medoc
			 */

			String[] myMedocs = new String(Files.readAllBytes(file)).split("#BEGIN_DRUGCARD ");

			int nbMedocs = myMedocs.length-1;
			System.out.println("J'ai " + nbMedocs + " medocs");
			System.out.println("Indexing...");

			int cpt = 0;



			for (String unMedoc : myMedocs) {
				if (cpt > 0) {
				//	System.out.println("Indexing "+cpt+" of "+nbMedocs+" medocs...");

					// Create lucene Document
					Document doc = new Document();

					// Get values from each medoc text
					String ID = StringTreatment.getID(unMedoc);
					String Brand_Mixtures = StringTreatment.getFieldContent("Brand_Mixtures", unMedoc);
					String Generic_Name = StringTreatment.getFieldContent("Generic_Name", unMedoc);
					String Synonyms = StringTreatment.getFieldContent("Synonyms", unMedoc);
					String Brand_Names = StringTreatment.getFieldContent("Brand_Names", unMedoc);
					String Description = StringTreatment.getFieldContent("Description", unMedoc);
					String Indication = StringTreatment.getFieldContent("Indication", unMedoc);
					String Pharmacology = StringTreatment.getFieldContent("Pharmacology", unMedoc);
					String Drug_Interactions = StringTreatment.getFieldContent("Drug_Interactions", unMedoc);


					// Index values

					doc.add(new TextField("ID", ID, Store.YES));
					doc.add(new LongPoint("modified", lastModified)); // utile?

					doc.add(new TextField("Brand_Mixtures", Brand_Mixtures, Store.YES));
					doc.add(new TextField("Generic_Name", Generic_Name, Store.YES));
					doc.add(new TextField("Synonyms", Synonyms, Store.NO));
					doc.add(new TextField("Brand_Names", Brand_Names, Store.NO));
					doc.add(new TextField("Description", Description, Store.NO));
					doc.add(new TextField("Indication", Indication, Store.NO));
					doc.add(new TextField("Pharmacology", Pharmacology, Store.NO));
					doc.add(new TextField("Drug_Interactions", Drug_Interactions, Store.YES));

					// Updates a document by first deleting the document(s)
					// containing <code>term</code> and then adding the new
					// document. The delete and then add are atomic as seen
					// by a reader on the same index
					writer.updateDocument(new Term("path", file.toString()), doc);

				}
				cpt++;
			}

			System.out.println("Done!");

		}
	}
}
