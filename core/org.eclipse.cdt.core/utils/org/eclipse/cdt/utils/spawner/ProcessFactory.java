package org.eclipse.cdt.utils.spawner;

/*
 * (c) Copyright QNX Software Systems Ltd. 2002.
 * All Rights Reserved.
 */

import java.io.File;
import java.io.IOException;

public class ProcessFactory {

	static private ProcessFactory instance;
	private boolean hasSpawner;
	private Runtime runtime;

	private ProcessFactory() {
		hasSpawner = false;
		String OS = System.getProperty("os.name").toLowerCase();
		runtime = Runtime.getRuntime();
		try {
			// Spawner does not work for Windows 98 fallback
			if (OS != null && OS.equals("windows 98")) {
				hasSpawner = false;
			} else {
				System.loadLibrary("spawner"); //$NON-NLS-1$
				hasSpawner = true;
			}
		} catch (SecurityException e) {
			//e.printStackTrace();
		} catch (UnsatisfiedLinkError e) {
			//e.printStackTrace();
		}
	}

	public static ProcessFactory getFactory() {
		if (instance == null)
			instance = new ProcessFactory();
		return instance;
	}

	public Process exec(String cmd) throws IOException {
		if (hasSpawner)
			return new Spawner(cmd);
		return runtime.exec(cmd);
	}

	public Process exec(String[] cmdarray) throws IOException {
		if (hasSpawner)
			return new Spawner(cmdarray);
		return runtime.exec(cmdarray);
	}

	public Process exec(String[] cmdarray, String[] envp) throws IOException {
		if (hasSpawner)
			return new Spawner(cmdarray, envp);
		return runtime.exec(cmdarray, envp);
	}

	public Process exec(String cmd, String[] envp) throws IOException {
		if (hasSpawner)
			return new Spawner(cmd, envp);
		return runtime.exec(cmd, envp);
	}

	public Process exec(String cmd, String[] envp, File dir)
		throws IOException {
		if (hasSpawner)
			return new Spawner(cmd, envp, dir);
		return runtime.exec(cmd, envp, dir);
	}

	public Process exec(String cmdarray[], String[] envp, File dir)
		throws IOException {
		if (hasSpawner)
			return new Spawner(cmdarray, envp, dir);
		return runtime.exec(cmdarray, envp, dir);
	}
}
