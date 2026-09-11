package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AtividadeDao {

    @Insert
    suspend fun inserir(atividade: Atividade): Long

    @Update
    suspend fun editar(atividade: Atividade)

    @Delete
    suspend fun excluir(atividade: Atividade)

    @Query("SELECT * FROM atividade WHERE idAtividade = :idAtividade")
    suspend fun buscarPorId(idAtividade: Long): Atividade?

    @Query("SELECT * FROM atividade WHERE idRegistro = :idRegistro ORDER BY idAtividade ASC")
    fun listarPorRegistro(idRegistro: Long): Flow<List<Atividade>>
}
