package com.hpe.g11n.sourcechecker.cli.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;

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
	@Parameter(names = { "-p", "--name" }, description = "Project name.")
	private String projectName;
	@Parameter(names = { "-v", "--version" }, description = "Project version.")
	private String projectVersion;
	@Parameter(names = { "-s", "--state" }, description = "State")
	private String state;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

		sb.append(String
				.format("\tfor example: -r 0,1 means using rules %s and %s to check and report.",
						ruleNames.get(0), ruleNames.get(1)));
		return sb.toString();
	}

	public boolean validate() {
		try {
			System.out
					.println("Please input the parameters (right format e.g. LR>V1.0>All>C:\\test.lpu>C:\\tmp>0,1,2,3):");
			boolean flag = true;
			while(flag){
				BufferedReader in = new BufferedReader(new InputStreamReader(
						System.in));
				String line = in.readLine();
				if(line.equals("help")){
					return false;
				}else{
					if (line.length() <= 0){
						logger.debug("Please input the parameters!");
						continue;
					}
					String[] param = line.trim().split(">");
					if(param.length !=6){
						logger.debug("The intput parameters are not right!");
						continue;
					}
					
					if(param[0].equals("") || param[0]== null){
						logger.debug("project name is not empty!");
						continue;
					}
					if(!StringUtil.formatRight(param[0])){
						logger.debug("Product's format is not correct, it is not be contains \"\\\",\"/\",\"<\",\">");
						continue;
					}
					
					if(param[1].equals("") || param[1]== null){
						logger.debug("project version is not empty!");
						continue;
					}
					
					if(!StringUtil.formatRight(param[1])){
						logger.debug("Version's format is not correct, it is not be contains \"\\\",\"/\",\"<\",\">");
						continue;
					}
					if(param[2].equals("") || param[2] == null){
						logger.debug("State is not empty!");
						continue;
					}
					if(!param[2].equals(Constant.STATE_ALL)
							&& !param[2].equals(Constant.STATE_NEW_CHANGED)){
						logger.debug("The state right key words is \""+Constant.STATE_ALL +"\" or \"" + Constant.STATE_NEW_CHANGED + "\"");
						continue;
					}
					String inPath = param[3];
					File file_input = new File(inPath);
					if (!file_input.isFile() || !file_input.exists()) {
						logger.debug("'" + inPath + "' is not a file or is not exist!");
						continue;
					}
					setSourceUrl(inPath);

					String outPath = param[4];
					File file_output = new File(outPath);
					if (!file_output.isDirectory() || !file_output.exists()) {
						logger.debug("'" + outPath
								+ "' is not directory or is not exist!");
						continue;
					}
					setOutputUrl(outPath);

					String selectRule = param[5];
					String[] rule = selectRule.split(",");
					List<Integer> list = new ArrayList<Integer>();
					for (String r : rule) {
						list.add(Integer.valueOf(r));
					}
					setSelectRules(list);
					
					String projectName = param[0];
					setProjectName(projectName);
					String projectVersion = param[1];
					setProjectVersion(projectVersion);
					String state = param[2];
					setState(state);
					
					if (Strings.isNullOrEmpty(sourceUrl)) {
						logger.debug("-i or --input is need to set source url!");
						continue;
					}
					if (Strings.isNullOrEmpty(outputUrl)) {
						logger.debug("-o or --output is need to set output url!");
						continue;
					}
					if (selectRules == null || selectRules.size() == 0) {
						logger.debug("-r or --rules is need to set rules index to be select !");
						continue;
					}
				}
				flag = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
