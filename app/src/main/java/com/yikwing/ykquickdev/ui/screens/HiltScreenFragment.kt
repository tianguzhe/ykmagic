package com.yikwing.ykquickdev.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.yikwing.logger.Logger
import com.yikwing.ykextension.app.PackageInfo
import com.yikwing.ykextension.app.getPackageInfo
import com.yikwing.ykextension.unSafeLazy

class HiltScreenFragment : Fragment() {

    private val packageInfo by unSafeLazy {
        requireContext().getPackageInfo("com.yktc.nutritiondiet")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp),
                        ) {
                            TopHeader(packageInfo = packageInfo)

                            val context = LocalContext.current

                            PackageInfoDes(
                                "versionCode:",
                                (packageInfo?.versionCode ?: 0).toString(),
                            )
                            PackageInfoDes(
                                "versionName:",
                                (packageInfo?.versionName ?: "").toString(),
                            )
                            PackageInfoDes(
                                "MD5:",
                                (packageInfo?.signMD5 ?: "").toString(),
                                onClick = {
                                    copyToClipboard(context, packageInfo?.signMD5, "MD5值已复制")
                                },
                            )
                            PackageInfoDes(
                                "SHA1:",
                                (packageInfo?.signSHA1 ?: "").toString(),
                                onClick = {
                                    copyToClipboard(context, packageInfo?.signSHA1, "SHA1值已复制")
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, copyStr: String?, tips: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("simple text", copyStr)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, tips, Toast.LENGTH_SHORT).show()
}

@Composable
fun TopHeader(packageInfo: PackageInfo?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = packageInfo?.appIcon,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(packageInfo?.appName ?: "")
        Spacer(modifier = Modifier.height(4.dp))
        Text(packageInfo?.appPackageName ?: "")
    }
    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun PackageInfoDes(
    title: String,
    info: String,
    onClick: () -> Unit = {
        Logger.d("$111=== $title")
    },
) {
    Column {
        Text(title, color = Color(0xFF999999))
        Text(info, color = Color(0xFF666666), modifier = Modifier.clickable { onClick() })
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview
@Composable
fun PackageInfoDesPreview() {
    PackageInfoDes("123", "456")
}
