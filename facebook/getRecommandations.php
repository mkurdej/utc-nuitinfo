<?php

// required parameters: 
//  user_id=...
//  friend_id=...
//  access_token=...

require 'facebook-php-sdk/src/facebook.php';
require 'util.php';

function getGiftRecommandations($friendData) {
    $maxRecommendationsPerCategory = 3;
    
    $recommandations = array();
    
    $requests = array('books', 'likes', 'movies', 'music');
    for($i = 0; i < sizeof($requests); ++$i) {
        $reqName = $requests[$i];
        if(!is_array($friendData[$reqName])) {
            continue;
        }
        sort_desc_by_created_time($friendData[$reqName]);
        
        for($j = 0; j < $maxRecommendationsPerCategory && j < sizeof($friendData[$reqName]); ++$j) {
            echo $sizeof($friendData[$reqName]) . '\n';
            $newRecommandation = array(
                'category' => $reqName,
                'name' => $friendData[$reqName]['name'],
                'id' => $friendData[$reqName]['id'],
                'created_time' => $friendData[$reqName]['created_time'],
            );
            print_r($newRecommandation);
            $recommandations = array_merge($recommandations, $newRecommandation);
        }
        $friendData[$reqName];
    }
    return $recommandations;
}

$user_id = $_GET["user_id"];
$friend_id = $_GET["friend_id"];
$access_token = $_GET["access_token"];

$user_id=1526860101;
$friend_id=29709340;
$access_token=AAAD59V4w2QsBAHvZAVa4kL55dfAOIEEsdqyAQWn1FZART28hhRKcKZCom8EQXTWmBtq4614bpFuSxyUyhUmUIGiLLKSGOUZD;

// Create our Application instance (replace this with your appId and secret).
// UTC-NuitInfo11
// https://developers.facebook.com/apps/274832242563339/
$facebook = new Facebook(array(
  'appId' => '274832242563339',
  'secret' => '4946a0747eb4c8b07340c856bd6a741a',
));

if ($user_id) {
  try {
    // Proceed knowing you have a logged in user who's authenticated.
    $user_profile = $facebook->api('/me');
  } catch (FacebookApiException $e) {
    error_log($e);
    $user_id = null;
  }
}

// Login or logout url will be needed depending on current user state.
if ($user_id) {
    $requests = array('books', 'movies', 'music');
    for($i = 0; $i < sizeof($requests); ++$i) {
        $reqName = $requests[$i];
        $friendDataUrl = 'https://graph.facebook.com/' . $friend_id . '/' . $reqName . '?access_token=' . $access_token;
        $friendData[$reqName] = json_fetch_and_decode($friendDataUrl, true);
        $friendData[$reqName] = $friendData[$reqName]['data'];
    }
    $recommandations = getGiftRecommandations($friendData);
    print_r(json_encode($recommandations));
    //exit();
} else {
    // TODO: send error message
    $error = array('error' => 'error message');
    print_r(json_encode($error));
    exit(-1);
}
