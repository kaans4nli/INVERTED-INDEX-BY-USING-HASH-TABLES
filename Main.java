package blessed_data_hw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		HashedDictionary<String, String> hashTable = new HashedDictionary<String, String>();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Please enter the load factor size : ");
		double load_factor = sc.nextDouble();
		hashTable.chosenLoadFactor(load_factor);
		
		System.out.print("Please enter the hash funtion (SSF / PAF) : ");
		String hash_function = sc.next();
		hashTable.chosenHashFunction(hash_function);
		
		System.out.print("Please enter the collision handling (LP / DH) : ");
		String collision_handling = sc.next();
		hashTable.chosenCollisionHandling(collision_handling);
		
		String DELIMITERS = "[-+=" +

		        " " +        //space

		        "\r\n " +    //carriage return line fit

				"1234567890" + //numbers

				"’'\"" +       // apostrophe

				"(){}<>\\[\\]" + // brackets

				":" +        // colon

				"," +        // comma

				"‒–—―" +     // dashes

				"…" +        // ellipsis

				"!" +        // exclamation mark

				"." +        // full stop/period

				"«»" +       // guillemets

				"-‐" +       // hyphen

				"?" +        // question mark

				"‘’“”" +     // quotation marks

				";" +        // semicolon

				"/" +        // slash/stroke

				"⁄" +        // solidus

				"␠" +        // space?   

				"·" +        // interpunct

				"&" +        // ampersand

				"@" +        // at sign

				"*" +        // asterisk

				"\\" +       // backslash

				"•" +        // bullet

				"^" +        // caret

				"¤¢$€£¥₩₪" + // currency

				"†‡" +       // dagger

				"°" +        // degree

				"¡" +        // inverted exclamation point

				"¿" +        // inverted question mark

				"¬" +        // negation

				"#" +        // number sign (hashtag)

				"№" +        // numero sign ()

				"%‰‱" +      // percent and related signs

				"¶" +        // pilcrow

				"′" +        // prime

				"§" +        // section sign

				"~" +        // tilde/swung dash

				"¨" +        // umlaut/diaeresis

				"_" +        // underscore/understrike

				"|¦" +       // vertical/pipe/broken bar

				"⁂" +        // asterism

				"☞" +        // index/fist

				"∴" +        // therefore sign

				"‽" +        // interrobang

				"※" +          // reference mark

		        "]";
		
		String[] splitted;
		
		SingleLinkedList stop_words = new SingleLinkedList();
		String row;
		BufferedReader br = new BufferedReader(new FileReader("src/stop_words_en.txt"));
		while ((row = br.readLine()) != null) {
			br.readLine();
			stop_words.add(row);
		}
		br.close();
		
		File folder = new File("src/bbc");
		File[] listOfFiles = folder.listFiles();
		File[] listOfTexts = null;
		
		long start_indexing_time = System.nanoTime();
		for (int i = 0; i < listOfFiles.length; i++) {
			listOfTexts = listOfFiles[i].listFiles();
			
			for (int j = 0; j < listOfTexts.length; j++) {
				BufferedReader my_text = new BufferedReader(new FileReader(listOfTexts[j]));
				while ((row = my_text.readLine()) != null) {
					splitted = row.split(DELIMITERS);
					
					for (int k = 0; k < splitted.length; k++) {
						if (!(stop_words.isContainOfValue(splitted[k].toLowerCase()))) {
							hashTable.put(splitted[k].toLowerCase(), listOfTexts[j].toString());
						}
					}
				}
				my_text.close();
			}
		}
		long estimated_indexing_time = System.nanoTime() - start_indexing_time;
		
		long row_count = 0;
		long min_time = 999999999;
		long max_time = 0;
		long searched_time = 0;
		
		long start_time_avg = System.nanoTime();
		BufferedReader searching_text = new BufferedReader(new FileReader("src/search.txt"));
		while ((row = searching_text.readLine()) != null) {
			searched_time = System.nanoTime();
			hashTable.getValue(row);
			if (System.nanoTime()-searched_time > max_time)
				max_time = System.nanoTime()-searched_time;
			if (System.nanoTime()-searched_time < min_time)
				min_time = System.nanoTime()-searched_time;
			row_count++;
		}
		searching_text.close();
		long estimated_time_avg = System.nanoTime() - start_time_avg;
		
		long avg_search_time = estimated_time_avg / row_count;
		System.out.println(hashTable.getCC());
		System.out.println(estimated_indexing_time);
		System.out.println(avg_search_time);
		System.out.println(min_time);
		System.out.println(max_time);
		
		
		
	}

}
