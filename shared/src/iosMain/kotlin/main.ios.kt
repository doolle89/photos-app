import androidx.compose.ui.window.ComposeUIViewController
import com.example.ui.App

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }