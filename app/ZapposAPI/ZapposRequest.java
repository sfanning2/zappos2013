package ZapposAPI;

import java.util.concurrent.Callable;

abstract class ZapposRequest<T> implements Callable<T>, Comparable<ZapposRequest<T>> {

	private final int priority;

	public ZapposRequest() {
		this.priority = Integer.MAX_VALUE;
	}
	
	public ZapposRequest(int priority) {
		this.priority = priority;
	}

	@Override
	public abstract T call() throws Exception;

	@Override
	public int compareTo(ZapposRequest<T> o) {
		return priority - o.getPriority();
	}
	
	public int getPriority() {
		return priority;
	}

}
