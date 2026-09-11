package pedro.borges.henrique.projeto_diario_de_obras.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Testes instrumentados do banco de dados (Room), rodando em memória para não afetar
 * o banco real do app. Precisa de um emulador ou dispositivo físico conectado.
 *
 * Cobre o critério "Fim do Dia 2" do planejamento: inserir e consultar uma Obra de teste,
 * além de validar a exclusão em cascata.
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var obraDao: ObraDao
    private lateinit var registroDao: RegistroDiarioDao
    private lateinit var atividadeDao: AtividadeDao
    private lateinit var fotoDao: FotoDao
    private lateinit var funcionarioDao: FuncionarioDao
    private lateinit var registroFuncionarioDao: RegistroDiarioFuncionarioDao

    @Before
    fun criarBanco() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // só para simplificar os testes
            .build()
        usuarioDao = db.usuarioDao()
        obraDao = db.obraDao()
        registroDao = db.registroDiarioDao()
        atividadeDao = db.atividadeDao()
        fotoDao = db.fotoDao()
        funcionarioDao = db.funcionarioDao()
        registroFuncionarioDao = db.registroDiarioFuncionarioDao()
    }

    @After
    @Throws(IOException::class)
    fun fecharBanco() {
        db.close()
    }

    @Test
    fun inserirEListarObra() = runBlocking {
        val idUsuario = usuarioDao.inserir(Usuario(nome = "José Victor", cargo = "Engenheiro Civil"))

        obraDao.inserir(
            Obra(
                nome = "Residencial Bela Vista",
                endereco = "Rua das Flores, 123",
                dataInicio = System.currentTimeMillis(),
                status = "Em andamento",
                idResponsavel = idUsuario
            )
        )

        val obras = obraDao.listarTodas().first()
        assertEquals(1, obras.size)
        assertEquals("Residencial Bela Vista", obras[0].nome)
    }

    @Test
    fun excluirObraDeveExcluirRegistrosAtividadesEFotosEmCascata() = runBlocking {
        val idUsuario = usuarioDao.inserir(Usuario(nome = "Pedro", cargo = "Engenheiro"))
        val idObra = obraDao.inserir(
            Obra(
                nome = "Galpão Industrial",
                endereco = "Av. Central, 500",
                dataInicio = System.currentTimeMillis(),
                status = "Em andamento",
                idResponsavel = idUsuario
            )
        )

        val idRegistro = registroDao.inserir(
            RegistroDiario(
                idObra = idObra,
                idUsuario = idUsuario,
                data = System.currentTimeMillis(),
                turno = "Manhã",
                observacoes = "Fundação concluída"
            )
        )
        atividadeDao.inserir(Atividade(idRegistro = idRegistro, descricao = "Concretagem", status = "Concluída"))
        fotoDao.inserir(Foto(idRegistro = idRegistro, caminhoArquivo = "/fake/path.jpg", data = System.currentTimeMillis()))

        assertEquals(1, registroDao.listarPorObra(idObra).first().size)
        assertEquals(1, atividadeDao.listarPorRegistro(idRegistro).first().size)
        assertEquals(1, fotoDao.listarPorRegistro(idRegistro).first().size)

        val obra = obraDao.buscarPorId(idObra)!!
        obraDao.excluir(obra)

        assertTrue(registroDao.listarPorObra(idObra).first().isEmpty())
        assertTrue(atividadeDao.listarPorRegistro(idRegistro).first().isEmpty())
        assertTrue(fotoDao.listarPorRegistro(idRegistro).first().isEmpty())
    }

    @Test
    fun associarFuncionariosAoRegistro() = runBlocking {
        val idUsuario = usuarioDao.inserir(Usuario(nome = "Randson", cargo = "Engenheiro"))
        val idObra = obraDao.inserir(
            Obra(
                nome = "Reforma Escola Municipal",
                endereco = "Rua da Escola, 10",
                dataInicio = System.currentTimeMillis(),
                status = "Em andamento",
                idResponsavel = idUsuario
            )
        )
        val idRegistro = registroDao.inserir(
            RegistroDiario(idObra = idObra, idUsuario = idUsuario, data = System.currentTimeMillis(), turno = "Tarde")
        )

        val idFunc1 = funcionarioDao.inserir(Funcionario(nome = "João Pedreiro", funcao = "Pedreiro"))
        val idFunc2 = funcionarioDao.inserir(Funcionario(nome = "Maria Servente", funcao = "Servente"))

        registroFuncionarioDao.editarFuncionariosDoRegistro(idRegistro, listOf(idFunc1, idFunc2))

        val funcionarios = registroFuncionarioDao.listarFuncionariosDoRegistro(idRegistro).first()
        assertEquals(2, funcionarios.size)
    }
}
