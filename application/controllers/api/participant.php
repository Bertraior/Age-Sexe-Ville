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

class Participant extends REST_Controller
{
    function participants_get()
    {
		$participants = NULL;
		if($this->get('iduseridmobile', TRUE) && $this->get('idevenement', TRUE))
        {
        	if($this->get('idgroupe', TRUE)){
        		$this->load->model('user_model');
				$user_id = $this->user_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
				
        		$this->load->model('groupe_model');
				$result = $this->groupe_model->legroupe($user_id[0]->iduser, $this->get('idgroupe', TRUE));
				if(count($result) > 0){
					$this->load->model('appartenir_model');
					$membre = $this->appartenir_model->chargemembreetadmin($result[0]->idgroupe);
					$createurgroupe = $this->user_model->get_user_by_id($result[0]->groupecreateur);
					array_push($membre, $createurgroupe[0]);
					
					$this->load->model('participerevent_model');
					$reponseparticipants = $this->participerevent_model->chargereponse($this->get('idevenement', TRUE));
					foreach ($membre as $onemembre) {
						if($onemembre->iduser == $user_id[0]->iduser){
							$onemembre->userconnect = 1;
						}
						$participe = 0;
						foreach ($reponseparticipants as $reponse) {
							if($onemembre->iduser == $reponse->idparticipereventuser){
								$participe = $reponse->participereventreponse;
							}
						}
						$onemembre->participereventreponse = $participe;
					}
				}
				
			}
			$participants = $membre;
			/*
			foreach($membres as $membre){
				$enattente = TRUE;
				foreach($reponseparticipants as $reponse){
					if($membre->idapparteniruser == $reponse->idparticipereventuser){
						$enattente = FALSE;
					}
				}
				if($enattente == TRUE && $membre->idapparteniruser != $event[0]->iduserevenementcreateur){
					array_push($membresansreponse, $membre);
				}
			}
			$participant = $membres;
			*/
			$listeparticipant = array();
			foreach ($participants as $participant) {
				array_push($listeparticipant, (array) $participant);
			}
			$participants = $listeparticipant;
		}
		
        if($participants)
        {
            $this->response($participants, 200); // 200 being the HTTP response code
        }

        else
        {
            $this->response(array('error' => 'Couldn\'t find any participant!'), 404);
        }
    }

	public function participe_post(){
		$this->load->model('asvuser_model');
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
			$this->load->library('form_validation');
			
			$this->form_validation->set_rules('idforum', '"idforum"', 'trim|required|xss_clean|integer');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->load->model('asvparticiper_model');
				if($this->asvparticiper_model->nb_participants($this->post('idforum')) > 9){
					$this->response(array(array('success' => FALSE)));
				}else{
					$this->asvparticiper_model->participer($user[0]->iduserasv, $this->post('idforum'));
					$this->response(array(array('success' => TRUE)));
				}
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}else{
			$this->response(array(array('success' => FALSE)));
		}
	}
	
	public function quitte_post(){
		$this->load->model('asvuser_model');
		$user = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduserasv)){
			$this->load->library('form_validation');
			
			$this->form_validation->set_rules('idforum', '"idforum"', 'trim|required|xss_clean|integer');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->load->model('asvparticiper_model');
				$this->asvparticiper_model->quitter($user[0]->iduserasv, $this->post('idforum'));
				$this->response(array(array('success' => TRUE)));
			}else{
				$this->response(array(array('success' => FALSE)));
			}
		}else{
			$this->response(array(array('success' => FALSE)));
		}
	}

	public function participant_put()
	{
		var_dump($this->put('foo'));
	}
}