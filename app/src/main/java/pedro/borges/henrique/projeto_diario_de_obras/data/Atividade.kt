package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Atividade(id_atividade, id_registro FK, descricao, status)
 *
 * Excluir o Registro Diário remove suas atividades (CASCADE).
 */
@Entity(
    tableName = "atividade",
    foreignKeys = [
        ForeignKey(
            entity = RegistroDiario::class,
            parentColumns = ["idRegistro"],
            childColumns = ["idRegistro"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idRegistro")]
)
data class Atividade(
    @PrimaryKey(autoGenerate = true)
    val idAtividade: Long = 0L,
    val idRegistro: Long,
    val descricao: String,
    val status: String
)
