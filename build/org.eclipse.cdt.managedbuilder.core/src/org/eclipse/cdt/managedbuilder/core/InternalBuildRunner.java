/*******************************************************************************
 * Copyright (c) 2010, 2011 Wind River Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - Initial API and implementation
 * James Blackburn (Broadcom Corp.)
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.cdt.core.ErrorParserManager;
import org.eclipse.cdt.core.ICommandLauncher;
import org.eclipse.cdt.core.IMarkerGenerator;
import org.eclipse.cdt.core.resources.IConsole;
import org.eclipse.cdt.internal.core.BuildRunnerHelper;
import org.eclipse.cdt.managedbuilder.buildmodel.BuildDescriptionManager;
import org.eclipse.cdt.managedbuilder.buildmodel.IBuildDescription;
import org.eclipse.cdt.managedbuilder.internal.buildmodel.BuildStateManager;
import org.eclipse.cdt.managedbuilder.internal.buildmodel.DescriptionBuilder;
import org.eclipse.cdt.managedbuilder.internal.buildmodel.IConfigurationBuildState;
import org.eclipse.cdt.managedbuilder.internal.buildmodel.IProjectBuildState;
import org.eclipse.cdt.managedbuilder.internal.buildmodel.ParallelBuilder;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedMakeMessages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * The build runner for the internal builder.
 *
 * @author dschaefer
 * @since 8.0
 */
public class InternalBuildRunner extends AbstractBuildRunner {
	private static final int MONITOR_SCALE = 100;

	@Override
	public boolean invokeBuild(int kind, IProject project, IConfiguration configuration,
			IBuilder builder, IConsole console, IMarkerGenerator markerGenerator,
			IncrementalProjectBuilder projectBuilder, IProgressMonitor monitor) throws CoreException {

		BuildRunnerHelper buildRunnerHelper = new BuildRunnerHelper(project);

		try {
			if (monitor == null) {
				monitor = new NullProgressMonitor();
			}
			monitor.beginTask("", 3 * MONITOR_SCALE); //$NON-NLS-1$

			boolean isParallel = builder.getParallelizationNum() > 1;
			boolean resumeOnErr = !builder.isStopOnError();

			int flags = 0;
			IResourceDelta delta = projectBuilder.getDelta(project);
			BuildStateManager bsMngr = BuildStateManager.getInstance();
			IProjectBuildState pBS = bsMngr.getProjectBuildState(project);
			IConfigurationBuildState cBS = pBS.getConfigurationBuildState(configuration.getId(), true);

//			if(delta != null){
			flags = BuildDescriptionManager.REBUILD | BuildDescriptionManager.REMOVED | BuildDescriptionManager.DEPS;
//				delta = getDelta(currentProject);
//			}

			boolean buildIncrementaly = delta != null;

			// Prepare launch parameters for BuildRunnerHelper
			String cfgName = configuration.getName();
			String toolchainName = configuration.getToolChain().getName();
			boolean isConfigurationSupported = configuration.isSupported();

			URI workingDirectoryURI = ManagedBuildManager.getBuildLocationURI(configuration, builder);

			String[] errorParsers = builder.getErrorParsers();
			ErrorParserManager epm = new ErrorParserManager(project, workingDirectoryURI, markerGenerator, errorParsers);

			buildRunnerHelper.prepareStreams(epm, null, console, new SubProgressMonitor(monitor, 1 * MONITOR_SCALE));

			IBuildDescription des = BuildDescriptionManager.createBuildDescription(configuration, cBS, delta, flags);
			DescriptionBuilder dBuilder = null;
			if (!isParallel) {
				dBuilder = new DescriptionBuilder(des, buildIncrementaly, resumeOnErr, cBS);
				if (dBuilder.getNumCommands() <= 0) {
					buildRunnerHelper.printLine(ManagedMakeMessages.getFormattedString("ManagedMakeBuilder.message.no.build", project.getName())); //$NON-NLS-1$
					return false;
				}
			}

			buildRunnerHelper.removeOldMarkers(project, new SubProgressMonitor(monitor, 1 * MONITOR_SCALE, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK));

			if (buildIncrementaly) {
				buildRunnerHelper.greeting(IncrementalProjectBuilder.INCREMENTAL_BUILD, cfgName, toolchainName, isConfigurationSupported);
			} else {
				buildRunnerHelper.greeting(ManagedMakeMessages.getResourceString("ManagedMakeBuider.type.rebuild"), cfgName, toolchainName, isConfigurationSupported); //$NON-NLS-1$
			}
			buildRunnerHelper.printLine(ManagedMakeMessages.getResourceString("ManagedMakeBuilder.message.internal.builder.header.note")); //$NON-NLS-1$

			OutputStream stdout = buildRunnerHelper.getOutputStream();
			OutputStream stderr = buildRunnerHelper.getErrorStream();

			int status;
			if (dBuilder != null) {
				status = dBuilder.build(stdout, stderr, new SubProgressMonitor(monitor, 1 * MONITOR_SCALE, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK));
			} else {
				status = ParallelBuilder.build(des, null, null, stdout, stderr, new SubProgressMonitor(monitor, 1 * MONITOR_SCALE, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK), resumeOnErr, buildIncrementaly);
				buildRunnerHelper.printLine(ManagedMakeMessages.getFormattedString("CommonBuilder.7", Integer.toString(ParallelBuilder.lastThreadsUsed))); //$NON-NLS-1$
			}

			bsMngr.setProjectBuildState(project, pBS);

			buildRunnerHelper.close();
			buildRunnerHelper.goodbye();

			if (status != ICommandLauncher.ILLEGAL_COMMAND) {
				buildRunnerHelper.refreshProject(monitor);
			}

		} catch (Exception e) {
			projectBuilder.forgetLastBuiltState();

			String msg = ManagedMakeMessages.getFormattedString("ManagedMakeBuilder.message.error.build", //$NON-NLS-1$
					new String[] { project.getName(), configuration.getName() });
			throw new CoreException(new Status(IStatus.ERROR, ManagedBuilderCorePlugin.PLUGIN_ID, msg, e));
		} finally {
			try {
				buildRunnerHelper.close();
			} catch (IOException e) {
				ManagedBuilderCorePlugin.log(e);
			}
			monitor.done();
		}

		return false;
	}
}
