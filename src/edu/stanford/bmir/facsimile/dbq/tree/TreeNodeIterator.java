package edu.stanford.bmir.facsimile.dbq.tree;

import java.util.Iterator;

/**
 * @author Rafael S. Goncalves <br>
 * Stanford Center for Biomedical Informatics Research (BMIR) <br>
 * School of Medicine, Stanford University <br>
 */
public class TreeNodeIterator<T> implements Iterator<TreeNode<T>> {
	private TreeNode<T> treeNode;
	private ProcessStages doNext;
	private TreeNode<T> next;
	private Iterator<TreeNode<T>> childrenCurNodeIter;
	private Iterator<TreeNode<T>> childrenSubNodeIter;
	enum ProcessStages { ProcessParent, ProcessChildCurNode, ProcessChildSubNode }

	
	/**
	 * Constructor
	 * @param treeNode	Tree node
	 */
	public TreeNodeIterator(TreeNode<T> treeNode) {
		this.treeNode = treeNode;
		this.doNext = ProcessStages.ProcessParent;
		this.childrenCurNodeIter = treeNode.children.iterator();
	}


	@Override
	public boolean hasNext() {
		if (this.doNext == ProcessStages.ProcessParent) {
			this.next = this.treeNode;
			this.doNext = ProcessStages.ProcessChildCurNode;
			return true;
		}

		if (this.doNext == ProcessStages.ProcessChildCurNode) {
			if (childrenCurNodeIter.hasNext()) {
				TreeNode<T> childDirect = childrenCurNodeIter.next();
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
	public TreeNode<T> next() {
		return this.next;
	}

	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}