package dadou.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
	String source;
	int idx = 0;

	public Parser(String source) {
		this.source = source;
		this.idx = 0;
	}

	public Int readInt() {
		int idxSave = idx;
		skip();
		if (idx == source.length()) {
			idx = idxSave;
			return null;

		}
		int idxBegin = idx;
		while (idx < source.length() && Character.isDigit(source.charAt(idx))) {
			idx++;
		}
		if (idx == idxBegin) {
			idx = idxSave;
			return null;
		}
		String s = source.substring(idxBegin, idx);

		return new Int(Integer.parseInt(s));

	}

	public void skip() {
		char space = ' ';
		while (idx < source.length()
				&& (source.charAt(idx) == space || source.charAt(idx) == '	'
						|| source.charAt(idx) == '\n' || source.charAt(idx) == '\t')) {

			idx++;
		}
	}

	public Real readReal() throws SyntaxError {
		int idxSave = idx;
		skip();
		if (idx == source.length()) {
			idx = idxSave;
			return null;

		}
		int idxBegin = idx;
		while (idx < source.length() && Character.isDigit(source.charAt(idx))) {
			idx++;
		}
		if (idx == source.length()) {
			throw new SyntaxError(this);

		}
		if (idx == idxBegin) {
			idx = idxSave;
			return null;
		}
		if (source.charAt(idx) != '.') {
			idx = idxSave;
			return null;
		}
		idx++;
		if (idx == source.length()) {
			throw new SyntaxError(this);

		}
		if (!Character.isDigit(source.charAt(idx))) {
			throw new SyntaxError(this);
		}
		idx++;
		while (idx < source.length() && Character.isDigit(source.charAt(idx))) {
			idx++;
		}
		String s = source.substring(idxBegin, idx);

		return new Real(Float.parseFloat(s));
	}

	public Symbol readSymbol() {
		int idxSave = idx;
		this.skip();
		int idxBegin = idx;
		while (idx < source.length()
				&& (Character.isAlphabetic(source.charAt(idx)) || source
						.charAt(idx) == '.')) {
			idx++;
		}
		if (idx == idxBegin) {
			idx = idxSave;
			return null;
		}
		String s = source.substring(idxBegin, idx);
		return new Symbol(s);

	}

	public Symbol readName() throws SyntaxError {
		int idxSave = idx;
		this.skip();
		if (idx == source.length()) {
			idx = idxSave;
			return null;
		}
		if (source.charAt(idx) != '@') {
			idx = idxSave;
			return null;
		}
		idx++;
		Symbol s = this.readSymbol();
		if (s == null) {
			throw new SyntaxError(this);
		}
		return s;
	}

	public boolean primReadString(StringBuffer sb) throws SyntaxError {
		skip();
		if (idx == source.length()) {

			return false;

		}
		char stringBegin = source.charAt(idx);
		if (stringBegin != '"' && stringBegin != '\'') {

			return false;
		}
		idx++;
		int idxBegin = idx;
		while (idx < source.length() && source.charAt(idx) != stringBegin) {
			idx++;
		}
		if (idx == source.length()) {
			throw new SyntaxError(this);

		}

		sb.append(source.substring(idxBegin, idx));
		idx++;

		return true;

	}

	public Str readStr() throws SyntaxError {
		StringBuffer sb = new StringBuffer();
		int idxSave = idx;
		if (!this.primReadString(sb)) {
			idx = idxSave;
			return null;
		}
		return new Str(sb.toString());

	}

	public Obj readObj() throws SyntaxError {
		Obj r;
		if (idx == source.length()) {
			return null;
		}
		Symbol name = this.readName();
		if (name != null) {
			r = map.get(name.valeur);
			if (r == null) {
				throw new SyntaxError(this);
			}
			return r;
		}
		r = this.readCollection();
		if (r != null) {
			return r;
		}
		r = this.readInstance();
		if (r != null) {
			return r;
		}
		r = this.readReal();
		if (r != null) {
			return r;
		}
		r = this.readInt();
		if (r != null) {
			return r;
		}

		r = this.readStr();
		if (r != null) {
			return r;
		}
		r = this.readFunctor();
		if (r != null) {
			return r;
		}
		
		r = this.readSymbol();
		return r;
	}

	public Collection readCollection() throws SyntaxError {
		int idxSave = idx;
		this.skip();
		if (idx == source.length() ) {
			idx = idxSave;
			return null;
		}
		if (source.charAt(idx) != '[') {
			idx = idxSave;
			return null;
		}
		idx++;
		Collection r = new Collection();
		Obj o = this.readObj();
		while (o != null) {
			r.values.add(o);
			o = this.readObj();

		}
		this.skip();
		if (source.charAt(idx) != ']') {
			throw new SyntaxError(this);

		}
		idx++;
		return r;

	}

	public Instance readInstance() throws SyntaxError {
		int idxSave = idx;
		this.skip();
		Symbol type = this.readSymbol();
		if (type == null) {
			idx = idxSave;
			return null;
		}
		this.skip();
		if (source.charAt(idx) != '{') {
			idx = idxSave;
			return null;
		}
		idx++;
		Instance r = new Instance();
		r.type = type.valeur;
		Symbol name = this.readSymbol();
		while (name != null) {

			Obj o = this.readObj();
			if (o == null) {
				throw new SyntaxError(this);
			}
			r.values.put(name.valeur, o);
			name = this.readSymbol();

		}
		this.skip();
		if (source.charAt(idx) != '}') {
			throw new SyntaxError(this);

		}
		idx++;
		return r;

	}

	public Functor readFunctor() throws SyntaxError {
		int idxSave = idx;
		this.skip();
		Symbol name = this.readSymbol();
		if (name == null) {
			idx = idxSave;
			return null;
		}
		if (source.charAt(idx) != '(') {
			idx = idxSave;
			return null;
		}
		idx++;
		Functor r = new Functor();
		r.name = name.valeur;
		Obj o = this.readObj();
		while (o != null) {
			r.values.add(o);
			o = this.readObj();

		}
		this.skip();
		if (source.charAt(idx) != ')') {
			throw new SyntaxError(this);

		}
		idx++;
		return r;

	}

	Map<String, Obj> map = new HashMap<>();

	public List<Obj> readAll() throws SyntaxError {
		List<Obj> result = new ArrayList<>();

		while (true) {
			Symbol name = this.readName();
			if (name != null) {
				Obj obj = this.readObj();
				if (obj == null) {
					throw new SyntaxError(this);
				}
				map.put(name.valeur, obj);
			} else {
				Obj obj = this.readObj();
				if (obj == null) {
					return result;
				}
				result.add(obj);

			}

		}

	}

}
