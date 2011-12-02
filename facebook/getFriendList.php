<?php

require 'facebook-php-sdk/src/facebook.php';
require 'util.php';

$user = $_GET["user_id"];
$access_token = $_GET["access_token"];

$user=1526860101;
$access_token=AAAD59V4w2QsBAHvZAVa4kL55dfAOIEEsdqyAQWn1FZART28hhRKcKZCom8EQXTWmBtq4614bpFuSxyUyhUmUIGiLLKSGOUZD;

// Create our Application instance (replace this with your appId and secret).
// UTC-NuitInfo11
// https://developers.facebook.com/apps/274832242563339/
$facebook = new Facebook(array(
  'appId' => '274832242563339',
  'secret' => '4946a0747eb4c8b07340c856bd6a741a',
));

if ($user) {
    // Get friends list URL
    // http://developers.facebook.com/docs/reference/api/
    $friendListUrl = 'https://graph.facebook.com/' . $user . '/friends?access_token=' . $access_token;
    //echo $friendListUrl . '\n';
    $friendList = json_fetch_and_decode($friendListUrl, true);
    $friendList = $friendList['data'];    //echo json_encode($friendList);
    print_r(json_encode($friendList));
} else {
    // TODO: send error message
    $error = array('error' => 'error message');
    print_r(json_encode($error));
    exit(-1);
}

?>