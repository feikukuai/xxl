from docx import Document
# 获取当前脚本所在目录的绝对路径
import os
import sys

exe_dir = os.path.dirname(sys.executable)
print(exe_dir)
script_dir = os.path.dirname(sys.executable)
print(script_dir)
# 改变当前工作目录到exe文件所在的目录
os.chdir(exe_dir)
source_dir = os.path.dirname(sys.executable)
print(f"正确工作路径 directory: {os.getcwd()}")

# 定义读取替换规则的函数
def read_replacement_rules_from_doc(file_path, delimiter=':'):
    doc = Document(file_path)
    replacement_rules = {}
    for paragraph in doc.paragraphs:
        text = paragraph.text
        parts = text.split(delimiter)
        if len(parts) == 2 and parts[0].strip() and parts[1].strip():
            key = parts[0].strip()
            value = parts[1].strip()
            replacement_rules[key] = value
    return replacement_rules

# 从fixtext.docx中读取替换规则
fixtext_path = os.path.join(current_dir, 'fixtext.docx')
replacement_rules = read_replacement_rules_from_doc(fixtext_path)

# 打印读取到的替换规则
print(replacement_rules)

# 检查并读取 text.txt 文件
txt_file_path = os.path.join(source_dir, 'text.txt')

# 如果文件不存在，则创建一个空文件
if not os.path.exists(txt_file_path):
    with open(txt_file_path, 'w') as file:
        print(f"已创建文件: text.txt")

# 读取 text.txt 文件内容并进行替换
with open(txt_file_path, 'r', encoding='utf-8') as file:
    content = file.read()

# 进行文本替换
for old_word, new_word in replacement_rules.items():
    content = content.replace(old_word, new_word)

# 将替换后的内容保存回 text.txt 文件
with open(txt_file_path, 'w', encoding='utf-8') as file:
    file.write(content)

# 确认文件已保存
print(f"文件 text.txt 已替换并保存。")