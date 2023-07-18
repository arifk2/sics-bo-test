package com.dxc.sics.bo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.dxc.sics.bo.test.executable.DatabaseMaintenanceLauncherExecutable;

public class SicsBusinessObjectsTest implements BeforeAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    // TODO Use https://www.testcontainers.org/ to create a new database (Oracle, SQL Server, PostgreSQL or MySQL). Use
    // https://github.dxc.com/sics/sics-database-schema-image-plugin to create the tables and import the data. Note that
    // this should only be done once and I think you can reuse what's done in SE-18983.
  }

  @TestFactory
  @DisplayName("Business Objects Tests")
  Collection<DynamicTest> businessObjectsTests() throws Throwable {
    String sicsProduct = this.sicsProduct();
    String databaseName = this.databaseName();
    String databaseDataSource = this.databaseDataSource();
    String databaseUsername = this.databaseUsername();
    String databasePassword = this.databasePassword();
    boolean useParameterizedSql = this.useParameterizedSql();
    int statementCacheSize = this.statementCacheSize();
    boolean optimizedMode = this.optimizedMode();
    String method = this.method().toUpperCase(Locale.ROOT);

    List<DynamicTest> tests = new ArrayList<DynamicTest>();
    tests.add(DynamicTest.dynamicTest("Create tables", new DatabaseMaintenanceLauncherExecutable(sicsProduct, databaseName, databaseDataSource,
        databaseUsername, databasePassword, useParameterizedSql, statementCacheSize, optimizedMode, Arrays.asList("-createBOReportingTables", "-reportingTablesMethod" + method))));
    tests.add(DynamicTest.dynamicTest("Create stored functions", new DatabaseMaintenanceLauncherExecutable(sicsProduct, databaseName, databaseDataSource,
        databaseUsername, databasePassword, useParameterizedSql, statementCacheSize, optimizedMode, Arrays.asList("-createBOReportingStoredFunctions", "-allBOReportingStoredFunctions"))));
    tests.add(DynamicTest.dynamicTest("Create views", new DatabaseMaintenanceLauncherExecutable(sicsProduct, databaseName, databaseDataSource, databaseUsername,
        databasePassword, useParameterizedSql, statementCacheSize, optimizedMode, Arrays.asList("-createBOReportingViews"))));
    tests.add(DynamicTest.dynamicTest("Create stored procedures", new DatabaseMaintenanceLauncherExecutable(sicsProduct, databaseName, databaseDataSource,
        databaseUsername, databasePassword, useParameterizedSql, statementCacheSize, optimizedMode, Arrays.asList("-createBOReportingStoredProcedures", "-allBOReportingStoredProcedures"))));
    return tests;
  }

  private String sicsProduct() {
    return System.getProperty("sics.product", "pc");
  }

  private String databaseName() {
    return System.getProperty("database.name");
  }

  private String databaseUsername() {
    return System.getProperty("database.username");
  }

  private String databasePassword() {
    return System.getProperty("database.password");
  }

  private String databaseDataSource() {
    return System.getProperty("database.dataSource");
  }

  private boolean useParameterizedSql() {
    try {
      return Boolean.parseBoolean(System.getProperty("database.useParameterizedSql", "true"));
    } catch (IllegalArgumentException e) {
      return true;
    }
  }

  private int statementCacheSize() {
    try {
      return Integer.valueOf(System.getProperty("database.statementCacheSize", "200"));
    } catch (NumberFormatException e) {
      return 200;
    }
  }

  private boolean optimizedMode() {
    try {
      return Boolean.parseBoolean(System.getProperty("database.optimizedMode", "false"));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
  
  private String method() {
    return System.getProperty("method", "A");
  }

}
