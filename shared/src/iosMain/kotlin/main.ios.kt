import androidx.compose.ui.window.ComposeUIViewController
import com.example.data.local.database.DatabaseManager
import com.example.ui.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController {
    initDatabase()
    App()
}

fun initDatabase() {
    GlobalScope.launch {
        DatabaseManager.initDatabase()
    }
}