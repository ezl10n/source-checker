package com.hpe.g11n.sourcechecker.fileparser;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.fileparser.BaseParser;
import com.hpe.g11n.sourcechecker.fileparser.IFileParser;
import com.hpe.g11n.sourcechecker.pojo.InputData;
import com.typesafe.config.Config;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年8月17日
 * @Time: 下午2:53:39
 *
 */
public class FileParserManager implements IFileParser {
	@Inject
	@Named("sourceCheckerParsers")
	List<BaseParser> parsers;

	@Inject
	@Named("sourceCheckerConfig")
	Config config;

	@Override
	public List<InputData> parser(String filePath,String scope) {
		for (BaseParser parser : parsers) {
			parser.setConfig(config);
			if (parser.isHandle(filePath)) {
				return parser.getInputData(filePath,scope);
			}
		}
		return null;
	}
}
