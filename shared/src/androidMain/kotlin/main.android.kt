import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.data.local.database.DatabaseManager
import com.example.ui.App

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = MainApp()

@Composable fun MainApp() {
    DatabaseManager.initDatabase(LocalContext.current.applicationContext)
    App()
}
