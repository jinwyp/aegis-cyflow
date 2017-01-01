#!/bin/bash

#./reset.sh

# 1.创建公司（inst）

init() {
curl -XPOST  -H "Content-Type: application/json" http://localhost:9000/api/inst -d '{"party":"financer","instanceId":"1","companyName":"融资方1"}'
curl -XPOST  -H "Content-Type: application/json" http://localhost:9000/api/inst -d '{"party":"fundProvider","instanceId":"1","companyName":"资金方1"}'
curl -XPOST  -H "Content-Type: application/json" http://localhost:9000/api/inst -d '{"party":"harbor","instanceId":"1","companyName":"港口1"}'
curl -XPOST  -H "Content-Type: application/json" http://localhost:9000/api/inst -d '{"party":"supervisor","instanceId":"1","companyName":"监管1"}'

# 2.创建3个用户，
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/financer/1/f1id -d '{"password":"123456","phone":"1310000001","email":"f1@123.com","name":"f1","username":"f1"}'
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/harbor/1/h1id -d '{"password":"123456","phone":"1310000001","email":"h1@123.com","name":"h1","username":"h1"}'
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/supervisor/1/s1id -d '{"password":"123456","phone":"1310000001","email":"s1@123.com","name":"s1","username":"s1"}'
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/fundProvider/1/zj1id -d '{"password":"123456","phone":"1310000001","email":"zj1@123.com","name":"zj1","username":"zj1"}'
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/user/fundProvider/1/zj2id -d '{"password":"123456","phone":"1310000001","email":"zj2@123.com","name":"zj2","username":"zj2"}'

# 3 创建user-group 关系
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/ugroup/4/1/zj1id
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/ugroup/4/2/zj2id
}

init > /dev/null 2>&1

