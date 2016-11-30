delete from pickuprequest; 
delete from deviceaddress;

copy pickuprequest from 'dynamodb://pickupRequest'
readratio 100
credentials 'aws_access_key_id=AKIAIWJ3UG4W3KIHHDNA;aws_secret_access_key=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'
timeformat 'YYYY-MM-DD HH:MI:SS';


copy deviceaddress from 'dynamodb://deviceAddress'
readratio 100
credentials 'aws_access_key_id=AKIAIWJ3UG4W3KIHHDNA;aws_secret_access_key=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';
commit;
