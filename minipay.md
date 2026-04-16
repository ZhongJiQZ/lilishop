收款key：25378601 
加密 key：84414440 这个是你们的正式参数

https://www.apizza.net/project/be71e2e78ff080ba428959b60975e9e9/browse
接口文档是这个，主要是对接小程序支付，需要增加传参sub_wx_mchid=884214372
签名算法和异步通知可以看指引这边

免费版
免费版接口数 ( 196 / 200 )
线上
http
微信公众号、小程序支付
聚合支付
POST
http://api2.lfwin.com/payapi/mini/wxpay

 发送
文档模拟Mock
Body
service
string
是
接口名称
选项 
comm.js.pay或comm.mini.pay
apikey
string
是
商家APIKEY，唯一标识，由服务商提供
00014005
money
string
是
支付金额，单位元，最小值0.01，表示0.01元
0.01
nonce_str
string
是
随机字符串，长度不超过32位
123456
sub_appid
string
是
小程序/公众号appid，sub_appid跟sub_openid对应关系,并且appid跟微信支付商户号关联
wx9c1b359290bc002c
sub_openid
string
是
消费者用户在sub_appid下的标识sub_openid
oEN9B5gYn3yaAdko9-OFjHRXDkko
sign
string
是
签名，参照签名算法
b0b12b65e6347c8d47923e7b2d53d7cc
remarks
string
否
订单备注
mch_orderid
string
否
商户外部订单号,必须保证每次下单唯一，如果下单失败请调用查询接口
20180209000000001
notify_url
string
否
异步通知地址，地址请回传完整网址(带http或https), 支付成功后服务器发起通知，带参数的话必须整个网址urlencode编码
http://www.xxx.com/notify
time_expire
string
否
订单失效到期时间(目前只支持微信官方通道/合利宝通道)，时间戳，单位秒，刷卡至少1分钟，默认15分钟失效, 官方通道最长2小时，合利宝最长24小时。例如1565618931 表示到2019-08-12 22:08:51过期
goods_info
string
否
商品详情，单品优惠活动该字段必传，JSON字符串格式，详见文档说明
goods_tag
string
否
微信官方订单优惠标记，代金券或立减优惠功能，如果设置该值，goods_tag内容与配置内容相同，且支付金额满足代金券使用条件才会触发代金券的使用。不设置则只要支付金额满足条件就会触发代金券的使用；
sub_wx_mchid
string
否
指定微信子商户号交易，资金管控渠道使用（合利宝/乐刷）
1346546
sign_type
string
否
签名方式，支持MD5或者RSA ，默认MD5
split_info
string
否
动态分账规则：json格式，account：商户号；amount：分账金额，单位元；列如[{"account":"E1213232","amount":"0.01"},{"account":"E1321546","amount":"0.02"}]
[{"account":"E1800305780","amount":10.00}]
split
string
否
分账标识 ，1：标识订单分账(乐刷)
1
attach
string
否
附加数据 ，支付成功通知回调时会原样返回此参数，长度1-128个字符，=、&、?字符请编码
score_money
string
否
【弃用】兑换积分金额，单位元，只支持合利宝订单
share_model
string
否
【弃用】分账模式（默认不设置为0，执行分账时验证固定分账设置。），只支持合利宝订单
选项 
0
fee_ratio_list
string
否
【弃用】动态分账比例（JSON格式，参照文档详细说明；如有设置该信息则 share_model 视为=1），只支持合利宝订单
[{"merchant_id":"C20200422000010003","ratio":100000},{"merchant_id":"C20200521000010030","amount":100}]
share_merchant_id
string
否
【弃用】指定商户编号（分账收取服务费），只支持合利宝订单
good_name
string
否
订单标题，不可使用特殊字符，如 /，=，& 等
 详细说明状态码
公众号支付/小程序支付, 均需提前在后台配置好APPID
公众号支付请在后台通道管理里配置好支付授权目录。​







请求本接口进行预下单后，将获得支付时使用到的参数，可用于公众号和小程序唤起支付。

appId：公众号id，例如：wx12345678901234

timeStamp：时间戳，例如：1672731633

nonceStr：随机字符串，例如：x5QUZc6W4fkFu4HJOLAoTDVLdJZSJDWk

package：订单详情扩展字符串，例如：prepay_id=wx03154033216927de84b89eaafa3bb00000

signType：签名方式，例如：RSA

paySign：签名



公众号支付使用html代码唤起，详见官方文档：

https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7

也可向我司技术获取示例代码Test_h5wxpay.html。

