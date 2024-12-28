package com.trent.simplepoketch.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.trent.simplepoketch.R
import com.trent.simplepoketch.presentation.theme.SimplePoketchTheme
import com.trent.simplepoketch.presentation.theme.getDrawableResource
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Define the Pokemon Font Family
val PokemonFont = FontFamily(
    Font(R.font.dppt) // Ensure the name matches your font file in res/font
)

// Define the Text Style
val PokemonTextStyle = TextStyle(
    fontFamily = PokemonFont,
    fontSize = 14.sp, // Adjust font size as needed
)

class MainActivity : ComponentActivity() {
    private var stepCount = mutableIntStateOf(0)
    private var heartRate = mutableIntStateOf(-1)
    private var isAmbientMode = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request permissions
        checkAndRequestPermissions()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Step counter listener
        val stepListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                stepCount.intValue = event.values[0].toInt()
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Register sensor listener
        sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_UI)

        // Ambient mode observer
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> isAmbientMode.value = false // Exit AOD
                    Lifecycle.Event.ON_PAUSE -> isAmbientMode.value = true // Enter AOD
                    else -> {}
                }
            }
        })

        // Set the UI content
        setContent {
            var themeIndex by remember { mutableIntStateOf(1) }

            SimplePoketchTheme(themeIndex = themeIndex) {
                SimplePoketchScreen(
                    currentThemeIndex = themeIndex,
                    onThemeChange = { newTheme -> themeIndex = newTheme },
                    stepCount = stepCount.intValue,
                    heartRate = heartRate.intValue,
                    isAmbientMode = isAmbientMode.value
                )
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permission = android.Manifest.permission.BODY_SENSORS
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), 1001)
        }
    }
}



@Composable
fun SimplePoketchScreen(
    currentThemeIndex: Int,
    onThemeChange: (Int) -> Unit,
    stepCount: Int,
    heartRate: Int?,
    isAmbientMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isAmbientMode) Color.Black else MaterialTheme.colors.background)
    ) {
        if (isAmbientMode) {
            // Minimal Always-On Display (AOD) Content
            TimeDisplay(
                themeIndex = currentThemeIndex,
                useDebugFont = false
            )
            MinimalPikachuSprite(themeIndex = currentThemeIndex)
        } else {
            // Regular Content
            StyledDateDisplay(
                modifier = Modifier.align(Alignment.TopCenter)
            )
            HeartRateDisplay(
                heartRate = heartRate,
                themeIndex = currentThemeIndex,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-30).dp, y = 37.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimeDisplay(
                    themeIndex = currentThemeIndex,
                    useDebugFont = true
                )
            }
            SliderComponent(
                themeIndex = currentThemeIndex,
                onThemeChange = onThemeChange,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-30).dp, y = (-35).dp)
            )
            PikachuSprite(
                themeIndex = currentThemeIndex,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-27).dp)
            )
            StepCounterDisplay(
                stepCount = stepCount,
                themeIndex = currentThemeIndex,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-4).dp)
            )
        }
    }
}

@Composable
fun StyledDateDisplay(modifier: Modifier = Modifier) {
    val currentDate = remember { mutableStateOf(Pair("", "")) }

    // Update the date strings dynamically
    LaunchedEffect(Unit) {
        val dayFormatter = DateTimeFormatter.ofPattern("EEEE") // Day name (e.g., Tuesday)
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM") // Month name (e.g., December)
        while (true) {
            val now = LocalDate.now()
            val dayName = now.format(dayFormatter)
            val fullDate = "${now.format(monthFormatter)} ${addOrdinalSuffix(now.dayOfMonth)}, ${now.year}"
            currentDate.value = Pair(dayName, fullDate)
            delay(86_400_000L) // Update every 24 hours
        }
    }

    // Display the date (day on top, full date below)
    Column(
        modifier = modifier.padding(top = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentDate.value.first, // Day name
            style = PokemonTextStyle.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary // Use dynamic theme color
            )
        )
        Text(
            text = currentDate.value.second, // Full date with ordinal suffix
            style = PokemonTextStyle.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colors.primary // Use dynamic theme color
            )
        )
    }
}

// Utility function to add ordinal suffix
fun addOrdinalSuffix(day: Int): String {
    return when {
        day in 11..13 -> "$day" + "th" // Special case for 11th, 12th, 13th
        day % 10 == 1 -> "$day" + "st"
        day % 10 == 2 -> "$day" + "nd"
        day % 10 == 3 -> "$day" + "rd"
        else -> "$day" + "th"
    }
}


