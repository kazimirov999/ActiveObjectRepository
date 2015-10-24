import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ActiveObject {
	private ExecutorService ex = Executors.newSingleThreadExecutor();
	private Random rand = new Random(47);
	private void pause(int factor){
		try {
			TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(factor));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public Future<Integer> calculateInt(final int x,final int y){
		return ex.submit(new Callable<Integer>(){
			@Override
			public Integer call() throws Exception {
				System.out.println("starting"+x+"+"+y);
				pause(500);
				return x+y;
			}}
		);
	}
	
	public Future<Float> calculateFloat(final float x, final float y){
		return ex.submit(new Callable<Float>(){
			@Override
			public Float call() throws Exception {
				System.out.println("starting"+x+"+"+y);
				pause(500);
				return x+y;
			}}
		);
	}
	@SuppressWarnings("unchecked")
	public void visible(){
		 ex.submit(new Callable(){
			@Override
			public Object call() throws Exception {
				pause(2000);
				System.out.println((char)('a'+rand.nextInt(40)));
				
				return null;
			}}
		);
	}
	public void shutdown(){ ex.shutdown();}
	
	public static void main(String...args){
		ActiveObject active = new ActiveObject();
		for(int i = 0; i < 20; i++)
		active.visible();
		List<Future<?>> results = new CopyOnWriteArrayList<>();
		for(float f = 0.0f; f < 1.0f; f += 0.2){
			System.out.println("ff");
			results.add(active.calculateFloat(f, f));
		}
		for(int i = 0; i < 10; i += 2){
			results.add(active.calculateInt(i, i));
		}
		System.out.println("All asynch calls made");
		active.pause(5000);
		System.out.println("zzzz");
		while(results.size() > 0){
			for(Future<?> future : results)
				if(future.isDone()){
					try {
						System.out.println(future.get());
					} catch (InterruptedException e){ 
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					results.remove(future);
				}
		}
		active.shutdown();
		System.out.println("Test Git");
		System.out.println("Test branch masterbranch")
		System.out.println("Test branch masterbranch twocomiit")
		
	}
}

