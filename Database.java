import java.io.*;
import java.util.Scanner;

/*
* Database class implements the Library Interface. It performs 
* multiple actions on a library of books. There are various books in the
* library.
*
*/
public class Database implements Library{

	//These attributes can be used throughout the class
	private static File fileName;
	private static String database;
	private String[] arrayOfBooksInLibrary;
	private String phrase;
	private double libraryLines = 0;
	
	//Constructor to set the database variable
	public Database(String database) {
		this.database = database;
	}
	



	/*
	*brings in the library fileName. Most likely will be database.txt
	*
	*@params String libraryFileName brings in the fileName of the library
	*@returns boolean if you can set the file
	*/
	@Override

	public boolean setLibraryFile(String libraryFileName) {
		// Scanner class to get input
		// fileName variable is set
		Scanner sc;
		try {
			sc = new Scanner(new File(libraryFileName));
			fileName =  new File(libraryFileName);
		}
		catch (IOException e) {
			System.out.println("The file " + libraryFileName +" does not exist in this directory.");
			return false;
		}
		sc.close();
		return true;
	}


	@Override
	public File getLibraryFile() {
		// TODO Auto-generated method stub
		//System.out.println(fileName);
		return fileName;
	}


	/*
	* Has two conditions, with or without path. Scans through the array of books 
	* in the book library and returns them based on the conditions.
	*
	*
	*@params boolean withPath will return bookName with path or just name
	*@returns String[] books. 
	*/
	@Override
	public String[] getLibraryBooks(boolean withPath) {
		// creates book array 
		String[] books;
		int counter = 0;
		Scanner sc = null;
		Scanner setArray = null;
		
		try {
			sc = new Scanner(new File(database));
			// gets the length of the database (counter)
			while (sc.hasNext() != false) {
				String token = sc.next();
				counter++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not find database.");
		}

		sc.close();
		//initializes books array with length counter
		books = new String[counter];
		try {
			setArray = new Scanner(new File(database));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find database.");
		}

		// loops thorugh the array and sets each index to the book names
		int i = 0;
		while (setArray.hasNext() != false) {
			String token = setArray.next();
			// with path, assign the whole string to the array index
			if (withPath == true) {
				books[i] = token;
				i++;
			} 
			// without path, assign only the name to the array index. 
			// cut off at '/' char. 
			else {
				int hold = 0;
				for (int j = 0; j < token.length(); j++) {
					char c = token.charAt(j);
					if (c == '/') {
						hold = j;
					}
				}
				String subset = token.substring(hold + 1);
				books[i] = subset;
				i++;
				
			}
			
		}
		setArray.close();
		//assigns attribute arrayOfBooksInLibrary
		arrayOfBooksInLibrary = books;
		return books;
	}
	

	/*
	* Iterates through the array to find its length
	*
	*
	* @returns int counter
	*
	*/

	@Override
	public int getNumberOfBooks() {
		// TODO Auto-generated method stub
		Scanner sc = null;
		int counter = 0;
		try {
			sc = new Scanner(new File(database));
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find the file.");
		}
		
		// counter will increase for each line in the array
		while (sc.hasNext() != false) {
			counter++;
			sc.next();
		}
		sc.close();
		//System.out.println(counter);
		return counter;
	}


	/*
	* for each book in the library, it will call getBookOccurences()
	* with the current index i and the param phrase
	*
	* @params String phrase phrase that needs to be found within the library
	* @returns int count number of times the phrase occurs
	*/
	@Override
	public int getLibraryOccurrences(String phrase) {

		int count = 0;
		for (int i=0;i<arrayOfBooksInLibrary.length;i++)
		{
			count += getBookOccurrences(i,phrase);
		}
		return count;
	}

	/*
	* Reads through the file and assigns each token to string contents
	* Makes all characters lowercase and without punctuation 
	*
	*
	*@params String bookName 
	*@ returns String contents - string of all the contents in the file
	*/

	private String getContentsInBook(String bookName) {
		Scanner sc = null;
		Scanner newScan = null;
		
		try {
			sc = new Scanner(new File("./text/" + bookName));
		}
		catch (FileNotFoundException e) {
			System.out.println("Not found");
		}

		// the lines will be amended to contents. All lowercase with no punctuation
		String contents = "";
		while (sc.hasNextLine() != false) {
			contents += sc.nextLine().toLowerCase();
			contents += "\n";
		}
		//System.out.println("Contents: " + contents);
		return contents.replaceAll("[!.;?,:\"'/\\()`]", " ");
	}
	
	/*
	*
	* Uses method indexOf to recursively find each phrase
	*
	* @params contents, phrase 
	* @returns int countPhrase - number of times phrase occurs in the string
	*/
	private int phrasesInString(String contents, String phrase) {
		int countPhrase = 0;
		// will be the index of the first instance of the phrase
		int contentIndex = contents.indexOf(phrase);

		while (contentIndex != -1) {
			countPhrase++;
			//will start search for the next instance of the phrase
			contentIndex = contents.indexOf(phrase,contentIndex+1);
		}
		return countPhrase;
	}
	

	/*
	* Helper method for getLibraryOccurences
	* reads through the contents in the specified book and uses the 
	* phrasesInString method to count the number of times the phrase is found
	* 
	*@params bookNumner, phrase
	*@returns int count
	*/
	@Override
	public int getBookOccurrences(int bookNumber, String phrase) {
		// calls getContentsInBook and phrasesInString
		String contents = getContentsInBook(arrayOfBooksInLibrary[bookNumber]);
		int count = phrasesInString(contents,phrase);
		return count;
	}

	/*
	* uses Scanner file reader to see how many lines
	* are in the book. Uses hasNexLine() method. 
	*
	* @params bookNumber
	* @returns int countLine 
	*/
	@Override
	public int getBookNumLines(int bookNumber) {
		// Initializes variables 
		Scanner sc = null;
		int countLine = 0;

		try {
			sc = new Scanner(new File("./text/" + arrayOfBooksInLibrary[bookNumber]));
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find book.");
		}

		// makes line attribute
		while (sc.hasNextLine() != false) {
			String line = sc.nextLine().toLowerCase().replaceAll("[!.;?,:\"'/\\()`]", " ");
			countLine++;
		}
		return countLine;
	}


	/*
	* get the percenrage of the parameter newlineChars in the library
	*
	* @params newlineChars
	* @returns double percent
	*/ 

	@Override
	public double getLibraryNewlinePercentage(char[] newlineChars) {
		// calls to get libraryLines variable
		getBookNewlineCount();
		int countAll = 0;
		// iterates through each book and then adds to countAll
		// calls getBookNewlinePercentage method
		for (int i = 0; i < arrayOfBooksInLibrary.length; i++) {
			countAll += getBookNewlinePercentage(newlineChars, i);
		}
		double percent = countAll/libraryLines;
		return 0.0;
	}

	/*
	* helper method for getLibraryNewlinePrecentage
	* iteraties through the string version of the file and
	* determines if how many characters there are
	* 
	* @params newlineChars, bookNumber
	* @returns count of how many times the newlineChars is in the book
	*/
	public int getBookNewlinePercentage(char[] newlineChars, int bookNumber) {
		int count = 0;
		double total = getBookNewlineCount();
		String newlineString = "";
		// creates string version of book file
		String ns = getContentsInBook(arrayOfBooksInLibrary[bookNumber]);
		
		// creates string version of character array
		for (int s = 0; s < newlineChars.length; s++) {
			newlineString += newlineChars[s];
		}

		// sets size
		int size = newlineChars.length;
		// i is index of first occurence of character
		int i = ns.indexOf(newlineString);
		while (i != -1) {
			if (size == 1) { 
				// There are only two possible starting points: '\r' or '\n'
				// First possibility is '\r'
				if (ns.charAt(i) == '\r') {
					
					if (i == 0) {
						// checks to see if it has double characters
						if (ns.charAt(i+1) == '\n') {
							count += 0;
						} else {
							
							count++;
						}
					}
					
					else {
						//checks for characters before and after to be '\n'
						if (ns.charAt(i-1) == '\n'){
							count += 0;
						}
						
						else if (i < ns.length()-1) {
							
							if (ns.charAt(i+1) == '\n') {
								count += 0;
							} else {
								
								count++;
							}
						}
						else {	
							count++;
						}
					}
				}
				// Second possibility
				if (ns.charAt(i) == '\n') {
					
					if (i == 0) {
						if (ns.charAt(i+1) == 'r') {
							count += 0;
						} else {
							count++;
						}
					}
					else {
						if (ns.charAt(i-1) == '\r'){
							count += 0;
						}
						
						else if (i < ns.length()-1) {
							
							//System.out.println("st length: " + st.length());
							if (ns.charAt(i+1) == '\r') {
								count += 0;
							} 
							else {
								
								count++;
							}
							
						}
					}
				} 
			}
			else {
				count++;
			}
		// recursion. will start looking for the next occurence of the character at the next index  
		i = ns.indexOf(newlineString,(i+1));
		}

		//System.out.println("Count is: " + count);
		
		return count;
	}

	/*
	*gets the number of times the newline character
	*occurs in the book
	*
	* @returns double libraryLineCount
	*/
	public double getBookNewlineCount() {
		double libraryLineCount = 0.0;
		//libraryLineCount uses getBookNumLines for each book
		for(int i = 0; i < arrayOfBooksInLibrary.length; i++) {
			libraryLineCount = getBookNumLines(i);
		}
		//Sets attribute
		this.libraryLines = libraryLineCount;
		return libraryLineCount;
	}
	

	/*
	* uses FileReader to see what newline character is being used in the book file
	*
	*@params bookNumber
	*@returns char[] charArray or null
	*/

	@Override
	public char[] getBookNewlineCharacters(int bookNumber){
		FileReader fr = null;
		BufferedReader br = null;
		
		
		try {
			br = new BufferedReader(new FileReader("./text/test.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file");
		}
		

		int i = 0;
		try {
			i = br.read();
		} catch (IOException e) {
			System.out.println("Could not do task.");
		}
		while (i != -1) {
			//System.out.println((char)i);
			if ((char)i == '\n'){
				if (i==0) {
					
						if((char)i == '\r') {
							char[] charArray = {'\n','\r'};
							return charArray;
						}
						else {
							char[] charArray = {'\n'};
							return charArray;
						}
					
				}
				else if ((char)(i+1) != '\r' && (char)(i-1) != '\r') {
					char[] charArray = {'\n'};
					return charArray;
				} 
				else {
					if ((char)(i-1) != '\r') {
						char[] charArray = {'\n','\r'};
						return charArray;
					}
				}
			}
				
			if ((char)i == '\r'){
				if (i==0) {
					if((char)(i+1) == '\n') {
						char[] charArray = {'\r','\n'};
						return charArray;
					}
					else {
						char[] charArray = {'\r'};
						return charArray;
					}
				}
				else if ((char)(i+1) != '\n' && (char)(i-1) != '\n') {
					char[] charArray = {'\r'};
					return charArray;
				} 
				else {
					if ((char)(i-1) != '\n') {
						char[] charArray = {'\r','\n'};
						return charArray;
					}
				}
			}
			try {
				i = br.read();
			} 
			catch (IOException e) {
				System.out.println("Could not read().");
			}
		}
		return null;
	}
	

	
	// public static void main(String[] args) {
	// 	System.out.print("Enter a database file: ");
	// 	Scanner in = new Scanner(System.in);
		
	// 	Database d = new Database(in.nextLine());
		
	//  	//set the library
	//  	boolean couldSetBook = d.setLibraryFile(database);
	//  	if(!couldSetBook) {
	//  		System.out.println("Could not set library!");
	//  	}
		
	//  	//get the library
	//  	File libraryFile = d.getLibraryFile();
	//  	if(libraryFile == null) {
	//  		System.out.println("Could not get library!");
	//  	}
		
	//  	//check number of books in the library
	//  	int numBooks = d.getNumberOfBooks();
	//  	if(numBooks == -1) {
	//  		System.out.println("Could get number of books!");
	//  	}
		
	//  	//get the list of books with paths
	//  	String[] books = d.getLibraryBooks(true);
	//  	if(books == null) {
	//  		System.out.println("Could get list of books (w/path)!");
	//  	}
	//  	System.out.println("Books:");
	//  	for(int i = 0; i < books.length; i++)
	//  		System.out.println("\t" + i + ". " + books[i]);
		
	//  	//get the list of books without paths
	//  	books = d.getLibraryBooks(false);
	//  	if(books == null) {
	//  		System.out.println("Could get list of books (no path)!");
	//  	}
	//  	System.out.println("Books:");
	//  	for(int i = 0; i < books.length; i++)
	//  		System.out.println("\t" + i + ". " + books[i]);
		
	//  	//number of lines
	//  	System.out.println("Number of lines per book:");
	//  	for(int i = 0; i < books.length; i++) {
	//  		int numLines = d.getBookNumLines(i);
	//  		System.out.println("\t" + i + ". " + ((numLines == -1) ? "Can not load book" : numLines));
	//  	}

	//  	double bookLines = d.getBookNewlineCount();
	//  	char[] charStuff = d.getBookNewlineCharacters(0);
	
	// 	//newline characters
	//  	System.out.println("Newline characters per book:");
	//  	for(int i = 0; i < books.length; i++) {
	//  		char[] newline = d.getBookNewlineCharacters(i);
	//  		System.out.print("\t" + i + ". ");
	//  		if(newline == null) {
	//  			System.out.println("Can not load book");
	//  		}
	//  		else if(newline.length == 0) {
	//  			System.out.println("No newline characters!");
	//  		}
	// 		else if(newline.length == 1) {
	//  			System.out.println(newline[0] == '\n' ? "\\n" : "\\r");
	//  		}
	//  		else {
	//  			System.out.print(newline[0] == '\n' ? "\\n" : "\\r");
	//  			System.out.println(newline[1] == '\n' ? "\\n" : "\\r");
	//  		}
	//  	}
		
	//  	// //newline characters
	//  	System.out.println("Percentage newline:");
	//  	System.out.println("\t\\r: " + String.format("%.2f",d.getLibraryNewlinePercentage(new char[] {'\r'})));
	//  	System.out.println("\t\\n: " + String.format("%.2f",d.getLibraryNewlinePercentage(new char[] {'\n'})));
	//  	System.out.println("\t\\r\\n: " + String.format("%.2f",d.getLibraryNewlinePercentage(new char[] {'\r','\n'})));
	//  	System.out.println("\t\\n\\r: " + String.format("%.2f",d.getLibraryNewlinePercentage(new char[] {'\n','\r'})));
		
	//  	//occurrences
	//  	String phrase = "the";
	//  	int occurr = d.getBookOccurrences(0, phrase);
	//  	if(occurr == -1) {
	//  		System.out.println("Could not load book!");
	//  	}
	//  	System.out.println("Occurrences of the phrase \""+phrase+"\":" + occurr);
	
	//  	//occurrences
	//  	phrase = "the house";
	//  	occurr = d.getBookOccurrences(0, phrase);
	//  	if(occurr == -1) {
	//  		System.out.println("Could not load book!");
	//  	}
	//  	System.out.println("Occurrences of the phrase \""+phrase+"\":" + occurr);
	// }
}

