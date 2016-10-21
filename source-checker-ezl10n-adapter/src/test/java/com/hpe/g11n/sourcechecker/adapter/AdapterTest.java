package com.hpe.g11n.sourcechecker.adapter;

import org.junit.Test;

import com.hpe.g11n.sourcechecker.adapter.impl.Adapter;

public class AdapterTest {
	@Test
	public void testExecute(){
		Adapter adapter =new Adapter();
		String projectName = "LR";
		String projectVersion ="V1.0";
		String state ="All";
		String sourcePath ="C:\\tmp\\psl-generate-sorucechecker-report\\ALI.lpu";
		String targetPath ="C:\\tmp";
		String rules ="0,1,2";
		adapter.execute(projectName, projectVersion, state, sourcePath, targetPath, rules);
	}
}
