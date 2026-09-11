package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Obra(id_obra, nome, endereco, data_inicio, data_previsao_termino, area, status, id_responsavel)
 *
 * id_responsavel referencia Usuario. Se o usuário responsável for excluído, a obra
 * NÃO é apagada (não faria sentido) — o campo apenas fica nulo (SET_NULL).
 *
 * Datas são guardadas como epoch millis (Long) para não depender de TypeConverters.
 */
@Entity(
    tableName = "obra",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idResponsavel"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("idResponsavel")]
)
data class Obra(
    @PrimaryKey(autoGenerate = true)
    val idObra: Long = 0L,
    val nome: String,
    val endereco: String,
    val dataInicio: Long,
    val dataPrevisaoTermino: Long? = null,
    val area: Double? = null,
    val status: String,
    val idResponsavel: Long? = null
)
