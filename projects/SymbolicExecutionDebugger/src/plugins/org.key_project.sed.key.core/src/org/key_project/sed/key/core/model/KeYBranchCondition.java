package org.key_project.sed.key.core.model;

import org.eclipse.core.runtime.Assert;
import org.eclipse.debug.core.DebugException;
import org.key_project.sed.core.model.ISEDBranchCondition;
import org.key_project.sed.core.model.ISEDThread;
import org.key_project.sed.core.model.impl.AbstractSEDBranchCondition;
import org.key_project.sed.key.core.util.KeYModelUtil;

import de.uka.ilkd.key.symbolic_execution.model.IExecutionBranchCondition;
import de.uka.ilkd.key.symbolic_execution.model.IExecutionNode;

/**
 * Implementation of {@link ISEDBranchCondition} for the symbolic execution debugger (SED)
 * based on KeY.
 * @author Martin Hentschel
 */
public class KeYBranchCondition extends AbstractSEDBranchCondition implements IKeYSEDDebugNode<IExecutionBranchCondition> {
   /**
    * The {@link IExecutionBranchCondition} to represent by this debug node.
    */
   private IExecutionBranchCondition executionNode;
   
   /**
    * The contained children.
    */
   private IKeYSEDDebugNode<?>[] children;

   /**
    * Constructor.
    * @param target The {@link KeYDebugTarget} in that this branch condition is contained.
    * @param parent The parent in that this node is contained as child.
    * @param thread The {@link ISEDThread} in that this node is contained.
    * @param executionNode The {@link IExecutionBranchCondition} to represent by this debug node.
    */
   public KeYBranchCondition(KeYDebugTarget target, 
                             IKeYSEDDebugNode<?> parent, 
                             ISEDThread thread, 
                             IExecutionBranchCondition executionNode) {
      super(target, parent, thread);
      Assert.isNotNull(executionNode);
      this.executionNode = executionNode;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public KeYDebugTarget getDebugTarget() {
      return (KeYDebugTarget)super.getDebugTarget();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public IKeYSEDDebugNode<?> getParent() throws DebugException {
      return (IKeYSEDDebugNode<?>)super.getParent();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IKeYSEDDebugNode<?>[] getChildren() throws DebugException {
      IExecutionNode[] executionChildren = executionNode.getChildren();
      if (children == null) {
         children = KeYModelUtil.createChildren(this, executionChildren);
      }
      else if (children.length != executionChildren.length) { // Assumption: Only new children are added, they are never replaced or removed
         children = KeYModelUtil.updateChildren(this, children, executionChildren);
      }
      return children;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IExecutionBranchCondition getExecutionNode() {
      return executionNode;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName() throws DebugException {
      return executionNode.getName();
   }
}