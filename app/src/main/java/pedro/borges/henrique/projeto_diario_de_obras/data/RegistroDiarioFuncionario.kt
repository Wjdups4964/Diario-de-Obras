package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Registro_Diario_Conta_Funcionario(id_registro FK, id_funcionario FK) — N:N
 *
 * Tabela associativa: quais funcionários estavam presentes em determinado registro
 * diário. Chave primária composta (idRegistro, idFuncionario).
 *
 * Excluir o Registro ou o Funcionário remove a associação (CASCADE), mas nunca o
 * "outro lado" da relação.
 */
@Entity(
    tableName = "registro_diario_conta_funcionario",
    primaryKeys = ["idRegistro", "idFuncionario"],
    foreignKeys = [
        ForeignKey(
            entity = RegistroDiario::class,
            parentColumns = ["idRegistro"],
            childColumns = ["idRegistro"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Funcionario::class,
            parentColumns = ["idFuncionario"],
            childColumns = ["idFuncionario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idRegistro"), Index("idFuncionario")]
)
data class RegistroDiarioFuncionario(
    val idRegistro: Long,
    val idFuncionario: Long
)
