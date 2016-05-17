<?php if (!defined('BASEPATH')) exit('No direct script access allowed');
 
class Applepush
{
	private $deviceToken;
	private $passphrase;
	private $arrayBody;
     
/*
|===============================================================================
| Constructeur
|===============================================================================
*/
     
    public function __construct()
	{
		// Put your device token here (without spaces):
		$this->deviceToken = '127ffdcae093484fc7cd21aa924e5aa79c21499781095a2b366b53edf858fbf3';
		
		// Put your private key's passphrase here:
		$this->passphrase = 'Pwoyfui8';
		
		// Put your alert message here:
		$this->message = 'My first push notification!';
		
		$this->arrayBody = array(
			'alert' => 'My first push notification!',
			'sound' => 'default',
		    'action' => '1'
			);
	}

	public function setdevicetoken($deviceToken){
		$this->deviceToken = $deviceToken;
	}

	public function setbody($arrayBody){
		$this->arrayBody = $arrayBody;
	}
	
	public function send()
	{
		////////////////////////////////////////////////////////////////////////////////
        
		$ctx = stream_context_create();
		
		stream_context_set_option($ctx, 'ssl', 'local_cert', 'ck.pem');
		stream_context_set_option($ctx, 'ssl', 'passphrase', $this->passphrase);
		
		// Open a connection to the APNS server
		$fp = stream_socket_client(
			'ssl://gateway.sandbox.push.apple.com:2195', $err,
			$errstr, 60, STREAM_CLIENT_CONNECT|STREAM_CLIENT_PERSISTENT, $ctx);
		
		if (!$fp)
			exit("Failed to connect: $err $errstr" . PHP_EOL);
		
		// Create the payload body
		$body['aps'] = $this->arrayBody;
		
		// Encode the payload as JSON
		$payload = json_encode($body);
		
		// Build the binary notification
		$msg = chr(0) . pack('n', 32) . pack('H*', $this->deviceToken) . pack('n', strlen($payload)) . $payload;
		
		// Send it to the server
		$result = fwrite($fp, $msg, strlen($msg));
		
        /*
		if (!$result)
			echo 'Message not delivered' . PHP_EOL;
		else
			echo 'Message successfully delivered' . PHP_EOL;
		*/
		// Close the connection to the server
		fclose($fp);
	}
}