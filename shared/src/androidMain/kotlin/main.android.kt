import androidx.compose.runtime.Composable
import com.example.ui.App

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()
