package com.hpe.g11n.sourcechecker.adapter;

public interface IAdapter {
	public void execute(String projectName,String projectVersion,String state,String sourcePath, String targetPath, String rules);
}
