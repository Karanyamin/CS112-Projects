package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		root = recursiveBuild(sc.nextLine());
	}
	
	private TagNode recursiveBuild(String tag) {
		TagNode temp;
		if (tag.charAt(0) == '<') {
			tag = tag.substring(1, tag.length() - 1);
			if (tag.charAt(0) != '/') {
				temp = new TagNode(tag, null, null);
				temp.firstChild = recursiveBuild(sc.nextLine());
				//WE want to check if scanner still has lines before we go for siblings
				if (!sc.hasNextLine()) return temp;
				temp.sibling = recursiveBuild(sc.nextLine());
			} else {
				return null;
			}
		} else {
			temp = new TagNode(tag, null, null);
			temp.sibling = recursiveBuild(sc.nextLine());
		}
		return temp;
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		recursiveReplace(root, newTag, oldTag);
	}
	
	private void recursiveReplace(TagNode ptr, String newTag, String oldTag) { //In order traversal
		if (ptr == null) return;
		recursiveReplace(ptr.firstChild, newTag, oldTag);
		if (ptr.tag.equals(oldTag)) ptr.tag = newTag;
		recursiveReplace(ptr.sibling, newTag, oldTag);
	}
	
	private void recursiveBoldRow(TagNode ptr, int row) {
		if (ptr == null) return;
		
		if (ptr.tag.equals("table")) {
			TagNode start = ptr.firstChild;
			int currentRow = 1;
			while (currentRow != row) {
				start = start.sibling;
				currentRow++;
			}
			start = start.firstChild; //This will go to the first td Node directly under tr
			bold(start);
		} else {
			recursiveBoldRow(ptr.firstChild, row);
			recursiveBoldRow(ptr.sibling, row);
		}
		
	}
	
	private void bold(TagNode ptr) {
		if (ptr == null) return;
		else {
			TagNode bold = new TagNode("b", ptr.firstChild, null);
			ptr.firstChild = bold;
			bold(ptr.sibling);
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		recursiveBoldRow(root, row);
	}
	
	private void replaceli(TagNode ptr) {
		if (ptr == null) return;
		replaceli(ptr.sibling);
		if (ptr.tag.equals("li")) ptr.tag = "p";
	}
	
	private TagNode recursiveRemove(TagNode ptr, String tag) {
		if (ptr == null) return null;
		if (ptr.tag.equals(tag)) {
			//Case 1 with two children
			if (ptr.tag.equals("ol") || ptr.tag.equals("ul")) replaceli(ptr.firstChild);
			if (ptr.firstChild != null && ptr.sibling != null) {
				//Find inorder predecessor
				TagNode pre = orderPredecessor(ptr.firstChild);
				pre.sibling = ptr.sibling;
				return ptr.firstChild;
			} else if (ptr.firstChild != null && ptr.sibling == null) {
				return ptr.firstChild;
			} else if (ptr.firstChild == null && ptr.sibling != null) {
				return ptr.sibling;
			} else {
				return null;
			}
		}
		ptr.firstChild = recursiveRemove(ptr.firstChild, tag);
		ptr.sibling = recursiveRemove(ptr.sibling, tag);
		return ptr;
	}
	
	private TagNode orderPredecessor(TagNode start) {
		if (start.sibling == null) return start;
		else return orderPredecessor(start.sibling);
	}
	
	private boolean search(TagNode ptr, String tag) {
		if (ptr == null) return false;
		if (ptr.tag.equals(tag)) return true;
		else {
			boolean temp = search(ptr.firstChild, tag);
			if (temp) return true;
			else return search(ptr.sibling, tag);
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		while (search(root, tag)) {
			recursiveRemove(root, tag);
		}
	}
	
	
	private void fix(TagNode original, TagNode ptr) {
		if (ptr == original || ptr == null) return;
		

		if (ptr.sibling != null)
			fix(original, ptr.sibling);
		else {
			ptr.sibling = original.sibling;
		}
	}
	
	
	
	private TagNode specialRecursiveBuild(TagNode ptr, String tagableWord, String tag) {
		String word = ptr.tag;
		if (word.length() == 0) return null;
		
		TagNode temp = ptr;
		TagNode temp2 = null;
		TagNode temp3 = null;
		TagNode temp4 = null; 
		while (word.toLowerCase().indexOf(tagableWord.toLowerCase()) != -1) { //That means tag is in word
			int index = word.toLowerCase().indexOf(tagableWord.toLowerCase());
			if (index == 0){
				if (index + tagableWord.length() < word.length()){ 
					if (word.charAt(index + tagableWord.length()) == '.' || word.charAt(index + tagableWord.length()) == ',' || word.charAt(index + tagableWord.length()) == '?' || word.charAt(index + tagableWord.length()) == '!' || word.charAt(index + tagableWord.length()) == ':' || word.charAt(index + tagableWord.length()) == ';'){
						if (index + tagableWord.length() + 1 < word.length()){
							if (word.charAt(index + tagableWord.length() + 1) == ' '){
								temp = new TagNode(tag, null, null);
								temp2 = new TagNode(word.substring(index, index + tagableWord.length() + 1), null, null);
								temp3 = new TagNode(word.substring(index + tagableWord.length() + 1), null, null);
								temp.firstChild = temp2;
								temp.sibling = specialRecursiveBuild(temp3, tagableWord, tag);
								break;
							} 
						} else {//Word with valid punctuation at end
							temp = new TagNode(tag, null, null);
							temp2 = new TagNode(word.substring(index, index + tagableWord.length() + 1), null, null);
							temp.firstChild = temp2;
							break;
						}
					} else if (word.charAt(index + tagableWord.length()) == ' '){
						temp = new TagNode(tag, null, null);
						temp2 = new TagNode(word.substring(index, index + tagableWord.length()), null, null);
						temp3 = new TagNode(word.substring(index + tagableWord.length()), null, null);
						temp.firstChild = temp2;
						temp.sibling = specialRecursiveBuild(temp3, tagableWord, tag);
						break;
					} 
				} else { //Word with no punctuation at end
					temp = new TagNode(tag, null, null);
					temp2 = new TagNode(word.substring(index, index + tagableWord.length()), null, null);
					temp.firstChild = temp2;
					break;
				}
			} else {
				if (word.charAt(index - 1) == ' '){ 
					if (index + tagableWord.length() < word.length()){ 
						if (word.charAt(index + tagableWord.length()) == '.' || word.charAt(index + tagableWord.length()) == ',' || word.charAt(index + tagableWord.length()) == '?' || word.charAt(index + tagableWord.length()) == '!' || word.charAt(index + tagableWord.length()) == ':' || word.charAt(index + tagableWord.length()) == ';'){
							if (index + tagableWord.length()  + 1 < word.length()){
								if (word.charAt(index + tagableWord.length() + 1) == ' '){
									temp = new TagNode(word.substring(0, index), null, null);
									temp2 = new TagNode(tag, null, null);
									temp3 = new TagNode(word.substring(index, index + tagableWord.length() + 1), null, null);
									temp4 = new TagNode(word.substring(index + tagableWord.length() + 1), null, null);
									temp.sibling = temp2;
									temp2.firstChild = temp3;
									temp2.sibling = specialRecursiveBuild(temp4, tagableWord, tag);
									break;
								} 
							} else {//Word with valid punctuation at end
								temp = new TagNode(word.substring(0, index), null, null);
								temp2 = new TagNode(tag, null, null);
								temp3 = new TagNode(word.substring(index, index + tagableWord.length() + 1), null, null);
								temp.sibling = temp2;
								temp2.firstChild = temp3;
								break;
							}
						} else if (word.charAt(index + tagableWord.length()) == ' '){
							temp = new TagNode(word.substring(0, index), null, null);
							temp2 = new TagNode(tag, null, null);
							temp3 = new TagNode(word.substring(index, index + tagableWord.length()), null, null);
							temp4 = new TagNode(word.substring(index + tagableWord.length()), null, null);
							temp.sibling = temp2;
							temp2.firstChild = temp3;
							temp2.sibling = specialRecursiveBuild(temp4, tagableWord, tag);
							break;
						} 
					} else { //Word with no punctuation at end
						temp = new TagNode(word.substring(0, index), null, null);
						temp2 = new TagNode(tag, null, null);
						temp3 = new TagNode(word.substring(index, index + tagableWord.length()), null, null);
						temp.sibling = temp2;
						temp2.firstChild = temp3;
						break;
					}
				} 
			}
		}
		return temp;
	}
	
	private void build(ArrayList<String> arr) {
		Stack<TagNode> main = new Stack<TagNode>();
		root = new TagNode("html", null, null);
		TagNode ptr = root;
		main.push(ptr);
		int i = 1;
		while(i < arr.size()){
			String nextLine = arr.get(i);
			if (nextLine.charAt(0) != '<'){//Means the line is a text
				//We need to check if the main.peek() has a child if no insert if yes travel to of child siblings list and insert
				if (main.peek().firstChild == null){
					main.peek().firstChild = new TagNode(nextLine, null, null);
				} else {
					TagNode tempPrev = null;
					TagNode tempPtr = main.peek().firstChild;
					while (tempPtr != null){
						tempPrev = tempPtr;
						tempPtr = tempPtr.sibling;
					}
					tempPrev.sibling = new TagNode(nextLine, null, null);
				}
			} else if (nextLine.charAt(0) == '<'){ //Means it was a tag
				if (nextLine.charAt(1) == '/'){ //We know its a closing tag and we can just push the latest tag in the Stack
					main.pop();
				} else { //Means it was an opening tag
					//The nextLine will be a Tag whose parent is the most recent Node in the Stack
					//We must check if the parent Node has any children, if not, make a new Node and make it its child if yes then traverse the silbing list until you can insert at the end
					if (main.peek().firstChild == null){
						TagNode temp = new TagNode(nextLine.substring(1, nextLine.length() - 1), null, null);
						main.peek().firstChild = temp;
						main.push(temp);
					} else {
						//We need to go to the end of the child's sibling list
						TagNode tempPrev = null;
						TagNode tempPtr = main.peek().firstChild;
						while (tempPtr != null){
							tempPrev = tempPtr;
							tempPtr = tempPtr.sibling;
						}
						TagNode temp = new TagNode(nextLine.substring(1, nextLine.length() - 1), null, null);
						tempPrev.sibling = temp;
						main.push(temp);
					}
				}
			}
			i++;
		}
	}
	
	
	private TagNode recursiveAddTag(TagNode ptr, String word, String tag) {
		if (ptr == null) return null;
		
		ptr.firstChild = recursiveAddTag(ptr.firstChild, word, tag);
		ptr.sibling = recursiveAddTag(ptr.sibling, word, tag);
		
		if (!ptr.tag.equals("html") && !ptr.tag.equals("body") && !ptr.tag.equals("p") && !ptr.tag.equals("em") && !ptr.tag.equals("b") && !ptr.tag.equals("table") && !ptr.tag.equals("tr") && !ptr.tag.equals("td") && !ptr.tag.equals("ol") && !ptr.tag.equals("ul") && !ptr.tag.equals("li")) { //Text Node
			TagNode changed = specialRecursiveBuild(ptr, word, tag);
			fix(ptr, changed);
			return changed; //NOTE: word in this method is not the same word in specialRecursiveBuild()
		}
		return ptr;
	}
	

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		//The problem is that when I edited text using substring so that indexOf doesnt index the same word again it messes up the index positions of the words. Need to fix asap
		/** COMPLETE THIS METHOD **/
		
		//root = recursiveAddTag(root, word, tag);
		String html = getHTML();
		StringTokenizer token = new StringTokenizer(html, "\n");
		ArrayList<String> main = new ArrayList<String>();
		while (token.hasMoreTokens()) {
			main.add(token.nextToken());
		}
		for (int i = 0; i < main.size(); i++) {
			if (main.get(i).charAt(0) != '<') { //Then we know it's a text Node
				//We have to parse through the text and separate what needs to be surrounded by tag and what needs to be left alone. We will put the separate tokens in a list
				//A word is valid if it contains the 'word' and has ". , ? ; :" right after it and nothing after that and nothing before that.
				//We can make everything lower-case to bypass case sensitivity
				word = word.toLowerCase();
				String text = main.get(i);
				String textcopy = text;
				ArrayList<Integer> seperated = new ArrayList<Integer>();
				int startingIndex = 0;
				while (text.toLowerCase().indexOf(word.toLowerCase(), startingIndex) != -1) {
					boolean changed = false;
					//We need to check if it starts with this word and nothing is before it
					int index = text.toLowerCase().indexOf(word.toLowerCase(), startingIndex);
					if (index - 1 >= 0) {
						if (text.charAt(index - 1) == ' ') {// Means that the word starts with the word and nothing before (Case 1)
							//Now (Case 2) We need to check whether there is a character after this word and if there is we need to check if it is equal to one of the valid characters.
							if (text.length() > index + word.length()) { // This tells us that there is a character after our word. Either a space or punc. We need to check both.
								if (text.charAt(index + word.length()) == '.' || text.charAt(index + word.length()) == ',' || text.charAt(index + word.length()) == '?' || text.charAt(index + word.length()) == ';' || text.charAt(index + word.length()) == ':') {
									//Now we need to check if there is no character second after the word
									if (text.length() > (index + word.length() + 1)) { // DONE
										if (text.charAt(index + word.length() + 1) == ' ') { //DONE
											//String indexes = "" + index + "" + index + word.length() + 1;
											seperated.add(index);
											seperated.add(index + word.length() + 1);
											//text = text.substring(index + word.length() + 1);
											startingIndex = index + word.length() + 1;
											changed = true;
										}
									} else { // punc is the last character 
										//String indexes = "" + index + "" + index + word.length() + 1;
										seperated.add(index);
										seperated.add(index + word.length() + 1);
										//text = text.substring(index + word.length() + 1);
										startingIndex = index + word.length() + 1;
										changed = true;
									}
								} else if (text.charAt(index + word.length()) == ' ') {//We can add it to the list 
									//String indexes = "" + index + "" + index + word.length();
									seperated.add(index);
									seperated.add(index + word.length());
									//text = text.substring(index + word.length());
									startingIndex = index + word.length();
									changed = true;
								}
							} else { //Case 2 but the word is at the end of the text // The
								//String indexes = "" + index + "" + word.length();
								seperated.add(index);
								seperated.add(index + word.length());
								//text = text.substring(index + word.length());
								startingIndex = index + word.length();
								changed = true;
							}
						}
					} else { //Out word is at the begining
						if (text.length() > index + word.length()) { // This tells us that there is a character after our word. Either a space or punc. We need to check both.
							if (text.charAt(index + word.length()) == '.' || text.charAt(index + word.length()) == ',' || text.charAt(index + word.length()) == '?' || text.charAt(index + word.length()) == ';' || text.charAt(index + word.length()) == ':') {
								//Now we need to check if there is no character second after the word
								if (text.length() > (index + word.length() + 1)) { // DONE
									if (text.charAt(index + word.length() + 1) == ' ') { //DONE
										//String indexes = "" + index + "" + index + word.length() + 1;
										seperated.add(index);
										seperated.add(index + word.length() + 1);
										//text = text.substring(index + word.length() + 1);
										startingIndex = index + word.length() + 1;
										changed = true;
									}
								} else { // punc is the last character 
									//String indexes = "" + index + "" + index + word.length() + 1;
									seperated.add(index);
									seperated.add(index + word.length() + 1);
									//text = text.substring(index + word.length() + 1);
									startingIndex = index + word.length() + 1;
									changed = true;
								}
							} else if (text.charAt(index + word.length()) == ' ') {//We can add it to the list 
								//String indexes = "" + index + "" + index + word.length();
								seperated.add(index);
								seperated.add(index + word.length());
								//text = text.substring(index + word.length());
								startingIndex = index + word.length();
								changed = true;
							}
						} else { //Case 2 but the word is at the end of the text // The
							//String indexes = "" + index + "" + word.length();
							seperated.add(index);
							seperated.add(index + word.length());
							//text = text.substring(index + word.length());
							startingIndex = index + word.length();
							changed = true;
						}
					}
					if (!changed) {
						break;
					}
				}
				//We need to check if anything actually needs to be changed in the text. If not then we break out
				if (seperated.size() == 0) continue;
				//Every element of separate has the index positions of the words we need to change
				//FIX buggy
				for (int k = 0; k < seperated.size(); k = k + 2) {
					int index1 = seperated.get(k);
					int index2 = seperated.get(k + 1);
					textcopy = textcopy.substring(0, index1) + "<" + tag + ">" + textcopy.substring(index1, index2) + "</" + tag + ">" + textcopy.substring(index2);
					for (int l = k + 2; l < seperated.size(); l++) {
						int changed = seperated.get(l) + 5 + 2 * tag.length();
						seperated.remove(l);
						seperated.add(l, changed);
						//seperated.set(k, seperated.get(k) + 5 + 2 * tag.length());
					}
				}
				//At this point textcopy has the edited text will the open and close tags in taggable words
				ArrayList<String> changed = new ArrayList<String>();
				while (textcopy.indexOf("<" + tag + ">") != -1) {
					int beginIndex = textcopy.indexOf("<" + tag + ">");
					int endIndex = textcopy.indexOf("</" + tag + ">");
					changed.add(textcopy.substring(0, beginIndex));
					changed.add("<" + tag + ">");
					changed.add(textcopy.substring(beginIndex + 2 + tag.length(), endIndex));
					changed.add("</" + tag + ">");
					textcopy = textcopy.substring(endIndex + 3 + tag.length());
				}
				changed.add(textcopy);
				main.remove(i);
				for (int j = changed.size() - 1; j >= 0; j--) {
					main.add(i, changed.get(j));
				}
				i = i + changed.size() - 1;
				
				//We need to remove any blank lines from main
				for (int j = 0; j < main.size(); j++) {
					if (main.get(j).length() == 0) {
						main.remove(j);
					}
				}
			}
		}
		build(main);
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
