/*****************************************************************
 * Copyright (c) 2010 Texas Instruments and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Chuong (Texas Instruments) - Pin and Clone Supports (331781)
 *****************************************************************/
package org.eclipse.cdt.debug.internal.ui.pinclone;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.cdt.debug.ui.CDebugUIPlugin;
import org.eclipse.cdt.debug.ui.IPinProvider;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * A utility class for pin and clone support.
 */
public class PinCloneUtils {
	public static String PIN_CLONE_VIEW_TAG = "PIN_CLONE_VIEW_"; //$NON-NLS-1$
	
	/**
	 * Encodes cloned part secondary id.
	 * 
	 * @param secondaryId the part's secondary id.
	 * @return an encoded part secondary id, can be <code>null</code>.
	 */
	public static String encodeClonedPartSecondaryId(String secondaryId) {
		return PIN_CLONE_VIEW_TAG + secondaryId;
	}
	
	/**
	 * Decodes cloned part secondary id.
	 * 
	 * @param secondaryId the part's secondary id.
	 * @return a decoded part secondary id.
	 */
	public static String decodeClonedPartSecondaryId(String secondaryId) {
		return secondaryId.replaceFirst(PIN_CLONE_VIEW_TAG, ""); //$NON-NLS-1$
	}
	
	/**
	 * Determine whether the view part is a cloned part.
	 * 
	 * @param part the view part
	 * @return true if it is a cloned part
	 */
	public static boolean isClonedPart(IViewPart part) {
		String secondaryId = part.getViewSite().getSecondaryId();
		return hasCloneTag(secondaryId);		
	}
	
	/**
	 * Determine whether the view reference is a cloned part.
	 *  
	 * @param ref the view reference
	 * @return true if it is a cloned part
	 */
	public static boolean isClonedPart(IViewReference ref) {
		String secondaryId = ref.getSecondaryId();
		return hasCloneTag(secondaryId);
	}
	
	/**
	 * Returns whether the id has the <code>PIN_CLONE_VIEW_TAG</code>.
	 * 
	 * @param id view id
	 * @return true if it has the tag, otherwise false
	 */
	private static boolean hasCloneTag(String id) {
		return id != null && id.startsWith(PIN_CLONE_VIEW_TAG);
	}	    
	    
	/**
	 * Set the part title to include the secondary id as part of the title.
	 * 
	 * @param part the view part
	 */
	public static void setPartTitle(IViewPart part) {
		try {
			if (!isClonedPart(part))
				return;
			
			String secondaryId = part.getViewSite().getSecondaryId();
			secondaryId = decodeClonedPartSecondaryId(secondaryId);			
			
			// use reflection to set the part name of the new view
			Method method = WorkbenchPart.class.getDeclaredMethod("setPartName", String.class); //$NON-NLS-1$
			if (method != null) {				
				if (part instanceof WorkbenchPart) {
					String name = ((WorkbenchPart) part).getPartName();
				
					String tag = " <" + secondaryId + ">";   //$NON-NLS-1$//$NON-NLS-2$
					if (!name.contains(tag)) {
						name = name + tag;
						
						method.setAccessible(true);
						method.invoke(part, name);	
					}
				}
			}
		} catch (Exception e) {
			CDebugUIPlugin.log(e);
		}
	}
	
	/**
	 * Set the part content description.
	 * 
	 * @param part the part
	 * @param description the new description
	 */
	public static void setPartContentDescription(IViewPart part, String description) {
		try {
			Method method = WorkbenchPart.class.getDeclaredMethod("setContentDescription", String.class); //$NON-NLS-1$
			if (method != null) {
				method.setAccessible(true);
				method.invoke(part, description);
			}			
		} catch (Exception e) {
			CDebugUIPlugin.log(e);
		}
	}
	
	/**
	 * Returns whether the debug context selection is pinnable.
	 * 
	 * @param part the workbench part were the pin action is triggered
	 * @param selection the debug context selection
	 * @return true if all elements are pinnable, otherwise false
	 */
	public static boolean isPinnable(IWorkbenchPart part, ISelection selection) {
		boolean pinnable = false;
		
		if (selection instanceof IStructuredSelection) {
			List <?> list = ((IStructuredSelection) selection).toList();			
			for (Object element : list) {
				pinnable = false;
				
				/* IPinProvider */
				if (element instanceof IAdaptable) {					
					IPinProvider pinProvider = (IPinProvider) ((IAdaptable)element).getAdapter(IPinProvider.class);
					if (pinProvider != null) {
						if (pinProvider.isPinnable(part,  element))
							pinnable = true;
					}
				}	

// TODO: support for CDI				
//				/* support CDebugElement */
//				if (!pinnable && (element instanceof ICDebugElement)) {					
//					pinnable = true;
//				}
			
				if (!pinnable) break;
			}
		}
		
		return pinnable;
	}
}