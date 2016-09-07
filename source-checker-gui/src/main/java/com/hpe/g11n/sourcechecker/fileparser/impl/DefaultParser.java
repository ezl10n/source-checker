package com.hpe.g11n.sourcechecker.fileparser.impl;

import java.util.List;

import com.hpe.g11n.sourcechecker.fileparser.BaseParser;
import com.hpe.g11n.sourcechecker.pojo.InputData;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月17日
 * @Time: 下午5:19:55
 *
 */
public class DefaultParser extends BaseParser {

	@Override
	public boolean isHandle(String source) {
		return true;
	}

	@Override
	public List<InputData> getInputData(String source) {
		throw new RuntimeException("Failue");
	}

}
