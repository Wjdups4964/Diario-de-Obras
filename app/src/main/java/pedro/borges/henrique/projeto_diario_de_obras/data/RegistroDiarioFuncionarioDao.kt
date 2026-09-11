package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDiarioFuncionarioDao {

    @Insert
    suspend fun inserir(associacao: RegistroDiarioFuncionario)

    @Delete
    suspend fun excluir(associacao: RegistroDiarioFuncionario)

    @Query("DELETE FROM registro_diario_conta_funcionario WHERE idRegistro = :idRegistro")
    suspend fun excluirTodosDoRegistro(idRegistro: Long)

    @Query("SELECT * FROM registro_diario_conta_funcionario")
    fun listarTodos(): Flow<List<RegistroDiarioFuncionario>>

    /**
     * "Editar" para uma tabela puramente associativa (sem colunas próprias) equivale a
     * substituir o conjunto de funcionários vinculados a um registro diário.
     */
    @Transaction
    suspend fun editarFuncionariosDoRegistro(idRegistro: Long, idsFuncionarios: List<Long>) {
        excluirTodosDoRegistro(idRegistro)
        idsFuncionarios.forEach { idFuncionario ->
            inserir(RegistroDiarioFuncionario(idRegistro = idRegistro, idFuncionario = idFuncionario))
        }
    }

    /** Funcionários presentes em um determinado registro diário. */
    @Query(
        """
        SELECT funcionario.* FROM funcionario
        INNER JOIN registro_diario_conta_funcionario
            ON funcionario.idFuncionario = registro_diario_conta_funcionario.idFuncionario
        WHERE registro_diario_conta_funcionario.idRegistro = :idRegistro
        ORDER BY funcionario.nome ASC
        """
    )
    fun listarFuncionariosDoRegistro(idRegistro: Long): Flow<List<Funcionario>>

    /** Registros diários em que um determinado funcionário esteve presente. */
    @Query(
        """
        SELECT registro_diario.* FROM registro_diario
        INNER JOIN registro_diario_conta_funcionario
            ON registro_diario.idRegistro = registro_diario_conta_funcionario.idRegistro
        WHERE registro_diario_conta_funcionario.idFuncionario = :idFuncionario
        ORDER BY registro_diario.data DESC
        """
    )
    fun listarRegistrosDoFuncionario(idFuncionario: Long): Flow<List<RegistroDiario>>
}
