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
    @Parameter(names = {"-h", "-help", "--help"}, help = true, description = "Lists available commands.")
    private boolean help;
    @Parameter(names = {"-i", "--input"}, description = "File to be processed.")
    private String sourceUrl;
    @Parameter(names = {"-o", "--output"}, description = "Write Output to the folder.")
    private String outputUrl;
    @Parameter(names = {"-r", "--rules"}, description = "Rules select to run.")
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

        sb.append(String.format("\tfor example: -r 0,1 means using rules %s and %s to check and report.", ruleNames.get(0), ruleNames.get(1)));
        return sb.toString();
    }
    public boolean validate(){
    	try{
    	List<String> lst = new ArrayList<String>();
    	lst.add("-i or --input is need to set source url:");
    	lst.add("-o or --output is need to set output url:");
    	lst.add("-r or --rules is need to set rules index to be select:");
    	 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	for(int i=0; i<lst.size();i++){
    		String s = lst.get(i);
    		 System.err.print(s);
    		 String line = in.readLine();
             if (line.length() <= 0)
                 break;
             if(i==0){
            	 File file = new File(line);
            	 if(!file.isFile() || !file.exists()){
            		 logger.debug("'"+ line + "' is not a file or is not exist!");
            		 return false;
            	 }
            	 setSourceUrl(line); 
             }
            	 
             if(i==1){
            	 File file = new File(line);
            	 if(!file.isDirectory() || !file.exists()){
            		 logger.debug("'"+ line + "' is not directory or is not exist!");
            		 return false;
            	 }
            	 setOutputUrl(line); 
             }
            	
             if(i==2){
            	 String[] rule = line.split(",");
            	 List<Integer> list = new ArrayList<Integer>();
            	 for(String r:rule){
            		 list.add(Integer.valueOf(r));
            	 }
            	 setSelectRules(list);
             }
    	}
    	}catch(Exception e){
    		 e.printStackTrace();
    	}
        if(Strings.isNullOrEmpty(sourceUrl)){
            logger.debug("-i or --input is need to set source url!");
            return false;
        }
        if(Strings.isNullOrEmpty(outputUrl)){
            logger.debug("-o or --output is need to set output url!");
            return false;
        }
        if(selectRules == null || selectRules.size() == 0){
            logger.debug("-r or --rules is need to set rules index to be select !");
            return false;
        }
        return true;
    }

}
