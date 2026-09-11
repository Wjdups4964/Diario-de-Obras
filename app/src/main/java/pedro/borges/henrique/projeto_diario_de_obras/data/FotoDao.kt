package pedro.borges.henrique.projeto_diario_de_obras.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoDao {

    @Insert
    suspend fun inserir(foto: Foto): Long

    @Update
    suspend fun editar(foto: Foto)

    @Delete
    suspend fun excluir(foto: Foto)

    @Query("SELECT * FROM foto WHERE idFoto = :idFoto")
    suspend fun buscarPorId(idFoto: Long): Foto?

    @Query("SELECT * FROM foto WHERE idRegistro = :idRegistro ORDER BY data DESC")
    fun listarPorRegistro(idRegistro: Long): Flow<List<Foto>>
}
