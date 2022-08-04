package io.orb.subject

import java.time.Duration
import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig, Topology}
import org.apache.kafka.streams.kstream.{Consumed, ForeachAction, KStream, KeyValueMapper, Predicate, Printed, Produced, ValueMapper}
import io.orb.subject.serdes.JsonSerde
import java.util.{Date, UUID}
import io.orb.subject.db.TiDBDAL.saveTransaction
import io.orb.subject.models.Transaction
import org.slf4j.LoggerFactory

object Application extends App{

  val logger = LoggerFactory.getLogger(this.getClass)

  //Load config file
  val conf = ConfigFactory.load()
  val envProps = conf.getConfig("dev") //getConfig(args(0))

  // Initialize and set properties
  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, envProps.getString("stream.application.id"))
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, envProps.getString("bootstrap.server"))
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
  val streamsConfig = new StreamsConfig(props)

  //Generic SerDes
  val stringSerde: Serde[String] = Serdes.String()
  val longSerde: Serde[Long] = Serdes.Long().asInstanceOf[Serde[Long]]

  //Explicit SerDes
  implicit val transactionSerde = new JsonSerde[Transaction]

  //Building source node
  val streamBuilder = new StreamsBuilder()
  val sourceStream: KStream[String, Transaction] = streamBuilder.stream[String, Transaction](envProps.getString("stream.source.topic"), Consumed.`with`(stringSerde, transactionSerde))

  //First processor: masking
  val  maskedTransaction: KStream[String, Transaction] = sourceStream.mapValues(t => t.maskCreditCard)

  //Branching the stream
  val highPriceItems = new Predicate[String, Transaction] {
    override def test(k: String, v: Transaction): Boolean = v.price > 8.00
  }
  val branches = maskedTransaction.branch(highPriceItems)

  //Persist into new topic
  val highPriceStream = branches(0).through(envProps.getString("stream.output.topic"), Produced.`with`(stringSerde, transactionSerde))

  //Generating new key in Long type
  //val keyedTransaction: KStream[Long, Transaction] = sourceStream.selectKey(new KeyValueMapper[String, Transaction, Long] {
  //  override def apply(key: String, value: Transaction): Long = value.purchaseDate.getTime
  //})
  //Generating new key in UUID type for storage
  val keyedTransaction: KStream[UUID, Transaction] = sourceStream.selectKey(new KeyValueMapper[String, Transaction, UUID] {
    override def apply(key: String, value: Transaction): UUID =  UUID.randomUUID
      //new UUID(value.purchaseDate.getTime, 36)
  })

  //Insert data on TiDB
  //saveTransaction("00000152-ebdb-5530-0000-000000000024", "Loxly", "Sarah", "5299-1561-5689-1938", "doughnuts", 2.0, 7.8255, "21842")
  val transactionForeachAction: ForeachAction[UUID, Transaction] = {
    (key, transaction) => saveTransaction(
                          key.toString,
                          transaction.lastName,
                          transaction.firstName,
                          transaction.creditCardNumber,
                          transaction.itemPurchased,
                          transaction.quantity,
                          transaction.price,
                          transaction.purchaseDate,
                          transaction.zipCode
                        )
  }
  keyedTransaction.foreach(transactionForeachAction)

  //Print the result to the console
  keyedTransaction.print(Printed.toSysOut[UUID, Transaction]().withLabel("Transactions"))
  //maskedTransaction.print(Printed.toSysOut[String, Transaction]().withLabel("source"))

  //Start Topology
  val streams:KafkaStreams = new KafkaStreams(streamBuilder.build(), streamsConfig)
    logger.info("Kafka Streams Application Started...")
    streams.start()
  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(65000))
  }
}
