(ns kafka-streams-demo-2.core
  (:import [org.apache.kafka.streams.kstream KStreamBuilder ValueMapper]
           [org.apache.kafka.streams KafkaStreams StreamsConfig]
           [org.apache.kafka.common.serialization Serdes])
  (:gen-class))

(def props
  {StreamsConfig/APPLICATION_ID_CONFIG, "kafka-streams-demo-2"
   StreamsConfig/BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"
   StreamsConfig/DEFAULT_KEY_SERDE_CLASS_CONFIG, (.getName (.getClass (Serdes/String)))
   StreamsConfig/DEFAULT_VALUE_SERDE_CLASS_CONFIG, (.getName (.getClass (Serdes/String)))})

(def config
  (StreamsConfig. props))

(def builder
  (KStreamBuilder.))

(def input-topic
  (into-array String ["my-input-topic"]))


(def value-mapper
  (reify ValueMapper
    (apply [_ value]
      (let [v (.toUpperCase value)]
        (prn "value:::" v)
        [v]))))

(->
 (.stream builder input-topic)
 (.flatMapValues value-mapper)
 (.to "my-output-topic"))

(def streams
  (KafkaStreams. builder config))

(defn -main 
  [& args]
  (println "Starting...")
  (.start streams))


;;(.start streams)

;;(.close streams)
