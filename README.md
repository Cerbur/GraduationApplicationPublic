这里是毕设论文这部分摘抄。README 之后再补充吧

# 系统详细设计与实现

本章内容主要介绍基于Java与微信小程序实现的校园找寻校卡服务系统的各个模块的具体实现，以具体核心思想与相关核心代码，对七大模块即用户、信息上传、信息展示、评论、管理员管理、消息推送、OCR服务模块进行功能展示与论述。

## 5.1 用户模块

### 5.1.1 登录模块的实现

借助于微信小程序的平台能力，使用微信自带的登录可以获取到一个编码字符串，将此字符串请求到后端客户端可以获取到目前这个用户的唯一标识，使用此唯一标识，则可以认为这个是这个微信登录了我们的系统，我们也以微信作为基本用户单位来标定我们的用户。当请求这个字符串到后端时，系统会通过这个唯一标识判断是否是第一次登录，如果是则创建这个用户的基本信息，并且返回该用户的登录态令牌。并将这个令牌放在前端的持久化存储中，在之后的请求带上这个令牌，已完成接口的鉴权。

登录核心代码：

```bash
export function login() {
	return new Promise(reslove => {
		tryLogin(0,reslove);
	})
}
function tryLogin(tryTime,reslove) {
	uni.login({
		success(res) {
			loginRequest(res.code).then(r =>{
				console.log("login success:");
				uni.setStorageSync('eToken',r.data.token)
				uni.setStorageSync('userId',r.data.id)
				reslove()
			}).catch(e=>{
				console.log("login success,error:" + e);
				if (tryTime < 3) {
					tryLogin(tryTime+1);
				} else {
					uni.showToast({
						icon: "error",
						title: "登陆失败请重启",
						duration: 2500,
})}})}})}
```

### 5.1.2 个人信息修改页面展示

在登陆后个人信息页面，可通过获取头像更新获取到微信的头像以更新头像，并在此页面中填写表单去修改个人的信息。

![Untitled](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/Untitled.png)

![Untitled](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/Untitled%201.png)

### 5.1.3 权限校验设计

在后端代码中设计一个 Controller 层的切面，通过获取请求头中的令牌进行解析，获取到个人的用户信息传递到 Controller 层，通过 Spring security 将这个用户信息传递到信息校验函数中进行校验完成对应的鉴权，如果出现权限不正确则会返回不可访问到前端，以保证系统的安全性。

```bash
@ModelAttribute
    public UserVO genUserVO(@RequestHeader(value = "Authorization", required = false) String jwt) {
        if (jwt == null) {
            return null;
        }
        log.info("jwt:"+jwt);
        Integer userId;
        try {
            userId = GenJWT.decode(jwt);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return userService.getUserVOById(userId);
    }

@GetMapping("/user/info")
@PreAuthorize("@UserAuthorize.access(#userVO)")
public Result<UserVO> getUserInfo(@ModelAttribute UserVO userVO) {
    userVO.setOpenId("");
    return Return.success(userVO);
}
```

## 5.2 信息上传模块

### 5.2.1 文件上传模块

通过调用摄像头在页面中显示取景框，点击拍照将照片读取出来转化为base64编码的字符串上传至服务器上，服务器获取后上传至对象存储平台并返回图片的下载链接至前端，用于后面的信息上传，如果这个上传的类型是校园卡类型，那么在服务器端还会通过异步请求获取我们的文字识别服务，将照片中的学号与姓名读取出来以简化用户的输入体验。

图片上传与文字识别如下所示

![83300bee82c62511467e67f77be7a49.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/83300bee82c62511467e67f77be7a49.jpg)

文件上传的核心代码

```bash
async ocr() {
	const { tempImagePath: path } = await becomePromise(
		this.context.takePhoto,
		{
			quality: 'low'
		}
	);
	this.path = path;
	const base64 = (await becomePromise(
		uni.getFileSystemManager().readFile,
		{
			filePath: path, //选择图片返回的相对路径
			encoding: 'base64' //编码格式
		}
	)).data;
	console.log(base64);
	let needOcr = false;
	if (this.typeId == 2) {
		needOcr = true;
	}
	uploadImage(base64,needOcr).then(
	res=>{
		this.info.image = res.data.url;
		this.$refs.uTips.show({
			title: '上传成功',
			type: 'success',
			duration: 4000,
		});
		if (this.typeId == 2) {
			this.info.lostName = res.data.name;
			this.info.schoolId = res.data.schoolId;
		}
	},
	rej=>{
		this.$refs.uTips.show({
			title: '上传失败重拍摄' + rej.msg,
			type: 'warning',
			duration: 4000,
		});
	})	
},
```