@Composable
fun TimeDisplay(themeIndex: Int, useDebugFont: Boolean) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime = LocalTime.now()
        }
    }

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")
    val formattedTime = currentTime.format(timeFormatter)

    val fontTable = if (useDebugFont) {
        DBitmapFont.getCharToDrawableMap(themeIndex, LocalContext.current)
    } else {
        BitmapFont.getCharToDrawableMap(themeIndex, LocalContext.current)
    }

    BitmapFontText(text = formattedTime, fontTable = fontTable)
}



@Composable
fun BitmapFontText(text: String, fontTable: Map<Char, Int>) {
    Row(
        horizontalArrangement = Arrangement.Center, // Center horizontally
        verticalAlignment = Alignment.CenterVertically, // Center within the row
        modifier = Modifier
            .fillMaxWidth() // Fill the width
    ) {
        for (char in text) {
            val drawableId = fontTable[char]
            if (drawableId != null) {
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = char.toString(),
                    modifier = Modifier.wrapContentSize() // Use intrinsic size
                )
            }
        }
    }
}


@Composable
fun PikachuSprite(themeIndex: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val pikachuDrawable = getDrawableResource(themeIndex, "pikachu", context)

    Image(
        painter = painterResource(id = pikachuDrawable),
        contentDescription = "Pikachu",
        modifier = modifier
    )
}


@Composable
fun SliderComponent(
    themeIndex: Int,
    onThemeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val kecleonDrawable = getDrawableResource(themeIndex, "keckleon", context)

    // Slider UI
    Image(
        painter = painterResource(id = kecleonDrawable),
        contentDescription = "Kecleon Slider",
        modifier = modifier
            .clickable {
                // Cycle through themes (1 to 8)
                val newThemeIndex = if (themeIndex < 8) themeIndex + 1 else 1
                onThemeChange(newThemeIndex)
            }
    )
}

@Composable
fun StepCounterDisplay(
    stepCount: Int, // Step count value
    themeIndex: Int, // Current theme index
    modifier: Modifier = Modifier // Modifier passed to support layout adjustments
) {
    val context = LocalContext.current
    val stepBorderDrawable = getDrawableResource(themeIndex, "step", context)
    val fontTable = StepBitmapFont.getCharToDrawableMap(themeIndex, context)

    // Format step count as string
    val stepCountString = stepCount.toString()

    // Layout for border and step count
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier // Apply modifier passed from parent
    ) {
        // Step border background
        Image(
            painter = painterResource(id = stepBorderDrawable),
            contentDescription = "Step Border",
            modifier = Modifier.wrapContentSize()
        )

        // Step count text using bitmap font
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (char in stepCountString) {
                val drawableId = fontTable[char]
                if (drawableId != null) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = char.toString(),
                        modifier = Modifier.wrapContentSize()
                    )
                }
            }
        }
    }
}

@Composable
fun HeartRateDisplay(
    heartRate: Int?, // Accept null for no heart rate
    themeIndex: Int, // Current theme index
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Determine the correct drawable based on heart rate value
    val heartBorderDrawable = if (heartRate != null && heartRate > 0) {
        getDrawableResource(themeIndex, "heart", context) // Normal border
    } else {
        getDrawableResource(themeIndex, "heart2", context) // Sad border
    }

    // Get the bitmap font for the current theme
    val fontTable = StepBitmapFont.getCharToDrawableMap(themeIndex, context)

    // Display the heart rate or nothing if heart rate is invalid
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Heart border background
        Image(
            painter = painterResource(id = heartBorderDrawable),
            contentDescription = "Heart Border",
            modifier = Modifier.wrapContentSize()
        )

        // Heart rate value using bitmap font, only if it's valid
        if (heartRate != null && heartRate > 0) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (char in heartRate.toString()) {
                    val drawableId = fontTable[char]
                    if (drawableId != null) {
                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = char.toString(),
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MinimalPikachuSprite(themeIndex: Int) {
    val context = LocalContext.current
    val minimalDrawable = getDrawableResource(themeIndex, "aod_pikachu", context)

    Image(
        painter = painterResource(id = minimalDrawable),
        contentDescription = "Minimal Pikachu Sprite",
        modifier = Modifier.size(48.dp) // Adjust size as needed
    )
}
