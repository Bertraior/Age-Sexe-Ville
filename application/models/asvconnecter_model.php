<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvconnecter_model extends CI_Model
{
	private $table = 'asvconnecter';
	
	public function save_connecter($iduserconnecterasv)
    {
		$this->db->set('iduserconnecterasv', $iduserconnecterasv);
		return $this->db->insert($this->table);
    }
	
	public function get_connecter()
	{
		return $this->db
    	->select('*')
		->from($this->table)
		->order_by('dateconnecterasv', 'DESC')
		->limit(30)
		->get()
		->result();
	}
	
	public function get_stats()
	{
		$requete = "SELECT DISTINCT (DATE(dateconnecterasv)) AS date, COUNT(*) AS nbconnect 
		FROM ".$this->table."
		Where iduserconnecterasv <> 1 
		GROUP BY date 
		ORDER BY date DESC
		LIMIT 30";
		return $this->db->query($requete)->result();
	}
	
	public function get_monthstats()
	{
		$requete = "SELECT DISTINCT (MONTH(dateconnecterasv)) AS date, COUNT(*) AS nbconnect 
		FROM ".$this->table."
		Where iduserconnecterasv <> 1 
		GROUP BY date 
		ORDER BY date DESC
		LIMIT 30";
		return $this->db->query($requete)->result();
	}
}