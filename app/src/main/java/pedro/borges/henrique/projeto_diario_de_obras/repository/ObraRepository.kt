package pedro.borges.henrique.projeto_diario_de_obras.repository

import kotlinx.coroutines.flow.Flow
import pedro.borges.henrique.projeto_diario_de_obras.data.Obra
import pedro.borges.henrique.projeto_diario_de_obras.data.ObraDao

/**
 * Camada intermediária entre o ViewModel e o Room (ObraDao).
 *
 * Isolar o acesso ao banco aqui traz duas vantagens:
 * - O ViewModel não conhece detalhes do Room, só fala com o Repository;
 * - Fica fácil testar o ViewModel isoladamente no futuro, passando um ObraDao
 *   (ou Repository) fake/mockado, sem precisar de um banco de verdade.
 */
class ObraRepository(private val obraDao: ObraDao) {

    fun listarTodas(): Flow<List<Obra>> = obraDao.listarTodas()

    fun listarPorStatus(status: String): Flow<List<Obra>> = obraDao.listarPorStatus(status)

    fun buscarPorNome(termo: String): Flow<List<Obra>> = obraDao.buscarPorNome(termo)

    suspend fun buscarPorId(idObra: Long): Obra? = obraDao.buscarPorId(idObra)

    suspend fun inserir(obra: Obra): Long = obraDao.inserir(obra)

    suspend fun editar(obra: Obra) = obraDao.editar(obra)

    suspend fun excluir(obra: Obra) = obraDao.excluir(obra)
}
