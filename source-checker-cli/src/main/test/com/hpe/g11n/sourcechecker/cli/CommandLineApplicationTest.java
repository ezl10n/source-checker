package com.hpe.g11n.sourceckecker.cli;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-23
 * Time: 17:16
 */
public class CommandLineApplicationTest {
    @Test
    public void cliTest() {
        String[] args = {"-p","LR","-v","V1.0","-s","All","-i", "C:\\tmp\\psl-generate-sorucechecker-report\\RC_9.50_SW_source_scoring.lpu", "-o", "C:\\tmp\\psl-generate-sorucechecker-report", "-r", "0,1,2,3"};
        CommandLineApplication.main(args);
    }
}