注意点1：前端js中各参数有区分大小写。(如果您前端使用vue，唤起支付时提示签名失败，尝试timeStamp改为timestamp）

注意点2：本接口返回有两个字段signType和sign_type，前者是支付参数之一，后者是本接口自身的签名方式，切勿混淆。





注: 下单后如果15分钟未支付, 系统会自动关闭订单





小程序端唤起支付详见官方文档sdk

https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5





--------------------------------------------------





支付交易渠道( json字符串) 说明： 

支付宝：

例如：[{"amount":"9.98","fund_channel":"MDISCOUNT"},{"amount":"0.02","fund_channel":"ALIPAYACCOUNT"}]



amont:金额，单位元；

fund_channel 交易渠道：

COUPON ：       支付宝红包

ALIPAYACCOUNT： 支付宝账户

POINT:            集分宝

DISCOUNT:        折扣券

PCARD:           预付卡

MCARD:           商家储值卡

MDISCOUNT:      商户优惠券

MCOUPON:        商户红包

PCREDIT:          蚂蚁花呗



微信

例如：{"promotion_detail":[{"promotion_id":"2683768328","name":"单品15号测试优惠","scope":"SINGLE","type":"DISCOUNT","amount":1,"activity_id":"4218002","wxpay_contribute":0,"merchant_contribute":1,"other_contribute":0,"goods_detail":[{"goods_id":"6923450656181","quantity":1,"price":2,"discount_amount":1}]}]}

promotion_detail：营销详情

promotion_id：券ID

name：优惠名称

scope：优惠范围

amount：优惠面额，单位分

activity_id：活动ID

wxpay_contribute：微信出资，单位分

merchant_contribute：商户出资，单位分

other_contribute：其他出资，单位分

goods_detail：单品列表

goods_id：商品编码

goods_remark：商品备注

discount_amount：商品优惠金额，单位分

quantity：商品数量

price：商品价格，单位分





-------------------------------------------------



公众号支付测试用例

测试用这组

service = heli.js.pay

sub_appid=wxb1bec7d809e7cf40

sub_openid=ohyVV0v4UBc63GL8D8nlEL0UO5vE



-----------------------------------------------



动态分账split_info传参说明

分账规则：json格式

account：商户号  不可传发起方自身

amount：分账金额，单位元；

is_original(可选参数): 有传并且值为1, 表示接收方收到的金额, 程序会自动扣掉手续费; 不传则按原始分账金额分给接收方

注: 一笔订单可以传多个接收方, 接收方不可重复

例:

split_info=[{"account":"E1800305780","amount":10.00}] //接收方实际到账10元

split_info=[{"account":"E1800305780","amount":10.00,"is_original":1}] //接收方实际到账(10-10元手续费)





--------------------------------------------



split 订单分账标识(乐刷通道)



订单分账


--------------------------------------------



其他参数说明：

fee_ratio_list动态分账比例，JSON格式，参数说明：

merchant_id ：待分账商户编号（注意，不能设置分账给收款主体商户自己，否则将无视）

ratio：分账比例（最小精度0.0001% = 0.000001 需转换为整形 ( X 1000000) =》 1 传入）

amount：固定分账金额（单位：分）





---------------------------------------





支付宝、微信、银联单品优惠活动：



goods_info商品详情，单品优惠活动该字段必传，JSON字符串格式，例如：





 [{"goods_id":"apple-01","goods_name":"iPhone6s 32G","quantity":"1","price":"528800"},{"goods_id":"apple-02","goods_name":"iPhone6s 64G","quantity":"1","price":"608800"}]


名称 变量名 描述

商品编码 goods_id 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成，string类型

商品名称 goods_name 商品的实际名称，string类型

商品数量 quantity 用户购买的数量，string类型

商品单价 price 单位为：分，string类型。如果商户有优惠，需传输商户优惠后的单价(例如：用户对一笔100元的订单使用了商场发的优惠券100-50，则活动商品的单价应为原单价-50)







-----------------------------------



订单超时时间说明


时间戳，单位秒，刷卡至少1分钟，默认15分钟失效, 官方通道最长2小时，合利宝最长24小时。

例如1565618931 表示到2019-08-12 22:08:51过期



---------------------------------



常见报错：

1、5035,sub mch id与sub appid不匹配(通道返回)

联系客服配置支付通道的appid和支付目录(通常能返回这个报错，说明接口是调通的)



2、调用支付JSAPI缺少参数：total_fee

没正确将支付参数赋值到支付页面，请检查。



3、当前页面的URL未注册

联系客服配置支付通道的appid和支付目录

success
{
"appId": "wx0fe958a999250740",发起微信支付参数，微信分配的小程序/公众号APPID <string>
"timeStamp": "'1524221960'",发起微信支付参数，时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间 <string>
"nonceStr": "t7qohzz2sb1e0t3njfqa9p42ghrzq5gs",发起微信支付参数，随机字符串，长度为32个字符以下。 <string>
"signType": "MD5",发起微信支付参数，签名算法 <string>
"package": "prepay_id=wx2018592074688612d0b75f414227509723",发起微信支付参数，统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=* <string>
"paySign": "B7021278F31DA6B3BD1C6CB1DE38B3DF",发起微信支付参数，签名 <string>
"service": "wxpay.mini.pay",接口名称 <string>
"orderid": "201804201859200070044104",系统订单号，唯一值 <string>
"order_time": "1524221960",下单时间,时间戳格式，精确到秒 <string>
"mch_orderid": "",商户外部订单号,必须保证每次下单唯一，如果下单失败请调用查询接口 <string>
"qr_code": "",二维码链接 <string>
"trade_type": "JSAPI",交易类型，如下：JSAPI <string>
"prepay_id": "wx2018592074688612d0b75f414227509723",预支付交易会话标识 <string>
"version": "3.0",接口版本号，3.0 <string>
"charset": "UTF-8",接口字符编码，UTF-8 <string>
"message": "SUCCESS",错误信息 <string>
"status": "10000",错误状态 <string>
"sign_type": "MD5",签名方式，注意不要与signType混淆 <string>
"sign": "5064ba8df2a4960f3e2219f1d83d1f56"签名字符串，参照签名方法 <string>
}
浮动注释
service
接口名称
string
version
接口版本号，3.0
number
charset
接口字符编码，UTF-8
string
message
错误信息
string
status
错误状态
number
sign_type
签名方式，注意不要与signType混淆
string
sign
签名字符串，参照签名方法
string
appId
发起微信支付参数，微信分配的小程序/公众号APPID
string
timeStamp
发起微信支付参数，时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
string
nonceStr
发起微信支付参数，随机字符串，长度为32个字符以下。
string
signType
发起微信支付参数，签名算法
string
package
发起微信支付参数，统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
string
paySign
发起微信支付参数，签名
string
service
接口名称
string
order_time
下单时间,时间戳格式，精确到秒
number
mch_orderid
商户外部订单号,必须保证每次下单唯一，如果下单失败请调用查询接口
string
qr_code
二维码链接
string
trade_type
交易类型，如下：JSAPI
string
prepay_id
预支付交易会话标识
string
orderid
系统订单号，唯一值
string






免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
线下支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
http
对接须知
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
暂无数据
 详细说明状态码
接口说明
测试环境域名： http://api2uat.lfwin.com(测试环境请使用以下测试参数)

测试key 00014005

加密key punr8ucu

生产环境域名： http://api2.lfwin.com 或 https://api2.lfwin.com (推荐使用https, 安全性更高)



传输方式：HTTP/HTTPS 传输

通讯方式：采用 POST 方法提交，（请使用 application/x-www-form-urlencoded 此 Content-Type 提交），为了保证接收方数据正确，数据必须签名（signKey不需要提交），统一采用 UTF-8 字符编码，默认使用MD5 签名通讯验签

字符编码：统一采用 UTF-8 字符编码。

签名算法：支持 MD5、RSA 签名算法。（具体的签名sign算法参照签名算法篇章）

签名要求：先按一定规则拼接要签名的原始串，再选择具体的算法和密钥计算出签名结果。一般失败的结果不签名。





对接调试工具
可使用以下对接工具

https://www.lfwin.com/apitest/







接口对接DEMO
文档有提供常用开发语言对接demo, demo中有提供签名验签的示例, 可以参考.

如需其他开发语言demo可咨询技术获取









接口对接流程
以条码支付为例



通过条码支付接口请求发起支付
通过支付状态轮询接口查询订单支付状态------------------（频次建议2秒一次）
根据实际状态展示或提示相应报错


如发生退款（退款注意事项请查看对应接口文档说明）



通过退款接口发起退款请求
通过退款查询接口查询退款实际状态
根据实际状态展示或提示相应报错








MD5通讯示例
请求报文实例:



POST http://api2uat.lfwin.com/payapi/pay/qrcode HTTP/1.1
Content-Type: application/x-www-form-urlencoded
Host: ap2uat.lfwin.com
Content-Length: 107




service=pay.alipay.qrcode&money=0.01&apikey=00014005&nonce_str=123456&sign=e2bc07b313c49f166ec03796370d8eb2


返回报文实例:





HTTP/1.1 200 OK
Server: nginx
Date: Wed, 27 Jun 2018 02:12:11 GMT
Content-Type: text/html
Transfer-Encoding: chunked
Connection: keep-alive
Keep-Alive: timeout=60






{"service":"pay.alipay.qrcode","orderid":"201806271012110070023531","qr_code":"https:\/\/qr.alipay.com\/bax082633oevohfefy3t2073","paymoney":"0.01","pri_paymoney":"0.01","paystatus":"0","order_time":"1530065531","mch_orderid":"","code_url":"http:\/\/api2uat.lfwin.com\/payapi\/index\/showqr\/code\/aHR0cHM6Ly9xci5hbGlwYXkuY29tL2JheDA4MjYzM29ldm9oZmVmeTN0MjA3Mw%253D%253D","version":"3.0","charset":"UTF-8","message":"SUCCESS","status":"10000","sign_type":"MD5","sign":"ab7a90a880e7c41d3cb498f64b42a275"}








备注：
1、每个支付接口都有对应的service，如果没有确定不更换其他通道，请选择对应自动切换的service，列如 条码支付接口的：service = pay.comm.barcode

2、测试环境更换成生产环境只要更改接口域名中 api2uat 为 api2 ，其他不需要改动

3、测试环境没有异步通知 ，并且测试环境只能用00014005测试apikey

4、签名验签参照 签名算法以及demo 那边签名校对，待签名字符串注意先排序后签名

5、H5支付、公众号、服务窗、小程序支付必须在微信app 或者支付宝app内才能唤起支付，H5支付只是我们帮你封装好公众号，服务窗支付，原理跟后者一样。

6、1.0接口跟2.0接口中的 apikey跟加密key 是通用的，本接口也是基于apikey 找到对应的商户

7、签名跟验签基本能杜绝报文数据伪造，如果有更严密的安全需求请参照des加密传输章节

8、异步通知也是以post方式通讯，后期接口会增加参数，不会减少现有参数，对接需做好扩展参数的兼容



----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



如果不确定支付渠道，或者商户会更换支付渠道，建议使用自动切换渠道service：

接口名称 service（自动切换） 测试环境接口地址

条码支付 pay.comm.barcode http://api2uat.lfwin.com/payapi/pay/qrcode

扫码支付 http://api2uat.lfwin.com/payapi/pay/qrcode

alipay.comm.qrcode （支付宝）

wxpay.comm.qrcode （微信）

unpay.comm.qrcode （云闪付）







支付状态轮询 pay.comm.query_order http://api2uat.lfwin.com/payapi/pay/query_order

退款请求 pay.comm.refund_order http://api2uat.lfwin.com/payapi/pay/refund_order

退款查询 pay.comm.query_refund http://api2uat.lfwin.com/payapi/pay/query_refund

撤销订单 pay.comm.cancel_order http://api2uat.lfwin.com/payapi/pay/cancel_order

关闭订单 pay.comm.close_order http://api2uat.lfwin.com/payapi/pay/close_order



链接跳转支付

alipay.comm.jspay（支付宝） http://api2uat.lfwin.com/payapi/pay/jspay

wxpay.comm.jspay （微信）

unpay.comm.jspay （云闪付）







支付宝服务窗、小程序支付 comm.js.pay 、comm.mini.pay http://api2uat.lfwin.com/payapi/trade/alipay

微信公众号、小程序支付 comm.js.pay 、 comm.mini.pay http://api2uat.lfwin.com/payapi/mini/wxpay











在线测试接口地址 http://coolaf.com/

测试url地址：http://api2uat.lfwin.com/deposit/pay/query_order

post参数

apikey=00014005&nonce_str=97790394887844114942319188479735&order_time=1565866548&orderid=201908151855480070052281&service=pay.comm.query_order&sign=ca1b549c859526b712dc0620e49341cc

success
  
浮动注释



免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
线下支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
http
各场景支付接入指引
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
暂无数据
 详细说明状态码
我有线下场所
线下场所泛指商超、便利店、餐饮、医院、学校、电影院和旅游景区等具有明确经营地址的实体场所。

场景一， 商家扫顾客

可以通过条码支付自行开发完成收款需求，支持微信、支付宝和云闪付条码。

条码支付即商家扫顾客出示的付款码完成支付，以微信为例，消费者出现付款码，商家使用扫码枪或其他扫码机具扫描后消费者输入密码（免密规则内无需密码）后自动完成支付. 

对应接口：条码支付（B扫C）/payapi/pay/barcode







场景二， 顾客扫商家

可以通过扫码支付接口生成动态的二维码，展示给顾客，顾客使用微信、支付宝APP扫码进入付款界面完成支付

对应接口：扫码支付（C扫B、支付宝链接跳转）/payapi/pay/qrcode 或者 聚合扫码支付/payapi/trans/kxpay （区别是聚合扫码支付生成的码多端可扫）











我有微信公众号、小程序
即需要用户在对应的公众号和小程序上完成支付，可以对接线上支付-公众号、小程序支付接口

对应接口：微信公众号、小程序支付 /payapi/mini/wxpay （需正式环境调试）













我有支付宝生活号、小程序
即需要用户在对应的支付宝生活号和小程序上完成支付，可以对接支付宝生活号、小程序支付

对应接口：微信公众号、小程序支付 /payapi/trade/alipay （需正式环境调试）







我有PC网站&POS&自动售卖机
支持完成域名ICP备案的网站、POS机、自动售卖机等接入支付功能。PC网站、POS机、自动售卖机接入支付，可以通过接口，自行开发生成二维码，用户使用微信/支付宝钱包/云闪付等APP“扫一扫”来完成支付。

对应接口：扫码支付（C扫B、支付宝链接跳转）/payapi/pay/qrcode 或者 聚合扫码支付/payapi/trans/kxpay （区别是聚合扫码支付生成的码多端可扫）










我有基于微信端的H5, 但没有公众号
该情况按正常支付流程会有主体不一致的情况, 可以对接下面接口

链接跳转支付(避免主体不一致问题): /payapi/pay/jspay3 (需正式环境)







我有微信小程序, 但APPID配置不上
该情况受官方行业限制, 可使用我们的小程序收银台支付解决方案, 联系业务咨询







我有H5或APP
H5或APP除了官方的SDK对接，还可以通过跳转的方式完成支付，以下是对应实现方式和接口。

微信：小程序跳转支付， 流程是通过请求接口，控制浏览器跳转接口返回的url参数，唤起微信支付

支付宝：扫码支付（C扫B、支付宝链接跳转），控制浏览器跳转接口返回的qr_code参数，唤起支付宝支付

对应接口：（需正式环境调试）

微信：微信小程序跳转支付 /payapi/mini/mini_url

支付宝：扫码支付（C扫B、支付宝链接跳转）/payapi/pay/qrcode

该方式都是通过浏览器跳转链接发起支付，不支持返回原APP

另：有提供APP直跳微信小程序的接口，可支持返回APP，可联系业务咨询











我有多场景的网站需要对接支付
可使用统一收银台接口 /index/Payment/pre_order

调用该接口可以快速对接支付, 无需对接多个支付方式, 直接通过返回收银台链接, 由收银台自动适配唤起相应支付

支持PC网站, H5, 微信公众号支付, 支付宝生活号支付.







间连通道特殊场景的微信支付对接


1. APP场景

目前解决方案是使用【微信小程序跳转支付】，如果小程序跳转支付被限制，暂时无其他解决方案；只能间接使用【扫码支付】生成二维码，让用户保存图片后识别付款。

2. 微信内H5场景

1). 如有公众号，可以直接使用【公众号支付】

