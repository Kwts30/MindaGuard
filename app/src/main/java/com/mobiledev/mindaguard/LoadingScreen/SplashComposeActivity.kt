package com.mobiledev.mindaguard.loadingscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobiledev.mindaguard.MainActivity
import com.mobiledev.mindaguard.R
import com.mobiledev.mindaguard.ui.theme.MindaGuardTheme
import kotlinx.coroutines.delay

/**
 * Compose-based splash activity (single-activity approach).
 *
 * NOTE: package is com.mobiledev.mindaguard.loadingscreen (lowercase).
 * Make sure your folder name matches (refactor -> rename folder to 'loadingscreen').
 */
class SplashComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash backport BEFORE super.onCreate
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MindaGuardTheme {
                // MindaGuardSplash will call onTimeout after a short delay (runtime)
                MindaGuardSplash(onTimeout = {
                    startActivity(Intent(this@SplashComposeActivity, MainActivity::class.java))
                    finish()
                })
            }
        }
    }
}

@Composable
fun MindaGuardSplash(onTimeout: () -> Unit) {
    val isPreview = LocalInspectionMode.current

    // Render visual only
    MindaGuardSplashContent()

    // Only trigger navigation when not in Preview
    if (!isPreview) {
        LaunchedEffect(Unit) {
            // Short delay so the visual renders; adjust if you need longer
            delay(10000)
            onTimeout()
        }
    }
}

@Composable
fun MindaGuardSplashContent(
    logoFraction: Float = 0.5f,
    subtextMaxHeightDp: Int = 80
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image (optional). If you don't have bk drawable, remove this block.
        if (remember { true }) {
            Image(
                painter = painterResource(id = R.drawable.bk),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Top-left small logo
        Image(
            painter = painterResource(id = R.drawable.mmcm_logo),
            contentDescription = stringResource(id = R.string.splash_logo_description),
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.TopCenter)
                .padding(top = 50.dp),
            contentScale = ContentScale.Fit
        )

        // Center column: main logo + subtext
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_only),
                contentDescription = stringResource(id = R.string.splash_logo_description),
                modifier = Modifier
                    .fillMaxWidth(logoFraction)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.icon_text),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = subtextMaxHeightDp.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

/* Previews — open the file and use the Preview pane in Android Studio */
@Preview(name = "Phone - Default", showBackground = true)
@Composable
fun PreviewSplash_Default() {
    MindaGuardTheme { MindaGuardSplashContent() }
}

@Preview(name = "Tablet 7\"", showBackground = true, widthDp = 600, heightDp = 960)
@Composable
fun PreviewSplash_Tablet() {
    MindaGuardTheme { MindaGuardSplashContent(logoFraction = 0.4f, subtextMaxHeightDp = 100) }
}