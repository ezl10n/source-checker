package com.hpe.g11n.sourcescoring.utils;


import com.google.common.base.Preconditions;
import com.hpe.g11n.sourcescoring.core.annotation.RuleData;
import com.typesafe.config.*;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//FIXEME should refactory...
public class SourceScoringConfigUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SourceScoringConfigUtil.class);
	private static String CONFIG_DIR = System.getProperty("source.scoring.config.basedir") != null ? System.getProperty("source.scoring.config.basedir") : System.getProperty("user.dir");
	private static String CONFIG_PATH = "/src/main/config/psl-generate-sourcescoring-report.conf";

	//FIXME just copy ezl10n,need fix.
	private static ConfigParseOptions parseOptions = ConfigParseOptions.defaults().setAllowMissing(false);
	private static ConfigResolveOptions resolveOptions = ConfigResolveOptions.defaults().setAllowUnresolved(true);

	static Config config = parse(Paths.get(CONFIG_DIR,CONFIG_PATH));
	private static final Reflections REFLECTIONS = buildReflections("com.hpe.g11n.sourcescoring.core.rules");
	private static List<String> checkboxNames = new ArrayList<>();
	private static List<Class> rules = new ArrayList<>();


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

	public static Config getConfig() {
		return config;
	}

	public static Object getValue(String key) {
		return config.getValue(key);
	}

	public static List<String> checkBoxs() {
		return checkboxNames;
	}

	public static List<Class> ruleClassList() {
		return rules;
	}

	//FIXME just copy from ezl10n code, need refactory
	public static Reflections buildReflections(String... packageNames) {
		ArrayList urls = new ArrayList();
		String[] var2 = packageNames;
		int var3 = packageNames.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			String packageName = var2[var4];
			urls.addAll(ClasspathHelper.forPackage(packageName, new ClassLoader[0]));
		}

		return new Reflections((new ConfigurationBuilder()).setUrls(urls).setScanners(new Scanner[]{new SubTypesScanner(), new TypeAnnotationsScanner(), new FieldAnnotationsScanner()}).filterInputsBy((new FilterBuilder()).includePackage(packageNames).exclude("(?i)^.+\\.(json|conf|properties|fxml|css|xml|txt|less|saas)$")));
	}
	//FIXME just copy from ezl10n code, need refactory
	public static Config parse(Path file, boolean withOverrides) {
		Preconditions.checkNotNull(file);
		String config1;
		if(Files.notExists(file, new LinkOption[0])) {
			config1 = String.format("File does not exist: [%s]", new Object[]{file});
			LOGGER.error("TypeSafeConfigUtils.parse: File does not exist: [{}]", file);
			throw new RuntimeException(new FileNotFoundException(config1));
		} else if(Files.isDirectory(file, new LinkOption[0])) {
			config1 = String.format("File is a directory: [%s]", new Object[]{file});
			LOGGER.error("TypeSafeConfigUtils.parse: File is a directory: [{}]", file);
			throw new RuntimeException(new IllegalArgumentException(config1));
		} else if(Files.notExists(file, new LinkOption[0])) {
			LOGGER.error("ConfigurationFileParser.parse: File not found: {}", file);
			return null;
		} else {
			Config config = ConfigFactory.parseFileAnySyntax(file.toFile(), parseOptions);
			if(withOverrides) {
				config = ConfigFactory.defaultOverrides().withFallback(config);
			}

			return config.resolve(resolveOptions);
		}
	}

	public static Config parse(Path file) {
		return parse(file, true);
	}
}
