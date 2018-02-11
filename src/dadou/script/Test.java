package dadou.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {

	  public static void main(String[] args) {
		  Test obj = new Test();
			System.out.println(obj.getFile("/dadou/completion.txt"));		
		  }

		  private String getFile(String fileName) {

			StringBuilder result = new StringBuilder("");


		
			InputStream in = dadou.MondeInterfacePrive.class.getResourceAsStream(fileName); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Scanner scanner = new Scanner(reader);

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.append(line).append("\n");
				}

				scanner.close();

			
			
			return result.toString();

		  }

}