2). 没有公众号，可使用【链接跳转支付】（荐）

3). 该场景也支持【微信小程序跳转支付】

3. 普通H5场景

仅支持【微信小程序跳转支付】

4. 商户自有小程序内支付

1). 如未收到对接发货管理通知，可直接使用原生【小程序支付】

2). 有收到对接发货管理通知，被限制小程序支付时，可对接【半屏支付】方案（荐）或对接发货管理走原生支付（需联系技术支持）



说明：【微信小程序跳转支付】，默认跳转服务商主体小程序，也可跳转商户自有小程序











以上场景均不满足或不清楚需要对接哪个接口, 请联系业务沟通
success
  
浮动注释


免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
线下支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
http
签名算法
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
signkey
string
是
商户加密秘钥，由服务商提供
punr8ucu
apikey
string
是
商家APIKEY，唯一标识，由服务商提供
00014005
 详细说明状态码
数字签名
(测试环境只能用00014005 这组apikey,调通流程上线生产环境请联系技术) 
测试环境只支持最新月份的订单



接口地址附带的?test=1只是方便测试环境调试接口，可以无视

例如：

http://api2uat.lfwin.com/payapi/pay/order_list?test=1

正常的接口地址应该是

http://api2uat.lfwin.com/payapi/pay/order_list

