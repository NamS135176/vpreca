## Get SSL pinning from server
openssl s_client -servername YOUR_SERVER -connect YOUR_SERVER:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64

example:
openssl s_client -servername execute-api.ap-southeast-1.amazonaws.com -connect execute-api.ap-southeast-1.amazonaws.com:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64

## Get SSL pinning from root CA 

openssl x509 -inform der -in YOUR_ROOT_CA.cer -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64


cat ca.pem | openssl x509 -inform pem -noout -outform pem -pubkey | openssl pkey -pubin -inform pem -outform der | openssl dgst -sha256 -binary | openssl enc -base64
