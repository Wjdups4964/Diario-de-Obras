package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Registro_Diario(id_registro, id_obra FK, id_usuario FK, data, turno, observacoes)
 *
 * Excluir a Obra remove seus registros (CASCADE).
 * Excluir o Usuario que fez o registro também remove o registro (CASCADE), já que o
 * app não tem login e não há por que manter um registro "órfão" sem autor.
 */
@Entity(
    tableName = "registro_diario",
    foreignKeys = [
        ForeignKey(
            entity = Obra::class,
            parentColumns = ["idObra"],
            childColumns = ["idObra"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idObra"), Index("idUsuario")]
)
data class RegistroDiario(
    @PrimaryKey(autoGenerate = true)
    val idRegistro: Long = 0L,
    val idObra: Long,
    val idUsuario: Long,
    val data: Long,
    val turno: String,
    val observacoes: String? = null
)