为了保证数据传输过程中的数据真实性和完整性，我们需要对数据进行数字签名，在接收签名数据之后进行签名校验。

数字签名有两个步骤，先按一定规则拼接要签名的原始串，再选择具体的算法和密钥计算出签名串sign。

签名的方式根据请求参数sign_type 分为MD5跟RSA， 2种签名算法，其中RSA需要在商户配置那边设置RSA公钥、私钥。





MD5签名算法:
MD5 是一种摘要生成算法，通过在签名原始串后加上商户通信密钥的内容，进行MD5运算，形成的摘要字符串即为签名结果。为了方便比较，签名结果统一转换为32位小写字符。

注意：签名时将字符串转化成字节流时指定的编码字符集（UTF-8）应与参数charset一致。





签名步骤：



1、在请求参数列表中，除去sign参数外，凡是有参与请求参数皆是待验签的参数。

2、将除去sign剩下的参数进行按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接组成字符串，得到待签名字符串

3、待签名字符串末尾再拼接上&signkey=商户密钥。

4、对待签名字符串进行md5加密生成32位小写的字符串。

5、将步骤4生成的字符串，当做sign参数值传参，参与报文提交。







验签步骤:



1、在请求接口同步返回参数列表中，除去sign参数外，凡是返回回来的参数皆是待验签的参数。（特别注意：后续任何接口都有可能增加返回参数，但不会删减已有的参数，注意也要参与验签）

2、将除去sign剩下的参数进行按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接组成字符串，得到待签名字符串

3、待签名字符串末尾再拼接上&signkey=商户密钥。

4、对待签名字符串进行md5加密生成32位小写的字符串。

5、将步骤4生成的字符串跟sign参数的值对比，如果一致即为签名验签成功，否则验签失败。

6、如果接口报文返回的status值非10000，这时候一般都是报文请求返回失败，无需签名验签。







注意事项：


无论是请求还是应答，签名原始串按以下方式组装成字符串：

 1、除sign字段外，所有参与请求的参数按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接而成，key值为sign不参与签名组串。

2、拼接完成后的字符串末尾再拼接上&signkey=商户密钥。

 3、签名原始串中，字段名和字段值都采用原始值，不进行URL Encode。

 4、平台返回的应答或通知消息可能会由于升级增加参数，请验证应答签名时注意允许这种情况。

 5、对平台返回的应答或通知消息进行验签过程也一样，得到的签名结果对比sign参数，如果值相等则签名通过，如果不等，则签名不通过。

6、如果有请求参数有notify_url参数,则notify_url参数需要urldecode编译。

7、生产环境用的apikey跟signkey，需要去对应的商户设备列表的获取响应的收款key 跟 加密key

8、只要参与提交参数都要参与签名（null值的参数不需要提交），列如post提交的参数a=&b=1, 那么a参数也应该参与待签名串的 ，待签名串: a=&b=1





签名示例：


调用扫码支付接口，接口有如下字段：



{
 "service": "pay.alipay.qrcode",
 "apikey": "00014005",
 "money": "0.01",
 "nonce_str": "123456",
}




正确的待签名字符串字段排序为：



apikey=00014005&money=0.01&nonce_str=123456&service=pay.alipay.qrcode




排序后的待签名字符串拼接商户加密key（apikey=00014005对应的 商户加密signkey=punr8ucu）, 生产环境用的apikey跟signkey，需要去对应的商户设备列表的获取响应的收款key 跟 加密key



apikey=00014005&money=0.01&nonce_str=123456&service=pay.alipay.qrcode&signkey=punr8ucu




对排序拼接好的字符串进行md5加密



sign = md5(apikey=00014005&money=0.01&nonce_str=123456&service=pay.alipay.qrcode&signkey=punr8ucu) //结果为 e2bc07b313c49f166ec03796370d8eb2






最终请求报文参数，注意不要包含signkey：



{
 "service": "pay.alipay.qrcode",
 "apikey": "00014005",
 "money": "0.01",
 "nonce_str": "123456",
 "sign": "e2bc07b313c49f166ec03796370d8eb2"
}






RSA签名算法:



签名步骤：



1、在请求参数列表中，除去sign参数外，凡是有参与请求参数皆是待验签的参数。

2、将出去sign剩下的参数进行按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接组成字符串，得到待签名字符串

3、加签——签名=RSA签名方法("排序后字符串","商户RSA私钥")；

4、将步骤3生成的签名，当做sign参数值传参，参与报文提交。







验签步骤:



1、在请求接口同步返回参数列表中，除去sign参数外，凡是返回回来的参数皆是待验签的参数。

2、将出去sign剩下的参数进行按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接组成字符串，得到待签名字符串

3、验签结果=RSA验证签名方法("排序后字符串","返回参数中的sign","服务端RSA公钥")：

4、验签结果==true，验签通过，注意 【一定不要用商户RSA公钥！！！】。

5、如果接口报文返回的status值非10000，这时候一般都是报文请求返回失败，无需签名验签。





注意事项：
参照签名的MD5签名方式注意事项。

success
  
浮动注释


免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
线下支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
支付完成异步通知参数
http
支付完成异步通知参数
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
orderid
string
是
系统订单号，唯一值
trade_no
string
是
通道流水号
dis_name
string
是
通道方订单号
paystatus
string
是
支付状态：1 支付成功 ，0待付款，2付款失败
paymoney
string
是
下单支付金额，单位元
pri_paymoney
string
是
商家优惠活动后，实际支付金额，单位元
order_time
string
是
下单时间。时间戳，单位秒
paytime
string
是
支付成功时间。时间戳，单位秒
mch_orderid
string
是
商户外部订单号
notify_url
string
是
通知地址
buyer_account
string
是
买家支付宝账号
attach
string
否
附加数据 ，下单时若有传此参数时，通知将原样返回
sign
string
是
签名字符串，参照签名方法
sign_type
string
否
签名方式，支持MD5或者RSA ，默认MD5
 详细说明状态码
（重要）：不保证通知最终一定能成功，在订单状态不明或者没有收到通知的情况下，建议商户主动调用支付状态轮询接口确认订单状态，不要太过于依赖回调.


1、正式环境下H5支付，扫码支付以及小程序支付下单时，如果有传notify_url参数，则订单支付成功时，系统会将支付结果通知到 notify_url。(测试环境不通知)。

2、参数以QueryString表单作为请求body，以POST方式发送到notify_url，该页面需要正确获取通知内容，例如：Map<String,String[ ]> getParameterMap()、$_POST、input('post.')。

3、被通知方收到通知后，必须输出"success"（7个字符，不包含引号），否则系统会认为通知失败，会继续通知支付结果，一般情况下，重试通知次数大约为4次，时间点为第一次通知后的1分钟、5分钟、15分钟、30分钟。



POST 方式发送支付结果参数示例：

