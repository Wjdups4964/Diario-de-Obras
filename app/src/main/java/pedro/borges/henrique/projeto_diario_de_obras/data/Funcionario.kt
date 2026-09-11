package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Funcionario(id_funcionario, nome, funcao)
 */
@Entity(tableName = "funcionario")
data class Funcionario(
    @PrimaryKey(autoGenerate = true)
    val idFuncionario: Long = 0L,
    val nome: String,
    val funcao: String
)
