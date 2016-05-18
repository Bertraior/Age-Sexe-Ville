<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvuser_model extends CI_Model
{
	private $table = 'asvuser';
	
	public function get_user_by_id($id){
    	return $this->db->select('iduserasv, mailasv, pseudoasv')
		->from($this->table)
		->where('iduserasv', $id)
		->get()
		->result();
	}
	
	public function get_all_user(){
		return $this->db
    	->select('iduserasv, mailasv, pseudoasv')
		->distinct()
		->from($this->table)
		->get()
		->result();
	}
	
	public function normalconnect($mailasv, $passwordasv){
		return $this->db
    	->select('iduserasv, pseudoasv')
		->from($this->table)
		->where('mailasv', $mailasv)
		->where('passwordasv', $passwordasv)
		->get()
		->result();
	}
	
	public function synchro_user_android_gcm($mailasv, $passwordasv, $gcm_regid)
    {
    	if($this->db
    	->select('iduserasv')
    	->where('mailasv', $mailasv)
		->where('passwordasv', $passwordasv)
		->count_all_results($this->table) == 1){
    		$data = array(
				'gcm_regid' => $gcm_regid
	        );
			$this->db->where('mailasv', $mailasv);
			$this->db->where('passwordasv', $passwordasv);
			$this->db->update($this->table, $data);
			return TRUE;
		}else{
			return FALSE;
		}
    }
	
	public function synchro_user_ios_deviceid($mailasv, $passwordasv, $iosdeviceid)
    {
    	if($this->db
    	->select('iduserasv')
    	->where('mailasv', $mailasv)
		->where('passwordasv', $passwordasv)
		->count_all_results($this->table) == 1){
    		$data = array(
				'iosdeviceid' => $iosdeviceid
	        );
			$this->db->where('mailasv', $mailasv);
			$this->db->where('passwordasv', $passwordasv);
			$this->db->update($this->table, $data);
			return TRUE;
		}else{
			return FALSE;
		}
    }
	
	public function synchro_user_android_ios_deviceidipad($mailasv, $passwordasv, $iosdeviceidipad)
    {
    	if($this->db
    	->select('iduserasv')
    	->where('mailasv', $mailasv)
		->where('passwordasv', $passwordasv)
		->count_all_results($this->table) == 1){
    		$data = array(
				'iosdeviceidipad' => $iosdeviceidipad
	        );
			$this->db->where('mailasv', $mailasv);
			$this->db->where('passwordasv', $passwordasv);
			$this->db->update($this->table, $data);
			return TRUE;
		}else{
			return FALSE;
		}
    }
	
	public function save_user_android_gcm($mailasv, $pseudoasv, $passwordasv, $gcm_regid)
    {
    	if($this->db
    	->select('iduserasv')
    	->where('mailasv', $mailasv)
		->count_all_results($this->table) == 0){
    		$this->db->set('mailasv', $mailasv);
			$this->db->set('pseudoasv', $pseudoasv);
			$this->db->set('passwordasv', $passwordasv);
			$this->db->set('gcm_regid', $gcm_regid);
			$this->db->insert($this->table);
			return $this->db->insert_id($this->table);
		}else{
			$user = $this->db->select('iduserasv')
			->from($this->table)
			->where('mailasv', $mailasv)
			->get()
			->result();
			return $user[0]->iduserasv;
		}
    }
	
	public function save_user_ios_deviceid($mailasv, $pseudoasv, $passwordasv, $iosdeviceid)
    {
    	if($this->db
    	->select('iduserasv')
    	->where('mailasv', $mailasv)
		->count_all_results($this->table) == 0){
    		$this->db->set('mailasv', $mailasv);
			$this->db->set('pseudoasv', $pseudoasv);
			$this->db->set('passwordasv', $passwordasv);
			$this->db->set('iosdeviceid', $iosdeviceid);
			$this->db->insert($this->table);
			return $this->db->insert_id($this->table);
		}
    }
    
    public function save_user_ios_deviceidipad($mailasv, $pseudoasv, $passwordasv, $iosdeviceidipad)
    {
        if($this->db
           ->select('iduserasv')
           ->where('mailasv', $mailasv)
           ->count_all_results($this->table) == 0){
            $this->db->set('mailasv', $mailasv);
			$this->db->set('pseudoasv', $pseudoasv);
			$this->db->set('passwordasv', $passwordasv);
            $this->db->set('iosdeviceidipad', $iosdeviceidipad);
            $this->db->insert($this->table);
            return $this->db->insert_id($this->table);
        }
    }
	
	public function modify_pseudo($iduserasv, $pseudoasv){
		$data = array(
			'pseudoasv' => $pseudoasv
        );
		$this->db->where('iduserasv', $iduserasv);
		$this->db->update($this->table, $data);
		return TRUE;
	}
	
	public function modify_gcm($iduserasv, $gcm_regid){
		$data = array(
			'gcm_regid' => $gcm_regid
        );
		$this->db->where('iduserasv', $iduserasv);
		$this->db->update($this->table, $data);
		return TRUE;
	}
	
	public function maj_user_ios_deviceid($iduserasv, $iosdeviceid){
		$data = array(
			'iosdeviceid' => $iosdeviceid
        );
		$this->db->where('iduserasv', $iduserasv);
		$this->db->update($this->table, $data);
		return TRUE;
	}
	
	public function get_user_by_idandroid($iduseridmobile){
		$requete = 
			"SELECT *
			FROM ".$this->table."
			WHERE iduserasv = '".$iduseridmobile."'";
		return $this->db->query($requete)
			->result();
	}
	
}