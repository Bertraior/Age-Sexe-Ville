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

class Commentforum extends REST_Controller
{
	public function check_get()
    {
		if($this->get('iduseridmobile', TRUE) && $this->get('idforum', TRUE) && $this->get('idmessage', TRUE))
        {
			$this->load->model('asvuser_model');
			$this->load->model('asvcommenter_model');
			
			$id_user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			
			$nbcomments = $this->asvcommenter_model->check_new_comment($this->get('idforum', TRUE), $this->get('idmessage', TRUE));
			if($nbcomments > 0){
				$comments = $this->asvcommenter_model->get_all($this->get('idforum', TRUE));
			
				$messages = array();
				foreach ($comments as $comment) {
					array_push($messages, (array) $comment);
				}
				$comments = $messages;
				$this->response($comments, 200);
			}
	        else
	        {
	            $this->response(array('error' => 'Couldn\'t find any $comments!'), 404);
	        }
	    }
    }
	
	
    public function commentsforum_get()
    {
		if($this->get('iduseridmobile', TRUE) && $this->get('idforum', TRUE))
        {
			$this->load->model('asvuser_model');
			$this->load->model('asvconnecter_model');
			$this->load->model('asvcommenter_model');
			
			$id_user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			$this->asvconnecter_model->save_connecter($id_user[0]->iduserasv);
			
			$comments = $this->asvcommenter_model->get_all($this->get('idforum', TRUE));
			
			$idusercommenterasvtemp = 0;
			$messagecommenterasvtemp = "";
			
			$messages = array();
			foreach ($comments as $comment) {
				if($comment->idusercommenterasv == $idusercommenterasvtemp){
					array_pop($messages);
					$newcomment = $comment->messagecommenterasv;
					$comment->messagecommenterasv = $messagecommenterasvtemp."\n".$newcomment;
				}
				array_push($messages, (array) $comment);
				$idusercommenterasvtemp = $comment->idusercommenterasv;
				$messagecommenterasvtemp = $comment->messagecommenterasv;
			}
			$comments = $messages;
             
	        if($comments)
	        {
	            $this->response($comments, 200); // 200 being the HTTP response code
	        }
	        else
	        {
	            $this->response(array('error' => 'Couldn\'t find any $comments!'), 404);
	        }
	    }
    }
	
	public function commente_post(){
		if($this->get('iduseridmobile', TRUE) && $this->get('idforum', TRUE))
        {
			$this->load->model('asvuser_model');
			$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			
			if(is_numeric($user[0]->iduserasv)){
				$this->load->library('form_validation');
				
				$this->form_validation->set_rules('message', '"message"', 'trim|required|xss_clean|strip_tags');
				$this->form_validation->set_rules('datemobile', '"datemobile"', 'trim|required|xss_clean|strip_tags');
				
				if ($this->form_validation->run() == TRUE)
				{
					$this->load->model('asvcommenter_model');
					
					/*
					$this->load->library('blacklist');
					if($this->blacklist->check_text($this->post('message'))->is_blocked() == TRUE && trim($this->post('message')) != "hello"){
						$this->asvcommenter_model->create_comment($user[0]->iduserasv, $this->get('idforum', TRUE), '***', $user[0]->pseudoasv, $this->post('datemobile'));
					}
					else{
			
					*/
						$this->asvcommenter_model->create_comment($user[0]->iduserasv, $this->get('idforum', TRUE), $this->post('message'), $user[0]->pseudoasv, $this->post('datemobile'));
					/*
					}
					*/
					
					$this->load->library('gcm');
					$this->load->library('applepush');
					$members = $this->asvcommenter_model->get_members($this->get('idforum', TRUE));
					$gcm_bool = FALSE;
					$nomforum = "";
	                foreach($members as $member) {
	                	$nomforum = $member->nomforumasv;
						if($user[0]->iduserasv != $member->iduserasv){
							if(isset($member->gcm_regid)){
								$gcm_bool = TRUE;
								$this->gcm->addRecepient($member->gcm_regid);
							}
							if(isset($member->iosdeviceid)){
								$this->applepush->setdevicetoken($member->iosdeviceid);
								$this->applepush->setbody(
									array(
							            'alert' => $nomforum." : ".$user[0]->pseudoasv." a commenté ".$this->post('message'),
                                        'badge' => 1,
										'sound' => 'default'
									)
								);
								$this->applepush->send();
							}
						}
					}
					if($gcm_bool == TRUE){
						$this->gcm->setMessage('Age Sexe Ville');
						
						$this->gcm->setTtl(500);
							
						$this->gcm->setData(array(
				            'text' => $nomforum." : ".$user[0]->pseudoasv." a commenté ".$this->post('message')
				        ));
						$this->gcm->send();
					}
					$this->response(array(array('success' => TRUE)));
				}else{
					$this->response(array(array('success' => FALSE)));
				}
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}
	}

	public function thecommentforum_get(){
		if($this->get('iduseridmobile', TRUE) && $this->get('idmessage', TRUE))
        {
			$this->load->model('asvuser_model');
			$this->load->model('asvcommenter_model');
			
			$id_user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			
			$comments = $this->asvcommenter_model->get_comment($this->get('idmessage', TRUE));
			
			$messages = array();
			foreach ($comments as $comment) {
				array_push($messages, (array) $comment);
			}
			$comments = $messages;
             
	        if($comments)
	        {
	            $this->response($comments, 200); // 200 being the HTTP response code
	        }
	        else
	        {
	            $this->response(array('error' => 'Couldn\'t find any $comments!'), 404);
	        }
			$this->response(array('error' => 'Couldn\'t find any $comments!'), 404); 
	    }
	}

}