
public class IntervalTree {
	// I will be writing the code in java and will be making an Interval class
	// to define the
	// intervals
	// comparable interface implemented so as to sort the intervals and
	// compare them on the basis of their low value
	// for simplicity I have assumed the data to be long as well

	class Interval implements Comparable<Interval> {

		private long left;
		private long right;
		private long val;

		public Interval(long left, long right, long val) {
			this.left = left;
			this.right = right;
			this.val = val;
		}
		// generating the getters and the setters

		public long getLeft() {
			return left;
		}

		public void setLeft(long left) {
			this.left = left;
		}

		public long getRight() {
			return right;
		}

		public void setRight(long right) {
			this.right = right;
		}

		public long getVal() {
			return val;
		}

		public void setVal(long val) {
			this.val = val;
		}

		@Override
		public int compareTo(Interval iv) {
			// TODO Auto-generated method stub
			if (left < iv.getLeft())
				return -1;
			else if (left > iv.getLeft())
				return 1;
			else if (right < iv.getRight())
				return -1;
			else if (right > iv.getRight())
				return 1;
			else
				return 0;
		}
	}

	// Implementing the interval node and I will implement RedBlack Tree for the
	// insertion
	// and deletion
	class RedBlackINode {
		Interval i;
		int max;
		int color;
		RedBlackINode left, right;
		long element;

		public RedBlackINode(Interval i) {
			this.element = i.getLeft();

		}

		public RedBlackINode(Interval i, RedBlackINode lt, RedBlackINode rt) {
			left = lt;
			right = rt;
			element = i.getLeft();
			color = 1;
		}

	}
	// constructor of the intervalTree

	private RedBlackINode current;
	private RedBlackINode parent;
	private RedBlackINode grand;
	private RedBlackINode great;
	private RedBlackINode header;
	private RedBlackINode nullNode;

	public IntervalTree() {

	}

	IntervalTree it = new IntervalTree();
	Interval i = it.new Interval(0, 0, 0);

	public IntervalTree(int neg) {
		// TODO Auto-generated constructor stub
		header = it.new RedBlackINode(it.new Interval(neg, neg, neg));
		header.left = null;
		header.right = null;
	}

	static final int BLACK = 1;
	static final int RED = 0;

	public boolean isEmpty() {
		return header.right == nullNode;
	}

	/* Make the tree logically empty */
	public void makeEmpty() {
		header.right = nullNode;
	}

	public void insert(Interval i) {
		current = parent = grand = header;
		nullNode.element = i.left;
		while (current.element != i.left) {
			great = grand;
			grand = parent;
			parent = current;
			current = i.left < current.element ? current.left : current.right;
			// Check if two red children and fix if so
			if (current.left.color == RED && current.right.color == RED)
				handleReorient(i.left);
		}
		// Insertion fails if already present
		if (current != nullNode)
			return;
		current = it.new RedBlackINode(i, nullNode, nullNode);
		// Attach to parent
		if (i.left < parent.element)
			parent.left = current;
		else
			parent.right = current;
		handleReorient(i.left);
	}

	private void handleReorient(long item) {
		// Do the color flip
		current.color = RED;
		current.left.color = BLACK;
		current.right.color = BLACK;

		if (parent.color == RED) {
			// Have to rotate
			grand.color = RED;
			if (item < grand.element != item < parent.element)
				parent = rotate(item, grand); // Start dbl rotate
			current = rotate(item, great);
			current.color = BLACK;
		}
		// Make root black
		header.right.color = BLACK;
	}

	private RedBlackINode rotate(long item, RedBlackINode parent) {
		if (item < parent.element)
			return parent.left = item < parent.left.element ? rotateWithLeftChild(parent.left)
					: rotateWithRightChild(parent.left);
		else
			return parent.right = item < parent.right.element ? rotateWithLeftChild(parent.right)
					: rotateWithRightChild(parent.right);
	}

	/* Rotate binary tree node with left child */
	private RedBlackINode rotateWithLeftChild(RedBlackINode k2) {
		IntervalTree.RedBlackINode k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;
	}

	/* Rotate binary tree node with right child */
	private RedBlackINode rotateWithRightChild(RedBlackINode k1) {
		IntervalTree.RedBlackINode k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		return k2;
	}

	// implementing the search function
	// reference geeksforgeeks and using the RedBlackIntervalNode
	public boolean doOVerlap(Interval i1, Interval i2) {
		if (i1.left <= i2.right && i2.left <= i1.right)
			return true;
		return false;
	}

	public Interval intervalSearch(RedBlackINode root, Interval i) {
		if (root == null)
			return null;

		// If given interval overlaps with root
		if (doOVerlap(root.i, i))
			return root.i;

		// If left child of root is present and max of left child is
		// greater than or equal to given interval, then i may
		// overlap with an interval is left subtree
		if (root.left != null && root.max >= i.left)
			return intervalSearch(root.left, i);

		// Else interval can only overlap with right subtree
		return intervalSearch(root.right, i);

	}

	// implementing the delete function
	
	/* there are 4 cases for deletion
	 * 1)If double black node becomes root then we are done. Turning it into
     * single black node just reduces one black in every path. 
     * 
	 * 2) If sibling is red and parent and sibling's children are black then rotate it
     * so that sibling becomes black. Double black node is still double black so we need
     * further processing.
	 * 
	 * 3) If sibling, sibling's children and parent are all black then turn sibling into red.
     * This reduces black node for both the paths from parent. Now parent is new double black
     * node which needs further processing by going back to case1.
     * 
     * 4)If sibling color is black, parent color is red and sibling's children color is black then swap color b/w sibling
     * and parent. This increases one black node on double black node path but does not affect black node count on
     * sibling path. We are done if we hit this situation.
	 *
	 */
	
	public static void main(String[] args) {
		IntervalTree it = new IntervalTree(Integer.MIN_VALUE);
		it.insert(it.new Interval(15, 20, 1));
		it.insert(it.new Interval(10, 30, 2));
		it.insert(it.new Interval(17, 19, 3));
		it.insert(it.new Interval(5, 20, 4));
		it.insert(it.new Interval(12, 15, 5));
		it.insert(it.new Interval(30, 40, 6));

		
	}

}
