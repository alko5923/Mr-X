package mrX_maven_run;

import java.util.ArrayList;
import java.util.List;
import com.rits.cloning.Cloner;

public class TreeNode<Hunter> {
	private Hunter hunter = null;
	private TreeNode<Hunter> parent = null;
	private List<TreeNode<Hunter>> children = new ArrayList<TreeNode<Hunter>>();
    private double nodeEvaluation;
    private final Cloner cloner;
    private List<Move> bestCombo = new ArrayList<Move>();

    public TreeNode(Hunter hunter, Cloner cloner) {
        this.hunter = hunter;
        this.cloner = cloner;
    }

    public void addChild(TreeNode<Hunter> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Hunter hunter) {
        TreeNode<Hunter> newChild = new TreeNode<Hunter>(hunter, cloner);
        this.addChild(newChild);
    }

    public void addChildren(List<TreeNode<Hunter>> children) {
        for(TreeNode<Hunter> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeNode<Hunter>> getChildren() {
        return children;
    }

    public Hunter getData() {
        return hunter;
    }

    public void setData(Hunter hunter) {
        this.hunter = hunter;
    }

    private void setParent(TreeNode<Hunter> parent) {
        this.parent = parent;
    }

    public TreeNode<Hunter> getParent() {
        return parent;
    }
    
    public double getNodeEvaluation() {
		return nodeEvaluation;
	}

	public void setNodeEvaluation(double nodeEvaluation) {
		this.nodeEvaluation = nodeEvaluation;
	}
	
	public Hunter getDeepCloneOfRepresentedState() {
        return cloner.deepClone(hunter);
    }
	
	public List<Move> getBestCombo() {
		return bestCombo;
	}
	
	public void setBestCombo(List<Move> bestCombo) {
		this.bestCombo = bestCombo;
	}
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("*** NODE INFO *** \n");
    	sb.append("Node evaluation = " + nodeEvaluation + "\n");
    	//sb.append(hunter.toString());
    	sb.append("NUMBER OF CHILDREN = " + this.getChildren().size() + "\n");
    	sb.append("CHILD NODES = \n");
    	for (int i = 0; i < this.getChildren().size(); i ++) {
    		sb.append(this.getChildren().get(i));
    	}
    	//sb.append("BEST COMBO = " + this.getBestCombo() + "\n");
    	return sb.toString();
    }

}