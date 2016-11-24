package com.hpe.g11n.sourcechecker.cli.command;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;

/**
 * Created with IntelliJ IDEA. 
 * User: Foy Lian 
 * Date: 2016-08-23 
 * Time: 15:25
 */
public class CommandOptions {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Parameter(names = { "-h", "-help", "--help" }, help = true, description = "Lists available commands.")
	private boolean help;
	@Parameter(names = { "-p", "--product" }, description = "Product.")
	private String product;
	@Parameter(names = { "-v", "--version" }, description = "Version.")
	private String version;
	@Parameter(names = { "-s", "--scope" }, description = "Scope")
	private String scope;
	@Parameter(names = { "-i", "--input" }, description = "File to be processed.")
	private String sourceUrl;
	@Parameter(names = { "-o", "--output" }, description = "Write Output to the folder.")
	private String outputUrl;
	@Parameter(names = { "-r", "--rules" }, description = "Rules select to run.")
	private List<Integer> selectRules;

	@Inject
	@Named("ruleNames")
	private List<String> ruleNames;

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getOutputUrl() {
		return outputUrl;
	}

	public void setOutputUrl(String outputUrl) {
		this.outputUrl = outputUrl;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<Integer> getSelectRules() {
		return selectRules;
	}

	public void setSelectRules(List<Integer> selectRules) {
		this.selectRules = selectRules;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}

	public String rulesUseage() {
		StringBuffer sb = new StringBuffer(100);
		sb.append("\tavailable rules:\n");
		for (int i = 0; i < ruleNames.size(); i++) {
			sb.append("\t" + i + ":" + ruleNames.get(i) + "\n");
		}
		sb.append(String.format("\tfor example: -r 0,1 means using rules %s and %s to check and report.",
						ruleNames.get(0), ruleNames.get(1)));
		return sb.toString();
	}

	public boolean validate() {
		boolean flag = true;
		try {
			if (Strings.isNullOrEmpty(product)) {
				logger.debug("-p or --input is need to set product!");
				flag = false;
			}
			if(StringUtil.pattern(product, RulePatternConstant.PRODUCT_FORMAT)){
				logger.debug("product can not contains \"[\",\"]\",\"/\",\"\\\",\":\",\"*\",\"?\",\"<\",\">\" and \"|\"");
				flag = false; 
		     }
			if (Strings.isNullOrEmpty(version)) {
				logger.debug("-v or --input is need to set version!");
				flag = false;
			}
			if (Strings.isNullOrEmpty(scope)) {
				logger.debug("-s or --input is need to set scope!");
				flag = false;
			}
			File file;
			if (Strings.isNullOrEmpty(sourceUrl)) {
				logger.debug("-i or --input is need to set source url!");
				flag = false;
			}
			String[] paths = sourceUrl.split(";");
			for(String path:paths){
				file = new File(path);
				if(!file.exists()){
					logger.debug("The \"" + path + "\" is not esists!");
					flag = false;
					break;
				}
				if(!file.isFile()){
					logger.debug("The \"" + path + "\" is not a file!");
					flag = false;
					break;
				}
			}
			if (Strings.isNullOrEmpty(outputUrl)) {
				logger.debug("-o or --output is need to set output url!");
				flag = false;
			}
			file = new File(outputUrl);
			if(!file.isDirectory()){
				logger.debug("The \"" + outputUrl + "\" is not directory!");
				flag = false;
			}
			
			if (selectRules == null || selectRules.size() == 0) {
				logger.debug("-r or --rules is need to set rules index to be select !");
				flag = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
