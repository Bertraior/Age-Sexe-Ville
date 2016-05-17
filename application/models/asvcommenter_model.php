<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvcommenter_model extends CI_Model
{
	private $table = 'asvcommenter';

	public function get_all($idforumcommenterasv){
		return $this->db->select('idasvcommenter, idusercommenterasv, idforumcommenterasv, messagecommenterasv, pseudocommenterasv, datecommenterasv')
		->from($this->table)
		->where("idforumcommenterasv", $idforumcommenterasv)
		->order_by("datecommenterasv", "asc")
		->limit(100)
		->get()
		->result();	
	}

	public function create_comment($idusercommenterasv, $idforumcommenterasv, $messagecommenterasv, $pseudocommenterasv, $datecommentermobileasv){
		$this->db->set('idusercommenterasv', $idusercommenterasv);	
		$this->db->set('idforumcommenterasv', $idforumcommenterasv);
		$this->db->set('messagecommenterasv', $messagecommenterasv);
		$this->db->set('pseudocommenterasv', $pseudocommenterasv);
		$this->db->set('datecommentermobileasv', $datecommentermobileasv);
		return $this->db->insert($this->table);
	}
	
	public function check_new_comment($idforumcommenterasv, $idasvcommenter){
		return $this->db
    	->select('idasvcommenter')
    	->where('idforumcommenterasv', $idforumcommenterasv)
		->where('idasvcommenter >', $idasvcommenter)
		->count_all_results($this->table);
	}

	public function get_comment($idasvcommenter){
		return $this->db->select('idasvcommenter, idusercommenterasv, idforumcommenterasv, messagecommenterasv, pseudocommenterasv, datecommenterasv')
		->from($this->table)
		->where("idasvcommenter", $idasvcommenter)
		->get()
		->result();	
	}
	
	public function get_members($idforumcommenterasv){
		return $this->db->select('idasvcommenter, idusercommenterasv, iduserasv, gcm_regid, iosdeviceid, pseudoasv, nomforumasv')
		->from($this->table)
		->join('asvuser', 'asvuser.iduserasv = '.$this->table.'.idusercommenterasv')
		->join('asvforum', 'asvforum.idforumasv = '.$this->table.'.idforumcommenterasv')
		->where("idforumcommenterasv", $idforumcommenterasv)
		->group_by("idusercommenterasv")
		->get()
		->result();	
	}
	
}