package mrX_maven_utilities;

import java.util.Iterator;

public class TreeNodeIter<T> implements Iterator<TreeNodeMrX<T>> {

	enum ProcessStages {
		ProcessParent, ProcessChildCurNode, ProcessChildSubNode
	}

	private TreeNodeMrX<T> treeNode;

	public TreeNodeIter(TreeNodeMrX<T> treeNode) {
		this.treeNode = treeNode;
		this.doNext = ProcessStages.ProcessParent;
		this.childrenCurNodeIter = treeNode.children.iterator();
	}

	private ProcessStages doNext;
	private TreeNodeMrX<T> next;
	private Iterator<TreeNodeMrX<T>> childrenCurNodeIter;
	private Iterator<TreeNodeMrX<T>> childrenSubNodeIter;

	@Override
	public boolean hasNext() {

		if (this.doNext == ProcessStages.ProcessParent) {
			this.next = this.treeNode;
			this.doNext = ProcessStages.ProcessChildCurNode;
			return true;
		}

		if (this.doNext == ProcessStages.ProcessChildCurNode) {
			if (childrenCurNodeIter.hasNext()) {
				TreeNodeMrX<T> childDirect = childrenCurNodeIter.next();
				childrenSubNodeIter = childDirect.iterator();
				this.doNext = ProcessStages.ProcessChildSubNode;
				return hasNext();
			}

			else {
				this.doNext = null;
				return false;
			}
		}
		
		if (this.doNext == ProcessStages.ProcessChildSubNode) {
			if (childrenSubNodeIter.hasNext()) {
				this.next = childrenSubNodeIter.next();
				return true;
			}
			else {
				this.next = null;
				this.doNext = ProcessStages.ProcessChildCurNode;
				return hasNext();
			}
		}

		return false;
	}

	@Override
	public TreeNodeMrX<T> next() {
		return this.next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}