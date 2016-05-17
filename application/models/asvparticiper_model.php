<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvparticiper_model extends CI_Model
{
	private $table = 'asvparticiper';

	public function participer($iduserparticiperasv, $idforumparticiperasv){
		if($this->db
    	->select('iduserparticiperasv')
    	->where('iduserparticiperasv', $iduserparticiperasv)
		->where('idforumparticiperasv', $idforumparticiperasv)
		->count_all_results($this->table) == 0){
    		$this->db->set('iduserparticiperasv', $iduserparticiperasv);
			$this->db->set('idforumparticiperasv', $idforumparticiperasv);
			$this->db->set('statutparticiperasv', 0);
			$this->db->insert($this->table);
			//return $this->db->insert_id($this->table);
		}
	}
	
	public function quitter($iduserparticiperasv, $idforumparticiperasv){
		$this->db->where('iduserparticiperasv', $iduserparticiperasv);
		$this->db->where('idforumparticiperasv', $idforumparticiperasv);
		$this->db->delete($this->table); 
	}
	
	public function nb_participants($idforumparticiperasv){
		return $this->db
    	->select('iduserparticiperasv')
		->where('idforumparticiperasv', $idforumparticiperasv)
		->count_all_results($this->table);
	}
}