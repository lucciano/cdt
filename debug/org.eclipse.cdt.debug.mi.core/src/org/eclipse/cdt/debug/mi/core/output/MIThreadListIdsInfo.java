package org.eclipse.cdt.debug.mi.core.output;

/**
 */
public class MIThreadListIdsInfo extends MIInfo {

	int[] threadIds;

	public MIThreadListIdsInfo(MIOutput out) {
		super(out);
	}

	public int[] getThreadIds() {
		if (threadIds == null) {
			parse();
		}
		return threadIds;
	}

	void parse() {
		if (isDone()) {
			MIOutput out = getMIOutput();
			MIResultRecord rr = out.getMIResultRecord();
			if (rr != null) {
				MIResult[] results =  rr.getMIResults();
				for (int i = 0; i < results.length; i++) {
					String var = results[i].getVariable();
					if (var.equals("thread-ids")) {
						MIValue val = results[i].getMIValue();
						if (val instanceof MITuple) {
							parseThreadIds((MITuple)val);
						}
					}
				}
			}
		}
		if (threadIds == null) {
			threadIds = new int[0];
		}
	}

	void parseThreadIds(MITuple tuple) {
		MIResult[] results = tuple.getMIResults();
		threadIds = new int[results.length];
		for (int i = 0; i < results.length; i++) {
			String var = results[i].getVariable();
			if (var.equals("thread-id")) {
				MIValue value = results[i].getMIValue();
				if (value instanceof MIConst) {
					String str = ((MIConst)value).getString();
					try {
						threadIds[i] = Integer.parseInt(str.trim());
					} catch (NumberFormatException e) {
					}
				}
			}
		}
	}
}
