package com.hpe.g11n.sourcechecker.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hpe.g11n.sourcechecker.adapter.impl.Adapter;

public class AdapterTest {
	Adapter adapter =new Adapter("C:\\configPath");
	@Test
	public void testExecute(){
		String product = "LR";
		String version ="V1.0";
		String scope ="All";
		String sourcePath ="C:\\tmp\\psl generate sorucechecker report\\ALI.lpu";
		String targetPath ="C:\\tmp";
		String rules ="0,1,2,3,4,5,6,7";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("-p", product);
		paramMap.put("-v", version);
		paramMap.put("-s", scope);
		paramMap.put("-i", sourcePath);
		paramMap.put("-o", targetPath);
		paramMap.put("-r", rules);
		adapter.execute(paramMap);
		
	}
	
	@Test
	public void testGetProoduct(){
		List<String> lstProduct = adapter.getProduct();
		for(String name:lstProduct){
			System.out.println(name);
		}
	}
}
