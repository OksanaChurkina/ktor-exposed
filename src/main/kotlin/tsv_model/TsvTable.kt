package tsv_model

import io.ktor.locations.Location
import org.jetbrains.exposed.sql.Table

object tsvTable: Table(){
    val contig = varchar("contig", 50)
    val left = integer("left")
    val mutation = varchar("mutation", 20).nullable()
    val right = integer("right")
    val rs = varchar("rs", 100)
}


@Location("/annotation/{rs}/contig/{contig}/leftInclusiveZeroBasedBoundary/{left}/rightExclusiveZeroBasedBoundary/{right}/sequence/{mutation}")
data class Tsv(val contig: String ,
               val left: Int,
               val mutation: String?,
               val right: Int ,
               val rs: String )