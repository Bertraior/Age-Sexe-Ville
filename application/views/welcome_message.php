<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Welcome to CodeIgniter</title>

<style type="text/css">

body {
 background-color: #fff;
 margin: 40px;
 font-family: Lucida Grande, Verdana, Sans-serif;
 font-size: 14px;
 color: #4F5155;
}

a {
 color: #003399;
 background-color: transparent;
 font-weight: normal;
}

h1 {
 color: #444;
 background-color: transparent;
 border-bottom: 1px solid #D0D0D0;
 font-size: 16px;
 font-weight: bold;
 margin: 24px 0 2px 0;
 padding: 5px 0 6px 0;
}

code {
 font-family: Monaco, Verdana, Sans-serif;
 font-size: 12px;
 background-color: #f9f9f9;
 border: 1px solid #D0D0D0;
 color: #002166;
 display: block;
 margin: 14px 0 14px 0;
 padding: 12px 10px 12px 10px;
}

</style>
</head>
<body>

<h1>Welcome to CodeIgniter!</h1>

<p>The page you are looking at is being generated dynamically by CodeIgniter.</p>

<h3>User</h3>
<ul>
	<li><a href="<?php echo site_url('api/user/connect');?>">Connection</a> - Post (username, password)</li>
	<li><a href="<?php echo site_url('api/user/inscription');?>">Inscription</a> - Post (pseudo, username, password, redid, coco) </li>
	<li><a href="<?php echo site_url('api/user/modify_pseudo/iduseridmobile/1');?>">Modifier pseudo</a>  - Get (iduseridmobile) - Post (pseudo) </li>
	<li><a href="<?php echo site_url('api/user/majgcm/iduseridmobile/1');?>">Mise à jour Google Cloud Messaging Id</a> - Get (iduseridmobile) - Post (gcm) </li>
	<li><a href="<?php echo site_url('api/user/modifyiosdeviceid/iduseridmobile/1');?>">Mise à jour IOS Cloud Messaging Id</a> - Get (iduseridmobile) - Post (iosdeviceid) </li>
	<li><a href="<?php echo site_url('api/user/signaler/iduseridmobile/1');?>">Signaler un commentaire</a> - Get (iduseridmobile) - Post (idcommenterasv) </li>
	<li><a href="<?php echo site_url('api/user/iospush');?>">Test d'envoie de notification sur Iphone vers tel à bertrand</a> </li>
</ul>

<h3>Chat</h3>
<ul>
	<li><a href="<?php echo site_url('api/chat/create/iduseridmobile/1');?>">Créer un forum</a> - Get (iduseridmobile) - Post (chat)</li>
	<li><a href="<?php echo site_url('api/chat/chats/iduseridmobile/1');?>">Liste des forums</a> - Get (iduseridmobile) </li>
</ul>

<h3>Commentforum</h3>
<ul>
	<li><a href="<?php echo site_url('api/commentforum/check/iduseridmobile/1/idforum/6/idmessage/1');?>">Check s'il y'a un nouveau message sur le forum (pour refresh le html)</a> - Get (iduseridmobile, idforum, idmessage)</li>
	<li><a href="<?php echo site_url('api/commentforum/commentsforum/iduseridmobile/1/idforum/6');?>">Liste des commentaire dans le forum</a> - Get (iduseridmobile, idforum) </li>
	<li><a href="<?php echo site_url('api/commentforum/commente/iduseridmobile/1/idforum/6');?>">Commenter dans le forum</a> - Get (iduseridmobile, idforum) - Post (message, datemobile)</li>
	<li><a href="<?php echo site_url('api/commentforum/thecommentforum/iduseridmobile/1/idmessage/14');?>">le message du forum</a> - Get (iduseridmobile, idmessage)</li>
</ul>

<h3>Mymessage</h3>
<ul>
	<li><a href="<?php echo site_url('api/mymessage/message/iduseridmobile/20');?>">Liste des messages privés</a> - Get (iduseridmobile)</li>
	<li><a href="<?php echo site_url('api/mymessage/messagewith/iduseridmobile/20/iddestinataire/181');?>">Liste des messages privés avec une personne</a> - Get (iduseridmobile, iddestinataire) </li>
	<li><a href="<?php echo site_url('api/mymessage/loadnumbermessage/iduseridmobile/1');?>">Check le nombre de nouveau messages privés</a> - Get (iduseridmobile)</li>
	<li><a href="<?php echo site_url('api/mymessage/updatenumbermessage/iduseridmobile/1');?>">Mise à jour du nombre de nouveau messages privés</a> - Get (iduseridmobile)</li>
	<li><a href="<?php echo site_url('api/mymessage/envoiemessage/iduseridmobile/1/iddestinataire/14');?>">Envoie d'un message privée</a> - Get (iduseridmobile, iddestinataire) - POST (messagetosend, datemessagemobile)</li>
</ul>

<p>If you are exploring CodeIgniter for the very first time, you should start by reading the <a href="user_guide/">User Guide</a>.</p>

<p><br />Page rendered in {elapsed_time} seconds</p>

<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	// Bind a click event to the 'ajax' object id
	$("#ajax").bind("click", function( evt ){
		// Javascript needs totake over. So stop the browser from redirecting the page
		evt.preventDefault();
		// AJAX request to get the data
		$.ajax({
			// URL from the link that was clicked on
			url: $(this).attr("href"),
			// Success function. the 'data' parameter is an array of objects that can be looped over
			success: function(data, textStatus, jqXHR){
				alert('Successful AJAX request!');
			}, 
			// Failed to load request. This could be caused by any number of problems like server issues, bad links, etc. 
			error: function(jqXHR, textStatus, errorThrown){
				alert('Oh no! A problem with the AJAX request!');
			}
		});
	});
});
</script>

</body>
</html>