package pedro.borges.henrique.projeto_diario_de_obras.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Usuario::class,
        Obra::class,
        RegistroDiario::class,
        Atividade::class,
        Foto::class,
        Funcionario::class,
        RegistroDiarioFuncionario::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun obraDao(): ObraDao
    abstract fun registroDiarioDao(): RegistroDiarioDao
    abstract fun atividadeDao(): AtividadeDao
    abstract fun fotoDao(): FotoDao
    abstract fun funcionarioDao(): FuncionarioDao
    abstract fun registroDiarioFuncionarioDao(): RegistroDiarioFuncionarioDao

    companion object {
        private const val DATABASE_NAME = "diario_de_obra.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retorna a instância única (singleton) do banco de dados.
         * Chame com o contexto da Application, ex.: AppDatabase.getInstance(applicationContext).
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}
