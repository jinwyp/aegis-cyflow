#!/bin/bash
res=$(curl -X GET http://localhost:9000/api/utask/financer/15011101/66661)
task_id=$(echo $res | jq ".tasks | to_entries | map(select(.value.flowId==$flow_id)) | .[0].key")
echo "task_id is $task_id"

k='{"flowId":"cang!financer-15011101!66661!1","taskId":'$task_id',"fileList":["1","2"]}'
res=$(curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/cang/financerTask/a12FinishedUpload/66661/15011101 -d "$k")
echo $res
