<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvforum_model extends CI_Model
{
	private $table = 'asvforum';

	public function get_all(){
		return $this->db->select('idforumasv, pseudoasv, nomforumasv, datecreationforumasv, count(statutparticiperasv) as nbparticipant')
		->from($this->table)
		->join('asvuser', 'asvuser.iduserasv = '.$this->table.'.iduserforumasv')
		->join('asvparticiper', $this->table.'.idforumasv = asvparticiper.idforumparticiperasv', 'left')
		->group_by('idforumasv')
		->order_by("datecreationforumasv", "desc")
		->limit(100)
		->get()
		->result();	
		/*
		$requete = 
			'SELECT idforumasv, pseudoasv, nomforumasv, datecreationforumasv, count(statutparticiperasv) 
			FROM '.$this->table.' as a
			JOIN asvuser as u1 ON a.iduserforumasv = u1.iduserasv
			LEFT JOIN asvparticiper as part ON a.idforumasv = part.idforumparticiperasv
			Group By a.idforumasv';
		return $this->db->query($requete)
			->result();
		 */
	}

	public function create_forum($iduserforumasv, $nomforumasv){
		$this->db->set('iduserforumasv', $iduserforumasv);
		$this->db->set('nomforumasv', $nomforumasv);
		$this->db->insert($this->table);
		return $this->db->insert_id($this->table);
	}
	
	public function supprimer_forum($iduserforumasv){
		$this->db->where('idevenement', $iduserforumasv);
		$this->db->delete($this->table); 
	}
}