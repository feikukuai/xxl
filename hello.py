import subprocess
import os

def clone_repository_with_credentials(repo_url, dest_dir, username, password):
    try:
        # 使用env设置环境变量，以避免在命令行中直接暴露密码
        env = os.environ.copy()
        env['GIT_USERNAME'] = username
        env['GIT_PASSWORD'] = password
        
        # 构造带凭证的URL
        credentials = f"{username}:{password}@"
        repo_url_with_credentials = repo_url.replace("https://", f"https://{credentials}")
        
        # 克隆仓库
        subprocess.run(['git', 'clone', repo_url_with_credentials], cwd=dest_dir, check=True, env=env)
        print(f"Repository cloned to {dest_dir}")
    except subprocess.CalledProcessError as e:
        print(f"An error occurred while cloning the repository: {e}")

# Git仓库URL
repo_url = 'https://github.com/feikukuai/word.git'
# 本地目标目录
dest_dir = '/home'
# GitHub用户名和密码
username = 'feihukuai'
password = 'a840467244'

# 克隆仓库
clone_repository_with_credentials(repo_url, dest_dir, username, password)
