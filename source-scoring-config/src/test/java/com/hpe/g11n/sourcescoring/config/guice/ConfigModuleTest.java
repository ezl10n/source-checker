package com.hpe.g11n.sourcescoring.config.guice;

import com.typesafe.config.Config;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-11
 * Time: 9:51
 */
public class ConfigModuleTest {
    @Test
    public void testLoadConfig(){
        Config c=ConfigModule.loadConfig();
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
}
