package dev.xero.paper.presentation.notes.edit_notes

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.xero.paper.domain.model.NoteDBEntity
import dev.xero.paper.presentation.notes.edit_notes.edit_note_components.BackButton
import dev.xero.paper.presentation.notes.edit_notes.edit_note_components.InputBox
import dev.xero.paper.presentation.notes.edit_notes.edit_note_components.SaveNoteButton
import dev.xero.paper.presentation.notes.edit_notes.utils.InputType
import dev.xero.paper.presentation.ui.theme.Primary
import dev.xero.paper.presentation.ui.theme.Secondary
import dev.xero.paper.presentation.ui.theme.SurfaceDark

@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: EditNoteScreenViewModel,
    noteContentState: NoteDBEntity?,
    onBackButtonClicked: () -> Unit,
    onSaveNoteButtonClicked: () -> Unit,
    onSetContent: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val title = viewModel.title
    val content = viewModel.content
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current // 获取当前上下文

    LaunchedEffect(Unit) {
        if (noteContentState != null) {
            viewModel.getNote(noteContentState.id)
            onSetContent()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        if (isDarkTheme) SurfaceDark else Secondary
                    )
            ) {
                BackButton(
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                    onButtonClick = onBackButtonClicked
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (viewModel.currentNote != null) {
                        viewModel.updateNote()
                    } else {
                        viewModel.addNote()
                    }
                    
                    // 调用
                    cufa()
                    val outtext = processText("你好") // 你看看这里附近有问题没，其他地方不用看
                    Toast.makeText(context, outtext, Toast.LENGTH_SHORT).show() // 使用正确的context
                    onSaveNoteButtonClicked()
                },
                backgroundColor = Primary,
                shape = RoundedCornerShape(4.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                SaveNoteButton(isDarkTheme = isDarkTheme)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            InputBox(
                inputType = InputType.Title,
                isDarkTheme = isDarkTheme,
                value = title,
                onValueChange = viewModel::updateTitle,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                focusRequester = focusRequester
            )

            Canvas(
                modifier = Modifier
                    .size(width = 100.dp, height = 2.dp)
                    .padding(start = 12.dp)
            ) {
                drawRoundRect(color = Primary, size = size)
            }

            InputBox(
                inputType = InputType.Content,
                isDarkTheme = isDarkTheme,
                value = content,
                onValueChange = viewModel::updateContent,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }
    }
}
// 输入一个字符串，返回处理后的字符串
fun processText(input: String): String {
    return "结果还行还行：$input"
}
// 定义一个名为 触发 的函数
fun cufa() {
    println("Hello World!")
}