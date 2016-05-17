<?php

/**
* Blacklist dictionary
*
* A simple blacklist library for codeigniter.
*
* @package 		Blacklist
* @version 		1.0
* @author  		Yang Hu <yangg.hu@gmail.com>
* @license 		Apache License v2.0
* @copyright 	2010 Yang Hu
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

// Forbidden IP adresses
$config['ip_addresses'] = array('10.55.66.*');

// Forbidden keywords
$config['words'] = array('GFW', 'FUCK', '群体灭绝', '红色恐怖', '迷魂药', '代开发票', 'anal', 'anus', 'arse', 'ass', 'ballsack', 'balls', 'bastard', 'bitch', 'biatch', 'bloody', 'blowjob', 'blow job', 'bollock', 'bollok', 'boner', 'boobbugger', 'bum', 'butt', 'buttplug', 'clitoris', 'cock', 'coon', 'crap', 'cunt', 'damn', 'dick', 'dildo', 'dyke', 'fag', 'feck', 'fellate', 'fellatio', 'felching', 'fuck', 'f u c k', 'fudgepacker', 'fudge packer', 'flange', 'Goddamn', 'God damn', 'hell', 'homo', 'jerk', 'jizz', 'knobend', 'knob end', 'labia', 'lmao', 'lmfao', 'muff', 'nigger', 'nigga', 'omg', 'penis', 'piss', 'poop', 'prick', 'pube', 'pussy', 'queer', 'scrotum', 'sex', 'shit', 's hit', 'sh1t', 'slut', 'smegma', 'spunk', 'suck', 'tit', 'tosser', 'turd', 'twat', 'vagina', 'wank', 'whore', 'wtf');

// Regexs (aka. regular expression) patterns
// It should be PERL style!!
$config['regexs'] = array();

/* End of file blacklist.php */
/* Location: ./system/application/config/blacklist.php */