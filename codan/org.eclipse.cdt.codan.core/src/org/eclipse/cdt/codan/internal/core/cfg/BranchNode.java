/*******************************************************************************
 * Copyright (c) 2009 Alena Laskavaia 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alena Laskavaia  - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.codan.internal.core.cfg;

import org.eclipse.cdt.codan.core.model.cfg.IBranchNode;

/**
 * TODO: add description
 */
public class BranchNode extends PlainNode implements IBranchNode {
	protected String label;

	public BranchNode(String label) {
		super();
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.cdt.codan.core.model.cfg.IBranchNode#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toStringData() {
		return label;
	}
}
