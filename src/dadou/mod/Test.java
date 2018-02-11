package dadou.mod;


import java.io.IOException;
import java.util.ArrayList;


public class Test  implements InterfaceObject{
	boolean executer = true;
	Driver driver;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Driver driver = new Driver(new Test(),8666);
		driver.waitConnection();
		driver.executer();
		

	}

	@Override
	public boolean isRuning() {
		// TODO Auto-generated method stub
		return executer;
	}

	@Override
	public void stop() {
		executer = false;
		
	}

	@Override
	public void setDriver(Driver driver) {
		this.driver = driver;
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		executer = true;
	}
	
	public int add(int a,int b) {
		return a+b;
	}
	public ArrayList<String> list() {
		return new ArrayList<>();
	}
	public void addList(ArrayList<String> ls,String s) {
		ls.add(s);
	}
	public int taille(ArrayList<String> ls) {
		return ls.size();
	}
	public String element(ArrayList<String> ls,int i) {
		return ls.get(i);
	}
	public void supprimer(long id) {
		driver.registry.remove(id);
	}

}
