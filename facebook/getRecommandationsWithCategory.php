<?php

// required parameters: 
//  user_id=...
//  access_token=...
//  friend_id=...
//  category=(books|movies|music)

// optional parameters:
//  max_items=...

require 'facebook-php-sdk/src/facebook.php';
require 'util.php';

function getGiftRecommandations($friendData, $category, $max_items) {
    $recommandations = array();

    if(!is_array($friendData)) {
        return $recommandations;
    }
    if(sizeof($friendData) == 0) {
        return $recommandations;
    }
    usort($friendData, compare_desc_by_created_time);
    
    $max_items = min($max_items, sizeof($friendData));
    for($j = 0; $j < $max_items; ++$j) {
        $element = $friendData[$j];
        $newRecommandation = array(
            'category' => $category,
            'name' => $element['name'],
            'id' => $element['id'],
            'created_time' => $element['created_time'],
        );
        array_push($recommandations, $newRecommandation);
    }
    return $recommandations;
}

$user_id = $_GET["user_id"];
$friend_id = $_GET["friend_id"];
$access_token = $_GET["access_token"];
$category = $_GET["category"];
$max_items = $_GET["max_items"];
if(is_null($max_items)) {
    $max_items = 10;
}

// temporary only
//$user_id = 1526860101;
//$access_token = 'AAAD59V4w2QsBAHvZAVa4kL55dfAOIEEsdqyAQWn1FZART28hhRKcKZCom8EQXTWmBtq4614bpFuSxyUyhUmUIGiLLKSGOUZD';
//$friend_id = 29709340;

// Create our Application instance
$facebook = new Facebook(array(
    'appId' => '274832242563339',
    'secret' => '4946a0747eb4c8b07340c856bd6a741a',
));

if ($user_id) {
    $friendDataUrl = 'https://graph.facebook.com/' . $friend_id . '/' . $category . '?access_token=' . $access_token;
    $friendData = json_fetch_and_decode($friendDataUrl, true);
    $friendData = $friendData['data'];

    $recommandations = getGiftRecommandations($friendData, $category, $max_items);
    echo json_encode($recommandations);
} else {
    $error = array('error' => 'User not connected');
    echo json_encode($error);
    exit(-1);
}
