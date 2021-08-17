package mrX_maven_utilities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNodeMrX<Station> implements Iterable<TreeNodeMrX<Station>> {

	public Station station;
	public TreeNodeMrX<Station> parent;
	public List<TreeNodeMrX<Station>> children;

	public Station getStation() {
		return station;
	}
	
	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	private List<TreeNodeMrX<Station>> elementsIndex;

	public TreeNodeMrX(Station station) {
		this.station = station;
		this.children = new LinkedList<TreeNodeMrX<Station>>();
		this.elementsIndex = new LinkedList<TreeNodeMrX<Station>>();
		this.elementsIndex.add(this);
	}

	public TreeNodeMrX<Station> addChild(Station child) {
		TreeNodeMrX<Station> childNode = new TreeNodeMrX<Station>(child);
		childNode.parent = this;
		this.children.add(childNode);
		this.registerChildForSearch(childNode);
		return childNode;
	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	private void registerChildForSearch(TreeNodeMrX<Station> node) {
		elementsIndex.add(node);
		if (parent != null)
			parent.registerChildForSearch(node);
	}

	public TreeNodeMrX<Station> findTreeNode(Comparable<Station> cmp) {
		for (TreeNodeMrX<Station> element : this.elementsIndex) {
			Station station = element.station;
			if (cmp.compareTo(station) == 0)
				return element;
		}

		return null;
	}
	
	public List<TreeNodeMrX<Station>> getChildren() {
        return children;
    }
	
	@Override
	public String toString() {
		return station != null ? station.toString() : "[data null]";
	}

	@Override
	public Iterator<TreeNodeMrX<Station>> iterator() {
		TreeNodeIter<Station> iter = new TreeNodeIter<Station>(this);
		return iter;
	}

}