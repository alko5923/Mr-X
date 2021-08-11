package mrX_maven_utilities;

import java.util.ArrayList;
import java.util.List;
import com.rits.cloning.Cloner;

import mrX_maven_game.Move;

public class TreeNode<GameState> {
	private GameState gameState = null;
	private TreeNode<GameState> parent = null;
	private List<TreeNode<GameState>> children = new ArrayList<TreeNode<GameState>>();
    private double nodeEvaluation;
    private final Cloner cloner;
    private List<Move> bestCombo = new ArrayList<Move>();

    public TreeNode(GameState gameState, Cloner cloner) {
        this.gameState = gameState;
        this.cloner = cloner;
    }

    public void addChild(TreeNode<GameState> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(GameState hunter) {
        TreeNode<GameState> newChild = new TreeNode<GameState>(hunter, cloner);
        this.addChild(newChild);
    }

    public void addChildren(List<TreeNode<GameState>> children) {
        for(TreeNode<GameState> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeNode<GameState>> getChildren() {
        return children;
    }

    public GameState getData() {
        return gameState;
    }

    public void setData(GameState gameState) {
        this.gameState = gameState;
    }

    private void setParent(TreeNode<GameState> parent) {
        this.parent = parent;
    }

    public TreeNode<GameState> getParent() {
        return parent;
    }
    
    public double getNodeEvaluation() {
		return nodeEvaluation;
	}

	public void setNodeEvaluation(double nodeEvaluation) {
		this.nodeEvaluation = nodeEvaluation;
	}
	
	public GameState getDeepCloneOfRepresentedState() {
        return cloner.deepClone(gameState);
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