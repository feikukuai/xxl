class StrictAPIExtension {
  constructor() {
    this.lastResponse = null;
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
          blockType: Scratch.BlockType.REPORTER,
          text: '语音合成 模型 [MODEL] 音色 [VOICE] 文本 [TEXT] 密钥 [KEY]',
          arguments: {
            MODEL: {
              type: Scratch.ArgumentType.STRING,
              menu: 'modelList',
              defaultValue: 'fishaudio/fish-speech-2.0'
            },
            VOICE: {
              type: Scratch.ArgumentType.STRING,
              menu: 'voiceList',
              defaultValue: 'alex'
            },
            TEXT: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: '今天天气真好'
            },
            KEY: {
              type: Scratch.ArgumentType.STRING,
              defaultValue: 'sk-your-token'
            }
          }
        },
        {
          opcode: 'getLastResult',
          blockType: Scratch.BlockType.REPORTER,
          text: '获取最后一次响应'
        }
      ],
      menus: {
        modelList: {
          acceptReporters: true,
          items: [
            { text: '🐟 fishaudio模型', value: 'fishaudio/fish-speech-2.0' },
            { text: '🤖 GPT语音模型', value: 'RVC-Boss/GPT-SoVITS' },
            { text: '🧠 深度语音模型', value: 'deepseek-tts/1.0' },
            { text: '✎ 自定义模型', value: 'custom' }
          ]
        },
        voiceList: {
          acceptReporters: true,
          items: [
            { text: '👨 男声-Alex', value: 'alex' },
            { text: '👩 女声-安娜', value: 'anna' },
            { text: '👦 童声-david', value: 'david' },
            { text: '✎ 自定义音色本杰明', value: 'benjamin' }
          ]
        }
      }
    };
  }

  strictCall(args) {
    return new Promise((resolve) => {
      // 处理自定义输入
      const finalModel = args.MODEL === 'custom' ? 
        Scratch.Cast.toString(args.MODEL) : args.MODEL;
      
      const finalVoice = args.VOICE === 'custom' ?
        Scratch.Cast.toString(args.VOICE) : args.VOICE;

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