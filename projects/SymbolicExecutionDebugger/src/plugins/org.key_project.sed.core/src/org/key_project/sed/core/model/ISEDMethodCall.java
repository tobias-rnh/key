package org.key_project.sed.core.model;

import org.eclipse.debug.core.model.IStackFrame;
import org.key_project.sed.core.model.impl.AbstractSEDMethodCall;
import org.key_project.sed.core.model.memory.SEDMemoryMethodCall;

/**
 * A node in the symbolic execution tree which represents a method call,
 * e.g. {@code foo()}.
 * <p>
 * A symbolic method call is also a normal stack frame ({@link IStackFrame})
 * for compatibility reasons with the Eclipse debug API.
 * </p>
 * <p>
 * Clients may implement this interface. It is recommended to subclass
 * from {@link AbstractSEDMethodCall} instead of implementing this
 * interface directly. {@link SEDMemoryMethodCall} is also a default
 * implementation that stores all values in the memory.
 * </p>
 * @author Martin Hentschel
 * @see ISEDDebugNode
 */
public interface ISEDMethodCall extends ISEDDebugNode, IStackFrame {

}