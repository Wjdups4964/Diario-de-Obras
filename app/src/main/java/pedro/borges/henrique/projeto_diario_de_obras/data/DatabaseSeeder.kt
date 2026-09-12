package pedro.borges.henrique.projeto_diario_de_obras.data

import kotlinx.coroutines.flow.first
object DatabaseSeeder {

    /** Só popula se o banco ainda estiver vazio, pra não duplicar dados a cada execução. */
    suspend fun popularSeNecessario(db: AppDatabase) {
        val obrasExistentes = db.obraDao().listarTodas().first()
        if (obrasExistentes.isNotEmpty()) return
        popularForcado(db)
    }

    /** Popula o banco ignorando se já existem dados (use com cuidado; pode duplicar). */
    suspend fun popularForcado(db: AppDatabase) {
        val umDiaMillis = 24L * 60 * 60 * 1000
        val hoje = System.currentTimeMillis()

        // --- Usuários ---
        val idJoseVictor = db.usuarioDao().inserir(Usuario(nome = "José Victor", cargo = "Engenheiro Civil"))
        val idPedro = db.usuarioDao().inserir(Usuario(nome = "Pedro Henrique", cargo = "Mestre de Obras"))
        val idRandson = db.usuarioDao().inserir(Usuario(nome = "Randson Bredley", cargo = "Arquiteto"))

        // --- Obras (variando status, pra testar filtros) ---
        val idObra1 = db.obraDao().inserir(
            Obra(
                nome = "Residencial Bela Vista",
                endereco = "Rua das Flores, 123 - Belo Jardim/PE",
                dataInicio = hoje - (60 * umDiaMillis),
                dataPrevisaoTermino = hoje + (120 * umDiaMillis),
                area = 850.0,
                status = "Em andamento",
                idResponsavel = idJoseVictor
            )
        )
        val idObra2 = db.obraDao().inserir(
            Obra(
                nome = "Galpão Industrial Zona Sul",
                endereco = "Av. Central, 500 - Belo Jardim/PE",
                dataInicio = hoje - (200 * umDiaMillis),
                dataPrevisaoTermino = hoje - (10 * umDiaMillis),
                area = 2200.0,
                status = "Concluída",
                idResponsavel = idPedro
            )
        )
        val idObra3 = db.obraDao().inserir(
            Obra(
                nome = "Reforma Escola Municipal",
                endereco = "Rua da Escola, 10 - Belo Jardim/PE",
                dataInicio = hoje - (15 * umDiaMillis),
                dataPrevisaoTermino = hoje + (45 * umDiaMillis),
                area = 430.0,
                status = "Paralisada",
                idResponsavel = idRandson
            )
        )
        db.obraDao().inserir(
            Obra(
                nome = "Condomínio Jardim das Palmeiras",
                endereco = "Estrada do Contorno, km 4 - Belo Jardim/PE",
                dataInicio = hoje - (5 * umDiaMillis),
                dataPrevisaoTermino = hoje + (300 * umDiaMillis),
                area = 5400.0,
                status = "Em andamento",
                idResponsavel = idJoseVictor
            )
        )

        // --- Funcionários ---
        val idFunc1 = db.funcionarioDao().inserir(Funcionario(nome = "João Silva", funcao = "Pedreiro"))
        val idFunc2 = db.funcionarioDao().inserir(Funcionario(nome = "Maria Souza", funcao = "Servente"))
        val idFunc3 = db.funcionarioDao().inserir(Funcionario(nome = "Carlos Lima", funcao = "Eletricista"))
        val idFunc4 = db.funcionarioDao().inserir(Funcionario(nome = "Ana Pereira", funcao = "Encanadora"))
        db.funcionarioDao().inserir(Funcionario(nome = "Paulo Costa", funcao = "Ajudante Geral"))

        // --- Registros diários (só na Obra 1 e Obra 3, pra simular obras "vivas") ---
        val idRegistro1 = db.registroDiarioDao().inserir(
            RegistroDiario(
                idObra = idObra1,
                idUsuario = idJoseVictor,
                data = hoje - (2 * umDiaMillis),
                turno = "Manhã",
                observacoes = "Fundação concluída, iniciando alvenaria do térreo."
            )
        )
        val idRegistro2 = db.registroDiarioDao().inserir(
            RegistroDiario(
                idObra = idObra1,
                idUsuario = idPedro,
                data = hoje - (1 * umDiaMillis),
                turno = "Tarde",
                observacoes = "Chuva no início da tarde, trabalho reduzido."
            )
        )
        val idRegistro3 = db.registroDiarioDao().inserir(
            RegistroDiario(
                idObra = idObra3,
                idUsuario = idRandson,
                data = hoje - (10 * umDiaMillis),
                turno = "Manhã",
                observacoes = "Obra paralisada aguardando liberação da prefeitura."
            )
        )

        // --- Atividades ---
        db.atividadeDao().inserir(Atividade(idRegistro = idRegistro1, descricao = "Levantamento de alvenaria - bloco A", status = "Em andamento"))
        db.atividadeDao().inserir(Atividade(idRegistro = idRegistro1, descricao = "Instalação elétrica - térreo", status = "Pendente"))
        db.atividadeDao().inserir(Atividade(idRegistro = idRegistro2, descricao = "Concretagem da laje", status = "Concluída"))
        db.atividadeDao().inserir(Atividade(idRegistro = idRegistro3, descricao = "Vistoria estrutural", status = "Pendente"))

        // --- Fotos ---
        db.fotoDao().inserir(Foto(idRegistro = idRegistro1, caminhoArquivo = "/storage/emulated/0/DiarioObra/fotos/obra1_dia1.jpg", legenda = "Alvenaria do bloco A", data = hoje - (2 * umDiaMillis)))
        db.fotoDao().inserir(Foto(idRegistro = idRegistro2, caminhoArquivo = "/storage/emulated/0/DiarioObra/fotos/obra1_dia2.jpg", legenda = "Laje concretada", data = hoje - (1 * umDiaMillis)))

        // --- Funcionários presentes em cada registro (N:N) ---
        db.registroDiarioFuncionarioDao().editarFuncionariosDoRegistro(idRegistro1, listOf(idFunc1, idFunc2, idFunc3))
        db.registroDiarioFuncionarioDao().editarFuncionariosDoRegistro(idRegistro2, listOf(idFunc1, idFunc4))
        db.registroDiarioFuncionarioDao().editarFuncionariosDoRegistro(idRegistro3, listOf(idFunc2))
    }
}
