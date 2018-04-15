public class PriorityQueue {

	double[] pq;
	int n = 1;
	int size = 0;

	public boolean less(int i, int j) {
		if( pq[i] < pq[j]) {
			return true;
		}
		return false;
	}
	
	public void swap(int i, int j) {
		double temp = pq[i];
		pq[i] = pq[j];
		pq[j] = temp;
	}
	
	public PriorityQueue(int size) {
		pq = new double[size + 1];
		this.size = size;
	}
	
	public void swim(int k) {
		while(k > 1 && less(k/2, k) ) {
			swap(k/2, k);
			k = k/2;
		}
	}
	
	public void sink(int k) {
		while((2*k) <= n) {
			int j = 2*k;
			 if (j < n && less(j, j+1)) j++;
			 if (!less(k, j)) break;
			 swap(k, j);
			 k = j;
		}
	}
	
	public void insert(double key) {
		pq[n] = key;
		swim(n);
		if(n < (size)) {
			n++;
		}
	}
	
	public double delmax() {
		double max = 0;
		max = pq[1];
		swap(1,n--);
		pq[n+1] = 0;
		sink(1);
		return max;
	}
}