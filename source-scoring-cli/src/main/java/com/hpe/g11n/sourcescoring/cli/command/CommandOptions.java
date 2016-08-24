package com.hpe.g11n.sourcescoring.cli.command;

import com.beust.jcommander.Parameter;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

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
        if(Strings.isNullOrEmpty(sourceUrl)){
            logger.error("-i or --input is need to set source url!");
            return false;
        }
        if(Strings.isNullOrEmpty(outputUrl)){
            logger.error("-o or --output is need to set output url!");
            return false;
        }
        if(selectRules == null || selectRules.size() == 0){
            logger.error("-r or --rules is need to set rules index to be select !");
            return false;
        }
        return true;
    }

}
