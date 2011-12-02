<?php

// required parameters: 
//  user_id=...
//  access_token=...

require 'facebook-php-sdk/src/facebook.php';
require 'util.php';

$user_id = $_GET["user_id"];
$access_token = $_GET["access_token"];

// temporary only
$user_id = 1526860101;
$access_token = 'AAAD59V4w2QsBAHvZAVa4kL55dfAOIEEsdqyAQWn1FZART28hhRKcKZCom8EQXTWmBtq4614bpFuSxyUyhUmUIGiLLKSGOUZD';

// Create our Application instance
$facebook = new Facebook(array(
    'appId' => '274832242563339',
    'secret' => '4946a0747eb4c8b07340c856bd6a741a',
));

if ($user_id) {
    // Get friends list URL
    $friendListUrl = 'https://graph.facebook.com/' . $user_id . '/friends?access_token=' . $access_token;
    $friendList = json_fetch_and_decode($friendListUrl, true);
    $friendList = $friendList['data'];
    
    usort($friendList, compare_asc_by_name);    echo json_encode($friendList);
} else {
    $error = array('error' => 'User not connected');
    echo json_encode($error);
    exit(-1);
}

?>
