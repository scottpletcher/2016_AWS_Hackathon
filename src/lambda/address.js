var AWS = require('aws-sdk');
var dynamo =  new AWS.DynamoDB.DocumentClient();

exports.handler = (event, context, callback) => {
    
    params = {
        TableName : 'deviceAddress',
    };
    
    dynamo.scan(params,function(err,data){
        if (err) callback(err);
        else callback(undefined,data);
    });
};