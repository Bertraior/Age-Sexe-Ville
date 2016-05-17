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

class Mylog extends REST_Controller
{
    function logs_get()
    {
		if($this->get('iduseridmobile', TRUE))
        {
    		$this->load->model('asvuser_model');
			$user_id = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			if($this->get('iduseridmobile', TRUE))
        	{
        		$this->load->model('asvconnecter_model');
				$logs = $this->asvconnecter_model->get_connecter();
				
				$listelog = array();
				foreach ($logs as $log) {
					$log->logmessage = $log->prenom." ".$log->nom." : ".date("d/m/Y - G:i", strtotime($log->dateconnexion));
					array_push($listelog, (array) $log);
				}
				$logs = $listelog;
				if($logs)
		        {
		            $this->response($logs, 200); // 200 being the HTTP response code
		        }
		
		        else
		        {
		            $this->response(array('error' => 'Couldn\'t find any participant!'), 404);
		        }
			}
		}
    }
	
	function stats_get()
    {
		if($this->get('iduseridmobile', TRUE))
        {
    		$this->load->model('asvuser_model');
			$user_id = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			if($this->get('iduseridmobile', TRUE))
        	{
        		$this->load->model('asvconnecter_model');
				$stats = $this->asvconnecter_model->get_stats();
				
				$listestat = array();
				foreach ($stats as $stat) {
					$stat->date = date("Y-m-d", strtotime($stat->date));
					array_push($listestat, (array) $stat);
				}
				$stats = $listestat;
				if($stats)
				{
					$this->response($stats, 200); // 200 being the HTTP response code
				}

				else
				{
					$this->response(array('error' => 'Couldn\'t find any stats!'), 404);
				}
			}
		}
    }
	
	function monthstats_get()
    {
		if($this->get('iduseridmobile', TRUE))
        {
    		$this->load->model('asvuser_model');
			$user_id = $this->asvuser_model->get_user_by_idandroid($this->get('iduseridmobile', TRUE));
			if($this->get('iduseridmobile', TRUE))
        	{
        		$this->load->model('asvconnecter_model');
				$stats = $this->asvconnecter_model->get_monthstats();
				
				$listestat = array();
				foreach ($stats as $stat) {
					array_push($listestat, (array) $stat);
				}
				$stats = $listestat;
				if($stats)
				{
					$this->response($stats, 200); // 200 being the HTTP response code
				}

				else
				{
					$this->response(array('error' => 'Couldn\'t find any stats!'), 404);
				}
			}
		}
    }
}