import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.jackson.jackson
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import tsv_service.DbFactory
import tsv_service.TsvService


fun Application.mainModule(){
    install(DefaultHeaders)
    install(CallLogging)


        install(ContentNegotiation){
        jackson{
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

  val hikari = HikariDataSource(HikariConfig().apply {

      driverClassName = "org.h2.Driver"
      jdbcUrl = "jdbc:h2:mem:test"
      maximumPoolSize = 3
      isAutoCommit = false
      transactionIsolation = "TRANSACTION_REPEATABLE_READ"
      validate()
  })

    Database.connect(hikari)
    DbFactory.init()


val tsvService = TsvService()
    install(Routing) {
        //tsvService.getRS("chr42", 9414470, "A", 9414473)
        tsvService.findBy("chr42", 9414470, "A" ,9414473)

    }
}

fun main(args: Array<String>){
    val port = 8080
    val server = embeddedServer(Netty, port, module = Application::mainModule)
    server.start()

}