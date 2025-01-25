# Please install OpenAI SDK first: `pip3 install openai`

from openai import OpenAI

client = OpenAI(api_key="sk-e7b07c77962f445e88eb184369d0e49f2", base_url="https://api.deepseek.com")

response = client.chat.completions.create(
    model="deepseek-chat",
    messages=[
        {"role": "system", "content": "You are a helpful assistant"},
        {"role": "user", "content": "Hello，Let's write a short article? Spring outing."},
    ],
    stream=False
)

print(response.choices[0].message.content)
import time

# 停顿五秒
time.sleep(5)

print("五秒已过")
print(response.choices[0].message.content)