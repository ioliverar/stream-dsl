package io.orb.subject.db

import java.sql.{Connection, DriverManager}
import java.util.Properties
import com.typesafe.config.ConfigFactory

trait TiDBServer {
  val conf = ConfigFactory.load()
  val envProps = conf.getConfig("dev") //getConfig(args(0))

  val TiDBHostname    = envProps.getString("tidb.jdbc.hostname")
  val TiDBPort        = envProps.getString("tidb.jdbc.port")
  val TiDBDatabase    = envProps.getString("tidb.jdbc.database")
  val TiDBUsername    = envProps.getString("tidb.jdbc.username")
  val TiDBPassword    = envProps.getString("tidb.jdbc.password")
  val TiDBDriverClass = envProps.getString("tidb.jdbc.driver")

  val jdbcUrl = s"jdbc:mysql://${TiDBHostname}:${TiDBPort}/${TiDBDatabase}"

  val connectionProperties = new Properties()
      connectionProperties.put("user", s"${TiDBUsername}")
      connectionProperties.put("password", s"${TiDBPassword}")
      connectionProperties.setProperty("Driver", TiDBDriverClass)
      connectionProperties.setProperty("AutoCommit", "true")
      connectionProperties.setProperty("useServerPrepStmts", "true")
      connectionProperties.setProperty("cachePrepStmts", "true")
      connectionProperties.setProperty("useLocalSessionState", "true")
      connectionProperties.setProperty("prepStmtCacheSize", "250")
      connectionProperties.setProperty("prepStmtCacheSqlLimit", "2048")

  var connection : Connection = _
  Class.forName(TiDBDriverClass)
  connection = DriverManager.getConnection(jdbcUrl, TiDBUsername, TiDBPassword)

}