orderid=20180202171044007002473&trade_no=4200000061201802025681296197&dis_name=201802021723331617316118&paystatus=1&paymoney=0.01&pri_paymoney=0.01&order_time=1517562644&paytime=1517562682&mch_orderid=&buyer_account=%E5%BE%AE%E4%BF%A1%E8%B4%A6%E5%8F%B7notify_url=http%3A%2F%2Fwww.xxx.com&sign=0ee2e3d34573de75f973630403db24b6









异步返回结果验签


异步通知返回sign_type ，可选参数，如果有返回sign_type且值为RSA，则需要RSA验签，否则默认为MD5验签。



MD5验签步骤：

1、在通知返回参数列表中，除了sign参数外，凡是通知返回回来的参数皆是待验签的参数，未来可能增加参数，请勿写死参与签名的参数。

2、去除sign参数，将剩下的参数进行按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接组成字符串，得到待签名字符串

3、待签名字符串末尾再拼接上&signkey=商户密钥。

4、对待签名字符串进行md5加密生成32位小写的字符串。

5、将步骤4生成的字符串跟sign参数的值对比，如果一致即为签名验签成功，否则验签失败。



MD5验签示例:

异步通知接收地址是http://www.baidu.com

收到的通知是 



orderid=201909061609431364590937&trade_no=4200000401201909061532983835&paystatus=1&paymoney=0.10&pri_paymoney=0.10&order_time=1567757383&paytime=1567757386&mch_orderid=7777M51909060005&notify_url=http%3a%2f%2fwww.baidu.com&buyer_account=%E5%BE%AE%E4%BF%A1&sign=99d2f41d1df83670b80dd9d7e8bc0939
去除sign参数，排序后，加入signkey的待验签串



buyer_account=微信&mch_orderid=7777M51909060005&notify_url=http://www.baidu.com&order_time=1567757383&orderid=201909061609431364590937&paymoney=0.10&paystatus=1&paytime=1567757386&pri_paymoney=0.10&trade_no=4200000401201909061532983835&signkey=punr8ucu
md5=



99d2f41d1df83670b80dd9d7e8bc0939






RSA验签步骤:



1、在请求接口同步返回参数列表中，除去sign参数外，凡是返回回来的参数皆是待验签的参数，未来可能增加参数，请勿写死参与签名的参数。

2、将出去sign剩下的参数进行按照字段名的ascii码从小到大排序后使用QueryString的格式（即key1=value1&key2=value2…）拼接组成字符串，得到待签名字符串

3、验签结果=RSA验证签名方法("排序后字符串","返回参数中的sign","服务端RSA公钥")：

4、验签结果==true，验签通过，注意 【一定不要用商户RSA公钥！！！】。

5、如果接口报文返回的status值非10000，这时候一般都是报文请求返回失败，无需签名验签。

success
  
浮动注释
免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
线下支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
支付完成异步通知参数
退款完成异步通知参数
http
退款完成异步通知参数
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
refund_orderid
string
是
系统退款订单号，唯一值
orderid
string
是
（原支付订单）系统订单号
addtime
number
是
退款下单时间。时间戳，单位秒
notify_url
string
是
通知地址
rt_trade_no
string
是
通道退款单号
mch_refund_no
string
是
商户退款单号
refundmoney
string
是
退款金额，单位元
refund_handmoney
string
是
退款手续费，单位元
refund_status
string
是
退款状态：1 退款成功 ，0 退款处理中，2 退款失败
refundtime
string
是
退款（完成）时间。时间戳，单位秒
attach
string
否
附加数据 ，退款时若有传此参数时，通知将原样返回
sign
string
是
签名字符串，参照签名方法
sign_type
string
否
签名方式，支持MD5或者RSA ，默认MD5
 详细说明状态码
（重要）：不保证通知最终一定能成功，在订单状态不明或者没有收到通知的情况下，建议商户主动调用支付退款状态轮询接口确认订单状态，不要太过于依赖回调.


1、正式环境下请求统一退款接口时，如果有传notify_url参数，则订单退款成功时，系统会将退款结果通知到 notify_url。(测试环境不通知)。

2、参数以QueryString表单作为请求body，以POST方式发送到notify_url，该页面需要正确获取通知内容，例如：Map<String,String[ ]> getParameterMap()、$_POST、input('post.')。

3、被通知方收到通知后，必须输出"success"（7个字符，不包含引号），否则系统会认为通知失败，会继续通知支付结果，一般情况下，重试通知次数大约为4次，时间点为第一次通知后的1分钟、5分钟、15分钟、30分钟。



暂时仅支持同步退款成功订单





注：异步返回结果签名规则同前“支付完成异步通知参数”文档描述，仅业务参数命名不同，此处不再累述。


success
  
浮动注释


免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
线下支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
支付完成异步通知参数
退款完成异步通知参数
全局状态码
http
全局状态码
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
暂无数据
 详细说明状态码
接口返回状态码status如下:



success
  
浮动注释


免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
POST
订单列表/订单详情
POST
支付状态轮询
POST
退款请求
POST
退款查询
POST
退款记录列表/退款记录详情
POST
关闭订单
POST
分账订单列表/详情(beta)
POST
统一收银台接口(beta)
线下支付
POST
条码支付（B扫C）
POST
扫码支付（C扫B、支付宝链接跳转）
POST
撤销订单
POST
当面付解码获取userid、openid（支付宝官方、微信官方）
POST
聚合扫码支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
POST
MD5签名校对（测试环境）
VIEW
C语言
VIEW
php语言
VIEW
java语言
VIEW
php语言图片上传DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
支付完成异步通知参数
退款完成异步通知参数
全局状态码
java语言
http
java语言
聚合支付
VIEW
http://

 发送
文档模拟Mock
Body
暂无数据
 详细说明状态码
HttpRequest.java 文件



import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.URL;

import java.net.URLConnection;

import java.util.List;

import java.util.Map;

 

public class HttpRequest {

   

  

 /**

  * 向指定URL发送GET方法的请求

  * 

  * @param url

  *   发送请求的URL

  * @param param

  *   请求参数，请求参数应该是 name1=value1&name2=value2 的形式。

  * @return URL 所代表远程资源的响应结果

  */

