import requests
from bs4 import BeautifulSoup

# 目标网页URL
url = 'https://www.hupu.com/'

# 发送HTTP请求
response = requests.get(url)

# 检查请求是否成功
if response.status_code == 200:
    # 解析网页内容
    soup = BeautifulSoup(response.text, 'html.parser')
    
    # 获取网页标题
    title = soup.title.string
    
    # 获取网页中的所有段落
    paragraphs = soup.find_all('p')
    
    # 打印标题和段落内容
    print(f'网页标题：{title}')
    for i, paragraph in enumerate(paragraphs):
        print(f'段落{i+1}：{paragraph.text}')
else:
    print(f'请求失败，状态码：{response.status_code}')
