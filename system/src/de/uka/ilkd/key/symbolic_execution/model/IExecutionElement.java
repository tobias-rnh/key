package de.uka.ilkd.key.symbolic_execution.model;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.proof.NodeInfo;
import de.uka.ilkd.key.proof.Proof;

public interface IExecutionElement {
   /**
    * Returns the {@link Services} used in {@link #getProof()}.
    * @return The {@link Services} used in {@link #getProof()}.
    */
   public Services getServices();
   
   /**
    * Returns the {@link Proof} from which the symbolic execution tree was extracted.
    * @return The {@link Proof} from which the symbolic execution tree was extracted.
    */
   public Proof getProof();
   
   /**
    * Returns the {@link Node} in KeY's proof tree which is represented by this execution tree node.
    * @return The {@link Node} in KeY's proof tree which is represented by this execution tree node.
    */
   public Node getProofNode();
   
   /**
    * Returns the {@link NodeInfo} of {@link #getProofNode()}.
    * @return The {@link NodeInfo} of {@link #getProofNode()}.
    */
   public NodeInfo getProofNodeInfo();
   
   /**
    * Returns a human readable name which describes this element.
    * @return The human readable name which describes this element.
    */
   public String getName();
}