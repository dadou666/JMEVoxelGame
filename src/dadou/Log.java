package dadou;

public class Log {
	public static void print(Object ...args) {
		try {
			throw new Error();
		} catch(Error e) 
		{
			System.out.print(e.getStackTrace()[1]);
			for(Object a:args) {
				System.out.print(" ");
				System.out.print(a);
			}
			System.out.println();
		}
	}
	public static void printCaller(Object ...args) {
		try {
			throw new Error();
		} catch(Error e) 
		{
			System.out.print(e.getStackTrace()[2]);
			for(Object a:args) {
				System.out.print(" ");
				System.out.print(a);
			}
			System.out.println();
		}
	}
}
