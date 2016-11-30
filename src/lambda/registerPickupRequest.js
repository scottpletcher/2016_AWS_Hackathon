'use strict';

var AWS = require('aws-sdk');

console.log('Loading function');


exports.handler = (event, context, callback) => {

var dynamo =  new AWS.DynamoDB.DocumentClient();

var id = Date.now().toString();



    var eventText = JSON.stringify(event, null, 2);
    console.log("Received event:", eventText);
    
    event.requestType = "requested";
    
    if (event.clickType === "SINGLE")
        event.deviceType = "garbage";
    if (event.clickType === "DOUBLE")
        event.deviceType = "recycling";
    if (event.clickType === "LONG")
        event.deviceType = "composting";

var deviceId;     
   
    if (! event.serialNumber) {
        deviceId = event.deviceId;
    } else
    {
        deviceId = event.serialNumber;
    }
    
        
    
var params = {
        TableName : 'pickupRequest',
        Item : {
              "id"          : id,
              "deviceId"    : deviceId,
              "deviceType"  : event.deviceType,
              "status"      : event.requestType,
              "timestamp": new Date().toISOString().
                            replace(/T/, ' ').      // replace T with a space
                            replace(/\..+/, '')
        }
};

    
    console.log(params);
    
    dynamo.put(params, function(err, data) {
        if (err) {
            console.log(err);
            callback(err);
        } else {
            callback(undefined,data);
        }
        
        
    });
    
};