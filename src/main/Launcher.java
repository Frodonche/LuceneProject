package main;

import java.util.ArrayList;
import java.util.Scanner;

import reader.MyIndexReader;
import writer.MyIndexWriter;

public class Launcher {

	public static void main(String[] args) throws Exception {
		System.out.println("Mini projet Lucene (v. 7.5.0)");

		int choice = -1;

		while (choice != 0) {
			afficherMenu();

			Scanner scanner = new Scanner(System.in);
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				MyIndexWriter writer = new MyIndexWriter("content/drugbank.txt", "indexedFiles");
				break;
			case 4:
				MyIndexReader.searchInField("Generic_Name", "aspirin");
				MyIndexReader.searchInField("Synonyms", "aspirin");
				MyIndexReader.searchInField("Brand_Names", "aspirin");
				break;
			case 5:
				System.out.println("Indication");
				ArrayList<String> indicArray = MyIndexReader.searchInField("Indication", "diabetes");
				System.out.println("Description");
				ArrayList<String> descArray = MyIndexReader.searchInField("Description", "diabetes");

				System.out.println("Common");
				ArrayList<String> commonArray = new ArrayList<String>();
				for(String s : indicArray)
					if(descArray.contains(s))
						commonArray.add(s);

				for(String s : commonArray)
					System.out.println(s);

				break;
			case 6:
				System.out.println("Drug_Interactions");
				MyIndexReader.searchInField("Drug_Interactions", "Mercaptopurine", "Generic_Name");

				break;
			case 0:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Unexpected choice");
			}
		}

	}

	private static void afficherMenu(){
		System.out.println();
		System.out.println("-- Menu --");
		System.out.println("1. Write index");
		System.out.println("4. Question 4");
		System.out.println("5. Question 5");
		System.out.println("6. Question 6");
		System.out.println("0. Exit");
	}
}
