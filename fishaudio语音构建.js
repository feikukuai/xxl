class StrictAPIExtension {
  constructor() {
    this.lastResponse = null;
    // 初始化默认模型和音色
    this.defaultModel = 'FunAudioLLM/CosyVoice2-0.5B';
    this.defaultVoice = 'alex';
  }

  getInfo() {
    return {
      id: 'smartTTS',
      name: '智能语音合成',
      color1: '#FF6B6B',
      color2: '#FF5252',
      blocks: [
        {
          opcode: 'strictCall',
          blockType: Scratch.BlockType.COMMAND,
          text: '语音合成 模型 [MODEL] 音色 [VOICE] 文本 [TEXT] 密钥 [KEY]',
          arguments: {
            MODEL: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: this.defaultModel // 直接使用用户输入的文本
            },
            VOICE: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: this.defaultVoice // 直接使用用户输入的文本
            },
            TEXT: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: '今天天气真好'
            },
            KEY: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: '你的key'
            }
          }
        },
        {
          opcode: 'getLastResult',
          blockType: Scratch.BlockType.REPORTER,
          text: '获取最后一次响应'
        },
        {
          opcode: 'setDefaultModel',
          blockType: Scratch.BlockType.COMMAND,
          text: '设置默认模型 [MODEL]',
          arguments: {
            MODEL: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: this.defaultModel
            }
          }
        },
        {
          opcode: 'setDefaultVoice',
          blockType: Scratch.BlockType.COMMAND,
          text: '设置默认音色 [VOICE]',
          arguments: {
            VOICE: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: this.defaultVoice
            }
          }
        }
      ]
    };
  }

  strictCall(args) {
    return new Promise((resolve) => {
      // 获取用户输入的模型和音色，如果未输入则使用默认值
      const finalModel = Scratch.Cast.toString(args.MODEL) || this.defaultModel;
      const finalVoice = Scratch.Cast.toString(args.VOICE) || this.defaultVoice;

      // 更新默认模型和音色
      this.defaultModel = finalModel;
      this.defaultVoice = finalVoice;

      const requestBody = {
        model: finalModel,
        input: Scratch.Cast.toString(args.TEXT),
        voice: `${finalModel}:${finalVoice}`,
        response_format: "mp3",
        sample_rate: 32000,
        speed: 1.0,
        gain: 0.0
      };

      fetch('https://api.siliconflow.cn/v1/audio/speech', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${Scratch.Cast.toString(args.KEY)}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      })
      .then(response => this._handleResponse(response))
      .then(result => {
        this.lastResponse = result;
        resolve(result);
      })
      .catch(error => {
        const errorResult = this._formatError(error);
        this.lastResponse = errorResult;
        resolve(errorResult);
      });
    });
  }

  setDefaultModel(args) {
    // 设置默认模型
    this.defaultModel = Scratch.Cast.toString(args.MODEL);
  }

  setDefaultVoice(args) {
    // 设置默认音色
    this.defaultVoice = Scratch.Cast.toString(args.VOICE);
  }

  getLastResult() {
    return this.lastResponse || '暂无响应数据';
  }

  _handleResponse(response) {
    return new Promise((resolve) => {
      const contentType = response.headers.get('content-type') || '';
      
      // 处理音频响应
      if (/audio\/mpeg/i.test(contentType)) {
        response.blob()
          .then(blob => {
            const reader = new FileReader();
            reader.onloadend = () => resolve(reader.result);
            reader.readAsDataURL(blob);
          });
        return;
      }

      // 处理JSON响应
      if (/application\/json/i.test(contentType)) {
        response.json()
          .then(data => resolve(JSON.stringify({
            status: response.status,
            data: data,
            headers: this._parseHeaders(response.headers)
          })));
        return;
      }

      // 处理文本响应
      response.text().then(resolve);
    });
  }

  _formatError(error) {
    return JSON.stringify({
      error: {
        type: "API_CALL_FAILURE",
        timestamp: new Date().toISOString(),
        details: {
          name: error.name,
          message: error.message,
          code: error.code || "N/A"
        }
      }
    });
  }

  _parseHeaders(headers) {
    const headerMap = {};
    headers.forEach((value, name) => {
      headerMap[name] = value;
    });
    return headerMap;
  }
}

Scratch.extensions.register(new StrictAPIExtension());