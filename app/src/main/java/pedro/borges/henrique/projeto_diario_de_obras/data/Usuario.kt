package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Usuario(id_usuario, nome, cargo)
 * Ex.: o(a) responsável por uma obra e/ou por lançar registros diários.
 */
@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val idUsuario: Long = 0L,
    val nome: String,
    val cargo: String
)
