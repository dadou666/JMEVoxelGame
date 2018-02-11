package dadou.test;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
			Thread t1 = new Thread(()->{
				for(int i=0;i < 10000;i++){
					System.out.println("t1 i="+i);
				}
				
			});
			Thread t2 = new Thread(()->{
				for(int i=0;i < 10000;i++){
					System.out.println("t2 i="+i);
				}
				
			});
			t1.start();
			t1.join();
			t2.start();
			
	}

}