 public static String sendGet(String url, String param) {

  String result = "";

  BufferedReader in = null;

  try {

   String urlNameString = url + "?" + param;

   URL realUrl = new URL(urlNameString);

   // 打开和URL之间的连接

   URLConnection connection = realUrl.openConnection();

   // 设置通用的请求属性

   connection.setRequestProperty("accept", "*/*");

   connection.setRequestProperty("connection", "Keep-Alive");

   connection.setRequestProperty("user-agent",

     "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

   // 建立实际的连接

   connection.connect();

   // 获取所有响应头字段

   Map<String, List<String>> map = connection.getHeaderFields();

   // 遍历所有的响应头字段

   for (String key : map.keySet()) {

    System.out.println(key + "--->" + map.get(key));

   }

   // 定义 BufferedReader输入流来读取URL的响应

   in = new BufferedReader(new InputStreamReader(

     connection.getInputStream()));

   String line;

   while ((line = in.readLine()) != null) {

    result += line;

   }

  } catch (Exception e) {

   System.out.println("发送GET请求出现异常！" + e);

   e.printStackTrace();

  }

  // 使用finally块来关闭输入流

  finally {

   try {

    if (in != null) {

     in.close();

    }

   } catch (Exception e2) {

    e2.printStackTrace();

   }

  }

  return result;

 }

 

 /**

  * 向指定 URL 发送POST方法的请求

  * 

  * @param url

  *   发送请求的 URL

  * @param param

  *   请求参数，请求参数应该是 name1=value1&name2=value2 的形式。

  * @return 所代表远程资源的响应结果

  */

 public static String sendPost(String url, String param) {

  PrintWriter out = null;

  BufferedReader in = null;

  String result = "";

  try {

   URL realUrl = new URL(url);

   // 打开和URL之间的连接

   URLConnection conn = realUrl.openConnection();

   // 设置通用的请求属性

   conn.setRequestProperty("accept", "*/*");

   conn.setRequestProperty("connection", "Keep-Alive");

   conn.setRequestProperty("user-agent",

     "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

   // 发送POST请求必须设置如下两行

   conn.setDoOutput(true);

   conn.setDoInput(true);

   // 获取URLConnection对象对应的输出流

   out = new PrintWriter(conn.getOutputStream());

   // 发送请求参数

   out.print(param);

   // flush输出流的缓冲

   out.flush();

   // 定义BufferedReader输入流来读取URL的响应

   in = new BufferedReader(

     new InputStreamReader(conn.getInputStream()));

   String line;

   while ((line = in.readLine()) != null) {

    result += line;

   }

  } catch (Exception e) {

   System.out.println("发送 POST 请求出现异常！"+e);

   e.printStackTrace();

  }

  //使用finally块来关闭输出流、输入流

  finally{

   try{

    if(out!=null){

     out.close();

    }

    if(in!=null){

     in.close();

    }

   }

   catch(IOException ex){

    ex.printStackTrace();

   }

  }

  return result;

 }  

  /*

 * 32位加密

 */

  public static String md5(String plainText){ 

    StringBuffer buf = null;

    try {

     MessageDigest md = MessageDigest.getInstance("MD5"); 

     md.update(plainText.getBytes());  

     byte[] b = md.digest();

     int i; 

     buf = new StringBuffer(""); 

      

     for (int offset = 0; offset < b.length; offset++) { 

       i = b[offset]; 

       if(i < 0) 

        i += 256; 

       if(i < 16) 

        buf.append("0"); buf.append(Integer.toHexString(i)); 

     } 

      

    } catch (Exception e) {

     e.printStackTrace();

    }

    return buf.toString();

  }

    /**

   * 使用 Map按key进行排序

   * @param map

   * @return

   */

  public Map<String, String> sortMapByKey(Map<String, String> map) {

    if (map == null || map.isEmpty()) {

      return null;

    }

    Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());

   

    sortMap.putAll(map);

   

    return sortMap;

  }

     

  class MapKeyComparator implements Comparator<String> {



  @Override

  public int compare(String str1, String str2) {



    return str1.compareTo(str2);

  }

  }

  /**

   * 获取排好序的参数

   * @param map

   * @return

   */

  private String getParams(Map<String,String> map){

    String params = "";

    for (String key : map.keySet()){

      if (TextUtils.isEmpty(map.get(key)))

        continue;

      if (TextUtils.isEmpty(params)){

        params = key + "=" + map.get(key);

      }else {

        params = params + "&" + key + "=" + map.get(key);

      }

    }

    return params;

  }

}

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

HelloJava.java文件



public class HelloJava {



public static void main(String[] args) {





//发送 POST 请求

  String sr=HttpRequest.sendPost("http://api2uat.lfwin.com/payapi/pay/qrcode", "service=pay.wxpay.qrcode&apikey=00014005&mch_orderid=0001400520180328153131000082&nonce_str=20180328153131&money=0.01&sign=672a56c450cabb60875989d3c25dbe82");

  System.out.println(sr);



 

}





}





success
  
浮动注释


免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
POST
订单列表/订单详情
POST
支付状态轮询
POST
退款请求
POST
退款查询
POST
退款记录列表/退款记录详情
POST
关闭订单
POST
分账订单列表/详情(beta)
POST
统一收银台接口(beta)
线下支付
POST
条码支付（B扫C）
POST
扫码支付（C扫B、支付宝链接跳转）
POST
撤销订单
POST
当面付解码获取userid、openid（支付宝官方、微信官方）
POST
聚合扫码支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
POST
MD5签名校对（测试环境）
VIEW
C语言
VIEW
php语言
VIEW
java语言
VIEW
php语言图片上传DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
支付完成异步通知参数
退款完成异步通知参数
全局状态码
java语言
小程序收银台支付解决方案
订单列表/订单详情
http
订单列表/订单详情
聚合支付
POST
https://api2uat.lfwin.com/payapi/pay/order_list

