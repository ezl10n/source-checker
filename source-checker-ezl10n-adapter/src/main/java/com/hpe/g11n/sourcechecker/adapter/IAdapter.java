package com.hpe.g11n.sourcechecker.adapter;

import java.util.List;
import java.util.Map;

public interface IAdapter {
	public void execute(Map<String,String> paramMap);
	
	public List<String> getProjectName();
}
