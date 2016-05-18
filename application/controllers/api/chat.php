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

class Chat extends REST_Controller
{   
    function create_post()
    {
        $this->load->model('asvuser_model');
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
			$this->load->library('form_validation');
			
			$this->form_validation->set_rules('chat', '"chat"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->load->model('asvforum_model');
				$this->load->model('asvparticiper_model');
				
				/*
				$this->load->library('blacklist');
				if($this->blacklist->check_text($this->post('chat'))->is_blocked() == TRUE){
					$idforum = $this->asvforum_model->create_forum($user[0]->iduserasv, '***');
				}
				else{
				*/ 
					$idforum = $this->asvforum_model->create_forum($user[0]->iduserasv, $this->post('chat'));
				/*
				}
				*/
				$this->asvparticiper_model->participer($user[0]->iduserasv, $idforum);
				$this->response(array(array('success' => TRUE, 'idforum' => $idforum)));
			}else{
        		$this->response(array(array('success' => FALSE)));
			}
		}else{
        	$this->response(array(array('success' => FALSE)));
		}
    }
    
    function chats_get()
    {
		if($this->get('iduseridmobile', TRUE))
        {
			$this->load->model('asvuser_model');
			$this->load->model('asvconnecter_model');
			$this->load->model('asvforum_model');
			
			$id_user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			$this->asvconnecter_model->save_connecter($id_user[0]->iduserasv);
			
			$chats = $this->asvforum_model->get_all();
			
			$forums = array();
			foreach ($chats as $chat) {
				array_push($forums, (array) $chat);
			}
			$chats = $forums;
             
	        if($chats)
	        {
	            $this->response($chats, 200); // 200 being the HTTP response code
	        }
	        else
	        {
	            $this->response(array('error' => 'Couldn\'t find any $chats!'), 404);
	        }
	    }
    }

	public function send_post()
	{
		var_dump($this->request->body);
	}


	public function send_put()
	{
		var_dump($this->put('foo'));
	}
}