 发送
文档模拟Mock
Body
service
string
是
接口名称
选项 
order.list
apikey
string
是
商家APIKEY，唯一标识，由服务商提供
00014005
nonce_str
string
是
随机字符串，长度不超过32位
123456
sign
string
是
签名，参照签名算法
09b926011f6316b3614cf53dd76f51ec
orderid
string
否
系统订单号，唯一值
201907151540461188277576
order_time_s
string
否
下单开始时间，时间戳，精确到秒
1694709031
order_time_e
string
否
下单结束时间，时间戳，精确到秒
1694759441
page_no
string
否
第几页，默认第1页
1
page_size
string
否
每页条数，默认20条,最大200条
2
mch_orderid
string
否
商户订单号，如果orderid跟mch_orderid同时存在，以orderid为准, 如果只传商户订单号，必须传下单时间
order_time
string
否
下单时间，时间戳，精确到秒，如果只传商户订单号，必须传下单时间
1564588800
paytype
string
否
支付方式：1条码 2扫码 3声波(已废弃) 4公众号 5小程序 6刷脸 等
2
bank_type
string
否
支付渠道： 0官方； 3 富友(间联2)； 4 间联3(下线)； 5合利宝(间联1)； 6乐刷(间联4)；等
m_paytype
string
否
支付通道：alipay=支付宝; wxpay=微信;unpay=云闪付;best=翼支付;dgpay=龙支付;dcpay=数字人民币;bdpay=百度钱包;todo,comm或空字符串=未知支付方式 等
wxpay
is_refund
string
否
退款标识： 0无退款或撤销 1有请求撤销 2有请求退款 ，撤销或退款状态请使用退款查询接口
2
fix_qrcode
string
否
是否固定二维码收款订单(包括H5支付)： 0 否 ，1是
paystatus
string
否
支付状态： 1支付成功， 2支付失败， 0待支付
mid
string
否
收款店员id
trade_no
string
否
订单流水号, 如果只传订单流水号，必须传下单时间
2019100922001450160572433541
scope
string
否
返回订单列表的范围：1、返回apikey的收款订单列表，2、返回apikey所属的门店的订单列表， 3、返回apikey所属商户的订单号列表，默认或者不传为 1
version
string
否
版本号，不传默认version=3.0，版本号可选version=4.0，该订单如果有退款会最近一笔退款的状态
4.0
sign_type
string
否
签名方式，支持MD5或者RSA ，默认MD5
 详细说明状态码
订单列表验签附加说明:
返回lists是数组格式，验签的时候数组json格式化成json字符串，参与签名

例如:

代签名字符串=

charset=UTF-8&current_no=1&lists=[{"orderid":"20171219163537000143109","trade_no":"","paymoney":"0.01","pri_paymoney":"0.01","receipt_amount":"0.00","buyer_pay_amount":"0.00","coupon_fee":"0.00","paytime":"0","paystatus":"0","order_time":"1513672537","mch_orderid":"","paytype":"1","bank_type":"0","m_paytype":"alipay","is_refund":"0"},{"orderid":"20171219163742000149502","trade_no":"","paymoney":"0.01","pri_paymoney":"0.01","receipt_amount":"0.00","buyer_pay_amount":"0.00","coupon_fee":"0.00","paytime":"0","paystatus":"0","order_time":"1513672662","mch_orderid":"","paytype":"1","bank_type":"0","m_paytype":"alipay","is_refund":"0"}]&message=SUCCESS&page_size=2&sign_type=MD5&status=10000&total_items=558&total_no=279&version=3.0&signkey=punr8ucu









订单详情各金额关系
如需全额退款请使用paymoney返回的金额










各单号和后台订单对应关系如下:
orderid: 平台唯一订单号

trade_no: 官方流水号

mch_orderid: 外部订单号, 通常是对接方回传的商户系统单号, 正常也需确保唯一

dis_name: 通道方订单号, 通常指间连通道方的唯一单号











支付交易渠道( json字符串) 说明： 
支付宝：

例如：[{"amount":"9.98","fund_channel":"MDISCOUNT"},{"amount":"0.02","fund_channel":"ALIPAYACCOUNT"}]



amont:金额，单位元；

fund_channel 交易渠道：

COUPON ：       支付宝红包

ALIPAYACCOUNT： 支付宝账户

POINT:            集分宝

DISCOUNT:        折扣券

PCARD:           预付卡

MCARD:           商家储值卡

MDISCOUNT:      商户优惠券

MCOUPON:        商户红包

PCREDIT:          蚂蚁花呗



微信

例如：{"promotion_detail":[{"promotion_id":"2683768328","name":"单品15号测试优惠","scope":"SINGLE","type":"DISCOUNT","amount":1,"activity_id":"4218002","wxpay_contribute":0,"merchant_contribute":1,"other_contribute":0,"goods_detail":[{"goods_id":"6923450656181","quantity":1,"price":2,"discount_amount":1}]}]}

promotion_detail：营销详情

promotion_id：券ID

name：优惠名称

scope：优惠范围

amount：优惠面额，单位分

activity_id：活动ID

wxpay_contribute：微信出资，单位分

merchant_contribute：商户出资，单位分

other_contribute：其他出资，单位分

goods_detail：单品列表

goods_id：商品编码

goods_remark：商品备注

discount_amount：商品优惠金额，单位分

quantity：商品数量

price：商品价格，单位分

success
{
"total_items": "1610",总记录数 <string>
"page_size": "2",每页记录数 <string>
"current_no": "1",当前页码 <string>
"total_no": "805",总页码数目 <string>
-"lists": [记录列表，array（）数组类型，对应key参数如下<array>
-{
"orderid": "202312261755442046679565",系统订单号 <string>
"trade_no": "",官方流水号 <string>
"paymoney": "620.00",下单支付金额，单位元 <string>
"pri_paymoney": "620.00",商家优惠活动后，实际支付金额，单位元 (该金额会随退款变动, 表示剩余可退金额) <string>
"receipt_amount": "620.00",商家实收金额，单位元 <string>
"buyer_pay_amount": "620.00",买家付款金额，单位元 <string>
"coupon_fee": "0.00",代金券金额，单位元 <string>
"paytime": "0",支付时间 <string>
"buyer_account": "",支付账号 <string>
"mid": "0",收款店员ID <string>
"remarks": "",订单备注 <string>
"cdid": "700",设备ID <string>
"paystatus": "2",支付状态 1支付成功 2支付失败 0待支付 <string>
"order_time": "1703584544",下单时间 <string>
"mch_orderid": "2023122648554953_yl_1703584544",商户订单号 <string>
"paytype": "4",支付方式：1条码 2扫码 3声波(已废弃) 4公众号 5小程序 6刷脸 等 <string>
"bank_type": "0",支付渠道： 0官方； 3 富友(间联2)； 4 间联3(下线)； 5合利宝(间联1)； 6乐刷(间联4)；等 <string>
"m_paytype": "wxpay",支付通道：:alipay=支付宝; wxpay=微信;unpay=云闪付;best=翼支付;dgpay=龙支付;dcpay=数字人民币;bdpay=百度钱包; todo,comm或空字符串=未知支付方式 等 <string>
"is_refund": "3",标识：2退款 1撤销 0普通订单 <string>
"fund_bill_list": "",交易支付使用的资金渠道 <string>
"fix_qrcode": "0",是否固定二维码收款订单(包括H5支付)： 0 否 ，1是 <string>
"storediscount": "0.00",商家店铺优惠活动金额，单位元 <string>
"refundmoney": "0.00",累计退款总金额，单位元 <string>
"point_amount": "0.00",... <string>
"service": "pay.wxpay.jspay",该订单的支付service <string>
"openid": "",... <string>
"buyer_id": "",... <string>
"handmoney": "1.98",交易手续费，单位元 <string>
"controlType": "1",订单是否管控，1是，0否 <string>
"source": "1",... <string>
"is_sharing": "0",是否分账订单，0普通订单，1分账订单 <string>
"company": "汕头市微赢家信息科技有限公司",商家名称 <string>
"nickname": "微赢家",商家昵称 <string>
"shopname": "微赢家灵风分店"... <string>
}
],
"version": "3.0",接口版本号，3.0 <string>
"charset": "UTF-8",接口字符编码，UTF-8 <string>
"message": "SUCCESS",错误信息 <string>
"status": "10000",错误状态 <string>
"sign_type": "MD5",签名方式，MD5 <string>
"sign": "0764e9d17495423bf34b7e71d10a9605"签名字符串，参照签名方法 <string>
}
浮动注释
total_items
总记录数
number
page_size
每页记录数
number
current_no
当前页码
number
total_no
总页码数目
number
lists
记录列表，array（）数组类型，对应key参数如下
array
lists.orderid
系统订单号
number
lists.trade_no
官方流水号
string
lists.paymoney
下单支付金额，单位元
number
lists.pri_paymoney
商家优惠活动后，实际支付金额，单位元 (该金额会随退款变动, 表示剩余可退金额)
number
lists.receipt_amount
商家实收金额，单位元
number
lists.buyer_pay_amount
买家付款金额，单位元
number
lists.coupon_fee
代金券金额，单位元
number
lists.paytime
支付时间
number
list.shopname
门店名称
string
lists.paystatus
支付状态 1支付成功 2支付失败 0待支付
number
lists.order_time
下单时间
number
lists.mch_orderid
商户订单号
string
lists.paytype
支付方式：1条码   2扫码  3声波(已废弃)  4公众号  5小程序  6刷脸 等
string
lists.bank_type
支付渠道： 0官方；  3 富友(间联2)； 4 间联3(下线)；  5合利宝(间联1)；  6乐刷(间联4)；等
string
lists.m_paytype
支付通道：:alipay=支付宝; wxpay=微信;unpay=云闪付;best=翼支付;dgpay=龙支付;dcpay=数字人民币;bdpay=百度钱包; todo,comm或空字符串=未知支付方式 等
string
lists.is_refund
标识：2退款  1撤销    0普通订单
string
lists.fund_bill_list
交易支付使用的资金渠道
string
lists.fix_qrcode
是否固定二维码收款订单(包括H5支付)： 0 否 ，1是
string
version
接口版本号，3.0
number
charset
接口字符编码，UTF-8
string
message
错误信息
string
status
错误状态
number
sign_type
签名方式，MD5
string
sign
签名字符串，参照签名方法
string
lists.buyer_account
支付账号
string
lists.mid
收款店员ID
string
lists.remarks
订单备注
string
lists.cdid
设备ID
string
lists.company
商家名称
string
lists.nickname
商家昵称
string
lists.storediscount
商家店铺优惠活动金额，单位元
string
lists.refundmoney
累计退款总金额，单位元
string
lists.service
该订单的支付service
string
lists.refund_status
version=4.0，该订单如果有退款会最近一笔退款的状态,其他不返回该字段
string
lists.handmoney
交易手续费，单位元
string
lists.sharing_handmoney
订单余额分账预扣手续费，单位元（目前仅乐刷开通余额分账时返回）
string
lists.is_sharing
是否分账订单，0普通订单，1分账订单
string
lists.controlType
订单是否管控，1是，0否
string


免费版
聚合支付
聚合支付3.0（测试环境）
搜索 名称 和 URL
历史列表
开发指引
VIEW
对接须知
VIEW
各场景支付接入指引
VIEW
签名算法
VIEW
支付完成异步通知参数
VIEW
退款完成异步通知参数
VIEW
全局状态码
订单通用接口
POST
订单列表/订单详情
POST
支付状态轮询
POST
退款请求
POST
退款查询
POST
退款记录列表/退款记录详情
POST
关闭订单
POST
分账订单列表/详情(beta)
POST
统一收银台接口(beta)
线下支付
POST
条码支付（B扫C）
POST
扫码支付（C扫B、支付宝链接跳转）
POST
撤销订单
POST
当面付解码获取userid、openid（支付宝官方、微信官方）
POST
聚合扫码支付
线上支付
VIEW
线上支付对接须知
POST
微信公众号、小程序支付
POST
支付宝生活号、小程序支付
VIEW
微信小程序跳转支付
POST
H5支付（支付宝官方通道）
POST
APP支付（支付宝官方通道）
POST
H5支付（微信官方通道）
POST
APP支付（微信官方通道）
POST
链接跳转支付（避免主体不一致问题）
VIEW
小程序收银台支付解决方案
接口DEMO
POST
MD5签名校对（测试环境）
VIEW
C语言
VIEW
php语言
VIEW
java语言
VIEW
php语言图片上传DEMO
分账通用接口
汇总分账(乐刷通道)
订单分账(微信官方通道)
业务相关
刷脸支付
其他支付
商户进件
预授权支付
统计相关
合利宝余额分账
直付通
备忘录
开放平台
码上惠
免费版接口数 ( 196 / 200 )
未选择环境
微信公众号、小程序支付
对接须知
各场景支付接入指引
签名算法
支付完成异步通知参数
退款完成异步通知参数
全局状态码
java语言
小程序收银台支付解决方案
订单列表/订单详情
支付状态轮询
http
支付状态轮询
聚合支付
POST
https://api2uat.lfwin.com/payapi/pay/query_order

