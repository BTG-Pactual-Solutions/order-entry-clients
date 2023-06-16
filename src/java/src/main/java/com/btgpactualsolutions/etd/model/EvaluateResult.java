package com.btgpactualsolutions.etd.model;

public class EvaluateResult {
	private final String issue;

	/**
	 * 
	 * @param issue
	 */
	public EvaluateResult(String issue) {
		this.issue = issue;
	}

	public boolean hasIssue() {
		return issue != null && issue.length() > 0;
	}

	public String getIssue() {
		return this.issue;
	}
}
