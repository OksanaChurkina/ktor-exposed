import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.jackson.jackson
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
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
        gson {

            setPrettyPrinting()
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

        val rs  =  tsvService.findBy("chr42", 9411246, "G", 9411250)

        get("/") {

            call.respond(mapOf("RS-identifier" to rs))


        }

    }

}


fun main(){
    val port = 8080
    val server = embeddedServer(Netty, port, module = Application::mainModule)
    server.start()

}