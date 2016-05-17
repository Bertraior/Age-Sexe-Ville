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

	function modify_post()
    {
		$this->load->model('user_model');
		$user = $this->user_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
		
		if(is_numeric($user[0]->iduser)){
			$this->load->library('form_validation');
			
			$this->form_validation->set_rules('idevenementmodif', '"idevenement"', 'trim|required|xss_clean|strip_tags');
			$this->form_validation->set_rules('title', '"title"', 'trim|required|xss_clean|strip_tags');
			$this->form_validation->set_rules('dateStart', '"dateStart"', 'trim|required|xss_clean|strip_tags');
			$this->form_validation->set_rules('dateEnd', '"dateEnd"', 'trim|xss_clean|strip_tags');
			$this->form_validation->set_rules('localisation', '"localisation"', 'trim|xss_clean|strip_tags');
			$this->form_validation->set_rules('description', '"description"', 'trim|xss_clean|strip_tags');
			$this->form_validation->set_rules('groupe', '"groupe"', 'trim|required|xss_clean|strip_tags');
			
			if ($this->form_validation->run() == TRUE)
			{
				$this->load->model('evenement_model'); 
				if($this->post('groupe') == 0){
					$result = $this->evenement_model->maj_eventwithoutfb(
						$this->post('idevenementmodif'),
						$this->post('title'), 
						null,
						$this->post('dateStart'),
						$this->post('dateEnd'),
						$this->post('localisation'), 
						$this->post('description'), 
						$this->post('localisation'), 
						$user[0]->iduser, 
						0);
	        		$this->response(array(array('success' => TRUE)));
	        	}else{
					$this->load->model('groupe_model'); 
					$listemesgroupes = $this->groupe_model->mesgroupes($user[0]->iduser);
					$secure = FALSE;
					foreach ($listemesgroupes as $groupe) {
						if($groupe->idgroupe == $this->post('groupe')){
							$createurgroupe = $groupe->groupecreateur;
							$titredugroupe = $groupe->titre;
							$secure = TRUE;
						}
					}
					if($secure == TRUE){
						$result = $this->evenement_model->maj_eventgroupe(
							$this->post('idevenementmodif'),
							$this->post('title'), 
							NULL, 
							$this->post('dateStart'),
							$this->post('dateEnd'), 
							$this->post('localisation'), 
							$this->post('description'),  
							$this->post('localisation'), 
							0);
					
						$this->groupe_model->maj_time($this->post('groupe'));
						
						//envoie des notifications au gens du groupe
							
						$this->load->model('appartenir_model');
						$membre = $this->appartenir_model->membre($this->post('groupe'));
						
						$this->load->model('notification_model');
						$this->load->library('gcm');
						$this->load->library('applepush');
						
						$gcm_bool = FALSE;
						if($user[0]->iduser != $createurgroupe){
							$this->notification_model->creernotificationmodifiereventgroupe($createurgroupe, $this->post('groupe'), $this->post('idevenementmodif'), $user[0]->prenom, $user[0]->nom, $this->post('title'), $titredugroupe);
							$creator = $this->user_model->get_user_by_id($createurgroupe);
							if(isset($creator[0]->gcm_regid)){
								//gcm test
								$gcm_bool = TRUE;
								$this->gcm->addRecepient($creator[0]->gcm_regid);
							}
							if(isset($creator[0]->iosdeviceid)){
								$this->applepush->setdevicetoken($creator[0]->iosdeviceid);
								$this->applepush->setbody(
									array(
										'action' => '3',
							            'idevenement' => $this->post('idevenementmodif'),
										'title' => $this->post('title'), 
										'dateStart' => $this->post('dateStart'),
										'dateEnd' => $this->post('dateEnd'), 
										'localisation' => $this->post('localisation'), 
										'description' => $this->post('description'),  
							            'alert' => $user[0]->prenom." ".$user[0]->nom." a modifié ".$this->post('title'),
                                          'badge' => 1,
										'sound' => 'default'
									)
								);
								$this->applepush->send();
							}
						}
						foreach($membre as $membregroupe){
							if($user[0]->iduser != $membregroupe->idapparteniruser){
								$this->notification_model->creernotificationmodifiereventgroupe($membregroupe->idapparteniruser, $this->post('groupe'), $this->post('idevenementmodif'), $user[0]->prenom, $user[0]->nom, $this->post('title'), $titredugroupe);
								if(isset($membregroupe->gcm_regid)){
									//gcm test
									$gcm_bool = TRUE;
									$this->gcm->addRecepient($membregroupe->gcm_regid);
								}
								if(isset($membregroupe->iosdeviceid)){
									$this->applepush->setdevicetoken($membregroupe->iosdeviceid);
									$this->applepush->setbody(
										array(
											'action' => '3',
								            'idevenement' => $this->post('idevenementmodif'),
											'title' => $this->post('title'), 
											'dateStart' => $this->post('dateStart'),
											'dateEnd' => $this->post('dateEnd'), 
											'localisation' => $this->post('localisation'), 
											'description' => $this->post('description'),  
								            'alert' => $user[0]->prenom." ".$user[0]->nom." a modifié ".$this->post('title'),
                                              'badge' => 1,
											'sound' => 'default'
										)
									);
									$this->applepush->send();
								}
							}
						}
						if($gcm_bool == TRUE){
							$this->gcm->setMessage('social event');
							
							$this->gcm->setTtl(500);
							
							$this->gcm->setData(array(
					            'action' => '3',
					            'idevenement' => $this->post('idevenementmodif'),
								'title' => $this->post('title'), 
								'dateStart' => $this->post('dateStart'),
								'dateEnd' => $this->post('dateEnd'), 
								'localisation' => $this->post('localisation'), 
								'description' => $this->post('description'),  
					            'text' => $user[0]->prenom." ".$user[0]->nom." a modifié ".$this->post('title')
					        ));
							$this->gcm->send();
						}
						$this->response(array(array('success' => TRUE)));
					}
					else{
						$this->response($message, 200);
					}
				}
			}else{
        		$this->response($message, 200);
			}
		}else{
        	$this->response($message, 200);
		}
    }
	
	function eventstest_get()
    {
    	/*
    	$this->load->model('evenement_model');
		
		$eventstmp = $this->evenement_model->get_event_without_fbmobile_android(49);
		$eventsgroupes = $this->evenement_model->get_event_allgroupemobile_android(49);
		$eventsparticipe = $this->evenement_model->get_event_participemobile_android(49);
			
			foreach($eventstmp as $event){
				$event->participereventreponse = 1;
			}
			
			foreach($eventsgroupes as $eventgroupe){
				$eventgroupe->participereventreponse = 0;
				foreach($eventsparticipe as $eventparticipe){
					if($eventgroupe->idevenement == $eventparticipe->idevenement){
						$eventgroupe->participereventreponse = $eventparticipe->participereventreponse;
					}
				}
			}
		
		$events = array();
			$temp = 0;
			
			if(isset($eventstmp)){
				foreach($eventstmp as $evenementtmp){
					if(isset($eventsgroupes) && isset($eventsgroupes[$temp])){
						while(isset($eventsgroupes[$temp]) && strtotime($evenementtmp->start_timeevenement) > strtotime($eventsgroupes[$temp]->start_timeevenement)){
							array_push($events, $eventsgroupes[$temp]);
							$temp++;	
						}
					}
					array_push($events, $evenementtmp);
				}
			}
			$i = 0;
			if(isset($eventsgroupes) && isset($eventsgroupes[$temp])){
				for($i = $temp; $i < count($eventsgroupes); $i++){
					array_push($events, $eventsgroupes[$i]);
				}
			}
			
			$evenements = array();
			foreach ($events as $evenement) {
				array_push($evenements, (array) $evenement);
			}
			$events = $evenements;
			*/
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