package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Foto(id_foto, id_registro FK, legenda, data)
 *
 * OBS: foi adicionado o campo [caminhoArquivo] (não estava no modelo lógico original),
 * pois sem ele não há como o app saber qual arquivo/imagem exibir — ele guarda o
 * caminho (URI) do arquivo salvo localmente no dispositivo. Se preferirem remover,
 * basta apagar o campo e o parâmetro correspondente no DAO/Entity.
 *
 * Excluir o Registro Diário remove suas fotos (CASCADE).
 */
@Entity(
    tableName = "foto",
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
data class Foto(
    @PrimaryKey(autoGenerate = true)
    val idFoto: Long = 0L,
    val idRegistro: Long,
    val caminhoArquivo: String,
    val legenda: String? = null,
    val data: Long
)
