package com.yikwing.ykquickdev.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BaseVerificationCodeTextField(
    modifier: Modifier = Modifier,
    codeLength: Int = 4,
    onVerify: (String) -> Unit = {},
    codeBox: @Composable RowScope.(codeLength: Int, index: Int, code: String) -> Unit
) {
    //  存储文本输出的值
    var text by remember { mutableStateOf("") }

    // 管理当前获得焦点的文本值
    val focusManager = LocalFocusManager.current

    //  用于请求焦点以显示软键盘
    val focusRequester = remember { FocusRequester() }

    //  控制软键盘显示与隐藏
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = text,
        singleLine = true,
        onValueChange = { newText ->
            if (newText.length <= codeLength && newText.all { it.isDigit() }) {
                text = newText

                if (newText.length == codeLength) {
                    onVerify(newText)
                    focusManager.clearFocus()
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .padding(horizontal = 26.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    keyboardController?.show()
                }
            }
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            for (i in 0 until codeLength) {
                codeBox(codeLength, i, text)
            }
        }
    }
}

private enum class CodeState {
    ENTERED,
    INPUTTING,
    PENDING
}

@Composable
fun VerificationCodeTextField(
    modifier: Modifier = Modifier,
    onVerify: (String) -> Unit = {}
) {
    BaseVerificationCodeTextField(
        onVerify = onVerify
    ) { _, index, code ->
        val isHasCode = index < code.length
        val fontSize = 24.sp

        val codeState = if (isHasCode) {
            CodeState.ENTERED
        } else if (index == code.length) {
            CodeState.INPUTTING
        } else {
            CodeState.PENDING
        }

        val cardColor = when (codeState) {
            CodeState.ENTERED -> Color(0xFF466EFF)
            CodeState.INPUTTING -> Color.White
            CodeState.PENDING -> Color(0xFFF5F5F5)
        }

        val elevation = when (codeState) {
            CodeState.ENTERED -> 3.dp
            CodeState.INPUTTING -> 6.dp
            CodeState.PENDING -> 0.dp
        }

        val textColor = when (codeState) {
            CodeState.ENTERED -> Color.White
            CodeState.INPUTTING -> Color.Gray
            CodeState.PENDING -> Color.Gray
        }

        val blinkInterval = 1000L
        var isVisible by remember { mutableStateOf(true) }
        LaunchedEffect(blinkInterval) {
            while (true) {
                isVisible = !isVisible
                delay(blinkInterval)
            }
        }

        key(elevation) {
            Card(
                modifier = Modifier.size(46.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = elevation
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (codeState) {
                        CodeState.ENTERED -> {
                            Text(
                                code[index].toString(),
                                style = TextStyle(
                                    fontSize = fontSize,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        CodeState.INPUTTING -> {
                            if (isVisible) {
                                Text(
                                    "-",
                                    style = TextStyle(
                                        fontSize = fontSize,
                                        color = textColor,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }

                        CodeState.PENDING -> {}
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun VerificationCodeTextFieldPreview() {
    VerificationCodeTextField {
        println("======= $it")
    }
}
