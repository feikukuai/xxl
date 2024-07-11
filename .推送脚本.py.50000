import subprocess

def git_command(command):
    result = subprocess.run(['git'] + command.split(), stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    if result.returncode == 0:
        return result.stdout
    else:
        raise Exception(f"Git command failed with error: {result.stderr}")

# 查看当前git项目的状态
print(git_command('status'))

# 获取当前分支名称
print(git_command('rev-parse --abbrev-ref HEAD'))

# 查看提交历史
print(git_command('log'))

# 查看远程仓库
print(git_command('remote -v'))

# ...等等其他git命令
