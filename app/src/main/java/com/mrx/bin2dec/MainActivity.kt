package com.mrx.bin2dec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrx.bin2dec.ui.theme.Bin2DecTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bin2DecTheme {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val binaryValue = rememberSaveable { mutableStateOf("") }
    val decimalValue = rememberSaveable { mutableIntStateOf(0) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Convert Binary to Decimal") })
        },
        bottomBar = {
            BottomAppBar(Modifier.imePadding()) {
                Box(
                    Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .weight(1f)
                ) {
                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(decimalValue.intValue.toString()))
                    }, Modifier.fillMaxWidth()) {
                        Text(text = "Copy Value")
                    }
                }
                Box(
                    Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .weight(1f)
                ) {
                    Button(
                        onClick = { decimalValue.intValue = bin2dec(binaryValue.value) },
                        Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Convert")
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${decimalValue.intValue}", fontSize = 48.sp)
            }
            Box(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = binaryValue.value,
                    onValueChange = { newText ->
                        if (newText.all { it.isDigit() } && newText.all { it == '0' || it == '1' }) {
                            binaryValue.value = newText
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Binary")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Bin2DecTheme {
        HomeScreen()
    }
}

fun bin2dec(bin: String): Int {
    var dec = 0
    var power = 0

    for (i in bin.length - 1 downTo 0) {
        val digit = bin[i].code - '0'.code
        dec += digit * Math.pow(2.0, power.toDouble()).toInt()
        power++
    }

    return dec
}