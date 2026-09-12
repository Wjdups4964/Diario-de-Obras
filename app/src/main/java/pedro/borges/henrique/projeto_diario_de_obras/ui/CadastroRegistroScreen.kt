package pedro.borges.henrique.projeto_diario_de_obras.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pedro.borges.henrique.projeto_diario_de_obras.data.AppDatabase
import pedro.borges.henrique.projeto_diario_de_obras.repository.RegistroRepository
import pedro.borges.henrique.projeto_diario_de_obras.viewmodel.RegistroViewModel

private val TURNOS = listOf("Manhã", "Tarde", "Noite")
private val CONDICOES_CLIMA = listOf("Ensolarado", "Nublado", "Chuvoso", "Muito quente", "Ventania")

/**
 * Tela "Cadastro de Registro Diário". Campos: data (usa a data de hoje, simplificado —
 * o Randson pode trocar por um DatePicker depois), turno, clima, nº de trabalhadores
 * e observações (equivalente ao campo "descrição" do wireframe).
 *
 * [idUsuarioAtual] é temporário: como o app não tem login, precisa vir de algum lugar
 * (ex.: um usuário fixo "usuário padrão" cadastrado no seed, ou um seletor simples).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroRegistroScreen(
    idObra: Long,
    idUsuarioAtual: Long,
    onVoltar: () -> Unit,
    onSalvo: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: RegistroViewModel = viewModel(
        factory = RegistroViewModel.factory(
            RegistroRepository(AppDatabase.getInstance(context).registroDiarioDao()),
            idObra
        )
    )

    var turno by remember { mutableStateOf(TURNOS.first()) }
    var turnoExpandido by remember { mutableStateOf(false) }

    var clima by remember { mutableStateOf(CONDICOES_CLIMA.first()) }
    var climaExpandido by remember { mutableStateOf(false) }

    var numeroTrabalhadoresTexto by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    val mensagemErro by viewModel.mensagemErro.collectAsState()
    val salvouComSucesso by viewModel.salvandoComSucesso.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensagemErro) {
        mensagemErro?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparErro()
        }
    }

    LaunchedEffect(salvouComSucesso) {
        if (salvouComSucesso) {
            viewModel.resetarSucesso()
            onSalvo()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Registro Diário") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Data: hoje") // simplificado; trocar por DatePicker quando a navegação estiver pronta

            ExposedDropdownMenuBox(
                expanded = turnoExpandido,
                onExpandedChange = { turnoExpandido = it }
            ) {
                OutlinedTextField(
                    value = turno,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Turno") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = turnoExpandido) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                androidx.compose.material3.ExposedDropdownMenu(
                    expanded = turnoExpandido,
                    onDismissRequest = { turnoExpandido = false }
                ) {
                    TURNOS.forEach { opcao ->
                        DropdownMenuItem(
                            text = { Text(opcao) },
                            onClick = {
                                turno = opcao
                                turnoExpandido = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = climaExpandido,
                onExpandedChange = { climaExpandido = it }
            ) {
                OutlinedTextField(
                    value = clima,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Clima") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = climaExpandido) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                androidx.compose.material3.ExposedDropdownMenu(
                    expanded = climaExpandido,
                    onDismissRequest = { climaExpandido = false }
                ) {
                    CONDICOES_CLIMA.forEach { opcao ->
                        DropdownMenuItem(
                            text = { Text(opcao) },
                            onClick = {
                                clima = opcao
                                climaExpandido = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = numeroTrabalhadoresTexto,
                onValueChange = { novoValor -> if (novoValor.all { it.isDigit() }) numeroTrabalhadoresTexto = novoValor },
                label = { Text("Nº de trabalhadores") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = observacoes,
                onValueChange = { observacoes = it },
                label = { Text("Descrição / observações do dia") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                minLines = 3
            )

            Button(
                onClick = {
                    viewModel.inserir(
                        idUsuario = idUsuarioAtual,
                        data = System.currentTimeMillis(),
                        turno = turno,
                        clima = clima,
                        numeroTrabalhadores = numeroTrabalhadoresTexto.toIntOrNull(),
                        observacoes = observacoes
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar registro")
            }
        }
    }
}
