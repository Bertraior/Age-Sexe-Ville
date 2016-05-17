<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Welcome extends CI_Controller {

	function __construct()
	{
		parent::__construct();
	}

	function index()
	{
		$this->load->helper('url');
        
		$this->load->library('form_validation');
    	$this->load->model('asvuser_model');
		
		$iduserasv = $this->asvuser_model->get_user_by_idandroid("7b9cbb9db34e431df8dc0ff847aa0a5951021cae");
		var_dump($iduserasv[0]->iduserasv);
		
		$this->load->view('welcome_message');
	}
}

/* End of file welcome.php */
/* Location: ./system/application/controllers/welcome.php */