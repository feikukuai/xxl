package dev.xero.paper.presentation.notes.edit_notes

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
    modifier: Modifier = Modifier, // 默认的 Modifier
    viewModel: EditNoteScreenViewModel, // 视图模型，用于管理笔记数据
    noteContentState: NoteDBEntity?, // 当前笔记的内容状态（可能为 null，表示新建笔记）
    onBackButtonClicked: () -> Unit, // 返回按钮点击回调
    onSaveNoteButtonClicked: () -> Unit, // 保存按钮点击回调
    onSetContent: () -> Unit // 设置内容回调
) {
    // 检查当前是否为深色模式
    val isDarkTheme = isSystemInDarkTheme()
    // 从 ViewModel 中获取笔记的标题和内容
    val title = viewModel.title
    val content = viewModel.content
    // 获取焦点管理器，用于控制输入框的焦点
    val focusManager = LocalFocusManager.current
    // 创建一个焦点请求器，用于请求输入框的焦点
    val focusRequester = remember { FocusRequester() }

    // 当屏幕加载时，如果 noteContentState 不为 null，则获取笔记内容
    LaunchedEffect(Unit) {
        if (noteContentState != null) {
            viewModel.getNote(noteContentState.id)
            onSetContent()
        }
    }

    // 使用 Scaffold 构建页面布局
    Scaffold(
        topBar = {
            // 顶部栏：包含返回按钮
            Row(
                modifier = modifier
                    .fillMaxWidth() // 占满整个宽度
                    .background(
                        when (isDarkTheme) {
                            true -> SurfaceDark // 深色模式背景
                            else -> Secondary // 浅色模式背景
                        }
                    )
            ) {
                // 返回按钮
                BackButton(
                    isDarkTheme = isDarkTheme, // 是否深色模式
                    modifier = Modifier.padding(
                        horizontal = 6.dp, // 水平内边距
                        vertical = 8.dp // 垂直内边距
                    ),
                    onButtonClick = onBackButtonClicked // 点击回调
                )
            }
        },
        floatingActionButton = {
            // 悬浮按钮：保存笔记
            FloatingActionButton(
                onClick = {
                    // 如果当前笔记存在，则更新笔记；否则，添加新笔记
                    if (viewModel.currentNote != null)
                        viewModel.updateNote()
                    else
                        viewModel.addNote()
                    // 触发保存按钮点击回调
                    onSaveNoteButtonClicked()
                },
                backgroundColor = Primary, // 按钮背景颜色
                shape = RoundedCornerShape(4.dp), // 按钮形状（圆角矩形）
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp, // 默认阴影
                    pressedElevation = 0.dp, // 按下时的阴影
                    focusedElevation = 0.dp, // 聚焦时的阴影
                    hoveredElevation = 0.dp // 悬停时的阴影
                )
            ) {
                // 保存按钮的内容
                SaveNoteButton(
                    isDarkTheme = isDarkTheme // 是否深色模式
                )
            }
        }
    ) { padding ->
        // 主内容区域：包含标题和内容输入框
        Column(
            modifier = Modifier.padding(padding), // 添加内边距
            verticalArrangement = Arrangement.spacedBy(4.dp) // 垂直排列，间距为 4.dp
        ) {
            // 标题输入框
            InputBox(
                inputType = InputType.Title, // 输入类型为标题
                isDarkTheme = isDarkTheme, // 是否深色模式
                value = title, // 当前标题值
                onValueChange = {
                    viewModel.updateTitle(it) // 更新标题
                },
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) } // 按下 "Next" 键时，焦点移动到下一个输入框
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next // 键盘操作类型为 "Next"
                ),
                focusRequester = focusRequester // 焦点请求器
            )

            // 分隔线
            Canvas(
                modifier = Modifier
                    .size(
                        width = 100.dp, // 宽度
                        height = 2.dp // 高度
                    )
                    .padding(start = 12.dp) // 左边距
            ) {
                // 绘制一个圆角矩形作为分隔线
                drawRoundRect(
                    color = Primary, // 颜色
                    size = size // 大小
                )
            }

            // 内容输入框
            InputBox(
                inputType = InputType.Content, // 输入类型为内容
                isDarkTheme = isDarkTheme, // 是否深色模式
                value = content, // 当前内容值
                onValueChange = {
                    viewModel.updateContent(it) // 更新内容
                },
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() } // 按下 "Done" 键时，清除焦点
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 键盘操作类型为 "Done"
                )
            )
        }
    }
}