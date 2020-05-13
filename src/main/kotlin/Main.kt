import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import tsv_service.DbFactory
import tsv_service.TsvService


@Location("/annotation") data class tsv(
        val contig: String,
        val leftInclusiveZeroBasedBoundary: Int,
        val rightExclusiveZeroBasedBoundary: Int,
        val sequence: String
)


fun Application.mainModule(){
    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)


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

    /*
     * Если в запрос ввести неверные значения, исключение
     * выбросит "RS-identifier not found"
     */

    install(Routing) {

        get<tsv>{
            tsv ->
           val rs = tsvService.findBy(tsv.contig, tsv.leftInclusiveZeroBasedBoundary,
                   tsv.sequence, tsv.rightExclusiveZeroBasedBoundary)
            call.respond(mapOf("RS-identifier" to rs))}

        }
}


fun main(){
    val port = 8080
    val server = embeddedServer(Netty, port, module = Application::mainModule)
    server.start()

}
