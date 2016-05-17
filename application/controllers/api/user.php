<?php defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Example
 *
 * This is an example of a few basic user interaction methods you could use
 * all done with a hardcoded array.
 *
 * @package		CodeIgniter
 * @subpackage	Rest Server
 * @category	Controller
 * @author		Phil Sturgeon
 * @link		http://philsturgeon.co.uk/code/
*/

// This can be removed if you use __autoload() in config.php OR use Modular Extensions
require APPPATH.'/libraries/REST_Controller.php';

class User extends REST_Controller
{
	function connect_get()
    {
    }
    
    function connect_post()
    {
    	$this->load->model('asvconnecter_model');
    	$this->load->model('asvuser_model');
		$this->load->library('form_validation');
		
	    $this->form_validation->set_rules('username', '"Adresse email"', 'trim|required|xss_clean|valid_email|strip_tags');
	    $this->form_validation->set_rules('password','"Mot de passe"', 'trim|required|xss_clean|strip_tags');
		//$this->form_validation->set_rules('redid','"redid"', 'trim|required|xss_clean|strip_tags');
		
		if ($this->form_validation->run() == TRUE)
		{
			$graindesel = 'wolfius';
			$password = md5($this->post('password').$graindesel);
			
			$id_user = $this->asvuser_model->normalconnect($this->post('username'), $password);
			if(count($id_user) > 0){
				//$this->asvuser_model->synchro_user_android_gcm($this->post('username'), $password, $this->post('redid'));
				$this->asvconnecter_model->save_connecter($id_user[0]->iduserasv);
				$this->response(array(array('iduserasv' => $id_user[0]->iduserasv, 'pseudoasv' => $id_user[0]->pseudoasv)));
			}else{
				$this->response(array(array('iduserasv' => 0)));
			}
		}else{
			$this->response(array(array('iduserasv' => 0)));
		}
    }
	
	function inscription_post()
    {
    	$this->load->model('asvconnecter_model');
    	$this->load->model('asvuser_model');
		$this->load->library('form_validation');
		
		$this->form_validation->set_rules('pseudo', '"Prenom"', 'trim|required|xss_clean|strip_tags');
	    $this->form_validation->set_rules('username', '"Adresse email"', 'trim|required|xss_clean|strip_tags');
	    $this->form_validation->set_rules('password','"Mot de passe"', 'trim|required|xss_clean|strip_tags');
		$this->form_validation->set_rules('redid','"redid"', 'xss_clean|strip_tags');
		$this->form_validation->set_rules('coco','"coco"', 'xss_clean|strip_tags');
		
		/*
		 * if ($this->form_validation->run() == TRUE && ($this->post('password') == "ios" || $this->post('password') == "android") && (md5("0fob".round(time()/1000)) == substr($this->post('coco'), 5, -3) || md5("0fob".round(time()/1000) - 1) == substr($this->post('coco'), 5, -3)))
		 */ 
		if ($this->form_validation->run() == TRUE)
		{
			$graindesel = 'wolfius';
			$password = md5($this->post('password').$graindesel);
			
			$id_user = $this->asvuser_model->save_user_android_gcm($this->post('username'), $this->post('pseudo'), $password, $this->post('redid'));
			if(is_numeric($id_user)){
				$this->asvconnecter_model->save_connecter($id_user);
				
				/* envoie de mail*/
				$to      = 'bertrand@viravong.fr';
				$subject = trim($this->post('pseudo'))." vient de s'inscrire sur asv";
				$message = trim($this->post('username'))." vient de s'inscrire sur asv";
				$headers = 'From: contact@agesexeville.com' . "\r\n" .
				'Reply-To: contact@agesexeville.com' . "\r\n" .
				'X-Mailer: PHP/' . phpversion();
				mail($to, $subject, $message, $headers);
				
				$this->response(array(array('iduser' => $id_user)));
			}else{
				$this->response(array(array('iduser' => 0)));
			}
		}else{
			$this->response(array(array('iduser' => 0)));
		}
    }

	function modify_pseudo_post()
    {
		$this->load->library('form_validation');
    	$this->load->model('asvuser_model');
		
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
		    $this->form_validation->set_rules('pseudo', '"Pseudo"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->asvuser_model->modify_pseudo($user[0]->iduserasv, $this->post('pseudo'));
				$this->response(array(array('success' => TRUE)));
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}else{
			$this->response(array(array('success' => FALSE)));
		}
    }
	
	function majgcm_post(){
		$this->load->library('form_validation');
    	$this->load->model('asvuser_model');
		
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
		    $this->form_validation->set_rules('gcm', '"gcm"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->asvuser_model->modify_gcm($user[0]->iduserasv, $this->post('gcm'));
				$this->response(array(array('success' => TRUE)));
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}else{
			$this->response(array(array('success' => FALSE)));
		}
	}
	
	function modifyiosdeviceid_post(){
		$this->load->library('form_validation');
    	$this->load->model('asvuser_model');
		
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
		    $this->form_validation->set_rules('iosdeviceid', '"iosdeviceid"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->asvuser_model->maj_user_ios_deviceid($user[0]->iduserasv, $this->post('iosdeviceid'));
				$this->response(array(array('success' => TRUE)));
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}else{
			$this->response(array(array('success' => FALSE)));
		}
	}
    
    function connect_delete()
    {
    	//$this->some_model->deletesomething( $this->get('id') );
        $message = array('id' => $this->get('id'), 'message' => 'DELETED!');
        
        $this->response($message, 200); // 200 being the HTTP response code
    }
    
    function connects_get()
    {
        //$users = $this->some_model->getSomething( $this->get('limit') );
        $users = array(
			array('id' => 1, 'name' => 'Some Guy', 'email' => 'example1@example.com'),
			array('id' => 2, 'name' => 'Person Face', 'email' => 'example2@example.com'),
			3 => array('id' => 3, 'name' => 'Scotty', 'email' => 'example3@example.com', 'fact' => array('hobbies' => array('fartings', 'bikes'))),
		);
        
        if($users)
        {
            $this->response($users, 200); // 200 being the HTTP response code
        }

        else
        {
            $this->response(array('error' => 'Couldn\'t find any users!'), 404);
        }
    }
	
	function signaler_post(){
		$this->load->library('form_validation');
    	$this->load->model('asvuser_model');
		
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
		    $this->form_validation->set_rules('idcommenterasv', '"idcommenterasv"', 'trim|required|xss_clean|strip_tags');
			if ($this->form_validation->run() == TRUE)
			{
				/* envoie de mail*/
				$to      = "contact@geness.fr";
				$subject = $user[0]->iduserasv." signale le post ".$this->post('idcommenterasv');
				$message = $user[0]->iduserasv." signale ".$this->post('idcommenterasv');
				$headers = 'From: contact@geness.fr' . "\r\n" .
				'Reply-To: contact@geness.fr' . "\r\n" .
				'X-Mailer: PHP/' . phpversion();
				mail($to, $subject, $message, $headers);
				$this->response(array(array('success' => TRUE)));
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}
	}
	
	public function iospush_get()
	{
		$this->load->library('applepush');
		$this->applepush->setdevicetoken('c4fa016fad052a0ae780375cf26faef4420e66ee0e535b52509bab93d85ea2b3');
		$this->applepush->setbody(
			array(
	            'alert' => "test alert",
                'badge' => 1,
				'sound' => 'default'
			)
		);
		$this->applepush->send();
	}
}