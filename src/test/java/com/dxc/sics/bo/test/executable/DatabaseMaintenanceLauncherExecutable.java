package com.dxc.sics.bo.test.executable;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import com.dxc.sics.bo.test.utils.SicsDatabaseUtilities;

public class DatabaseMaintenanceLauncherExecutable implements Executable {

  private static final String MAIN_CLASS = "com.csc.sics.launcher.DatabaseMaintenanceLauncherMain";

  private final String product;
  private final String databaseName;
  private final String databaseDataSoure;
  private final String databaseUsername;
  private final String databasePassword;
  private final boolean useParameterizedSql;
  private final int statementCacheSize;
  private final boolean optimizedMode;
  private final List<String> arguments;

  public DatabaseMaintenanceLauncherExecutable(String product, String databaseName, String databaseDataSoure, String databaseUsername, String databasePassword,
      boolean useParameterizedSql, int statementCacheSize, boolean optimizedMode, List<String> arguments) {
    super();
    this.product = product;
    this.databaseName = databaseName;
    this.databaseDataSoure = databaseDataSoure;
    this.databaseUsername = databaseUsername;
    this.databasePassword = databasePassword;
    this.useParameterizedSql = useParameterizedSql;
    this.statementCacheSize = statementCacheSize;
    this.optimizedMode = optimizedMode;
    this.arguments = arguments;
  }

  @Override
  public void execute() throws Throwable {
    SicsDatabaseUtilities.createDatabaseSourceXml(databaseName, databaseDataSoure, useParameterizedSql, statementCacheSize, optimizedMode);
    ProcessBuilder builder = new ProcessBuilder(this.getCommand());
    builder.redirectErrorStream(true);
    builder.directory(new File("target/runtime"));
    builder.redirectError(Redirect.INHERIT);
    builder.redirectOutput(Redirect.INHERIT);
    Process process = builder.start();
    int status = process.waitFor();
    Assertions.assertEquals(0, status);
  }

  private List<String> getCommand() {
    List<String> command = new ArrayList<String>();
    command.add("java");
    command.add("-cp");
    command.add(this.getClasspath());
    command.add(MAIN_CLASS);
    command.add(product.toUpperCase(Locale.ROOT));
    command.add("-dn" + databaseName);
    command.add("-du" + databaseUsername);
    command.add("-dp" + databasePassword);
    command.addAll(arguments);
    return command;
  }

  private String getClasspath() {
    String pathSeparator = System.getProperty("path.separator");
    StringBuilder sb = new StringBuilder();
    sb.append("conf");
    sb.append(pathSeparator);
    sb.append("lib");
    sb.append(pathSeparator);
    sb.append("lib/sics.launcher.jar");
    sb.append(pathSeparator);
    sb.append("lib/sics.dev.launcher.jar");
    sb.append(pathSeparator);
    sb.append("lib/sics.classloader.jar");
    return sb.toString();
  }

}