### 5.2.2 信息上传模块

完成文件上传后，上传填写的信息，即可生成一条发布的个人信息。同时，如果这是一张校园卡信息，将会在数据库中去匹配这张校卡的用户，若能在系统中匹配到这张校卡的用户，将会写一条消息到消息队列 Kafka 中，用于后续消息推送处理。

异步写队列与信息上传的核心代码

```bash
public Integer postNewLostBO(NewLostBO newLostBO) {
        LostInfo lostInfo = LostInfo.builder()
                .title(newLostBO.getTitle())
                .postUser(newLostBO.getPostUser())
                .foundStatus(LostConstant.FOUND_STATUS_N)
                .lostType(newLostBO.getLostType())
                .schoolId(newLostBO.getSchoolId())
                .lostName(newLostBO.getLostName())
                .description(newLostBO.getDescription())
                .image(newLostBO.getImage())
                .location(newLostBO.getLocation())
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();
        lostInfoDao.createNewLostInfo(lostInfo);
        Integer lostId = lostInfo.getId();
        if (LostConstant.TYPE_SCHOOL_CARD.equals(newLostBO.getLostType())) {
            User user = User.builder()
                    .schoolId(newLostBO.getSchoolId())
                    .name(newLostBO.getLostName())
                    .build();
            User lostUser = userDao.searchUserBySchoolIdAndName(user);
            if (lostUser != null) {
                UniformMessageDTO build = UniformMessageDTO.builder()
                        .lostId(lostId)
                        .openId(lostUser.getOpenId())
                        .schoolId(lostUser.getSchoolId())
                        .lostName(lostUser.getName())
                        .location(lostInfo.getLocation())
                        .description(lostInfo.getDescription())
                        .build();
                uniformMessageProducerService.sendMessage(build);
            }
        }
        return lostId;
    }
```

失物信息上传后的展示

![454b883824d47ca86fff9f04b87c69f.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/454b883824d47ca86fff9f04b87c69f.jpg)

## 5.3 信息展示模块

### 5.3.1 相关信息展示

由于学号相近的同学可能所在的班级相同或者是所在的寝室相同或相近，我们会优先匹配学号相关的同学，和处在同一个班级的同学的信息展示给用户。因为用户认识失主的概率会更大。

![f09af0c0e095b111a658d2000368f7f.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/f09af0c0e095b111a658d2000368f7f.jpg)

### 5.3.2 信息查询展示

此外还会在广场提供查询功能方便用户检索信息。

![d7f926a7344ad8ff7cfa2b9af2e8ce3.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/d7f926a7344ad8ff7cfa2b9af2e8ce3.jpg)

## 5.4 评论模块

在失物信息的详情页面，用户可以在次发表评论与回复评论。

![89a5ffde5ecd19a4ddd380fce643c5c.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/89a5ffde5ecd19a4ddd380fce643c5c.jpg)

![f019aea8409110290382c829ba297e2.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/f019aea8409110290382c829ba297e2.jpg)

## 5.5 文字识别模块

通过 Tr 库训练出来的 OCR 文字识别模型，封装出一个调用接口，并在我们的业务代码中调用此接口完成对应的文字识别。并过滤出置信度大于0.9的数据

文字识别核心调用代码

```bash
public CompletableFuture<HttpResponse<String>> sendAsync(String base64) {
    Map<Object, Object> values = new HashMap<>(3);
    values.put("is_draw", "0");
    values.put("compress", this.compress);
    values.put("img", base64);
    HttpRequest request = HttpRequest.newBuilder(URI.create(this.url))
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(ofFormData(values))
            .build();
    HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
    return httpClient.sendAsync(request, bodyHandler);
}

public List<String> getResult(HttpResponse<String> stringHttpResponse) {
    String s = StringEscapeUtils.unescapeJava(stringHttpResponse.body());
    OcrResult ocrResult = null;
    try {
        ocrResult = objectMapper.readValue(s, OcrResult.class);
    } catch (JsonProcessingException e) {
        log.error(e.getMessage());
    }
    if (ocrResult == null || ocrResult.getCode() == null) {
        return new ArrayList<>();
    }
    if (ocrResult.getCode() != 200) {
        return new ArrayList<>();
    }
    List<OcrMap> ocrMapList = ocrResult.getOcrMapList();
    List<String> res = new ArrayList<>();
    ocrMapList.forEach(ocrMap -> {
        Double confidence = ocrMap.getConfidence();
        if (confidence.compareTo(0.9) > 0) {
            res.add(ocrMap.getValue());
        }
    });
    return res;
}
```

