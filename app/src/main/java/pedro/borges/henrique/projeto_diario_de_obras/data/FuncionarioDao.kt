package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FuncionarioDao {

    @Insert
    suspend fun inserir(funcionario: Funcionario): Long

    @Update
    suspend fun editar(funcionario: Funcionario)

    @Delete
    suspend fun excluir(funcionario: Funcionario)

    @Query("SELECT * FROM funcionario WHERE idFuncionario = :idFuncionario")
    suspend fun buscarPorId(idFuncionario: Long): Funcionario?

    @Query("SELECT * FROM funcionario ORDER BY nome ASC")
    fun listarTodos(): Flow<List<Funcionario>>
}
