import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class thi {
	//array to be summed
	static double[] arr = new double[ 9000000];

	public static void main(String[] args) throws Exception {
		//fill the array with random (small) values
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Math.random();
		}
		//new forkjoinpool to handle the splitting and integration 
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		//have the pool start a new instance of "par" passing the array and store the return value to result
		//by invoking the recursive task, compute will automatically be called
		double result = forkJoinPool.invoke(new par(arr));
		//print the result
		System.out.println(result);
	}

	@SuppressWarnings("serial")
	static class par extends RecursiveTask<Double> {
		//array to be summed
		double[] arr;
		//bounds to sum from/to
		int start, end;
		
		//default constructor for easier calling from main
		par(double[] a) {
			arr = a;
			start = 0;
			end = arr.length;
		}
		//recursive constructor
		par(double[] a, int s, int e) {
			arr = a;
			start = s;
			end = e;
		}

		@Override
		//This method implements a recursive divide and conquer approach to split the array into manageable chunks
		protected Double compute() {
			// TODO Auto-generated method stub
			//If the sub array size is less than 50 go ahead and calculate it
			if (end - start <= 50) {
				double sum = 0;

				for (int i = start; i < end; ++i)
					sum += arr[i];
				return sum;
			}
			//otherwise split the array into 2 pieces and calculate their values
			else {
				int mid = start + (end - start) / 2;
				par left = new par(arr, start, mid);
				par right = new par(arr, mid, end);
				
				//let the left side go do its own thing
				left.fork();
				//compute the right
				double rightResult = right.compute();
				//join, add, and return the sides
				double leftResult = left.join();
				return leftResult + rightResult;
			}
		}

	}
}