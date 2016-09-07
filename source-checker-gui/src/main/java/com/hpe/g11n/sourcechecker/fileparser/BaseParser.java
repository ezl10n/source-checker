package com.hpe.g11n.sourcechecker.fileparser;

import java.util.List;

import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月17日
 * @Time: 下午2:49:59
 *
 */
public abstract class BaseParser {
	
	protected Config config;
	
	public abstract boolean isHandle(String source);

	public abstract List<InputData> getInputData(String source);
	
	public void setConfig(Config config){
		this.config=config;
	};
}
