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
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/ugroup/5/1/zj1id
curl -X POST  -H "Content-Type: application/json" http://localhost:9000/api/ugroup/5/2/zj2id
}

init > /dev/null 2>&1

# 4>  创建流程
res=$(curl -X POST  -H "Content-Type: application/json" -d '{"basicInfo":{"applyUserName":"wangqi","coalIndex_NCV":1,"coalIndex_ADV":0.02,"financingDays":30,"downstreamContractNo":"24678","financeEndTime":"2017-01-10 10:06:09","applyUserId":"f1id","upstreamContractNo":"没合同","applyCompanyId":"1","coalAmount":0.01,"financeCreateTime":"2017-01-10 10:06:09","businessCode":"123","coalIndex_RS":0.01,"applyCompanyName":"阿里巴巴","applyUserPhone":"13000000001","stockPort":"heheh","interestRate":0.5,"coalType":"不知道","downstreamCompanyName":"企鹅","financingAmount":1000},"investigationInfo":{"finalConclusion":"aaa","transitPort":"123","qualityInspectionUnit":"hhwh","historicalCooperationDetail":"heheh ","downstreamContractCompany":"百度","businessRiskPoint":"hhwh","financingPeriod":1,"transportParty":"hahah","quantityInspectionUnit":"wwww","applyCompanyName":"阿里巴巴","terminalServer":"heheh ","mainBusinessInfo":"hahah","upstreamContractCompany":"企鹅","businessStartTime":"2017-01-10 10:06:09","interestRate":0.01,"businessTransferInfo":"123","ourContractCompany":"阿里巴巴","financingAmount":1000,"performanceCreditAbilityEval":"wwww"},"supervisorInfo":{"finalConclusion":"没意见","historicalCooperationDetail":"hehe","operatingStorageDetail":"heihei","storageProperty":"天空","supervisionCooperateDetail":"Bukan","supervisionScheme":"ssss","storageLocation":"上海","portStandardDegree":"haha","storageAddress":"1000弄"}}
' http://localhost:9000/api/cang/startflow) 2>/dev/null;

#read a

flow_id=$(echo $res | jq ".data.flow_id")
flow_id='"cang!financer-1!f1id!1"'
echo "flow_id = $flow_id"

# 5> 用贸易方身份查看任务
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

#read a

# 6> 贸易方指定四个参与方
k='{"flowId":'$flow_id',"taskId":'$task_id', "harborUserId":"h1id","harborCompanyId":"1","supervisorUserId":"s1id","supervisorCompanyId":"1","fundProviderCompanyId":"1","fundProviderUserId":"zj1id","fundProviderAccountantUserId":"zj2id"}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a11SelectHarborAndSupervisor/77777/trader/88888888 -d "$k")
echo $res

#read a

# 7> 融资方上传文件
res=$(curl -X GET http://localhost:9000/api/utask/financer/1/f1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"fileList":["1","2"]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a12FinishedUpload/f1id/financer/1 -d "$k")
echo $res
#read a

#8>监管方上传合同
res=$(curl -X GET http://localhost:9000/api/utask/supervisor/1/s1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"fileList":["3","4"]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a14FinishedUpload/s1id/supervisor/1 -d "$k")
echo $res
#read a


#9>港口上传合同和确认吨数
res=$(curl -X GET http://localhost:9000/api/utask/harbor/1/h1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"harborConfirmAmount":1000.12,"fileList":["5","6"]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a13FinishedUpload/h1id/harbor/1 -d "$k")
echo $res
#read a

#10>贸易方审核
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"approvedStatus":1,"fundProviderInterestRate":1024.1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a15traderAudit/77777/trader/88888888 -d "$k")
echo $res
#read a


#11>贸易商给出放款建议金额
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/88888)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"loanValue":1024.1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a16traderRecommendAmount/88888/trader/88888888 -d "$k")
echo $res
#read a


#12>资金方审核
res=$(curl -X GET http://localhost:9000/api/utask/fundProvider/1/zj1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"approvedStatus":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a17fundProviderAudit/zj1id/fundProvider/1 -d "$k")
echo $res
#read a


#13>资金方财务审核
res=$(curl -X GET http://localhost:9000/api/utask/fundProvider/1/zj2id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a18fundProviderAccountantAudit/zj2id/fundProvider/1 -d "$k")
echo $res
#read a


#sleep 5

#14>资金方放款（自动任务）
#k='http://localhost:9000/api/cang/fortest/'$flowId'/fundProviderPaySuccess/success'
#echo $k
#res=$(curl -X GET http://localhost:9000/api/cang/fortest/cang\!financer-1\!f1id\!1/fundProviderPaySuccess/success)
#echo $res

read a

#sleep 5
#15>贸易方向融资方放款（自动任务）
#res=$(curl -X GET http://localhost:9000/api/cang/fortest/cang\!financer-1\!f1id\!1/traderPaySuccess/success)
#echo $res

read a

#16>融资方确认回款
res=$(curl -X GET http://localhost:9000/api/utask/financer/1/f1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"repaymentValue":512.1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a19SecondReturnMoney/f1id/financer/1 -d "$k")
echo $res

#read a

#17>融资方回款自动任务
#sleep 5
#res=$(curl -X GET http://localhost:9000/api/cang/fortest/cang\!financer-1\!f1id\!1/financerPaySuccess/success)
#echo $res
read a

#18>贸易方通知港口放款
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"fileList":["7","8"],"redemptionAmount":500.12,"goodsReceiveCompanyName":"腾讯"}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a20noticeHarborRelease/77777/trader/88888888 -d "$k")
echo $res

#read a

#19>港口放货
res=$(curl -X GET http://localhost:9000/api/utask/harbor/1/h1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a21harborRelease/h1id/harbor/1 -d "$k")
echo $res

#read a

#20>贸易商确认回款完成
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":0}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a22traderAuditIfComplete/77777/trader/88888888 -d "$k")
echo $res
#read a

#############################################################################循环##############
#16>融资方确认回款
res=$(curl -X GET http://localhost:9000/api/utask/financer/1/f1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"repaymentValue":511}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a19SecondReturnMoney/f1id/financer/1 -d "$k")
echo $res

#read a

#17>融资方回款自动任务
#sleep 5
#res=$(curl -X GET http://localhost:9000/api/cang/fortest/cang\!financer-1\!f1id\!1/financerPaySuccess/success)
#echo $res

read a

#18>贸易方通知港口放款
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"fileList":["9","10"],"redemptionAmount":500,"goodsReceiveCompanyName":"腾讯"}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a20noticeHarborRelease/77777/trader/88888888 -d "$k")
echo $res

#read a

#19>港口放货
res=$(curl -X GET http://localhost:9000/api/utask/harbor/1/h1id)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a21harborRelease/h1id/harbor/1 -d "$k")
echo $res

#read a

#20>贸易商确认回款完成
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a22traderAuditIfComplete/77777/trader/88888888 -d "$k")
echo $res

#read a

##################################################################################################

#21>贸易商确认回款
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/77777)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a23ReturnMoney/77777/trader/88888888 -d "$k")
echo $res

#read a

#22>贸易商财务确认回款，流程结束
res=$(curl -X GET http://localhost:9000/api/utask/trader/88888888/88888)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":'$flow_id',"taskId":'$task_id',"status":1}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financeorders/action/a24AccountantReturnMoney/88888/trader/88888888 -d "$k")
echo $res

read a

#23>贸易商回款自动任务
#res=$(curl -X GET http://localhost:9000/api/cang/fortest/cang\!financer-1\!f1id\!1/traderRepaySuccess/success)
#echo $res
#read a
