package com.dxc.sics.bo.test.utils;

import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SicsDatabaseUtilities {

  public static void createDatabaseSourceXml(String name, String dataSource, boolean useParameterizedSql, int statementCacheSize, boolean optimizedMode) {
    try (FileOutputStream output = new FileOutputStream("target/runtime/conf/sics-database-sources.xml")) {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      Element rootElement = doc.createElement("database-sources");
      doc.appendChild(rootElement);

      Element source = doc.createElement("source");
      source.setAttribute("name", name);
      rootElement.appendChild(source);

      Element driverElement = doc.createElement("driver");
      driverElement.setTextContent(getJdbcDriverFromDataSource(dataSource));
      source.appendChild(driverElement);

      Element accessor = doc.createElement("accessor");
      accessor.setTextContent("com.csc.troll.accessor.JDBCDatabaseAccessor");
      source.appendChild(accessor);

      Element interfaceElement = doc.createElement("interface");
      interfaceElement.setTextContent(getInterfaceFromDataSource(dataSource));
      source.appendChild(interfaceElement);

      Element dataSourceElement = doc.createElement("dataSource");
      dataSourceElement.setTextContent(dataSource);
      source.appendChild(dataSourceElement);

      Element useParameterizedSqlElement = doc.createElement("useParameterizedSql");
      useParameterizedSqlElement.setTextContent(String.valueOf(useParameterizedSql));
      source.appendChild(useParameterizedSqlElement);

      Element statementCacheSizeElement = doc.createElement("statementCacheSize");
      statementCacheSizeElement.setTextContent(String.valueOf(statementCacheSize));
      source.appendChild(statementCacheSizeElement);

      Element optimizedModeElement = doc.createElement("optimizedMode");
      optimizedModeElement.setTextContent(String.valueOf(optimizedMode));
      source.appendChild(optimizedModeElement);

      TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
    } catch (Exception e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException) e;
      }
      throw new RuntimeException(e);
    }
  }

  private static String getInterfaceFromDataSource(String dataSource) throws IllegalArgumentException {
    if (dataSource.startsWith("jdbc:oracle")) {
      return "com.csc.troll.accessor.Oracle11Interface";
    } else if (dataSource.startsWith("jdbc:sqlserver")) {
      return "com.csc.troll.accessor.SQLServer2000Interface";
    } else if (dataSource.startsWith("jdbc:mysql")) {
      return "com.csc.troll.accessor.MySqlDatabaseInterface";
    } else if (dataSource.startsWith("jdbc:postgresql")) {
      return "com.csc.troll.accessor.PostgresDatabaseInterface";
    } else {
      throw new IllegalArgumentException("Invalid datasource");
    }
  }

  private static String getJdbcDriverFromDataSource(String dataSource) throws IllegalArgumentException {
    if (dataSource.startsWith("jdbc:oracle")) {
      return "oracle.jdbc.driver.OracleDriver";
    } else if (dataSource.startsWith("jdbc:sqlserver")) {
      return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    } else if (dataSource.startsWith("jdbc:mysql")) {
      return "com.mysql.cj.jdbc.Driver";
    } else if (dataSource.startsWith("jdbc:postgresql")) {
      return "org.postgresql.Driver";
    } else {
      throw new IllegalArgumentException("Invalid datasource");
    }
  }

  private SicsDatabaseUtilities() {
  }
}
