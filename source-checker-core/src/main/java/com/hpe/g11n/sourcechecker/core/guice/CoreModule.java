package com.hpe.g11n.sourcechecker.core.guice;

import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.hpe.g11n.sourcechecker.core.ISourceChecker;
import com.hpe.g11n.sourcechecker.core.SourceCheckerRuleManager;
import com.hpe.g11n.sourcechecker.core.annotation.RuleData;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;


/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-10
 * Time: 16:09
 */
public class CoreModule extends AbstractModule {

    private static List<String> checkboxNames = new ArrayList<>();
    private static List<Class> rules = new ArrayList<>();
    private static final Reflections REFLECTIONS = buildReflections(Constant.RULES_PACKAGE);
    static {
        REFLECTIONS
                .getTypesAnnotatedWith(RuleData.class)
                .stream()
                .sorted((a, b) -> Integer.compare(
                        a.getAnnotation(RuleData.class).order(), b
                                .getAnnotation(RuleData.class).order()))
                .forEach(c -> {
                    RuleData data = c.getAnnotation(RuleData.class);
                    checkboxNames.add(data.name());
                    rules.add(data.ruleClass());
                });
    }
    @Override
    protected void configure() {
        bind(ISourceChecker.class).to(SourceCheckerRuleManager.class);
        bind(new TypeLiteral<List<Class>>(){}).annotatedWith(Names.named("ruleClasses")).toInstance(getRules());
        bind(new TypeLiteral<List<String>>(){}).annotatedWith(Names.named("ruleNames")).toInstance(checkboxs());
    }

    @Provides
    @Singleton
    public List<Class> getRules(){
        return rules;
    }

    @Provides
    @Singleton
    public List<String> checkboxs(){
        return checkboxNames;
    }
    public static Reflections buildReflections(String... packageNames) {
        ArrayList urls = new ArrayList();
        String[] names = packageNames;
        int length = packageNames.length;

        for(int i = 0; i < length; ++i) {
            String packageName = names[i];
            urls.addAll(ClasspathHelper.forPackage(packageName, new ClassLoader[0]));
        }
        return new Reflections((new ConfigurationBuilder()).setUrls(urls).setScanners(new Scanner[]{new SubTypesScanner(), new TypeAnnotationsScanner(), new FieldAnnotationsScanner()}).filterInputsBy((new FilterBuilder()).includePackage(packageNames).exclude("(?i)^.+\\.(json|conf|properties|fxml|css|xml|txt|less|saas)$")));
    }
}