# 4>  创建流程
res=$(curl -X POST  -H "Content-Type: application/json" -d '{"basicInfo":{"applyUserName":"wangqi","coalIndex_NCV": 1, "coalIndex_ADV": 0.02, "financingDays": 30, "downstreamContractNo": "24678", "financeEndTime": "2016-12-29 15:49:24", "applyUserId": "f1id", "upstreamContractNo": "没合同", "applyCompanyId":"1", "coalAmount": 0.01, "financeCreateTime": "2016-12-29 15:49:24", "businessCode": "123", "coalIndex_RS": 0.01, "applyCompanyName": "阿里巴巴", "auditFileList": [{ "name": "文件1", "originName": "www.baidu.com", "url": "12345", "fileType": "default" }, { "name": "文件2", "originName": "www.baidu.com", "url": "23456", "fileType": "default" }], "applyUserPhone": "13000000001", "stockPort": "heheh", "interestRate": 0.5, "coalType": "不知道", "downstreamCompanyName": "企鹅", "financingAmount": 1000 }, "investigationInfo": { "finalConclusion": "asdsa", "transitPort": "123", "qualityInspectionUnit": "hhwh", "historicalCooperationDetail": "wawa", "downstreamContractCompany": "百度", "businessRiskPoint": "adas", "financingPeriod": 1, "transportParty": "hahah", "quantityInspectionUnit": "wwww", "applyCompanyName": "阿里巴巴", "terminalServer": "heheh ", "mainBusinessInfo": "sdasda", "upstreamContractCompany": "企鹅", "businessStartTime": "2016-12-29 15:49:24", "interestRate": 0.01, "businessTransferInfo": "11ss", "ourContractCompany": "阿里巴巴", "financingAmount": 1000, "performanceCreditAbilityEval": "qqqq" }, "supervisorInfo": { "finalConclusion": "没意见", "historicalCooperationDetail": "hehe", "operatingStorageDetail": "heihei", "storageProperty": "天空", "supervisionCooperateDetail": "Bukan", "supervisionScheme": "ssss", "storageLocation": "上海", "portStandardDegree": "haha", "storageAddress": "1000弄" }}' http://localhost:9000/cang/startflow) 2>/dev/null;

flow_id=$(echo $res | jq ".data.flow_id")
echo "flow_id = $flow_id"

# 5> 用贸易方身份查看任务
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

# 6> 贸易方指定四个参与方
k='{"flowId":'$flow_id',"taskId":'$task_id', "harborUserId":"h1id","harborCompanyId":"1","supervisorUserId":"s1id","supervisorCompanyId":"1","fundProviderCompanyId":"1"}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a11SelectHarborAndSupervisor/77777/trader/88888888 -d "$k")
echo $res

# 7> 融资方上传文件
res=$(curl -X GET http://localhost:9000/api/utask/financer/1/f1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"fileList":[{"name":"文件1","originName":"www.baidu.com","url":"12345","fileType":"default"},{"name":"文件2","originName":"www.baidu.com","url":"23456","fileType":"default"}]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a12FinishedUpload/f1id/financer/1 -d "$k")
echo $res

#8>监管方上传合同
res=$(curl -X GET http://localhost:9000/api/utask/supervisor/1/s1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"fileList":[{"name":"文件1","originName":"www.baidu.com","url":"12345","fileType":"default"},{"name":"文件2","originName":"www.baidu.com","url":"23456","fileType":"default"}]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a14FinishedUpload/s1id/supervisor/1 -d "$k")
echo $res

#9>港口上传合同和确认吨数
res=$(curl -X GET http://localhost:9000/api/utask/harbor/1/h1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"confirmCoalAmount":1000.12,"fileList":[{"name":"文件1","originName":"www.baidu.com","url":"12345","fileType":"default"},{"name":"文件2","originName":"www.baidu.com","url":"23456","fileType":"default"}]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a13FinishedUpload/h1id/harbor/1 -d "$k")
echo $res

#10>贸易方审核
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1,"fundProviderInterestRate":1024.1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a15traderAudit/77777/trader/88888888 -d "$k")
echo $res

#11>贸易商给出放款建议金额
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/88888)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"recommendAmount":1024.1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a16traderRecommendAmount/88888/trader/88888888 -d "$k")
echo $res

#12>资金方审核
res=$(curl -X GET http://localhost:9000/api/utask/fundProvider/1/zj1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a17fundProviderAudit/zj1id/fundProvider/1 -d "$k")
echo $res

#13>资金方财务审核
res=$(curl -X GET http://localhost:9000/api/utask/fundProvider/1/zj2id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a18fundProviderAccountantAudit/zj2id/fundProvider/1 -d "$k")
echo $res

#sleep 5

#14>资金方放款（自动任务）
#k='http://localhost:9000/cang/fortest/'$flowId'/fundProviderPaySuccess/success'
#echo $k
res=$(curl -X GET http://localhost:9000/cang/fortest/cang\!financer-1\!f1id\!1/fundProviderPaySuccess/success)
echo $res

#sleep 5
#15>贸易方向融资方放款（自动任务）
res=$(curl -X GET http://localhost:9000/cang/fortest/cang\!financer-1\!f1id\!1/traderPaySuccess/success)
echo $res

#16>融资方确认回款
res=$(curl -X GET http://localhost:9000/api/utask/financer/1/f1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"repaymentAmount":1024.1024}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a19SecondReturnMoney/f1id/financer/1 -d "$k")
echo $res

#17>融资方回款自动任务
#sleep 5
res=$(curl -X GET http://localhost:9000/cang/fortest/cang\!financer-1\!f1id\!1/financerPaySuccess/success)
echo $res

#18>贸易方通知港口放款
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"goodsFileList":[{"name":"文件1","originName":"www.baidu.com","url":"12345","fileType":"default"},{"name":"文件2","originName":"www.baidu.com","url":"23456","fileType":"default"}],"releaseAmount":1024.1,"goodsReceiveCompanyName":"腾讯"}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a20noticeHarborRelease/77777/trader/88888888 -d "$k")
echo $res

#19>港口放货
res=$(curl -X GET http://localhost:9000/api/utask/harbor/1/h1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a21harborRelease/h1id/harbor/1 -d "$k")
echo $res

#20>贸易商确认回款完成
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":0}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a22traderAuditIfComplete/77777/trader/88888888 -d "$k")
echo $res

#############################################################################循环##############
#16>融资方确认回款
res=$(curl -X GET http://localhost:9000/api/utask/financer/1/f1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"repaymentAmount":2048}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a19SecondReturnMoney/f1id/financer/1 -d "$k")
echo $res

#17>融资方回款自动任务
#sleep 5
res=$(curl -X GET http://localhost:9000/cang/fortest/cang\!financer-1\!f1id\!1/financerPaySuccess/success)
echo $res

#18>贸易方通知港口放款
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"goodsFileList":[{"name":"文件2","originName":"www.baidu.com","url":"12345","fileType":"default"},{"name":"文件3","originName":"www.baidu.com","url":"23456","fileType":"default"}],"releaseAmount":2048.1,"goodsReceiveCompanyName":"腾讯"}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a20noticeHarborRelease/77777/trader/88888888 -d "$k")
echo $res

#19>港口放货
res=$(curl -X GET http://localhost:9000/api/utask/harbor/1/h1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a21harborRelease/h1id/harbor/1 -d "$k")
echo $res

#20>贸易商确认回款完成
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a22traderAuditIfComplete/77777/trader/88888888 -d "$k")
echo $res

##################################################################################################

#21>贸易商确认回款
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a23ReturnMoney/77777/trader/88888888 -d "$k")
echo $res

#22>贸易商财务确认回款，流程结束
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/88888)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a24AccountantReturnMoney/88888/trader/88888888 -d "$k")
echo $res


