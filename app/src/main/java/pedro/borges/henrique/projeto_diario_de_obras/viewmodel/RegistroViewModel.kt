package pedro.borges.henrique.projeto_diario_de_obras.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pedro.borges.henrique.projeto_diario_de_obras.data.RegistroDiario
import pedro.borges.henrique.projeto_diario_de_obras.repository.RegistroRepository

/**
 * ViewModel da tela de Lista/Cadastro de Registro Diário de uma Obra específica.
 * Recebe [idObra] porque a tela sempre trabalha no contexto de "os registros desta obra".
 */
class RegistroViewModel(
    private val repository: RegistroRepository,
    private val idObra: Long
) : ViewModel() {

    /** Registros da obra atual, do mais recente para o mais antigo — atualiza sozinho a cada mudança no banco. */
    val registros: StateFlow<List<RegistroDiario>> = repository.listarPorObra(idObra)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    private val _salvandoComSucesso = MutableStateFlow(false)
    /** true por um instante após salvar com sucesso; a tela pode observar isso para voltar/limpar o formulário. */
    val salvandoComSucesso: StateFlow<Boolean> = _salvandoComSucesso

    fun inserir(
        idUsuario: Long,
        data: Long,
        turno: String,
        clima: String? = null,
        numeroTrabalhadores: Int? = null,
        observacoes: String? = null
    ) {
        if (turno.isBlank()) {
            _mensagemErro.value = "Selecione o turno."
            return
        }
        if (numeroTrabalhadores != null && numeroTrabalhadores < 0) {
            _mensagemErro.value = "Número de trabalhadores não pode ser negativo."
            return
        }

        viewModelScope.launch {
            repository.inserir(
                RegistroDiario(
                    idObra = idObra,
                    idUsuario = idUsuario,
                    data = data,
                    turno = turno.trim(),
                    clima = clima?.trim()?.ifBlank { null },
                    numeroTrabalhadores = numeroTrabalhadores,
                    observacoes = observacoes?.trim()?.ifBlank { null }
                )
            )
            _salvandoComSucesso.value = true
        }
    }

    fun excluir(registro: RegistroDiario) {
        viewModelScope.launch { repository.excluir(registro) }
    }

    fun limparErro() {
        _mensagemErro.value = null
    }

    fun resetarSucesso() {
        _salvandoComSucesso.value = false
    }

    companion object {
        /**
         * Uso típico numa Composable:
         * ```
         * val viewModel: RegistroViewModel = viewModel(
         *     factory = RegistroViewModel.factory(
         *         RegistroRepository(AppDatabase.getInstance(LocalContext.current).registroDiarioDao()),
         *         idObra = obraId
         *     )
         * )
         * ```
         */
        fun factory(repository: RegistroRepository, idObra: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegistroViewModel(repository, idObra) as T
                }
            }
    }
}