## 5.5 消息推送模块

利用消息队列 Kafka 将接口服务与推送服务接口，使用消息队列的订阅发布机制，将服务进行拆分，在接口服务器将消息推送至 Kafka，之后推送服务从 Kafka 中拉取消息，将对应的内容推送到微信服务中，完成消息的推送。

消息订阅的核心代码

```bash
@KafkaListener(topics = {"${kafka.topic.uniformMessage}"}, groupId = "group1")
public void consumeMessage(UniformMessageDTO dto) {
  log.info("消费者消费{}的消息 -> {}", topicUniformMessage, dto.toString());
  List<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<>();
  dataList.add(new WxMaSubscribeMessage.MsgData("thing4", dto.getLocation()));
  dataList.add(new WxMaSubscribeMessage.MsgData("thing5", dto.getDescription()));
  dataList.add(new WxMaSubscribeMessage.MsgData("thing6", dto.getLostName()));
  dataList.add(new WxMaSubscribeMessage.MsgData("character_string7", dto.getSchoolId()));
  WxMaSubscribeMessage build = WxMaSubscribeMessage
          .builder()
          .toUser(dto.getOpenId())
          .templateId("hhzDaLTtvyYiLqLrU7-lScjOvaZjDYmq6IWvTVyCOeU")
          .page("/pages/lost-detail/lost-detail?id=" + dto.getLostId())
          .miniprogramState(WxMaConstants.MiniProgramState.DEVELOPER)
          .data(dataList)
          .build();
  try {
      wxMaService.getMsgService().sendSubscribeMsg(build);
  } catch (WxErrorException e) {
      log.error(e.getMessage());
  }
}
```

## 5.6 管理员模块

管理员模块是独立于微信小程序客户端的网页端，由于系统不提供账号密码登录，所以使用扫码登录管理端网页。通过生成令牌的形式，将令牌绑定相应的信息发送给Web前端，生成对应的二维码，之后定时一小段时间向服务器查询这个令牌的状态，是否过期，是否被扫码，是否被确认。之后执行相应的状态更新，生成登录态令牌保存在浏览器，在后续请求过程带上这个登录态令牌以便在服务器完成对应的权限校验。下面简述一下扫码登录的过程与时序图

![扫码登录时序图.drawio.png](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/%E6%89%AB%E7%A0%81%E7%99%BB%E5%BD%95%E6%97%B6%E5%BA%8F%E5%9B%BE.drawio.png)

首先打开网页端后网页端发现无登录态令牌，向服务器发起请求获取到扫码登录令牌，转化成二维码展示给前端（请求1、返回2）。之后便开始定时轮询的任务检测扫码登录令牌状态，扫码登录令牌状态包括，未被扫码，已被扫码但未被确认，已确认，三种状态。（轮询3、返回4）未被扫码状态可以被角色为管理员的用户扫码并将扫码状态绑定到这个扫码登录令牌，并在小程序端弹出确认登录弹窗。（扫码5、请求6、返回7）管理端在此轮询周期会验证到登录态令牌已被扫码等待确认。（轮询8，返回9）小程序端点击确认登录，向服务器发起确认登录请求，将绑定个人信息与确认态绑定到登录态令牌中。（请求9，返回10）管理端在下一次轮询周期中检测到登录态令牌为已确认状态，获取其在Redis存储的信息拿到扫码的用户生成其登录态凭证存储在管理端用于后面的请求。（轮询11，返回12）此外在整个登录过程中，为了防止二维码泄露造成的安全问题，还设计了一个设备绑定的随机令牌，管理端在每次轮询中会携带此令牌，若使用攻击获取到二维码内容，强行获取登录态凭证，则会因为没有这个设备绑定的随机令牌而禁止访问。

登录成功示意图

![21f9527948c6c74e94ea76e81dc66a0.jpg](%E7%B3%BB%E7%BB%9F%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%20821d84983a5c45bda4894cbb8c4fc14c/21f9527948c6c74e94ea76e81dc66a0.jpg)

登录成功后，可在管理员页面管理相关的失物发布与评论信息。