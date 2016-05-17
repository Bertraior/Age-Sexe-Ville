<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 
class Asvmessage_model extends CI_Model
{
	/* notificationvu = 0 pas vu
	 * notificationvu = 1 nombre de notification vu
	 * notificationvu = 2 la notification a été vue
	 */
	private $table = 'asvmessage';
	
	public function checkmessage($idexpediteur, $datemessagemobile){
		return $this->db
    	->select('idasvmessage')
		->from($this->table)
    	->where('idexpediteurasvmessage', $idexpediteur)
		->where('dateasvmessage', $datemessagemobile)
		->get()
		->result();
	}
	
	public function loadmessage($iddestinataire){
		return $this->db
    	->select('iduserasv, pseudoasv, publicasv, dateasvmessage, idexpediteurasvmessage, iddestinataireasvmessage, messagevuasvmessage, messageasvmessage, idasvmessage')
		->from($this->table)
		->join('asvuser', 'asvuser.iduserasv = '.$this->table.'.idexpediteurasvmessage')
    	->where('iddestinataireasvmessage', $iddestinataire)
		->order_by('dateasvmessage', 'DESC')
		->limit(10)
		->get()
		->result();
	}
	
	public function loadnewmessage($iddestinataire, $idenvoyermessage){
		return $this->db
    	->select('*')
		->from($this->table)
		->join('asvuser', 'asvuser.iduserasv = '.$this->table.'.idexpediteurasvmessage')
    	->where('iddestinataireasvmessage', $iddestinataire)
		->where('idasvmessage > '.$idenvoyermessage)
		->order_by('dateasvmessage', 'DESC')
		->get()
		->result();
	}
	
	public function loadnbnewmessage($iddestinataire){
		return $this->db->where('iddestinataireasvmessage', $iddestinataire)
    	->where('messagevuasvmessage', 0)
		->from($this->table)
		->count_all_results();
	}
	
	public function lastidmessage($iddestinataire, $idenvoyermessage){
		return $this->db
    	->select('idasvmessage')
		->from($this->table)
    	->where('iddestinataireasvmessage', $iddestinataire)
		->where('idasvmessage > '.$idenvoyermessage)
		->order_by('dateasvmessage', 'DESC')
		->limit(1)
		->get()
		->result();
	}
	
	public function loadallmessage($iddestinataire){
		$requete1 = 
			'(SELECT u1.iduserasv, u1.pseudoasv, a.idasvmessage, a.idexpediteurasvmessage, a.messageasvmessage, a.dateasvmessage, a.messagevuasvmessage 
			FROM '.$this->table.' as a
			JOIN asvuser as u1 ON a.iddestinataireasvmessage = u1.iduserasv
			WHERE a.idexpediteurasvmessage = '.$iddestinataire.')';
		$requete2 =
			'(SELECT u2.iduserasv, u2.pseudoasv, b.idasvmessage, b.idexpediteurasvmessage, b.messageasvmessage, b.dateasvmessage, b.messagevuasvmessage 
			FROM '.$this->table.' as b
			JOIN asvuser as u2 ON b.idexpediteurasvmessage = u2.iduserasv
			WHERE b.iddestinataireasvmessage = '.$iddestinataire.')';
		return $this->db->query($requete1.' UNION '.$requete2.' ORDER BY dateasvmessage DESC')
			->result();
	}
	
	public function loadmessagedestinataire($idexpediteur, $iddestinataire){
		$where = '(idexpediteurasvmessage = '.$idexpediteur.' AND iddestinataireasvmessage = '.$iddestinataire.') OR (idexpediteurasvmessage = '.$iddestinataire.' AND iddestinataireasvmessage = '.$idexpediteur.')';
		return $this->db
    	->select('*')
		->from($this->table)
    	->where($where)
		->order_by('dateasvmessage', 'DESC')
		->get()
		->result();
	}

	public function loadmessagefor($idexpediteur, $iddestinataire){
		$where = '(idexpediteurasvmessage = '.$idexpediteur.' AND iddestinataireasvmessage = '.$iddestinataire.') OR (idexpediteurasvmessage = '.$iddestinataire.' AND iddestinataireasvmessage = '.$idexpediteur.')';
		return $this->db
    	->select('idasvmessage, idexpediteurasvmessage, messageasvmessage, dateasvmessage, pseudoasv')
		->from($this->table)
		->join('asvuser', 'asvuser.iduserasv = '.$this->table.'.idexpediteurasvmessage')
    	->where($where)
		->order_by('dateasvmessage', 'ASC')
		->get()
		->result();
	}

	public function loadnewmessagefor($iddestinataire, $idexpediteur, $idenvoyermessage){
		return $this->db
    	->select('idasvmessage, messageasvmessage, dateasvmessage, pseudoasv')
		->from($this->table)
		->join('asvuser', 'asvuser.iduserasv = '.$this->table.'.idexpediteurasvmessage')
    	->where('iddestinataireasvmessage', $iddestinataire)
		->where('idexpediteurasvmessage', $idexpediteur)
		->where('idasvmessage > '.$idenvoyermessage)
		->order_by('dateasvmessage', 'DESC')
		->get()
		->result();
	}
	
	public function loadlastnewmessagefor($iddestinataire, $idexpediteur, $idenvoyermessage){
		return $this->db
    	->select('idasvmessage')
		->from($this->table)
    	->where('iddestinataireasvmessage', $iddestinataire)
		->where('idexpediteurasvmessage', $idexpediteur)
		->where('idasvmessage > '.$idenvoyermessage)
		->order_by('dateasvmessage', 'DESC')
		->limit(1)
		->get()
		->result();
	}
	
	public function creermessage($message, $idexpediteur, $iddestinataire, $datemessagemobile){
		$this->db->set('messageasvmessage', $message);
		$this->db->set('idexpediteurasvmessage', $idexpediteur);
		$this->db->set('iddestinataireasvmessage', $iddestinataire);
		$this->db->set('datemobileasvmessage', $datemessagemobile);
		$this->db->set('messagevuasvmessage', 0);
		$this->db->insert($this->table);
		return $this->db->insert_id($this->table);
	}
	
	public function updatevunbmessage($iddestinataire)
	{
		$data = array(
           'messagevuasvmessage' => 1
        );
		$this->db->where('iddestinataireasvmessage', $iddestinataire);
		$this->db->where('messagevuasvmessage', 0);
		$this->db->update($this->table, $data); 
	}
	
	public function updatevumessage($idenvoyermessage)
	{
		$data = array(
           'messagevuasvmessage' => 2
        );
		$this->db->where('idasvmessage', $idenvoyermessage);
		$this->db->update($this->table, $data); 
	}
	
}