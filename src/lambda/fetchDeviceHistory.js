var AWS = require('aws-sdk');
var dynamo =  new AWS.DynamoDB.DocumentClient();

exports.handler = (event, context, callback) => {
    
    params = {
        TableName : 'pickupRequest',
        FilterExpression : 'deviceId = :deviceId',
        ExpressionAttributeValues : {':deviceId' : event.deviceId}
    };
    
    dynamo.scan(params,function(err,data){
        if (err) callback(err);
        else callback(undefined,data);
    });
};