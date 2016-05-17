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

class Demanderami extends REST_Controller
{   
    function checkdemande_get()
    {
        $this->load->model('asvuser_model');
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
			if($this->get('iduser', TRUE))
        	{
        		$this->load->model('asvdemander_model');
        		$results = $this->asvdemander_model->check($user[0]->iduserasv, $this->get('iduser', TRUE));

				$resultat = array();
				foreach ($results as $result) {
					array_push($resultat, (array) $result);
				}
				$results = $resultat;
	             
		        if($results)
		        {
		            $this->response($results, 200); // 200 being the HTTP response code
		        }
		        else
		        {
		            $this->response(array('error' => 'Couldn\'t find any $results!'), 404);
		        }
			}else{
        		$this->response(array(array('success' => FALSE)));
			}
		}else{
        	$this->response(array(array('success' => FALSE)));
		}
    }
    
    function loaddemande_get()
    {
		if($this->get('iduseridmobile', TRUE))
        {
			$this->load->model('asvuser_model');
			$this->load->model('asvdemander_model');
			
			$id_user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			if(is_numeric($id_user[0]->iduserasv)){
				$demandes = $this->asvdemander_model->loaddemande($id_user[0]->iduserasv);
				$demandestemp = array();
				foreach ($demandes as $demande) {
					array_push($demandestemp, (array) $demande);
				}
				$demandes = $demandestemp;
				if($demandes)
		        {
		            $this->response($demandes, 200); // 200 being the HTTP response code
		        }
		        else
		        {
		            $this->response(array('error' => 'Couldn\'t find any $chats!'), 404);
		        }
			}
	    }
    }

	function demander_post()
    {
		$this->load->model('asvuser_model');
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
			$this->load->library('form_validation');
			
			$this->form_validation->set_rules('iduser', '"iduser"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->load->model('asvdemander_model'); 
				$result = $this->asvdemander_model->demander($user[0]->iduserasv, $this->post('iduser'));
        		$this->response(array(array('success' => TRUE)));
			}
			else{
				$this->response(array(array('success' => FALSE)));
			}
		}
	}

	function accepter_post()
    {
		$this->load->model('asvuser_model');
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
			$this->load->library('form_validation');
			
			$this->form_validation->set_rules('iduser', '"iduser"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->load->model('asvdemander_model'); 
				$result = $this->asvdemander_model->accepter($this->post('iduser'), $user[0]->iduserasv);
        		$this->response(array(array('success' => TRUE)));
			}else{
        		$this->response($message, 200);
			}
		}else{
        	$this->response($message, 200);
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