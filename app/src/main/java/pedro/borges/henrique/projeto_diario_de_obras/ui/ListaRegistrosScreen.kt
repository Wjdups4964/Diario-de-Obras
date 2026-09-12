package pedro.borges.henrique.projeto_diario_de_obras.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pedro.borges.henrique.projeto_diario_de_obras.data.AppDatabase
import pedro.borges.henrique.projeto_diario_de_obras.data.RegistroDiario
import pedro.borges.henrique.projeto_diario_de_obras.repository.RegistroRepository
import pedro.borges.henrique.projeto_diario_de_obras.viewmodel.RegistroViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Tela "Lista de Registros da Obra". Mostra os registros diários de uma obra específica,
 * do mais recente para o mais antigo, com botão flutuante para cadastrar um novo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRegistrosScreen(
    idObra: Long,
    nomeObra: String,
    onVoltar: () -> Unit,
    onNovoRegistro: () -> Unit,
    onRegistroClick: (RegistroDiario) -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: RegistroViewModel = viewModel(
        factory = RegistroViewModel.factory(
            RegistroRepository(AppDatabase.getInstance(context).registroDiarioDao()),
            idObra
        )
    )
    val registros by viewModel.registros.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(nomeObra) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovoRegistro) {
                Icon(Icons.Default.Add, contentDescription = "Novo registro")
            }
        }
    ) { innerPadding ->
        if (registros.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nenhum registro diário ainda.")
                Text("Toque em + para lançar o primeiro registro desta obra.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(registros, key = { it.idRegistro }) { registro ->
                    RegistroCard(registro = registro, onClick = { onRegistroClick(registro) })
                }
            }
        }
    }
}

@Composable
private fun RegistroCard(registro: RegistroDiario, onClick: () -> Unit) {
    val formatoData = formatoDataPadrao()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = formatoData.format(Date(registro.data)),
                style = MaterialTheme.typography.titleMedium
            )
            Text("Turno: ${registro.turno}")
            registro.clima?.let { Text("Clima: $it") }
            registro.numeroTrabalhadores?.let { Text("Trabalhadores: $it") }
            registro.observacoes?.takeIf { it.isNotBlank() }?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

private fun formatoDataPadrao(): SimpleDateFormat =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
