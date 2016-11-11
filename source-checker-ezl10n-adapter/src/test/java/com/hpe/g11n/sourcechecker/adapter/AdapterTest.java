package com.hpe.g11n.sourcechecker.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hpe.g11n.sourcechecker.adapter.impl.Adapter;

public class AdapterTest {
	Adapter adapter =new Adapter();
	@Test
	public void testExecute(){
		
		String projectName = "LR";
		String projectVersion ="V1.0";
		String state ="All";
		String sourcePath ="C:\\tmp\\psl-generate-sorucechecker-report\\ALI.lpu";
		String targetPath ="C:\\tmp";
		String rules ="0,1,2";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("-p", projectName);
		paramMap.put("-v", projectVersion);
		paramMap.put("-s", state);
		paramMap.put("-i", sourcePath);
		paramMap.put("-o", targetPath);
		paramMap.put("-r", rules);
		adapter.execute(paramMap);
		
	}
	
	@Test
	public void testGetProjectName(){
		List<String> lstProjectName = adapter.getProjectName();
		for(String name:lstProjectName){
			System.out.println(name);
		}
	}
}
