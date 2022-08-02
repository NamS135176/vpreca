## Get SSL pinning from server
openssl s_client -servername YOUR_SERVER -connect YOUR_SERVER:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64

example:
openssl s_client -servername execute-api.ap-southeast-1.amazonaws.com -connect execute-api.ap-southeast-1.amazonaws.com:443 | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64

## Get SSL pinning from root CA 

openssl x509 -inform der -in YOUR_ROOT_CA.cer -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64


openssl s_client -connect vpcevssl.lifecard.co.jp:443 2>/dev/null </dev/null |  sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' | openssl x509 -outform DER > vpcevssl.lifecard.co.jp.cer
