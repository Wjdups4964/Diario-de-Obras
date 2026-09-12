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

    /** Busca por nome (para a barra de pesquisa da tela de Lista de Obras). */
    @Query("SELECT * FROM obra WHERE nome LIKE '%' || :termo || '%' ORDER BY nome ASC")
    fun buscarPorNome(termo: String): Flow<List<Obra>>

    /** Obras sob responsabilidade de um usuário específico. */
    @Query("SELECT * FROM obra WHERE idResponsavel = :idUsuario ORDER BY dataInicio DESC")
    fun listarPorResponsavel(idUsuario: Long): Flow<List<Obra>>

    /** Contagem total de obras (útil para dashboards/telas de resumo). */
    @Query("SELECT COUNT(*) FROM obra")
    fun contarTodas(): Flow<Int>
}
