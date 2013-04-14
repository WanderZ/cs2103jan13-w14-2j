package ezxpns.util;

/**
 * A pair of anything, useful for duality
 * @param <L>
 * @param <R>
 * @author A0099621X
 */
public class Pair<L, R> {
	private final L left;
	private final R right;
	
	/**
	 * @param left
	 * @param right
	 */
	public Pair(L left, R right) {
		this.left = left;
	    this.right = right;
	}
	
	/**
	 * Get the left object
	 * @return the left object
	 */
	public L getLeft() { return left; }
	/**
	 * Get the right object
	 * @return the right object
	 */
	public R getRight() { return right; }
	
	@Override
	public int hashCode() { return left.hashCode() ^ right.hashCode(); }
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (!(o instanceof Pair)) return false;
	    Pair pairo = (Pair) o;
	    return this.left.equals(pairo.getLeft()) &&
	           this.right.equals(pairo.getRight());
	}
}
