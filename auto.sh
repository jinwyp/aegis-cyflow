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
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/cang/financeorders/action/a11SelectHarborAndSupervisor -d "$k")
echo $res









