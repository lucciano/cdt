/*
 *(c) Copyright QNX Software Systems Ltd. 2002.
 * All Rights Reserved.
 * 
 */

package org.eclipse.cdt.debug.internal.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.cdt.debug.core.cdi.CDIException;
import org.eclipse.cdt.debug.core.cdi.model.ICDIValue;
import org.eclipse.cdt.debug.core.cdi.model.ICDIVariable;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * 
 * The value of a variable.
 * 
 * @since Aug 9, 2002
 */
public class CValue extends CDebugElement implements IValue
{
	/**
	 * Underlying CDI value.
	 */
	private ICDIValue fValue;

	/**
	 * List of child variables.
	 */
	private List fVariables = Collections.EMPTY_LIST;

	/**
	 * Constructor for CValue.
	 * @param target
	 */
	public CValue( CDebugTarget target, ICDIValue cdiValue )
	{
		super( target );
		fValue = cdiValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException
	{
		String result = null;
		if ( getUnderlyingValue() != null )
		{
			try
			{
				result = getUnderlyingValue().getValueString();
			}
			catch( CDIException e )
			{
				// change this
				requestFailed( "", e );
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public IVariable[] getVariables() throws DebugException
	{
		List list = getVariables0();
		return (IVariable[])list.toArray( new IVariable[list.size()] );
	}

	protected synchronized List getVariables0() throws DebugException 
	{
		if ( !isAllocated() )
			return Collections.EMPTY_LIST;
		if ( fVariables.size() == 0 )
		{
			List vars = getCDIVariables();
			fVariables = new ArrayList( vars.size() );
			Iterator it = vars.iterator();
			while( it.hasNext() )
			{
				fVariables.add( new CLocalVariable( this, (ICDIVariable)it.next() ) );
			}
		}
		return fVariables;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException
	{
		try
		{
			ICDIValue value = getUnderlyingValue();
			if ( value != null )
				return value.hasChildren();
		}
		catch( CDIException e )
		{
			targetRequestFailed( "Operation failed. Reason: ", e );
		}
		return false;
	}

	/**
	 * Creates the appropriate kind of value, or <code>null</code>.
	 * 
	 */
	public static CValue createValue( CDebugTarget target, ICDIValue value ) 
	{
		return new CValue( target, value );
	}

	/**
	 * Returns this value's underlying CDI value
	 */
	protected ICDIValue getUnderlyingValue()
	{
		return fValue;
	}
	
	protected List getCDIVariables() throws DebugException
	{
		try
		{
			ICDIValue value = getUnderlyingValue();
			if ( value != null )
			{
				return Arrays.asList( value.getVariables() );
			}
		}
		catch( CDIException e )
		{
			targetRequestFailed( "Operation failed. Reason: ", e );
		}
		return Collections.EMPTY_LIST;
	}
}
