import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.example.data.local.database.DatabaseManager
import com.example.ui.App

actual fun getPlatformName(): String = "Desktop"

@Composable fun MainView() = MainApp()

@Composable fun MainApp() {
    DatabaseManager.initDatabase()
    App()
}

@Preview
@Composable
fun AppPreview() {
    App()
}