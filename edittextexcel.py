
import pandas as pd
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
file_path = os.path.join(current_dir, 'input1.docx')
replacement_rules = read_replacement_rules_from_doc(fixtext_path)

docx_file = os.path.join(current_dir, 'input1.docx')

excel_file = os.path.join(current_dir, 'output.xlsx')

# 读取docx文件并提取中文句子
def read_docx(file_path):
    doc = docx.Document(file_path)
    sentences = []
    current_sentence = ""

    for para in doc.paragraphs:
        if para.text.strip() != "":
            for sentence in para.text.split('。'):
                sentence = sentence.strip()
                if sentence.endswith('！') or sentence.endswith('？'):
                    sentences.append(current_sentence + sentence)
                    current_sentence = ""
                else:
                    current_sentence += sentence + '。'
    
    # 添加最后一个句子（如果文件不以句号结尾）
    if current_sentence.strip() != "":
        sentences.append(current_sentence.strip())

    return sentences

# 将句子列表存储为Excel
def save_to_excel(sentences, excel_path):
    df = pd.DataFrame(sentences, columns=["Sentences"])
    df.to_excel(excel_path, index=False)

# 主函数
def main():  # 输出的Excel文件名
    sentences = read_docx(docx_file)
    save_to_excel(sentences, excel_file)
    print("完成，已保存到", excel_file)

if __name__ == "__main__":
    main()





