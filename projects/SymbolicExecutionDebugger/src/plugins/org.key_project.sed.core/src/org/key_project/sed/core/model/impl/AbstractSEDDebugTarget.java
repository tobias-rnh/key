package org.key_project.sed.core.model.impl;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IElementContentProvider;
import org.key_project.sed.core.model.ISEDDebugTarget;
import org.key_project.sed.core.provider.SEDDebugTargetContentProvider;

/**
 * Provides a basic implementation of {@link ISEDDebugTarget}.
 * @author Martin Hentschel
 * @see ISEDDebugTarget
 */
@SuppressWarnings("restriction")
public abstract class AbstractSEDDebugTarget extends DebugElement implements ISEDDebugTarget {
   /**
    * The {@link ILaunch} in that this {@link IDebugTarget} is used.
    */
   private ILaunch launch;
   
   /**
    * Indicates that the connection to the process is disconnected or not.
    */
   private boolean disconnected = false;
   
   /**
    * Indicates that the process is currently suspended or not.
    */
   private boolean suspended = false;

   /**
    * Indicates that the process is termianted or not.
    */
   private boolean terminated = false;
   
   /**
    * The defined model identifier.
    */
   private String modelIdentifier;

   /**
    * The name of this debug target.
    */
   private String name;
   
   /**
    * Constructor.
    * @param launch The {@link ILaunch} in that this {@link IDebugTarget} is used.
    */
   public AbstractSEDDebugTarget(ILaunch launch) {
      super(null);
      this.launch = launch;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public ISEDDebugTarget getDebugTarget() {
      return this;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getModelIdentifier() {
      return modelIdentifier;
   }
   
   /**
    * Sets the unique model identifier.
    * @param modelIdentifier The unique model identifier to set.
    */
   protected void setModelIdentifier(String modelIdentifier) {
      this.modelIdentifier = modelIdentifier;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ILaunch getLaunch() {
      return launch;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public IThread[] getThreads() throws DebugException {
      return new IThread[0];
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean hasThreads() throws DebugException {
      IThread[] threads = getThreads();
      return threads != null && threads.length >= 1;
   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Object getAdapter(Class adapter) {
      if (IElementContentProvider.class.equals(adapter)) {
         // Change used content provider to show SED specific children instead of the default one from the debug API.
         return SEDDebugTargetContentProvider.getDefaultInstance();
      }
      else {
         return Platform.getAdapterManager().getAdapter(this, adapter);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean supportsBreakpoint(IBreakpoint breakpoint) {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IProcess getProcess() {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean canTerminate() {
      return !isTerminated();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isTerminated() {
      return terminated;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void terminate() throws DebugException {
      terminated = true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean canResume() {
      return isSuspended();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean canSuspend() {
      return !isSuspended();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isSuspended() {
      return suspended;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void resume() throws DebugException {
      suspended = false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void suspend() throws DebugException {
      suspended = true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void breakpointAdded(IBreakpoint breakpoint) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean canDisconnect() {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void disconnect() throws DebugException {
      this.disconnected = true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isDisconnected() {
      return disconnected;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean supportsStorageRetrieval() {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName() throws DebugException {
      return name;
   }

   /**
    * Sets the name of this debug target.
    * @param name The name to set.
    */
   protected void setName(String name) {
      this.name = name;
   }
}