package dadou.data;

public class SyntaxError extends Exception {
	public Parser  parser;
	public SyntaxError(Parser p) {
		this.parser = p;
		
	}
	public String toString() {
		return "<"+parser.idx+">"+ parser.source.substring(0, parser.idx);
	}

}
