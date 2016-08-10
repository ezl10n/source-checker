package com.hpe.g11n.sourcescoring.utils;


import com.typesafe.config.Config;
import org.junit.Test;

public class SourceScoringConfigUtilTest {
    @Test
    public void printConfig(){
        Config c=SourceScoringConfigUtil.getConfig();
        System.out.println("####################key-words##################");
        c.getStringList("psl.psl-generate-sourcescoring-report.concatenation.key-words").stream().forEach(e -> System.out.println(e));
        System.out.println("###############################################");
        System.out.println("####################variables##################");
        System.out.println(c.getString("psl.psl-generate-sourcescoring-report.concatenation.variables"));
        System.out.println("###############################################");
        System.out.println("####################camel-case##################");
        System.out.println(c.getString("psl.psl-generate-sourcescoring-report.camel-case"));
        System.out.println("###############################################");
        System.out.println("####################date-time-format##################");
        c.getStringList("psl.psl-generate-sourcescoring-report.date-time-format").stream().forEach(e -> System.out.println(e));
        System.out.println("###############################################");
    }
    @Test
    public void printCheckBox(){
        SourceScoringConfigUtil.checkBoxs().stream().forEach(c -> System.out.println(c));
    }
}
