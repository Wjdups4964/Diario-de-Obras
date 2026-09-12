package pedro.borges.henrique.projeto_diario_de_obras.repository

import kotlinx.coroutines.flow.Flow
import pedro.borges.henrique.projeto_diario_de_obras.data.RegistroDiario
import pedro.borges.henrique.projeto_diario_de_obras.data.RegistroDiarioDao

class RegistroRepository(private val registroDao: RegistroDiarioDao) {

    fun listarPorObra(idObra: Long): Flow<List<RegistroDiario>> = registroDao.listarPorObra(idObra)

    fun listarTodos(): Flow<List<RegistroDiario>> = registroDao.listarTodos()

    fun contarPorObra(idObra: Long): Flow<Int> = registroDao.contarPorObra(idObra)

    suspend fun buscarPorId(idRegistro: Long): RegistroDiario? = registroDao.buscarPorId(idRegistro)

    suspend fun buscarUltimoDaObra(idObra: Long): RegistroDiario? = registroDao.buscarUltimoDaObra(idObra)

    suspend fun inserir(registro: RegistroDiario): Long = registroDao.inserir(registro)

    suspend fun editar(registro: RegistroDiario) = registroDao.editar(registro)

    suspend fun excluir(registro: RegistroDiario) = registroDao.excluir(registro)
}
