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
			// String s ="-i ddd;-o ddd;-r d,d,d";
			System.out
					.println("Please input the parameters (e.g. -i C:\\test.lpu>-o C:\\tmp>-r 0,1,2,3):");
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
					if(param.length !=3){
						logger.debug("The intput parameters are not right!");
						continue;
					}
					if(param[0].split(" ").length !=2){
						logger.debug("The path of source file is not right!");
						continue;
					}
					
					if(param[1].split(" ").length !=2){
						logger.debug("The path of output folder is not right!");
						continue;
					}
					String inPath = param[0].split(" ")[1];
					File file_input = new File(inPath);
					if (!file_input.isFile() || !file_input.exists()) {
						logger.debug("'" + inPath + "' is not a file or is not exist!");
						continue;
					}
					setSourceUrl(inPath);

					String outPath = param[1].split(" ")[1];
					File file_output = new File(outPath);
					if (!file_output.isDirectory() || !file_output.exists()) {
						logger.debug("'" + outPath
								+ "' is not directory or is not exist!");
						continue;
					}
					setOutputUrl(outPath + "/");

					String selectRule = param[2].split(" ")[1];
					String[] rule = selectRule.split(",");
					List<Integer> list = new ArrayList<Integer>();
					for (String r : rule) {
						list.add(Integer.valueOf(r));
					}
					setSelectRules(list);
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
