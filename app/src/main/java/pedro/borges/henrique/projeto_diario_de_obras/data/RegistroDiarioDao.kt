package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDiarioDao {

    @Insert
    suspend fun inserir(registro: RegistroDiario): Long

    @Update
    suspend fun editar(registro: RegistroDiario)

    @Delete
    suspend fun excluir(registro: RegistroDiario)

    @Query("SELECT * FROM registro_diario WHERE idRegistro = :idRegistro")
    suspend fun buscarPorId(idRegistro: Long): RegistroDiario?

    @Query("SELECT * FROM registro_diario ORDER BY data DESC")
    fun listarTodos(): Flow<List<RegistroDiario>>

    /** Lista os registros diários de uma obra específica, do mais recente para o mais antigo. */
    @Query("SELECT * FROM registro_diario WHERE idObra = :idObra ORDER BY data DESC")
    fun listarPorObra(idObra: Long): Flow<List<RegistroDiario>>

    @Query("SELECT * FROM registro_diario WHERE idUsuario = :idUsuario ORDER BY data DESC")
    fun listarPorUsuario(idUsuario: Long): Flow<List<RegistroDiario>>
}
