package pedro.borges.henrique.projeto_diario_de_obras.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pedro.borges.henrique.projeto_diario_de_obras.data.Obra
import pedro.borges.henrique.projeto_diario_de_obras.repository.ObraRepository

/**
 * ViewModel da tela de Lista/Cadastro de Obra.
 *
 * Expõe [obras] como StateFlow (pronto pra Compose observar com collectAsStateWithLifecycle)
 * e os métodos de inserir/editar/excluir, cada um com uma validação básica de formulário.
 */
class ObraViewModel(private val repository: ObraRepository) : ViewModel() {

    /** Lista reativa de obras: qualquer inserção/edição/exclusão no banco atualiza a tela sozinha. */
    val obras: StateFlow<List<Obra>> = repository.listarTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _mensagemErro = MutableStateFlow<String?>(null)
    /** Mensagem de validação/erro pra tela exibir (ex.: num Snackbar). Null = sem erro. */
    val mensagemErro: StateFlow<String?> = _mensagemErro

    fun inserir(
        nome: String,
        endereco: String,
        dataInicio: Long,
        dataPrevisaoTermino: Long? = null,
        area: Double? = null,
        status: String,
        idResponsavel: Long? = null
    ) {
        if (!validarCampos(nome, endereco)) return

        viewModelScope.launch {
            repository.inserir(
                Obra(
                    nome = nome.trim(),
                    endereco = endereco.trim(),
                    dataInicio = dataInicio,
                    dataPrevisaoTermino = dataPrevisaoTermino,
                    area = area,
                    status = status,
                    idResponsavel = idResponsavel
                )
            )
        }
    }

    fun editar(obra: Obra) {
        if (!validarCampos(obra.nome, obra.endereco)) return
        viewModelScope.launch { repository.editar(obra) }
    }

    fun excluir(obra: Obra) {
        viewModelScope.launch { repository.excluir(obra) }
    }

    suspend fun buscarPorId(idObra: Long): Obra? = repository.buscarPorId(idObra)

    fun limparErro() {
        _mensagemErro.value = null
    }

    private fun validarCampos(nome: String, endereco: String): Boolean {
        return when {
            nome.isBlank() -> {
                _mensagemErro.value = "O nome da obra é obrigatório."
                false
            }
            endereco.isBlank() -> {
                _mensagemErro.value = "O endereço da obra é obrigatório."
                false
            }
            else -> true
        }
    }

    companion object {
        /**
         * Factory manual (o projeto ainda não usa Hilt/Dagger). Uso típico numa Composable:
         *
         * ```
         * val viewModel: ObraViewModel = viewModel(
         *     factory = ObraViewModel.factory(
         *         ObraRepository(AppDatabase.getInstance(LocalContext.current).obraDao())
         *     )
         * )
         * ```
         */
        fun factory(repository: ObraRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ObraViewModel(repository) as T
                }
            }
    }
}