 发送
文档模拟Mock
Body
service
string
是
接口名称
选项 
pay.comm.query_order
orderid
string
是
系统订单号，与商户订单号mch_orderid、dis_name三选一
201807101612530070089833
apikey
string
是
商家APIKEY，唯一标识，由服务商提供
00014005
sign
string
是
签名，参照签名算法
d347ea0a78d2e0581aecca6634e2748d
nonce_str
string
是
随机字符串，长度不超过32位
123456
mch_orderid
string
否
商户订单号，如果orderid跟mch_orderid同时存在，以orderid为准
15181042584813748
order_time
string
否
下单时间，时间戳，精确到秒，如果只传商户订单号，必须传下单时间
1518104258
dis_name
string
否
间联渠道订单号
1001190599123456
sign_type
string
否
签名方式，支持MD5或者RSA ，默认MD5
 详细说明状态码
下单后，若返回支付状态为待支付，需要通过本接口查询订单支付状态。也可通过本接口查询订单详情。




请判断status==10000，且paystatus==1，表示支付成功。
请判断status==10000，且paystatus==2，表示支付失败。
其他不明确状态均可作为待付款状态处理
特殊情况：如遇到HTTP请求失败(504超时异常等)也需按待付款处理，可继续轮询查询状态也可联系客服确认，最终以上述两种明确状态为准。




接口支持传orderid、mch_orderid、dis_name，三选一；

当选择传mch_orderid时，必须同时传下单时间的时间戳order_time，order_time可不用特别精确，能让接口定位订单所在月的数据表即可。



频次建议每2秒查询一次（过快查询容易被防火墙拉黑），查询1分钟左右就可以停止了。

如1分钟后状态不明请让商户与顾客确认支付结果，再手动查询订单状态。

success
{
"service": "pay.wxpay.query_order",接口名称 <string>
"orderid": "20171220114136000149917",系统订单号，唯一值 <string>
"trade_no": "4200000017201712207715771529",通道流水号 <string>
"paystatus": "1",支付状态：1 支付成功 ，0待付款，2付款失败 <string>
"paymoney": "0.01",下单支付金额，单位元 <string>
"pri_paymoney": "0.01",实际支付金额，单位元 <string>
"order_time": "1513741296",下单时间。时间戳，单位秒 <string>
"paytime": "1513741304",支付成功时间。时间戳，单位秒 <string>
"mch_orderid": "",商户外部订单号 <string>
"receipt_amount": "0.00",商家实收金额，单位元 <string>
"buyer_pay_amount": "0.01",买家付款金额，单位元 <string>
"coupon_fee": "0.00",代金券金额，单位元 <string>
"buyer_account": "微信",消费者账号 <string>
"controlType": "1",订单是否管控，1是，0否 <string>
"version": "3.0",接口版本号，3.0 <string>
"charset": "UTF-8",接口字符编码，UTF-8 <string>
"message": "SUCCESS",错误信息 <string>
"status": "10000",错误状态 <string>
"sign_type": "MD5",签名方式，MD5 <string>
"sign": "5caabe5f5b54e2402ca765a54894fb2e"签名字符串，参照签名方法 <string>
}
浮动注释
service
接口名称
string
orderid
系统订单号，唯一值
number
trade_no
通道流水号
number
paystatus
支付状态：1 支付成功 ，0待付款，2付款失败
number
paymoney
下单支付金额，单位元
number
pri_paymoney
实际支付金额，单位元
number
order_time
下单时间。时间戳，单位秒
number
paytime
支付成功时间。时间戳，单位秒
number
mch_orderid
商户外部订单号
string
receipt_amount
商家实收金额，单位元
number
buyer_pay_amount
买家付款金额，单位元
number
coupon_fee
代金券金额，单位元
number
version
接口版本号，3.0
number
charset
接口字符编码，UTF-8
string
message
错误信息
string
status
错误状态
number
sign_type
签名方式，MD5
string
sign
签名字符串，参照签名方法
string
storediscount
商家店铺优惠活动金额，单位元
string
refundmoney
累计退款总金额，单位元
string
buyer_account
消费者账号
string
m_paytype
支付方式：alipay=支付宝; wxpay=微信;unpay=云闪付;best=翼支付;dgpay=龙支付;dcpay=数字人民币;bdpay=百度钱包;todo,comm或空字符串=未知支付方式 等
string
controlType
订单是否管控，1是，0否
string
