/**********************************************************************
 * Copyright (c) 2002,2003 QNX Software Systems and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * QNX Software Systems - Initial API and implementation
***********************************************************************/
package org.eclipse.cdt.make.internal.core;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.ICDescriptor;
import org.eclipse.cdt.core.ICOwner;
import org.eclipse.cdt.make.core.MakeScannerProvider;
import org.eclipse.core.runtime.CoreException;

public class MakeProject implements ICOwner {

	public void configure(ICDescriptor cproject) throws CoreException {
		cproject.create(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID, MakeScannerProvider.INTERFACE_IDENTITY);
	}

	public void update(ICDescriptor cproject, String extensionID) throws CoreException {
		if ( extensionID.equals(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID)) {
			cproject.create(CCorePlugin.BUILD_SCANNER_INFO_UNIQ_ID, MakeScannerProvider.INTERFACE_IDENTITY);
		}
		if ( extensionID.equals(CCorePlugin.BINARY_PARSER_UNIQ_ID)) {
			cproject.create(CCorePlugin.BINARY_PARSER_UNIQ_ID, CCorePlugin.PLUGIN_ID + ".Elf"); //$NON-NLS-1$
		}
		
	}
}
