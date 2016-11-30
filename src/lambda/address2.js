var AWS = require('aws-sdk');
var dynamo =  new AWS.DynamoDB.DocumentClient();

exports.handler = function(event, context) {
    var tableName = "deviceAddress";
    var array = [];
    
    dynamodb.scan({
        TableName : tableName,
        Limit : 10
    }, function(err, data) {
        if (err) {
            context.done('error','reading dynamodb failed: '+err);
        }
        for (var i in data.Items) {
            var params = {
                TableName: "Users",
                KeyConditionExpression: "#deviceId = :deviceId",
                ExpressionAttributeNames:{
                    "#deviceId": "deviceId"
                },
                ExpressionAttributeValues: {
                    ":deviceId": i.deviceId
                }
            };
     
            var requests = docClient.query(params);
            array.push(request.Items);
        }
        data.Items = array
    });
};