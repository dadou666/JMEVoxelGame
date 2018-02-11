package dadou.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestExecutor {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newWorkStealingPool();
		executor.execute(()->{
			System.out.println("hello");
		});
		executor.execute(()->{
			System.out.println("hello2");
		});
		executor.awaitTermination(-1, TimeUnit.HOURS);

	}

}
