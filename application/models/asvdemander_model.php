<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvdemander_model extends CI_Model
{
	private $table = 'asvdemander';

	public function loaddemande($idstarasvdemander){
		return $this->db->select('iduserasv, pseudoasv, mailasv, statutasvdemander')
		->from($this->table)
		->join('asvuser', $this->table.'.iddemandeurasvdemander = asvuser.iduserasv')
		->where('idstarasvdemander', $idstarasvdemander)
		->get()
		->result();
	}

	public function check($iddemandeurasvdemander, $idstarasvdemander){
		return $this->db->select('statutasvdemander, mailasv')
		->from($this->table)
		->join('asvuser', $this->table.'.idstarasvdemander = asvuser.iduserasv')
		->where('iddemandeurasvdemander', $iddemandeurasvdemander)
		->where('idstarasvdemander', $idstarasvdemander)
		->get()
		->result();
	}

	public function demander($iddemandeurasvdemander, $idstarasvdemander){
		$this->db->set('iddemandeurasvdemander', $iddemandeurasvdemander);
		$this->db->set('idstarasvdemander', $idstarasvdemander);
		$this->db->set('statutasvdemander', 0);
		$this->db->insert($this->table);
	}
	
	public function accepter($iddemandeurasvdemander, $idstarasvdemander){
		$data = array(
			'statutasvdemander' => 1
        );
		$this->db->where('iddemandeurasvdemander', $iddemandeurasvdemander);
		$this->db->where('idstarasvdemander', $idstarasvdemander);
		$this->db->update($this->table, $data);
		return TRUE;
	}
}