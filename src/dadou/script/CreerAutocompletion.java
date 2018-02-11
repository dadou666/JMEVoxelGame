package dadou.script;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dadou.MondeInterfacePublic;

public class CreerAutocompletion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		for(Method m:MondeInterfacePublic.class.getDeclaredMethods()) {
			System.out.println(m.getName()+";$."+m.getName()+";");
			
		}

	}

}
