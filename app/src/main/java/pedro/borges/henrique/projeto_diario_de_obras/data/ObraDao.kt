package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ObraDao {

    @Insert
    suspend fun inserir(obra: Obra): Long

    @Update
    suspend fun editar(obra: Obra)

    @Delete
    suspend fun excluir(obra: Obra)

    @Query("SELECT * FROM obra WHERE idObra = :idObra")
    suspend fun buscarPorId(idObra: Long): Obra?

    @Query("SELECT * FROM obra ORDER BY dataInicio DESC")
    fun listarTodas(): Flow<List<Obra>>

    @Query("SELECT * FROM obra WHERE status = :status ORDER BY dataInicio DESC")
    fun listarPorStatus(status: String): Flow<List<Obra>>
}
