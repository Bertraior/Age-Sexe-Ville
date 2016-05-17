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

class Mymessage extends REST_Controller
{
    public function message_get()
    {
    	if($this->get('iduseridmobile', TRUE))
        {
			$this->load->model('asvuser_model');
			$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile'));
			
            if(is_numeric($user[0]->iduserasv)){
            	$this->load->model('asvmessage_model');
                $messages = $this->asvmessage_model->loadmessage($user[0]->iduserasv);
    			
    			$messagestemp = array();
    			foreach ($messages as $message) {
    				array_push($messagestemp, (array) $message);
    			}
    			$messages = $messagestemp;
    			
    	        if($messages)
    	        {
					echo json_encode($messages);
    	            //$this->response($messages, 200); // 200 being the HTTP response code
    	        }
    	
    	        else
    	        {
    	            $this->response(array('error' => 'Couldn\'t find any message!'), 404);
    	        }
            }else{
                $this->response(array('error' => 'Couldn\'t find any message!'), 404);
            }
	    }
    }

	public function messagewith_get()
    {
		if($this->get('iduseridmobile', TRUE) && $this->get('iddestinataire', TRUE))
        {
			$this->load->model('asvuser_model');
			
			$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile'));
            if(is_numeric($user[0]->iduserasv)){
            	$this->load->model('asvmessage_model');
                $messages = $this->asvmessage_model->loadmessagefor($user[0]->iduserasv, $this->get('iddestinataire', TRUE));
    			
    			$messagestemp = array();
    			foreach ($messages as $message) {
    				array_push($messagestemp, (array) $message);
    			}
    			$messages = $messagestemp;
    			
    	        if($messages)
    	        {
					echo json_encode($messages);
    	            //$this->response($messages, 200); // 200 being the HTTP response code
    	        }
    	        else
    	        {
    	            echo json_encode(array('error' => 'Couldn\'t find any message!'));
    	        }
            }else{
            	echo json_encode(array('error' => 'Couldn\'t find any message!'));
            }
	    }
    }

	public function loadnumbermessage_get(){
		if($this->get('iduseridmobile', TRUE))
        {
			$this->load->model('asvuser_model');
			
			$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile'));
            if(is_numeric($user[0]->iduserasv)){
            	$this->load->model('asvmessage_model');
                $nbmessages = $this->asvmessage_model->loadnbnewmessage($user[0]->iduserasv);
    			
    	        $this->response(array('nbmessages' => $nbmessages), 200);
            }else{
                $this->response(array('error' => 'Couldn\'t find any message!'), 404);
            }
	    }
	}
	
	public function updatenumbermessage_get(){
		if($this->get('iduseridmobile', TRUE))
        {
			$this->load->model('asvuser_model');
			
			$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile'));
            if(is_numeric($user[0]->iduserasv)){
            	$this->load->model('asvmessage_model');
                $nbmessages = $this->asvmessage_model->updatevunbmessage($user[0]->iduserasv);
    			
    	        $this->response(array(array('success' => TRUE)));
            }else{
                $this->response(array(array('success' => FALSE)));
            }
	    }
	}
	
    public function envoiemessage_post()
    {
        if($this->get('iduseridmobile', TRUE) && $this->get('iddestinataire', TRUE))
        {
            $this->load->model('asvuser_model');
    		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
    		if(is_numeric($user[0]->iduserasv)){
    			$this->load->library('form_validation');
				$this->form_validation->set_rules('messagetosend', '"messagetosend"', 'trim|required|xss_clean|strip_tags');
				$this->form_validation->set_rules('datemessagemobile', '"datemessagemobile"', 'trim|required|xss_clean|strip_tags');
				
				if ($this->form_validation->run() == TRUE)
                {
                	$this->load->model('asvmessage_model');
					
					$checkmessage = $this->asvmessage_model->checkmessage($user[0]->iduserasv, $this->post('datemessagemobile'));
					if(count($checkmessage) > 0){
						$this->response(array(array('success' => TRUE,
													'idmessage' => $checkmessage[0]->idasvmessage,
													'datecreation' => date("Y-m-d H:i:s")
						)));
					}
					else{
						$idmessage = $this->asvmessage_model->creermessage($this->post('messagetosend'), $user[0]->iduserasv, $this->get('iddestinataire', TRUE), $this->post('datemessagemobile'));
	                    $destinaire = $this->asvuser_model->get_user_by_id($this->get('iddestinataire', TRUE));
						  
						echo json_encode(array(array('success' => TRUE,
													'idmessage' => $idmessage,
													'datecreation' => date("Y-m-d H:i:s")
						)));
						/*
						$this->response(array(array('success' => TRUE,
													'idmessage' => $idmessage,
													'datecreation' => date("Y-m-d H:i:s")
						)));
						*/
						
					}
                }
                else{
                    $this->response($message, 200);
                }
            }
            else{
                $this->response($message, 200);
            }
        }
        else{
            $this->response($message, 200);
        }
    }
}