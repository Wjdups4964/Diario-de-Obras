package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun inserir(usuario: Usuario): Long

    @Update
    suspend fun editar(usuario: Usuario)

    @Delete
    suspend fun excluir(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE idUsuario = :idUsuario")
    suspend fun buscarPorId(idUsuario: Long): Usuario?

    @Query("SELECT * FROM usuario ORDER BY nome ASC")
    fun listarTodos(): Flow<List<Usuario>>
}
