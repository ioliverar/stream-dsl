//Steps to run Stream DSL
1. Tener una instancia de TiDB instalada, ya teniendo los parametros ir a application.properties del proyecto y actualizarlos segun tu instancia.
2. Crear la tabla transactions en el schema de tu instancia de TiDB (scrpit poc_create_table_transactions.sql)
3. Tener una instancia de kafka instalada, donde se creara los topicos src-topic y out-topic.
4. Tener un generador de eventos para el topic src-topic. Diponible en:
	4.1. Descomprimir y ejecutar por linea de comandos estando dentro de la carpeta: java -jar json-data-generator-1.4.1.jar purchases-config.json
5. Ejecutar el proyecto en IDE de tu preferencia.
