package mrX_maven_utilities;

import java.util.ArrayList;
import java.util.List;
import com.rits.cloning.Cloner;
import mrX_maven_game.Move;

public class TreeNode<CoordinatePlayers> {
	private final Cloner cloner;
	private CoordinatePlayers coordinator = null;
	private TreeNode<CoordinatePlayers> parent = null;
	private List<TreeNode<CoordinatePlayers>> children = new ArrayList<TreeNode<CoordinatePlayers>>();
    private double nodeEvaluation;
    private List<Move> bestCombo = new ArrayList<Move>();

    public TreeNode(CoordinatePlayers coordinator, Cloner cloner) {
        this.coordinator = coordinator;
        this.cloner = cloner;
    }

    public void addChild(TreeNode<CoordinatePlayers> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(CoordinatePlayers coordinator) {
        TreeNode<CoordinatePlayers> newChild = new TreeNode<CoordinatePlayers>(coordinator, cloner);
        this.addChild(newChild);
    }

    public void addChildren(List<TreeNode<CoordinatePlayers>> children) {
        for(TreeNode<CoordinatePlayers> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeNode<CoordinatePlayers>> getChildren() {
        return children;
    }

    public CoordinatePlayers getData() {
        return coordinator;
    }

    public void setData(CoordinatePlayers coordinator) {
        this.coordinator = coordinator;
    }

    private void setParent(TreeNode<CoordinatePlayers> parent) {
        this.parent = parent;
    }

    public TreeNode<CoordinatePlayers> getParent() {
        return parent;
    }
    
    public double getNodeEvaluation() {
		return nodeEvaluation;
	}

	public void setNodeEvaluation(double nodeEvaluation) {
		this.nodeEvaluation = nodeEvaluation;
	}
	
	public CoordinatePlayers getDeepCloneOfRepresentedState() {
        return cloner.deepClone(coordinator);
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
    	sb.append("NUMBER OF CHILDREN = " + this.getChildren().size() + "\n");
    	sb.append("CHILD NODES = \n");
    	
    	for (int i = 0; i < this.getChildren().size(); i ++) {
    		sb.append(this.getChildren().get(i));
    	}
    	return sb.toString();
    }

}