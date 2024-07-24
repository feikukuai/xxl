import subprocess

def git_checkout_file(commit_hash, file_path):
    try:
        # 使用 subprocess.run 来执行 git checkout 命令，指定文件路径
        result = subprocess.run(['git', 'checkout', commit_hash, '--', file_path], check=True, text=True, capture_output=True)
        # 打印命令的输出
        return "Output:", result.stdout
    except subprocess.CalledProcessError as e:
        # 打印错误信息
        return "Error:", e.stderr

# 用实际的 commit-hash 替换 'commit-hash' 字符串
commit_hash = 'bbc886676e576b28c5f11ec52c89b6b2e001d0e7'
file_path = 'hello.py'  # 这里是你要还原的文件路径
git_checkout_file(commit_hash, file_path)
