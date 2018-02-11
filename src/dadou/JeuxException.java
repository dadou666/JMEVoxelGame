package dadou;

public class JeuxException extends Exception{
	public String msg;
	public Throwable throwable;
	public JeuxException(Object ...args) {
		StringBuilder sb = new StringBuilder();
		for(Object obj:args) {
		sb.append(obj);
		sb.append(" ");
			
		}
		this.msg = sb.toString();
		
	}
	public JeuxException(Throwable throwable,Object ...args) {
		StringBuilder sb = new StringBuilder();
		for(Object obj:args) {
		sb.append(obj);
		sb.append(" ");
			
		}
		this.msg = sb.toString();
		this.throwable = throwable;
		
	}
}
